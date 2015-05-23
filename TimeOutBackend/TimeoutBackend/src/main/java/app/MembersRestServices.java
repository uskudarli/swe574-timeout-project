package demo;

import helpers.ServiceHelper;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import repository.ActionRepository;
import common.BusinessException;
import common.DBUtility;
import common.ResponseHeader;
import entity.Action;
import entity.ActionUser;
import entity.User;
import enums.ActionType;

@RestController
public class MembersRestServices {

	@RequestMapping(value = "/event/members")
	@ResponseBody
	public Object eventMembers(
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "actionId") Integer actionId,
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);
		List<ActionUser> actionUsers;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			ActionRepository ar = new ActionRepository(em);
			Action action = ar.getActionById(actionId.longValue(), ActionType.EVENT.toString());
			actionUsers = ar.getMembersOfAction(action);

			DBUtility.commitTransaction(em);
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}

		return actionUsers;
	}
	
	@RequestMapping(value = "/group/members")
	@ResponseBody
	public Object groupMembers(
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "actionId") Integer actionId,
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);
		List<ActionUser> actionUsers;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			ActionRepository ar = new ActionRepository(em);
			Action action = ar.getActionById(actionId.longValue(), ActionType.GROUP.toString());
			actionUsers = ar.getMembersOfAction(action);

			DBUtility.commitTransaction(em);
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}

		return actionUsers;
	}
	
	@RequestMapping(value = "/event/invite")
	@ResponseBody
	public Object inviteToEvent(
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "actionId") Integer actionId,
			@RequestParam(value = "invitedPeople") String invitedPeople, //json List<integer> olarak user idleri
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);

		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			ActionRepository ar = new ActionRepository(em);
			Action action = ar.getActionById(actionId.longValue(), ActionType.EVENT.toString());
			ar.insertInvitedPeople(invitedPeople, action);

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
	
	@RequestMapping(value = "/group/invite")
	@ResponseBody
	public Object inviteToGroup(
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "actionId") Integer actionId,
			@RequestParam(value = "invitedPeople") String invitedPeople, //json List<integer> olarak user idleri
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);

		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			ActionRepository ar = new ActionRepository(em);
			Action action = ar.getActionById(actionId.longValue(), ActionType.GROUP.toString());
			ar.insertInvitedPeople(invitedPeople, action);

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
	
	
	
	@RequestMapping(value = "/group/acceptInvitation")
	@ResponseBody
	public Object acceptInviteToGroup(
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "actionUserId") Integer actionUserId,
			HttpServletResponse resp) {
		
   //todo devam et
		
//		EntityManager em = ServiceHelper.initialize(resp);
//
//		try {
//			User user = ServiceHelper.getSessionUser(em, sessionId);
//			
//			ActionRepository ar = new ActionRepository(em);
//			Action action = ar.getActionById(actionId.longValue(), ActionType.GROUP.toString());
//			ar.insertInvitedPeople(invitedPeople, action);
//
//			DBUtility.commitTransaction(em);
//		}catch (BusinessException e) {
//			DBUtility.rollbackTransaction(em);
//			return new ResponseHeader(false, e.getCode(), e.getMessage());
//		}catch (Exception e) {
//			DBUtility.rollbackTransaction(em);
//			return new ResponseHeader(false, e.getMessage());
//		}

		return new ResponseHeader();
	}
}
