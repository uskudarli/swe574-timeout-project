package demo;

import common.BusinessException;
import common.DBUtility;
import common.ResponseHeader;
import dto.ActionDTO;
import dto.ActionMemberDTO;
import entity.Action;
import entity.Tag;
import entity.User;
import enums.ActionType;
import helpers.ServiceHelper;
import helpers.ValidationHelper;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import repository.ActionRepository;
import repository.MembersRepository;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class ActionRestServices {

	@RequestMapping(value = "/event/create")
	@ResponseBody
	public Object createEvent(
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "eventName") String eventName,
			@RequestParam(value = "eventDescription", required = false) String eventDescription,
			@RequestParam(value = "startTime", required = false) String startTimeString,
			@RequestParam(value = "endTime", required = false) String endTimeString,
			@RequestParam(value = "invitedPeople", required = false) String invitedPeople, //json List<integer> olarak user idleri
			@RequestParam(value = "tag", required = false) String tagString,//json List<Tag> olarak Tag objeleri (tagName + context)
			@RequestParam(value = "privacy", required = false) String privacy,
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);

		Action action;
		try {
			User creator = ServiceHelper.getSessionUser(em, sessionId);
			ValidationHelper.validateEvent(eventName);
			Date startTime = null;
			Date endTime = null;
			if (!ValidationHelper.isNullOrWhitespace(startTimeString))
				startTime = ServiceHelper.dateParser(startTimeString);
			if (!ValidationHelper.isNullOrWhitespace(endTimeString))
				endTime = ServiceHelper.dateParser(endTimeString);
			
			action = new Action(eventName, eventDescription,
					ActionType.EVENT.toString(), startTime, endTime);
			action.setPrivacy(privacy);
			
			ActionRepository ar = new ActionRepository(em);
			MembersRepository mr = new MembersRepository(em);
			action = ar.insertAction(action);

			ar.insertCreator(creator, action);

			if (!ValidationHelper.isNullOrWhitespace(invitedPeople))
				mr.insertInvitedPeople(invitedPeople, action);
			if (!ValidationHelper.isNullOrWhitespace(tagString))
				ar.insertTags(tagString, creator, action);

			DBUtility.commitTransaction(em);
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}

		return action;
	}

	@RequestMapping(value = "/group/create")
	@ResponseBody
	public Object createGroup(
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "groupName") String groupName,
			@RequestParam(value = "groupDescription", required = false) String groupDescription,
			@RequestParam(value = "invitedPeople", required = false) String invitedPeople,
			@RequestParam(value = "tag", required = false) String tagString,
			@RequestParam(value = "privacy", required = false) String privacy,
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);
		
		Action action;
		try {
			User creator = ServiceHelper.getSessionUser(em, sessionId);
			ValidationHelper.validateGroup(groupName);
			
			action = new Action(groupName, groupDescription,
					ActionType.GROUP.toString());
			action.setPrivacy(privacy);

			ActionRepository ar = new ActionRepository(em);
			MembersRepository mr = new MembersRepository(em);
			action = ar.insertAction(action);

			ar.insertCreator(creator, action);
			
			if (!ValidationHelper.isNullOrWhitespace(invitedPeople))
				mr.insertInvitedPeople(invitedPeople, action);
			if (!ValidationHelper.isNullOrWhitespace(tagString))
				ar.insertTags(tagString, creator, action);

			DBUtility.commitTransaction(em);
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}

		return action;
	}
	
	@RequestMapping(value = "/event/edit")
	@ResponseBody
	public Object editEvent(
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "event") String eventJsonString,
			@RequestParam(value = "tagList", required = false) String tagListJsonString,
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);

		Action action = null;
		Action result = null;
		List<Tag> tags = null;
		try {
			User creator = ServiceHelper.getSessionUser(em, sessionId);
			
			Gson gson = new Gson();
			Type type = new TypeToken<Action>() {}.getType();
			action = gson.fromJson(eventJsonString, type);
			
			if (!ValidationHelper.isNullOrWhitespace(tagListJsonString)){
				Type listType = new TypeToken<ArrayList<Tag>>() {}.getType();
				tags = gson.fromJson(tagListJsonString, listType);
			}
			
			ValidationHelper.validateEvent(action.getTitle());
			
			Date startTime = null ;
			Date endTime = null;
			if (!ValidationHelper.isNullOrWhitespace(action.getStartTime().toString())){
				startTime = ServiceHelper.dateParser(action.getStartTime().toString());
			}
			if (!ValidationHelper.isNullOrWhitespace(action.getEndTime().toString())){
				endTime = ServiceHelper.dateParser(action.getEndTime().toString());
			}
			action.setStartTime(startTime);
			action.setEndTime(endTime);
			
			ActionRepository ar = new ActionRepository(em);
			result = ar.editAction(action, tags);
			DBUtility.commitTransaction(em);
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}

		return result;
	}
	
	@RequestMapping(value = "/group/edit")
	@ResponseBody
	public Object editGroup(
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "group") String groupJsonString,
			@RequestParam(value = "tagList", required = false) String tagListJsonString,
			HttpServletResponse resp) {

		EntityManager em = ServiceHelper.initialize(resp);

		Action action = null;
		Action result = null;
		List<Tag> tags = null;
		try {
			User creator = ServiceHelper.getSessionUser(em, sessionId);
			
			Gson gson = new Gson();
			Type type = new TypeToken<Action>() {}.getType();
			action = gson.fromJson(groupJsonString, type);
			
			if (!ValidationHelper.isNullOrWhitespace(tagListJsonString)){
				Type listType = new TypeToken<ArrayList<Tag>>() {}.getType();
				tags = gson.fromJson(tagListJsonString, listType);
			}
			
			ValidationHelper.validateEvent(action.getTitle());
			
			Date startTime = null ;
			Date endTime = null;
			if (!ValidationHelper.isNullOrWhitespace(action.getStartTime().toString())){
				startTime = ServiceHelper.dateParser(action.getStartTime().toString());
			}
			if (!ValidationHelper.isNullOrWhitespace(action.getEndTime().toString())){
				endTime = ServiceHelper.dateParser(action.getEndTime().toString());
			}
			action.setStartTime(startTime);
			action.setEndTime(endTime);
			
			ActionRepository ar = new ActionRepository(em);
			result = ar.editAction(action, tags);
			DBUtility.commitTransaction(em);
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}

		return result;
	}

	@RequestMapping(value = "/event/created")
	@ResponseBody
	public Object getCreatedEvents(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		List<ActionDTO> actionDTOs;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			ActionRepository ar = new ActionRepository(em);

			actionDTOs = ar.prepareCreatedActionForUser(user, 
					ActionType.EVENT.toString());
			DBUtility.commitTransaction(em);
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return actionDTOs;
	}

	@RequestMapping(value = "/group/created")
	@ResponseBody
	public Object getCreatedGroups(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		List<ActionDTO> actionDTOs;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			ActionRepository ar = new ActionRepository(em);
			actionDTOs = ar.prepareCreatedActionForUser(user,
					ActionType.GROUP.toString());
			DBUtility.commitTransaction(em);
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return actionDTOs;
	}

	@RequestMapping(value = "/event/invited")
	@ResponseBody
	public Object getInvitedEvents(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {
		
		EntityManager em = ServiceHelper.initialize(resp);
		
		List<ActionDTO> actionDTOs;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			ActionRepository ar = new ActionRepository(em);
			
			actionDTOs = ar.prepareInvitedActionForUser(user,
					ActionType.EVENT.toString());
			DBUtility.commitTransaction(em);
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return actionDTOs;
	}

	@RequestMapping(value = "/group/invited")
	@ResponseBody
	public Object getInvitedGroups(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		List<ActionDTO> actionDTOs;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			ActionRepository ar = new ActionRepository(em);
			
			actionDTOs = ar.prepareInvitedActionForUser(user,
					ActionType.GROUP.toString());
			DBUtility.commitTransaction(em);
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return actionDTOs;
	}

	@RequestMapping(value = "/event/my")
	@ResponseBody
	public Object getMyEvents(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		List<ActionDTO> actionDTOs;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			ActionRepository ar = new ActionRepository(em);
			
			actionDTOs = ar.prepareActionForUser(user,
					ActionType.EVENT.toString());
			DBUtility.commitTransaction(em);
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return actionDTOs;
	}

	@RequestMapping(value = "/group/my")
	@ResponseBody
	public Object getMyGroups(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		List<ActionDTO> actionDTOs;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			ActionRepository ar = new ActionRepository(em);
			
			actionDTOs = ar.prepareActionForUser(user,
					ActionType.GROUP.toString());
			DBUtility.commitTransaction(em);
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return actionDTOs;
	}
	
	@RequestMapping(value = "/event/getById")
	@ResponseBody
	public Object getEventById(
            @RequestParam(value = "sessionId") String sessionId, 
            @RequestParam(value = "id") Long actionId,
            HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		Action action;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			ActionRepository ar = new ActionRepository(em);
			action = ar.getActionById(actionId, ActionType.EVENT.toString());
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return action;
	}
	
	@RequestMapping(value = "/group/getById")
	@ResponseBody
	public Object getGroupById(
            @RequestParam(value = "sessionId") String sessionId, 
            @RequestParam(value = "id") Long actionId,
            HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		Action action;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			ActionRepository ar = new ActionRepository(em);
			action = ar.getActionById(actionId, ActionType.GROUP.toString());
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return action;
	}
	
	@RequestMapping(value = "/group/new")
	@ResponseBody
	public Object getNewGroups(
            @RequestParam(value = "sessionId") String sessionId, 
            HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		List<ActionMemberDTO> newActions;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			ActionRepository ar = new ActionRepository(em);
			newActions = ar.getNewActions(ActionType.GROUP.toString());
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return newActions;
	}
	
	@RequestMapping(value = "/event/new")
	@ResponseBody
	public Object getNewEvents(
            @RequestParam(value = "sessionId") String sessionId, 
            HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		List<ActionMemberDTO> newActions;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			ActionRepository ar = new ActionRepository(em);
			newActions = ar.getNewActions(ActionType.EVENT.toString());
		}catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		}catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return newActions;
	}
}
