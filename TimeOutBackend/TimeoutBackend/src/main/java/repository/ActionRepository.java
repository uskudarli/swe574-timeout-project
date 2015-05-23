package repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import helpers.ServiceHelper;
import helpers.ValidationHelper;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import common.DBUtility;
import dto.ActionDTO;
import entity.Action;
import entity.ActionTag;
import entity.ActionUser;
import entity.Tag;
import entity.User;
import entity.UserTag;
import enums.ActionType;
import enums.ActionUserStatus;

public class ActionRepository {

	EntityManager em;

	public ActionRepository(EntityManager em) {
		this.em = em;
	}

	public Action getActionById(Long actionId, String actionType) {
		Action returnVal = null;
		if (actionId > 0) {
			Query query = null;
			if (actionType == ActionType.EVENT.toString()
					|| actionType == ActionType.GROUP.toString()) {
				query = em
						.createQuery(
								"FROM Action A WHERE A.actionId = :actionId AND "
										+ "A.actionType = :actionType")
						.setParameter("actionId", actionId)
						.setParameter("actionType", actionType);
			} else {
				query = em.createQuery(
						"FROM Action A WHERE A.actionId = :actionId")
						.setParameter("actionId", actionId);
			}

			returnVal = (Action) query.getSingleResult();
		}

		return returnVal;
	}

	public void insertAction(Action action) {
		em.persist(action);
	}

	public void insertCreator(User creatorUser, Action action) {

		ActionUser actionUser = new ActionUser();
		actionUser.setUser(creatorUser);
		actionUser.setAction(action);
		actionUser.setActionUserStatus(ActionUserStatus.CREATED);
		em.persist(actionUser);

	}



	// public void insertTagsOfActions(String tagString, Action action) {
	// if (tagString == null || tagString == "")
	// return;
	// String delims = ";|,";
	// String[] tagArray = tagString.split(delims);
	// List<String> tagList = Arrays.asList(tagArray);
	// for (int i = 0; i < tagList.size(); i++) {
	// String hql = "FROM Tag T WHERE T.tagName = :tagName";
	// Query query = em.createQuery(hql);
	// query.setParameter("tagName", tagList.get(i).trim());
	// List<Tag> results = query.getResultList();
	// Tag tag;
	// if (results == null || results.size() < 1) {
	// tag = new Tag();
	// tag.setTagName(tagList.get(i).trim());
	// em.persist(tag);
	// } else {
	// tag = results.get(0);
	// }
	//
	// ActionTag actionTag = new ActionTag();
	// actionTag.setAction(action);
	// actionTag.setTag(tag);
	// em.persist(actionTag);
	// }
	// }

	public List<ActionDTO> prepareCreatedActionForUser(User user,
			String actionType) {
		Query query = em
				.createQuery(
						"FROM ActionUser A WHERE A.actionUserStatus = :actionUserStatus AND A.user = :user")
				.setParameter("actionUserStatus", ActionUserStatus.CREATED)
				.setParameter("user", user);

		return prepareActionDTOList(actionType, query);
	}

	public List<ActionDTO> prepareInvitedActionForUser(User user,
			String actionType) {
		Query query = em
				.createQuery(
						"FROM ActionUser A WHERE A.actionUserStatus = :actionUserStatus AND A.user = :user")
				.setParameter("actionUserStatus", ActionUserStatus.INVITED)
				.setParameter("user", user);

		return prepareActionDTOList(actionType, query);
	}

	public List<ActionDTO> prepareActionForUser(User user, String actionType) {
		Query query = em.createQuery("FROM ActionUser A WHERE A.user = :user")
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

	public void insertTags(String tagString, User creator, Action action) {
		if (tagString == null || tagString == "")
			return;
		
		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<Tag>>() {
		}.getType();
		ArrayList<Tag> tagList = gson.fromJson(tagString, listType);
		
		for (Tag tag : tagList) {
			List<Tag> results = getTagsFromTagNameAndContextId(tag);
			
			if (results == null || results.size() < 1) {
				em.persist(tag);
			}

			insertActionTags(tag, action);
			insertUserTags(tag, creator);
		}
	}

	private List<Tag> getTagsFromTagNameAndContextId(Tag tag) {
		String hql = "FROM Tag T WHERE T.tagName = :tagName AND "
				+ "T.contectId = :contextId";
		Query query = em.createQuery(hql);
		query
		.setParameter("tagName", tag.getTagName())
		.setParameter("contectId", tag.getContextId());
		List<Tag> results = query.getResultList();
		return results;
	}

	private void insertUserTags(Tag tag, User creator) {
		String hql = "FROM UserTag UT WHERE UT.tagId = :tagId AND "
				+ "UT.userId = :userId";
		Query query = em.createQuery(hql);
		query
		.setParameter("tagId", tag.getTagId())
		.setParameter("userId", creator.getUserId());
		List<Tag> results = query.getResultList();
		
		if (results == null || results.size() == 0){
			UserTag userTag = new UserTag();
			userTag.setTag(tag);
			userTag.setUser(creator);
			em.persist(userTag);
		}
	}

	private void insertActionTags(Tag tag, Action action) {
		String hql = "FROM ActionTag AT WHERE AT.tagId = :tagId AND "
				+ "AT.actionId = :actionId";
		Query query = em.createQuery(hql);
		query
		.setParameter("tagId", tag.getTagId())
		.setParameter("actionId", action.getActionId());
		List<Tag> results = query.getResultList();
		
		if (results == null || results.size() == 0){
			ActionTag actionTag = new ActionTag();
			actionTag.setAction(action);
			actionTag.setTag(tag);
			em.persist(actionTag);
		}
	}
}
