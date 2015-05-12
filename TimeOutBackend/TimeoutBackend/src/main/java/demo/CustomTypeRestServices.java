package demo;

import helpers.ServiceHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import repository.ActionRepository;
import common.DBUtility;
import entity.Action;
import entity.Attribute;
import entity.CustomType;
import entity.User;

@RestController
public class CustomTypeRestServices {

	@RequestMapping(value = "/customType/create")
	@ResponseBody
	public CustomType createCustomType(
            @RequestParam(value = "sessionId") String sessionId, 
            @RequestParam(value = "actionId") Long actionId,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "attributes", required = false) String attributesJsonListString,
            HttpServletResponse resp) {
		ServiceHelper.setResponseHeaders(resp);
		return createCustomType(sessionId, actionId, name, attributesJsonListString);
	}
	
	@RequestMapping(value = "/customType/getListByActionId")
	@ResponseBody
	public List<CustomType> getCustomTypeListByActionId(
            @RequestParam(value = "sessionId") String sessionId, 
            @RequestParam(value = "actionId") Long actionId,
            HttpServletResponse resp) {
		ServiceHelper.setResponseHeaders(resp);
		return getCustomTypeListByActionId(sessionId, actionId);
	}
	
	private List<CustomType> getCustomTypeListByActionId(String sessionId,
			Long actionId) {
		EntityManager em = DBUtility.startTransaction();
		User user = ServiceHelper.getSessionUser(em, sessionId);

		List<CustomType> returnVal = null;
		
		if (actionId > 0) {
			Query query = null;
			query = em.createQuery(
					"FROM CustomType C WHERE C.actionId = :actionId")
					.setParameter("actionId", actionId);
			returnVal = query.getResultList();
		}
		return returnVal;
	}
	
	private CustomType createCustomType(String sessionId, Long actionId,
			String name, String attributesString) {
		EntityManager em = DBUtility.startTransaction();
		User user = ServiceHelper.getSessionUser(em, sessionId);
		
		CustomType cusType = new CustomType();
		cusType.setName(name);
		cusType.setUser(user);
        
		ActionRepository ar = new ActionRepository(em);
		Action action = ar.getActionById(actionId, "");
		cusType.setAction(action);
		
		insertAttributes(attributesString, cusType, em);
		
        em.persist(cusType); 

        DBUtility.commitTransaction(em);

        return cusType;
	}
	
	private void insertAttributes(String attributesString, CustomType cusType, EntityManager em) {
		if (attributesString == null || attributesString == "")
            return;
        
        List<Attribute> attributes = null;
        Gson gson = new Gson();
        
        Type listType = new TypeToken<ArrayList<Attribute>>() {}.getType();

        attributes = gson.fromJson(attributesString, listType);
        
        for (int i = 0; i < attributes.size(); i++) {
        	Attribute attribute = new Attribute();
        	attribute.setAttributeKey(attributes.get(i).getAttributeKey());
        	//attribute.setAttributeValue(attributes.get(i).getAttributeValue());
        	attribute.setCustomType(cusType);
            em.persist(attribute);
        }
	}
}
