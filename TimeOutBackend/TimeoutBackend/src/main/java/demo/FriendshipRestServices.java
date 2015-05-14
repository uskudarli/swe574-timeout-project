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
		List<User> users;

		EntityManager em = ServiceHelper.initialize(resp);
		
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			FriendshipRepository fr = new FriendshipRepository(em);
			users = fr.prepareFriendsForUser(user);

			DBUtility.commitTransaction(em);
		} catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		} catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return users;
	}
}
