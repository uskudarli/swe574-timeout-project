package repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dto.ActionDTO;
import dto.ActionMemberDTO;
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

	public Action insertAction(Action action) {
		return em.merge(action);
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

	// public List<ActionDTO> prepareGroups(List<Action> actionIds, String
	// actionType) {
	// Query query = em
	// .createQuery(
	// "SELECT A FROM Action A WHERE A.actionUserStatus = :actionUserStatus AND A.user = :user")
	// .setParameter("actionIds", actionIds)
	//
	// return prepareActionDTOList(actionType, query);
	// }

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
			ActionDTO actionDTO = actionUser.getAction().createActionDTO();
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
				tag = em.merge(tag);
			} else {
				tag = results.get(0);
			}

			insertActionTags(tag, action);
			// insertUserTags(tag, creator);
		}
	}

	private List<Tag> getTagsFromTagNameAndContextId(Tag tag) {
		String hql = "FROM Tag T WHERE T.tagName = :tagName AND "
				+ "T.contextId = :contextId";
		Query query = em.createQuery(hql);
		query.setParameter("tagName", tag.getTagName()).setParameter(
				"contextId", tag.getContextId());
		List<Tag> results = query.getResultList();
		return results;
	}

	private void insertUserTags(Tag tag, User creator) {
		String hql = "FROM UserTag UT WHERE UT.tag = :tag AND "
				+ "UT.userId = :userId";
		Query query = em.createQuery(hql);
		query.setParameter("tag", tag).setParameter("userId",
				creator.getUserId());
		List<Tag> results = query.getResultList();

		if (results == null || results.size() == 0) {
			UserTag userTag = new UserTag();
			userTag.setTag(tag);
			userTag.setUser(creator);
			em.persist(userTag);
		}
	}

	private void insertActionTags(Tag tag, Action action) {
		String hql = "FROM ActionTag AT WHERE AT.tag = :tag AND "
				+ "AT.action = :action";
		Query query = em.createQuery(hql);
		query.setParameter("tag", tag).setParameter("action", action);
		List<Tag> results = query.getResultList();

		if (results == null || results.size() == 0) {
			ActionTag actionTag = new ActionTag();
			actionTag.setAction(action);
			actionTag.setTag(tag);
			em.persist(actionTag);
		}
	}

	public List<ActionMemberDTO> getNewActions(String actionType) {
		String hql = "FROM Action A order by A.actionId desc ";
		Query query = em.createQuery(hql);
		query.setMaxResults(30);

		MembersRepository mr = new MembersRepository(em);
		List<Action> actions = query.getResultList();
		List<ActionMemberDTO> results = new ArrayList<ActionMemberDTO>();

		for (int i = 0; i < actions.size(); i++) {
			if (!actions.get(i).getActionType().equals(actionType)) {
				actions.remove(i);
			}
		}

		for (int i = 0; i < actions.size(); i++) {
			Action action = actions.get(i);
			ActionMemberDTO actionMemberDTO = new ActionMemberDTO();
			actionMemberDTO.setAction(action);

			List<ActionUser> actionUsers = mr.getMembersOfAction(action);
			List<User> users = new ArrayList<User>();
			for (ActionUser item : actionUsers) {
				users.add(item.getUser());
			}
			actionMemberDTO.setUsers(users);
			actionMemberDTO.setCount(users.size());
			results.add(actionMemberDTO);
		}
		return results;
	}
}
