package demo;

import common.DBUtility;
import common.ResponseHeader;
import org.hibernate.Criteria;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.security.acl.Owner;
import java.util.*;

@RestController
public class RestServices {

	@RequestMapping(value = "/register")
	public @ResponseBody ResponseHeader registerUser(
			@RequestParam(value = "userName") String userName,
			@RequestParam(value = "password") String password) {

		// Insert a few rows.
		EntityManager em = DBUtility.startTransaction();
		em.persist(new User(userName, password));
		DBUtility.commitTransaction(em);

		return new ResponseHeader();
	}

	@RequestMapping(value = "/login")
	public @ResponseBody ResponseHeader login(
			@RequestParam(value = "userName") String userName,
			@RequestParam(value = "password") String password) {
		EntityManager em = DBUtility.startTransaction();
		List<User> result = em.createQuery("FROM User ").getResultList();
		for (User g : result) {
			if (g.getUserName().equals(userName)) {
				if (g.getPassword().equals(password)) {
					return new ResponseHeader();
				}
				else{
					return new ResponseHeader ();
				}
			}
		}

		DBUtility.commitTransaction(em);
		ResponseHeader wrongResponse = new ResponseHeader();
		wrongResponse.setType("Fail");
		wrongResponse.setMessage("Specified information is wrong!");
		return wrongResponse;

	}

	@RequestMapping("/test")
	public boolean test() {
		Map<String, String> properties = DBUtility.putProperties();

		EntityManager em = DBUtility.startTransaction();

		em.persist(new Greeting("user", new Date(), "Hello!"));

		DBUtility.commitTransaction(em);

		return true;
	}

	@RequestMapping("/searchTag")
	public ArrayList<String> searchTag(@RequestParam(value = "tag") String tag) {
		return findRelatedTags(tag);
	}

	@RequestMapping("/findRelatedGroupsforTag")
	public List<Action> findRelatedGroupsforTag(@RequestParam(value = "tag") String tag) {

		EntityManager em = DBUtility.startTransaction();

		ArrayList<String> relatedTags = findRelatedTags(tag);

		ArrayList<Action> relatedGroups = new ArrayList<>();

		Query query = em
				.createQuery(
						"FROM Action A INNER JOIN A.actionTags tags WHERE tags.tag.tagName IN (:relatedTags) AND A.actionType = :actionType")
				.setParameter("actionType",
						ActionType.GROUP.toString())
				.setParameter("relatedTags", relatedTags);

		List<Object[]> queryResult = query.getResultList();
		for(Object[] o : queryResult){
			relatedGroups.add((Action)o[0]);
		}
		return relatedGroups;
	}

	@RequestMapping(value = "/event/create")
	@ResponseBody
	public Action createEvent(
			@RequestParam(value = "eventName") String eventName,
			@RequestParam(value = "eventDescription", required = false) String eventDescription,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "invitedPeople", required = false) List<User> invitedPeople,
			@RequestParam(value = "tag", required = false) String tagString) {

		EntityManager em = DBUtility.startTransaction();
		Action action = new Action(eventName, eventDescription, ActionType.EVENT.toString(), startTime,
				endTime);
		em.persist(action);

		insertInvitedPeople(invitedPeople, em, action);

		insertTagsOfActions(tagString, em, action);

		DBUtility.commitTransaction(em);

		return action;
	}

	@RequestMapping(value = "/group/create")
	@ResponseBody
	public Action createGroup(
			@RequestParam(value = "groupName") String groupName,
			@RequestParam(value = "groupDescription", required = false) String groupDescription,
			@RequestParam(value = "invitedPeople", required = false) List<User> invitedPeople,
			@RequestParam(value = "tag", required = false) String tagString) {

		EntityManager em = DBUtility.startTransaction();

		Action action = new Action(groupName, groupDescription, ActionType.GROUP.toString());
		em.persist(action);

		insertInvitedPeople(invitedPeople, em, action);

		insertTagsOfActions(tagString, em, action);

		DBUtility.commitTransaction(em);

		return action;
	}

	@RequestMapping(value = "/event/getCreated")
	@ResponseBody
	public List<Action> getCreatedEvents(
			@RequestParam(value = "userId") String userId) {

		EntityManager em = DBUtility.startTransaction();

		Query query = em
				.createQuery(
						"FROM ActionUser A WHERE A.actionUserStatus = :actionUserStatus AND A.userId = :userId")
				.setParameter("actionUserStatus",
						ActionUserStatus.CREATED.toString())
				.setParameter("userId", userId);

		List<ActionUser> results = query.getResultList();
		List<Action> actionList = new ArrayList<>();

		for (ActionUser actionUser : results) {
			Action action = actionUser.getAction();
			if (action.getActionType().equals("E")) {
				actionList.add(actionUser.getAction());
			}
		}

		return actionList;
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

	private void insertInvitedPeople(List<User> invitedPeople,
			EntityManager em, Action action) {
		if (invitedPeople == null)
			return;
		if (invitedPeople.size() < 1)
			return;
		for (int i = 0; i < invitedPeople.size(); i++) {
			ActionUser actionUser = new ActionUser();
			actionUser.setUser(invitedPeople.get(i));
			actionUser.setAction(action);
			actionUser.setActionUserStatus(ActionUserStatus.INVITED);
			em.persist(actionUser);
			
		
			
		}
	}



	private ArrayList<String> findRelatedTags(String tag){
		RestTemplate restTemplate = new RestTemplate();

		String getItemIdUrl = "https://www.wikidata.org/w/api.php?action=wbgetentities&sites=enwiki&titles="
				+ tag + "&normalize=&format=json";
		Object getItemIdResponse = restTemplate.getForObject(getItemIdUrl,
				Object.class);
		String itemId = ((HashMap) Arrays
				.asList(((HashMap) ((HashMap) getItemIdResponse)
						.get("entities")).values()).get(0).iterator().next())
				.get("id").toString();

		String searchTagQueryUrl = "https://wdq.wmflabs.org/api?q=tree["
				+ itemId.substring(1) + "][31] OR tree[" + itemId.substring(1)
				+ "][279]";
		Object searchTagQueryResponse = restTemplate.getForObject(
				searchTagQueryUrl, Object.class);
		ArrayList<Integer> searchTagResultList = (ArrayList<Integer>) ((HashMap) searchTagQueryResponse)
				.get("items");

		ArrayList<String> searchTagResults = new ArrayList<>();

		for (Integer searchTagResult : searchTagResultList) {
			String getItemNameUrl = "https://www.wikidata.org/w/api.php?action=wbgetentities&ids=Q"
					+ searchTagResult
					+ "&props=labels&languages=en&format=json";
			Object getItemNameResponse = restTemplate.getForObject(
					getItemNameUrl, Object.class);
			String item = ((HashMap) (Arrays
					.asList(((HashMap) ((HashMap) Arrays
							.asList(((HashMap) ((HashMap) getItemNameResponse)
									.get("entities")).values()).get(0)
							.iterator().next()).get("labels")).values()).get(0)
					.iterator().next())).get("value").toString();
			searchTagResults.add(item);
		}
		return searchTagResults;
	}
}
