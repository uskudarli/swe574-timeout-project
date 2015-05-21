package demo;

import helpers.ServiceHelper;
import helpers.ValidationHelper;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import repository.UserRepository;
import common.BusinessException;
import common.DBUtility;
import common.ErrorMessages;
import common.ResponseHeader;
import entity.Role;
import entity.Session;
import entity.User;
import entity.UserBasicInfo;
import entity.UserCommInfo;
import entity.UserExtraInfo;

@RestController
public class UserRestServices {

	//register function
	@RequestMapping(value = "/register")
	public @ResponseBody Object registerUser(
			@RequestParam(value = "userEmail") String userEmail,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "firstName") String firstName,
			@RequestParam(value = "lastName") String lastName,
			@RequestParam(value = "role") String role,
			HttpServletResponse resp) {
		
		EntityManager em = ServiceHelper.initialize(resp);
		User user = null;
		try {

			ValidationHelper.validateEmail(userEmail);
			ValidationHelper.validatePassword(password);
			
			UserRepository ur = new UserRepository(em);
			
			if (ur.getUserByUserNumberEmail(userEmail) > 0){
				ServiceHelper.businessError(ErrorMessages.userAlreadyExistCode,
						ErrorMessages.userAlreadyExist);
			}

			user = new User(userEmail, password);
			user.setUserBasicInfo(new UserBasicInfo());
			user.getUserBasicInfo().setUser(user);
			user.setUserCommInfo(new UserCommInfo());
			user.getUserCommInfo().setUser(user);
			user.setUserExtraInfo(new UserExtraInfo());
			user.getUserExtraInfo().setUser(user);

			Role roleObj = ur.getRoleByName(role);
			if (roleObj == null) {
				roleObj = new Role(role);
				ur.insertRole(roleObj);
			}
			user.setRole(roleObj);

			user.getUserBasicInfo().setFirstName(firstName);
			user.getUserBasicInfo().setLastName(lastName);

			ur.insertUser(user);

			DBUtility.commitTransaction(em);
		} catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		} catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return new ResponseHeader();
	}
	
	@RequestMapping(value = "/email/isAvailable")
	public @ResponseBody Object emailIsAvailable(
			@RequestParam(value = "userEmail") String userEmail,
			HttpServletResponse resp) {
		
		EntityManager em = ServiceHelper.initialize(resp);

		try {

			ValidationHelper.validateEmail(userEmail);
			
			UserRepository ur = new UserRepository(em);

			if (ur.getUserByUserNumberEmail(userEmail) > 0){
				ServiceHelper.businessError(ErrorMessages.emailNotAvailableCode, ErrorMessages.emailNotAvailable);
			}

			DBUtility.commitTransaction(em);
		} catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		} catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return new ResponseHeader();
	}

	//edit user function
	@RequestMapping(value = "/profile/edit")
	public @ResponseBody Object editProfile(
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "lastName", required = false) String lastName,
			@RequestParam(value = "Gsm", required = false) Long Gsm,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "birthdate", required = false) Date birthdate,
			@RequestParam(value = "about", required = false) String about,
			@RequestParam(value = "interests", required = false) String interests,
			@RequestParam(value = "gender", required = false) String gender,
			@RequestParam(value = "languages", required = false) String languages,
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);

		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			if (user.getUserBasicInfo() == null) {
				user.setUserBasicInfo(new UserBasicInfo());
				user.getUserBasicInfo().setUser(user);
			}

			if (user.getUserCommInfo() == null) {
				user.setUserCommInfo(new UserCommInfo());
				user.getUserCommInfo().setUser(user);
			}

			if (user.getUserExtraInfo() == null) {
				user.setUserExtraInfo(new UserExtraInfo());
				user.getUserExtraInfo().setUser(user);
			}
			// user.getUserExtraInfo().setUserId(user.getUserId());
			// if (user.getRole() == null){
			// user.setRole(new Role());
			// user.getRole().getUsers().add(user);
			// }
			if (firstName != "" && firstName != null)
				user.getUserBasicInfo().setFirstName(firstName);
			if (lastName != "" && lastName != null)
				user.getUserBasicInfo().setLastName(lastName);
			if (Gsm != null && Gsm > 0)
				user.getUserCommInfo().setMobilePhone(Gsm);
			if (address != "" && address != null)
				user.getUserCommInfo().setAddress(address);
			if (birthdate != null)
				user.getUserExtraInfo().setBirthDate(birthdate);
			if (about != "" && about != null)
				user.getUserExtraInfo().setAbout(about);
			if (interests != "" && interests != null)
				user.getUserExtraInfo().setInterests(interests);
			if (gender != "" && gender != null)
				user.getUserBasicInfo().setGender(gender);
			if (languages != "" && languages != null)
				user.getUserExtraInfo().setLanguages(languages);
			
			UserRepository ur = new UserRepository(em);
			ur.insertUser(user);
			
			DBUtility.commitTransaction(em);
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return new ResponseHeader();
	}

	//get user profile
	@RequestMapping(value = "/profile/get")
	public @ResponseBody Object getProfile(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);

		User user;
		try {
			user = ServiceHelper.getSessionUser(em, sessionId);
			
			DBUtility.commitTransaction(em);
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return user;
	}
	
	//login function
	@RequestMapping(value = "/login")
    public
    @ResponseBody
    ResponseHeader login(
            @RequestParam(value = "userEmail") String userEmail,
            @RequestParam(value = "password") String password, HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);
        
        ResponseHeader rh;
        
        try {
			UserRepository ur = new UserRepository(em);
			
			rh = ur.login(userEmail, password);
			
			DBUtility.commitTransaction(em);
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return rh;
    }
}
