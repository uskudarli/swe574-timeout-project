package demo;

import common.DBUtility;
import common.ResponseHeader;
import dto.ActionDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.*;

@RestController
public class RestServices {

    @RequestMapping(value = "/register")
    public
    @ResponseBody
    ResponseHeader registerUser(
            @RequestParam(value = "userEmail") String userEmail,
            @RequestParam(value = "password") String password, HttpServletResponse resp) {

        setResponseHeaders(resp);

        if (!isValidEmailAddress(userEmail))
            return new ResponseHeader(false, "Not a valid e-mail address!");

        EntityManager em = DBUtility.startTransaction();
        em.persist(new User(userEmail, password));
        DBUtility.commitTransaction(em);

        return new ResponseHeader();
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    @RequestMapping(value = "/profile/edit")
    public
    @ResponseBody
    ResponseHeader editProfile(
            @RequestHeader("Set-Cookie") String cookie,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "Gsm", required = false) Long Gsm,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "birthdate", required = false) Date birthdate,
            @RequestParam(value = "about", required = false) String about,
            @RequestParam(value = "interests", required = false) String interests,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "languages", required = false) String languages, HttpServletResponse resp) {

        setResponseHeaders(resp);

        User user = getSessionUser(cookie);
        if (user == null) {
            ResponseHeader wrongResponse = new ResponseHeader();
            wrongResponse.setType("Fail");
            wrongResponse.setMessage("Specified information is wrong!");
            return wrongResponse;
        }
        if (user.getUserBasicInfo() == null) {
            user.setUserBasicInfo(new UserBasicInfo());
            user.getUserBasicInfo().setUser(user);
        }
        //user.getUserBasicInfo().setUserId(user.getUserId());
        if (user.getUserCommInfo() == null) {
            user.setUserCommInfo(new UserCommInfo());
            user.getUserCommInfo().setUser(user);
        }
        //user.getUserCommInfo().setUserId(user.getUserId());
        if (user.getUserExtraInfo() == null) {
            user.setUserExtraInfo(new UserExtraInfo());
            user.getUserExtraInfo().setUser(user);
        }
        //user.getUserExtraInfo().setUserId(user.getUserId());
//		if (user.getRole() == null){
//			user.setRole(new Role());
//			user.getRole().getUsers().add(user);
//		}

        if (firstName != "" && firstName != null)
            user.getUserBasicInfo().setFirstName(firstName);
        if (lastName != "" && lastName != null)
            user.getUserBasicInfo().setLastName(lastName);
        if (Gsm != null && Gsm > 0)
            user.getUserCommInfo().setMobilePhone(Gsm);
        if (address != "" && address != null)
            user.getUserCommInfo().setAddress(address);
        if (birthdate != null)
            user.getUserExtraInfo().setBirthDate(birthdate);
        if (about != "" && about != null)
            user.getUserExtraInfo().setAbout(about);
        if (interests != "" && interests != null)
            user.getUserExtraInfo().setInterests(interests);
        if (gender != "" && gender != null)
            user.getUserBasicInfo().setGender(gender);
        if (languages != "" && languages != null)
            user.getUserExtraInfo().setLanguages(languages);

        EntityManager em = DBUtility.startTransaction();
        User user1 = em.merge(user);
        em.merge(user1.getUserBasicInfo());
        em.merge(user1.getUserCommInfo());
        em.merge(user1.getUserExtraInfo());
        DBUtility.commitTransaction(em);

        return new ResponseHeader();
    }

    @RequestMapping(value = "/profile/get")
    public
    @ResponseBody
    User getProfile(
            @RequestHeader("Set-Cookie") String cookie, HttpServletResponse resp) {

        setResponseHeaders(resp);

        User user = getSessionUser(cookie);
/*		if (user == null){
            ResponseHeader wrongResponse = new ResponseHeader();
			wrongResponse.setType("Fail");
			wrongResponse.setMessage("Specified information is wrong!");
			return wrongResponse;
		}*/
        return user;
    }

    public User getSessionUser(String cookie) {
        EntityManager em = DBUtility.startTransaction();
        Session result =
                (Session) em.createQuery("FROM Session S WHERE S.cookie = :cookie")
                        .setParameter("cookie", cookie.substring(10))
                        .getSingleResult();
        return result.getUser();
    }

    @RequestMapping(value = "/login")
    public
    @ResponseBody
    ResponseHeader login(
            @RequestParam(value = "userEmail") String userEmail,
            @RequestParam(value = "password") String password, HttpServletResponse resp) {

        setResponseHeaders(resp);

        EntityManager em = DBUtility.startTransaction();
        List<User> result =
                em.createQuery("FROM User U WHERE U.userEmail = :userEmail AND U.password = :password")
                        .setParameter("userEmail", userEmail)
                        .setParameter("password", password)
                        .getResultList();
        if (result != null && result.size() > 0) {

            String userCookie = new BigInteger(130, new Random()).toString(32).toUpperCase();

            em.persist(new Session(result.get(0), userCookie));
            DBUtility.commitTransaction(em);

            return new ResponseHeader("sessionId=" + userCookie);
        } else {
            ResponseHeader wrongResponse = new ResponseHeader();
            wrongResponse.setType("Fail");
            wrongResponse.setMessage("Specified information is wrong!");
            return wrongResponse;
        }
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
    public ArrayList<String> searchTag(@RequestParam(value = "tag") String tag, HttpServletResponse resp) {
        setResponseHeaders(resp);
        return findRelatedTags(tag);
    }

    @RequestMapping("/findRelatedGroupsforTag")
    public List<Action> findRelatedGroupsforTag(@RequestParam(value = "tag") String tag, HttpServletResponse resp) {

        setResponseHeaders(resp);

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
        for (Object[] o : queryResult) {
            relatedGroups.add((Action) o[0]);
        }
        return relatedGroups;
    }

    @RequestMapping(value = "/event/create")
    @ResponseBody
    public Action createEvent(
            @RequestHeader("Set-Cookie") String cookie,
            @RequestParam(value = "eventName") String eventName,
            @RequestParam(value = "eventDescription", required = false) String eventDescription,
            @RequestParam(value = "startTime", required = false) Date startTime,
            @RequestParam(value = "endTime", required = false) Date endTime,
            @RequestParam(value = "invitedPeople", required = false) List<User> invitedPeople,
            @RequestParam(value = "tag", required = false) String tagString,
            @RequestParam(value = "privacy", required = false) String privacy, HttpServletResponse resp) {

        setResponseHeaders(resp);

        EntityManager em = DBUtility.startTransaction();

        Action action = new Action(eventName, eventDescription, ActionType.EVENT.toString(), startTime,
                endTime);
        action.setPrivacy(privacy);
        em.persist(action);

        insertCreator(getSessionUser(cookie), em, action);

        insertInvitedPeople(invitedPeople, em, action);

        insertTagsOfActions(tagString, em, action);

        DBUtility.commitTransaction(em);

        return action;
    }

    @RequestMapping(value = "/group/create")
    @ResponseBody
    public Action createGroup(
            @RequestHeader("Set-Cookie") String cookie,
            @RequestParam(value = "groupName") String groupName,
            @RequestParam(value = "groupDescription", required = false) String groupDescription,
            @RequestParam(value = "invitedPeople", required = false) List<User> invitedPeople,
            @RequestParam(value = "tag", required = false) String tagString,
            @RequestParam(value = "privacy", required = false) String privacy, HttpServletResponse resp) {

        setResponseHeaders(resp);

        EntityManager em = DBUtility.startTransaction();

        Action action = new Action(groupName, groupDescription, ActionType.GROUP.toString());
        action.setPrivacy(privacy);

        em.persist(action);

        insertCreator(getSessionUser(cookie), em, action);

        insertInvitedPeople(invitedPeople, em, action);

        insertTagsOfActions(tagString, em, action);

        DBUtility.commitTransaction(em);

        return action;
    }

    @RequestMapping(value = "/event/created")
    @ResponseBody
    public List<ActionDTO> getCreatedEvents(
            @RequestHeader("Set-Cookie") String cookie, HttpServletResponse resp) {
        setResponseHeaders(resp);
        return prepareCreatedActionForUser(cookie, "E");
    }

    @RequestMapping(value = "/group/created")
    @ResponseBody
    public List<ActionDTO> getCreatedGroups(
            @RequestHeader("Set-Cookie") String cookie, HttpServletResponse resp) {
        setResponseHeaders(resp);
        return prepareCreatedActionForUser(cookie, "G");
    }

    @RequestMapping(value = "/event/invited")
    @ResponseBody
    public List<ActionDTO> getInvitedEvents(
            @RequestHeader("Set-Cookie") String cookie, HttpServletResponse resp) {
        setResponseHeaders(resp);
        return prepareInvitedActionForUser(cookie, "E");
    }

    @RequestMapping(value = "/group/invited")
    @ResponseBody
    public List<ActionDTO> getInvitedGroups(
            @RequestHeader("Set-Cookie") String cookie, HttpServletResponse resp) {
        setResponseHeaders(resp);
        return prepareInvitedActionForUser(cookie, "G");
    }

    @RequestMapping(value = "/event/my")
	@ResponseBody
	public List<ActionDTO> getMyEvents(
			@RequestHeader("Set-Cookie") String cookie, HttpServletResponse resp) {
    	setResponseHeaders(resp);
		return prepareActionForUser(cookie, "E");
	}
	
	@RequestMapping(value = "/group/my")
	@ResponseBody
	public List<ActionDTO> getMyGroups(
			@RequestHeader("Set-Cookie") String cookie, HttpServletResponse resp) {
		setResponseHeaders(resp);
		return prepareActionForUser(cookie, "G");
	}

	private List<ActionDTO> prepareActionForUser(String cookie, String actionType) {
		EntityManager em = DBUtility.startTransaction();

		User user = getSessionUser(cookie);

		Query query = em
				.createQuery(
						"FROM ActionUser A WHERE A.user = :user")
				.setParameter("user", user);

		List<ActionUser> results = query.getResultList();
		List<ActionDTO> actionList = new ArrayList<>();

		for (ActionUser actionUser : results) {
			ActionDTO actionDTO = actionUser.getAction().getActionDTO();
			if (actionUser.getAction().getActionType().equals(actionType)) {
				actionList.add(actionDTO);
			}
		}

		return actionList;
	}

	private List<ActionDTO> prepareInvitedActionForUser(String cookie,
			String actionType) {
		EntityManager em = DBUtility.startTransaction();

		User user = getSessionUser(cookie);

		Query query = em
				.createQuery(
						"FROM ActionUser A WHERE A.actionUserStatus = :actionUserStatus AND A.user = :user")
				.setParameter("actionUserStatus",
						ActionUserStatus.INVITED)
				.setParameter("user", user);

		List<ActionUser> results = query.getResultList();
		List<ActionDTO> actionList = new ArrayList<>();

		for (ActionUser actionUser : results) {
			ActionDTO actionDTO = actionUser.getAction().getActionDTO();
			if (actionUser.getAction().getActionType().equals(actionType)) {
				actionList.add(actionDTO);
			}
		}

		return actionList;
	}

    private List<ActionDTO> prepareCreatedActionForUser(String cookie, String actionType) {
        EntityManager em = DBUtility.startTransaction();

        User user = getSessionUser(cookie);

        Query query = em
                .createQuery(
                        "FROM ActionUser A WHERE A.actionUserStatus = :actionUserStatus AND A.user = :user")
                .setParameter("actionUserStatus",
                        ActionUserStatus.CREATED)
                .setParameter("user", user);

        List<ActionUser> results = query.getResultList();
        List<ActionDTO> actionList = new ArrayList<>();

        for (ActionUser actionUser : results) {
            ActionDTO actionDTO = actionUser.getAction().getActionDTO();
            if (actionUser.getAction().getActionType().equals(actionType)) {
                actionList.add(actionDTO);
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

    private void insertCreator(User creatorUser,
                               EntityManager em, Action action) {

        ActionUser actionUser = new ActionUser();
        actionUser.setUser(creatorUser);
        actionUser.setAction(action);
        actionUser.setActionUserStatus(ActionUserStatus.CREATED);
        em.persist(actionUser);

    }


    private ArrayList<String> findRelatedTags(String tag) {
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

    private void setResponseHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Set-Cookie");
    }
}
