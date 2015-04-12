package demo;

import common.DBUtility;
import common.ResponseHeader;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RestServices {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/register")
      @ResponseBody
      public boolean registerUser(@RequestParam(value="userName") String userName,
                                  @RequestParam(value="password") String password) {

        // Insert a few rows.
        EntityManager em = DBUtility.startTranscation();
        em.persist(new User(userName, password));
        DBUtility.commitTransaction(em);

        return true;
    }

    @RequestMapping(value = "/login")
    public @ResponseBody ResponseHeader login(@RequestParam(value="userName") String userName,
                                @RequestParam(value="password") String password) {
        EntityManager em = DBUtility.startTranscation();
        List<User> result = em
                .createQuery("FROM User")
                .getResultList();
        for (User g : result) {
            if(g.getUserName().equals(userName)){
                if(g.getPassword().equals(password)){
                    return new ResponseHeader();
                }
            }
        }

        DBUtility.commitTransaction(em);

        return new ResponseHeader();

    }

    @RequestMapping("/test")
    public boolean test() {
        Map<String, String> properties = DBUtility.putProperties();

        EntityManager em = DBUtility.startTranscation();

        em.persist(new Greeting("user", new Date(), "Hello!"));
        em.persist(new Greeting("user", new Date(), "Hi!"));

        DBUtility.commitTransaction(em);

        return true;
    }

    @RequestMapping("/searchTag")
    public ArrayList<String> searchTag(@RequestParam(value="tag") String tag) {
        RestTemplate restTemplate = new RestTemplate();

        String getItemIdUrl = "https://www.wikidata.org/w/api.php?action=wbgetentities&sites=enwiki&titles=" + tag + "&normalize=&format=json";
        Object getItemIdResponse = restTemplate.getForObject(getItemIdUrl, Object.class);
        String itemId = ((HashMap)Arrays.asList(((HashMap)((HashMap) getItemIdResponse).get("entities")).values()).get(0).iterator().next()).get("id").toString();

        String searchTagQueryUrl = "https://wdq.wmflabs.org/api?q=tree[" + itemId.substring(1) + "][31] OR tree[" + itemId.substring(1) +"][279]";
        Object searchTagQueryResponse = restTemplate.getForObject(searchTagQueryUrl, Object.class);
        ArrayList<Integer> searchTagResultList = (ArrayList<Integer>)((HashMap) searchTagQueryResponse).get("items");

        ArrayList<String> searchTagResults = new ArrayList<>();

        for(Integer searchTagResult : searchTagResultList){
            String getItemNameUrl = "https://www.wikidata.org/w/api.php?action=wbgetentities&ids=Q" + searchTagResult + "&props=labels&languages=en&format=json";
            Object getItemNameResponse = restTemplate.getForObject(getItemNameUrl, Object.class);
            String item = ((HashMap)(Arrays.asList(((HashMap)((HashMap)Arrays.asList(((HashMap)((HashMap) getItemNameResponse).get("entities")).values()).get(0).iterator().next()).get("labels")).values()).get(0).iterator().next())).get("value").toString();
            searchTagResults.add(item);
        }
        return searchTagResults;
    }
    
    @RequestMapping(value = "/event/create")
    @ResponseBody
    public Action createEvent(@RequestParam(value="eventName") String eventName
                              ,@RequestParam(value="eventDescription") String eventDescription
                              ,@RequestParam(value="startTime") Date startTime
                              ,@RequestParam(value="endTime") Date endTime
                              ,@RequestParam(value="invitedPeople") List<User> invitedPeople
                              ,@RequestParam(value="tag") String tagString) {
    						
      EntityManager em = DBUtility.startTranscation();
      Action action = new Action(eventName,eventDescription,"E",startTime,endTime);
      em.persist(action);
      
      insertInvitedPeople(invitedPeople, em, action);
      
      insertTagsOfActions(tagString, em, action);
      
      DBUtility.commitTransaction(em);

      return action;
  }
    
    @RequestMapping(value = "/group/create")
    @ResponseBody
    public Action createGroup(@RequestParam(value="groupName") String groupName
                              ,@RequestParam(value="groupDescription") String groupDescription
                              ,@RequestParam(value="invitedPeople") List<User> invitedPeople
                              ,@RequestParam(value="tag") String tagString) {
    						
      EntityManager em = DBUtility.startTranscation();
      
      Action action = new Action(groupName,groupDescription,"G");
      em.persist(action);
      
      insertInvitedPeople(invitedPeople, em, action);
      
      insertTagsOfActions(tagString, em, action);
      
      DBUtility.commitTransaction(em);

      return action;
  }

	private void insertTagsOfActions(String tagString, EntityManager em,
			Action action) {
		String delims = ";,";
		  String[] tagArray = tagString.split(delims);
		  List<String> tagList = Arrays.asList(tagArray);
		  for (int i=0; i < tagList.size(); i++){
			  String hql = "SELECT * FROM Tag T WHERE T.tagName = :tagName";
			  Query query = em.createQuery(hql);
			  query.setParameter("tagName", tagList.get(i));
			  List<Tag> results = query.getResultList();
			  Tag tag;
			  if (results == null || results.size() < 1){
				  tag = new Tag();
		    	  tag.setTagName(tagList.get(i));
		    	  em.persist(tag);
			  }else{
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
		for (int i=0; i < invitedPeople.size(); i++){
			  ActionUser actionUser = new ActionUser();
			  actionUser.setUser(invitedPeople.get(i));
			  actionUser.setAction(action);
			  actionUser.setActionUserStatus("I");
			  em.persist(actionUser);
		  }
	}
}
