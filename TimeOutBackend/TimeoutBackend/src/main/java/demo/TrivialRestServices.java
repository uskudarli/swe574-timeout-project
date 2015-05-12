package demo;

import java.util.Date;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import common.DBUtility;

import entity.Greeting;

@RestController
public class TrivialRestServices {

   
    @RequestMapping("/test")
    public boolean test() {
        Map<String, String> properties = DBUtility.putProperties();

        EntityManager em = DBUtility.startTransaction();

        em.persist(new Greeting("user", new Date(), "Hello!"));

        DBUtility.commitTransaction(em);

        return true;
    }
	
//	@RequestMapping(value = "/post/create")
//	@ResponseBody
//	public Post createPost(
//            @RequestParam(value = "sessionId") String sessionId, 
//            @RequestParam(value = "customTypeId") Long customTypeId,
//            HttpServletResponse resp) {
//		setResponseHeaders(resp);
//		return getCustomTypeListByActionId(sessionId, actionId);
//	}
	


//	@RequestMapping(value = "/friends/invite")
//	@ResponseBody
//	public List<User> inviteFriends(
//			@RequestHeader("Set-Cookie") String cookie, HttpServletResponse resp) {
//		setResponseHeaders(resp);
//		return inviteFriendsForUser(cookie);
//	}

}
