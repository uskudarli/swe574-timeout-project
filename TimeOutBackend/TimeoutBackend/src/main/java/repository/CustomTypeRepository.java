package repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import common.DBUtility;
import entity.Action;
import entity.Attribute;
import entity.CustomType;
import entity.User;

public class CustomTypeRepository {

	EntityManager em;
	
	public CustomTypeRepository(EntityManager em){
		this.em = em;
	}
	
	public CustomType createCustomType(User user, Long actionId, String name,
			String attributesJsonListString) {
		CustomType cusType = new CustomType();
		cusType.setName(name);
		cusType.setUser(user);
        
		ActionRepository ar = new ActionRepository(em);
		Action action = ar.getActionById(actionId, "");
		cusType.setAction(action);
		
		insertAttributes(attributesJsonListString, cusType, em);
		
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

	public List<CustomType> getCustomTypeListByActionId(Long actionId) {
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
}
