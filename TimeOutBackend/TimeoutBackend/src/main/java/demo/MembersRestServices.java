package demo;

import helpers.ServiceHelper;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import repository.ActionRepository;
import repository.MembersRepository;

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
			MembersRepository mr = new MembersRepository(em);
			Action action = ar.getActionById(actionId.longValue(), ActionType.EVENT.toString());
			actionUsers = mr.getMembersOfAction(action);

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
			MembersRepository mr = new MembersRepository(em);
			Action action = ar.getActionById(actionId.longValue(), ActionType.GROUP.toString());
			actionUsers = mr.getMembersOfAction(action);

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
			@RequestParam(value = "invitedPeople") String invitedPeople, //json List<Integer> olarak user idleri
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);

		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			ActionRepository ar = new ActionRepository(em);
			MembersRepository mr = new MembersRepository(em);
			Action action = ar.getActionById(actionId.longValue(), ActionType.EVENT.toString());
			mr.insertInvitedPeople(invitedPeople, action);

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
			@RequestParam(value = "invitedPeople") String invitedPeople, //json List<Integer> olarak user idleri
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);

		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			ActionRepository ar = new ActionRepository(em);
			MembersRepository mr = new MembersRepository(em);
			Action action = ar.getActionById(actionId.longValue(), ActionType.GROUP.toString());
			mr.insertInvitedPeople(invitedPeople, action);

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
			@RequestParam(value = "actionIds") String actionIdsString, //json List<Integer> olarak action idleri
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);

		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			ActionRepository ar = new ActionRepository(em);
			MembersRepository mr = new MembersRepository(em);
			
			ArrayList<Integer> actionIds = ServiceHelper.parseListFromJsonString(actionIdsString);
			
			for (Integer item : actionIds){
				Action action = ar.getActionById(item.longValue(), ActionType.GROUP.toString());
				mr.acceptInvitation(user, action);
			}
			
//			List<Action> actions = mr.getActionListByIds(actionIdsString)
			//Action action = ar.getActionById(actionId.longValue(), ActionType.GROUP.toString());
//			ar.insertInvitedPeople(invitedPeople, action);
//
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
}
