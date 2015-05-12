package demo;

import helpers.ServiceHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import repository.ActionRepository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import common.DBUtility;
import dto.ActionDTO;
import entity.Action;
import entity.ActionTag;
import entity.ActionUser;
import entity.Session;
import entity.Tag;
import entity.User;
import enums.ActionType;
import enums.ActionUserStatus;

@RestController
public class ActionRestServices {

	@RequestMapping(value = "/event/create")
	@ResponseBody
	public Action createEvent(
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "eventName") String eventName,
			@RequestParam(value = "eventDescription", required = false) String eventDescription,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "invitedPeople", required = false) String invitedPeople,
			@RequestParam(value = "tag", required = false) String tagString,
			@RequestParam(value = "privacy", required = false) String privacy,
			HttpServletResponse resp) {

		ServiceHelper.setResponseHeaders(resp);

		EntityManager em = DBUtility.startTransaction();

		Action action = new Action(eventName, eventDescription,
				ActionType.EVENT.toString(), startTime, endTime);
		action.setPrivacy(privacy);
		em.persist(action);

		insertCreator(ServiceHelper.getSessionUser(em, sessionId), em, action);

		insertInvitedPeople(invitedPeople, em, action);

		insertTagsOfActions(tagString, em, action);

		DBUtility.commitTransaction(em);

		return action;
	}

	@RequestMapping(value = "/group/create")
	@ResponseBody
	public Action createGroup(
			@RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "groupName") String groupName,
			@RequestParam(value = "groupDescription", required = false) String groupDescription,
			@RequestParam(value = "invitedPeople", required = false) String invitedPeople,
			@RequestParam(value = "tag", required = false) String tagString,
			@RequestParam(value = "privacy", required = false) String privacy,
			HttpServletResponse resp) {

		ServiceHelper.setResponseHeaders(resp);

		EntityManager em = DBUtility.startTransaction();

		Action action = new Action(groupName, groupDescription,
				ActionType.GROUP.toString());
		action.setPrivacy(privacy);

		em.persist(action);

		insertCreator(ServiceHelper.getSessionUser(em, sessionId), em, action);

		insertInvitedPeople(invitedPeople, em, action);

		insertTagsOfActions(tagString, em, action);

		DBUtility.commitTransaction(em);

		return action;
	}

	@RequestMapping(value = "/event/created")
	@ResponseBody
	public List<ActionDTO> getCreatedEvents(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {
		ServiceHelper.setResponseHeaders(resp);
		return prepareCreatedActionForUser(sessionId, ActionType.EVENT.toString());
	}

	@RequestMapping(value = "/group/created")
	@ResponseBody
	public List<ActionDTO> getCreatedGroups(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {
		ServiceHelper.setResponseHeaders(resp);
		return prepareCreatedActionForUser(sessionId, ActionType.GROUP.toString());
	}

	@RequestMapping(value = "/event/invited")
	@ResponseBody
	public List<ActionDTO> getInvitedEvents(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {
		ServiceHelper.setResponseHeaders(resp);
		return prepareInvitedActionForUser(sessionId, ActionType.EVENT.toString());
	}

	@RequestMapping(value = "/group/invited")
	@ResponseBody
	public List<ActionDTO> getInvitedGroups(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {
		ServiceHelper.setResponseHeaders(resp);
		return prepareInvitedActionForUser(sessionId, ActionType.GROUP.toString());
	}

	@RequestMapping(value = "/event/my")
	@ResponseBody
	public List<ActionDTO> getMyEvents(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {
		ServiceHelper.setResponseHeaders(resp);
		return prepareActionForUser(sessionId, ActionType.EVENT.toString());
	}

	@RequestMapping(value = "/group/my")
	@ResponseBody
	public List<ActionDTO> getMyGroups(
			@RequestParam(value = "sessionId") String sessionId,
			HttpServletResponse resp) {
		ServiceHelper.setResponseHeaders(resp);
		return prepareActionForUser(sessionId, ActionType.GROUP.toString());
	}
	
	@RequestMapping(value = "/event/getById")
	@ResponseBody
	public Action getEventById(
            @RequestParam(value = "sessionId") String sessionId, 
            @RequestParam(value = "id") Long actionId,
            HttpServletResponse resp) {
		ServiceHelper.setResponseHeaders(resp);
		EntityManager em = DBUtility.startTransaction();
		ActionRepository ar = new ActionRepository(em);
		return ar.getActionById(actionId, ActionType.EVENT.toString());
	}
	
	@RequestMapping(value = "/group/getById")
	@ResponseBody
	public Action getGroupById(
            @RequestParam(value = "sessionId") String sessionId, 
            @RequestParam(value = "id") Long actionId,
            HttpServletResponse resp) {
		ServiceHelper.setResponseHeaders(resp);
		EntityManager em = DBUtility.startTransaction();
		ActionRepository ar = new ActionRepository(em);
		return ar.getActionById(actionId, ActionType.GROUP.toString());
	}

	private void insertCreator(User creatorUser, EntityManager em, Action action) {

		ActionUser actionUser = new ActionUser();
		actionUser.setUser(creatorUser);
		actionUser.setAction(action);
		actionUser.setActionUserStatus(ActionUserStatus.CREATED);
		em.persist(actionUser);

	}

	private void insertInvitedPeople(String invitedPeopleString,
			EntityManager em, Action action) {
		if (invitedPeopleString == null || invitedPeopleString == "")
			return;
		// if (invitedPeople.size() < 1)
		// return;

		List<Integer> invitedPeople = null;
		Gson gson = new Gson();

		Type listType = new TypeToken<ArrayList<Integer>>() {
		}.getType();

		invitedPeople = gson.fromJson(invitedPeopleString, listType);

		for (int i = 0; i < invitedPeople.size(); i++) {

			ActionUser actionUser = new ActionUser();
			Query query = em
					.createQuery("FROM User U WHERE U.userId = :userId");
			query.setParameter("userId", invitedPeople.get(i).longValue());
			actionUser.setUser((User) query.getSingleResult());

			actionUser.setAction(action);
			actionUser.setActionUserStatus(ActionUserStatus.INVITED);
			em.persist(actionUser);
		}
	}

	private void insertTagsOfActions(String tagString, EntityManager em,
			Action action) {
		if (tagString == null || tagString == "")
			return;
		String delims = ";|,";
		String[] tagArray = tagString.split(delims);
		List<String> tagList = Arrays.asList(tagArray);
		for (int i = 0; i < tagList.size(); i++) {
			String hql = "FROM Tag T WHERE T.tagName = :tagName";
			Query query = em.createQuery(hql);
			query.setParameter("tagName", tagList.get(i).trim());
			List<Tag> results = query.getResultList();
			Tag tag;
			if (results == null || results.size() < 1) {
				tag = new Tag();
				tag.setTagName(tagList.get(i).trim());
				em.persist(tag);
			} else {
				tag = results.get(0);
			}

			ActionTag actionTag = new ActionTag();
			actionTag.setAction(action);
			actionTag.setTag(tag);
			em.persist(actionTag);
		}
	}
	
    private List<ActionDTO> prepareCreatedActionForUser(String cookie, String actionType) {
        EntityManager em = DBUtility.startTransaction();

        User user = ServiceHelper.getSessionUser(em, cookie);

        Query query = em
                .createQuery(
                        "FROM ActionUser A WHERE A.actionUserStatus = :actionUserStatus AND A.user = :user")
                .setParameter("actionUserStatus",
                        ActionUserStatus.CREATED)
                .setParameter("user", user);

        return prepareActionDTOList(actionType, query);
    }
    


	private List<ActionDTO> prepareActionDTOList(String actionType, Query query) {
		List<ActionUser> results = query.getResultList();
        List<ActionDTO> actionList = new ArrayList<>();
		Set<Long> actionIds = new HashSet<Long>();

		for (ActionUser actionUser : results) {
			ActionDTO actionDTO = actionUser.getAction().getActionDTO();
			if (actionUser.getAction().getActionType().equals(actionType)
					&& !actionIds.contains(actionDTO.getActionId())) {
				actionList.add(actionDTO);
				actionIds.add(actionDTO.getActionId());
			}
		}

        return actionList;
	}

	private List<ActionDTO> prepareActionForUser(String cookie, String actionType) {
		EntityManager em = DBUtility.startTransaction();

		User user = ServiceHelper.getSessionUser(em, cookie);

		Query query = em
				.createQuery(
						"FROM ActionUser A WHERE A.user = :user")
				.setParameter("user", user);

		return prepareActionDTOList(actionType, query);
	}

	private List<ActionDTO> prepareInvitedActionForUser(String cookie,
			String actionType) {
		EntityManager em = DBUtility.startTransaction();

		User user = ServiceHelper.getSessionUser(em, cookie);

		Query query = em
				.createQuery(
						"FROM ActionUser A WHERE A.actionUserStatus = :actionUserStatus AND A.user = :user")
				.setParameter("actionUserStatus",
						ActionUserStatus.INVITED)
				.setParameter("user", user);

		return prepareActionDTOList(actionType, query);
	}

}
