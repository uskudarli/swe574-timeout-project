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
import common.DBUtility;
import common.ResponseHeader;
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
			HttpServletResponse resp) {
		
		EntityManager em = DBUtility.startTransaction();
		ServiceHelper.setResponseHeaders(resp);
		
		try {

			ValidationHelper.validateEmail(userEmail);
			ValidationHelper.validatePassword(password);
			
			UserRepository ur = new UserRepository(em);
			ur.insertUser(userEmail, password);
			
			DBUtility.commitTransaction(em);
			return new ResponseHeader();
			
		} catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
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

		EntityManager em = DBUtility.startTransaction();
		ServiceHelper.setResponseHeaders(resp);

		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			ServiceHelper.authorize(user);
			
			if (user.getUserBasicInfo() == null) {
				user.setUserBasicInfo(new UserBasicInfo());
				user.getUserBasicInfo().setUser(user);
			}
			// user.getUserBasicInfo().setUserId(user.getUserId());
			if (user.getUserCommInfo() == null) {
				user.setUserCommInfo(new UserCommInfo());
				user.getUserCommInfo().setUser(user);
			}
			// user.getUserCommInfo().setUserId(user.getUserId());
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
			User user1 = em.merge(user);
			em.merge(user1.getUserBasicInfo());
			em.merge(user1.getUserCommInfo());
			em.merge(user1.getUserExtraInfo());
			DBUtility.commitTransaction(em);
		} catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return new ResponseHeader();
	}

	//get user profile
	@RequestMapping(value = "/profile/get")
	public @ResponseBody User getProfile(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {

		EntityManager em = DBUtility.startTransaction();
		ServiceHelper.setResponseHeaders(resp);

		User user = ServiceHelper.getSessionUser(em, sessionId);

		/*
		 * if (user == null){ ResponseHeader wrongResponse = new
		 * ResponseHeader(); wrongResponse.setType("Fail");
		 * wrongResponse.setMessage("Specified information is wrong!"); return
		 * wrongResponse; }
		 */
		return user;
	}
	
	//login function
	@RequestMapping(value = "/login")
    public
    @ResponseBody
    ResponseHeader login(
            @RequestParam(value = "userEmail") String userEmail,
            @RequestParam(value = "password") String password, HttpServletResponse resp) {

		ServiceHelper.setResponseHeaders(resp);

        EntityManager em = DBUtility.startTransaction();
        List<User> result =
                em.createQuery("FROM User U WHERE U.userEmail = :userEmail AND U.password = :password")
                        .setParameter("userEmail", userEmail)
                        .setParameter("password", password)
                        .getResultList();
        if (result != null && result.size() > 0) {

            String sessionId = new BigInteger(130, new Random()).toString(32).toUpperCase();

            em.persist(new Session(result.get(0), sessionId));
            DBUtility.commitTransaction(em);

            return new ResponseHeader(sessionId);
        } else {
            ResponseHeader wrongResponse = new ResponseHeader();
            wrongResponse.setType("Fail");
            wrongResponse.setMessage("Specified information is wrong!");
            return wrongResponse;
        }
    }
}
