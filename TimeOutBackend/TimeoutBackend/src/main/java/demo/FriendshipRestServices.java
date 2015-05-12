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

import common.DBUtility;
import entity.Friendship;
import entity.User;

@RestController
public class FriendshipRestServices {
	
	@RequestMapping(value = "/friends/my")
	@ResponseBody
	public List<User> getMyFriends(
            @RequestParam(value = "sessionId") String sessionId, HttpServletResponse resp) {
		ServiceHelper.setResponseHeaders(resp);
		return prepareFriendsForUser(sessionId);
	}
	
	private List<User> prepareFriendsForUser(String cookie) {
		EntityManager em = DBUtility.startTransaction();
		User user = ServiceHelper.getSessionUser(em, cookie);
		
		Query query = em
				.createQuery(
						"FROM Friendship F WHERE F.person = :person")
				.setParameter("person", user);
		
		List<Friendship> list = query.getResultList();
		List<User> returnList = new ArrayList<User>();
		
		for (Friendship friendship : list) {
			returnList.add(friendship.getFriend());
		}
	
		return returnList;
	}
}
