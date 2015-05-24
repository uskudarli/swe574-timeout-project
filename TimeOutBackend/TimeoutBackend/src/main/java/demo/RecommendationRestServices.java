package demo;

import common.BusinessException;
import common.DBUtility;
import common.ResponseHeader;
import entity.Action;
import entity.User;
import enums.ActionType;
import helpers.ServiceHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import recommendation.RecommendationEngine;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RecommendationRestServices {


	//edit user function
	@RequestMapping(value = "/insertRecommendation")
	 public @ResponseBody Object insertRecommendation(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);
		boolean updated = false;

		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			DBUtility.commitTransactionOnly(em);

			updated = RecommendationEngine.insertRecommendationsforUser(user);
		}catch (BusinessException e) {
//			DBUtility.rollbackTransaction(em);
			DBUtility.closeConnections();
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.closeConnections();
			return new ResponseHeader(false, e.getMessage());
		}
		DBUtility.closeConnections();

		if(updated){
			return new ResponseHeader();
		} else {
			return new ResponseHeader(false);
		}
	}

		@RequestMapping(value = "/getEventRecommendation")
	public @ResponseBody Object eventRecommendation(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);

		List<Action> recommendedEvents = new ArrayList<>();

		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);

			Query query = em.createQuery("SELECT Ar.action FROM ActionRecommendation Ar WHERE Ar.action.actionType = :actionType AND Ar.user = :user))")
					.setParameter("actionType", ActionType.EVENT.toString())
					.setParameter("user", user);
			recommendedEvents = query.getResultList();
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return recommendedEvents;
	}

	@RequestMapping(value = "/getGroupRecommendation")
	public @ResponseBody Object groupRecommendation(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);

		List<Action> recommendedGroups = new ArrayList<>();

		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);

			Query query = em.createQuery("SELECT Ar.action FROM ActionRecommendation Ar WHERE Ar.action.actionType = :actionType AND Ar.user = :user))")
					.setParameter("actionType", ActionType.GROUP.toString())
					.setParameter("user", user);
			recommendedGroups = query.getResultList();
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return recommendedGroups;
	}

	@RequestMapping(value = "/getUserRecommendation")
	public @ResponseBody Object userRecommendation(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);

		List<User> recommendedUsers = new ArrayList<>();

		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);

			Query query = em.createQuery("SELECT Ur.userRecommended2 FROM UserRecommendation Ur WHERE Ur.userRecommended1 = :user))")
					.setParameter("user", user);
			recommendedUsers = query.getResultList();
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return recommendedUsers;
	}


}
