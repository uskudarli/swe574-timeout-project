package demo;

import helpers.ServiceHelper;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import repository.FriendshipRepository;
import common.BusinessException;
import common.DBUtility;
import common.ErrorMessages;
import common.ResponseHeader;
import entity.Friendship;
import entity.User;

@RestController
public class FriendshipRestServices {

	@RequestMapping(value = "/friends/my")
	@ResponseBody
	public Object getMyFriends(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {
		List<Friendship> friendships;

		EntityManager em = ServiceHelper.initialize(resp);
		
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			FriendshipRepository fr = new FriendshipRepository(em);
			friendships = fr.prepareFriendsForUser(user);

			DBUtility.commitTransaction(em);
		} catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		} catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return friendships;
	}
	
	@RequestMapping(value = "/friends/invite")
	@ResponseBody
	public Object inviteFriends(
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "userIds") String friendsString,//json List<Integer> olarak user idleri
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);
		
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			FriendshipRepository fr = new FriendshipRepository(em);
			boolean result = fr.inviteFriends(user, friendsString);
			if (!result)
				ServiceHelper.businessError(
						ErrorMessages.inviteFriendFailCode, ErrorMessages.inviteFriendFail);

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
	
	@RequestMapping(value = "/friends/accept")
	@ResponseBody
	public Object acceptFriends(
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "friendshipIds") String friendshipsString,//json List<integer> olarak Friendship idleri
			HttpServletResponse resp) {
		
		EntityManager em = ServiceHelper.initialize(resp);
		
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			FriendshipRepository fr = new FriendshipRepository(em);
			boolean result = fr.acceptFriends(user, friendshipsString);
			if (!result)
				ServiceHelper.businessError(
						ErrorMessages.inviteFriendFailCode, ErrorMessages.inviteFriendFail);

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
	
	@RequestMapping(value = "/friends/reject")
	@ResponseBody
	public Object rejectFriends(
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "friendshipIds") String friendshipsString,//json List<integer> olarak Friendship idleri
			HttpServletResponse resp) {
		
		EntityManager em = ServiceHelper.initialize(resp);
		
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			FriendshipRepository fr = new FriendshipRepository(em);
			boolean result = fr.rejectFriends(user, friendshipsString);
			if (!result)
				ServiceHelper.businessError(
						ErrorMessages.inviteFriendFailCode, ErrorMessages.inviteFriendFail);

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
}
