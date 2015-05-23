package repository;

import helpers.ServiceHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import common.DBUtility;
import entity.Action;
import entity.Attribute;
import entity.CustomType;
import entity.Post;
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
        
        List<Attribute> attributes = ServiceHelper.parseListFromJsonString(attributesString);
        
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
			Query query = em.createQuery(
					"FROM CustomType C WHERE C.actionId = :actionId")
					.setParameter("actionId", actionId);
			returnVal = query.getResultList();
		}
		return returnVal;
	}

	public List<Post> getPostListByCustomTypeId(Long customTypeId) {
		List<Post> returnVal = null;
		if (customTypeId > 0) {
			Query query = em.createQuery(
					"FROM Post P WHERE P.customTypeId = :customTypeId")
					.setParameter("customTypeId", customTypeId);
			returnVal = query.getResultList();
		}
		return returnVal;
	}

	public List<Attribute> getAttributeListByCustomTypeId(Long customTypeId) {
		List<Attribute> returnVal = null;
		if (customTypeId > 0) {
			Query query = em.createQuery(
					"FROM Attribute A WHERE A.customTypeId = :customTypeId")
					.setParameter("customTypeId", customTypeId);
			returnVal = query.getResultList();
		}
		return returnVal;
	}

	public CustomType getCustomTypeById(Long customTypeId) {
		CustomType returnVal = null;
		if (customTypeId > 0) {
			Query query = em.createQuery(
					"FROM CustomType C WHERE C.customTypeId = :customTypeId")
					.setParameter("customTypeId", customTypeId);
			returnVal = (CustomType) query.getSingleResult();
		}
		return returnVal;
	}

	public CustomType editCustomType(Long customTypeId, String name) {
		CustomType customType = null;
		if (customTypeId > 0) {
			customType = getCustomTypeById(customTypeId);
			customType.setName(name);
			em.persist(customType);
		}
		return customType;
	}

	public Post getPostById(Long postId) {
		Post returnVal = null;
		if (postId > 0) {
			Query query = em.createQuery(
					"FROM Post C WHERE C.postId = :postId")
					.setParameter("postId", postId);
			returnVal = (Post) query.getSingleResult();
		}
		return returnVal;
	}

	public Post createPost(User user, Long customTypeId, String title,
			String text, String attributeValuesJsonListString) {
		Post post = null;
		if (user != null && customTypeId > 0 && !title.isEmpty()){
			post = new Post();
			post.setCustomType(getCustomTypeById(customTypeId));
			post.setTitle(title);
			post.setText(text);
			post.setTime(new Date());
			em.persist(post);
		}
		return post;
	}
}
