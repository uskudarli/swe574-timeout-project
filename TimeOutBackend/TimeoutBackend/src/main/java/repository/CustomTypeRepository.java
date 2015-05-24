package repository;

import helpers.ServiceHelper;
import helpers.ValidationHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import common.BusinessException;
import common.DBUtility;

import entity.Action;
import entity.Attribute;
import entity.AttributeValue;
import entity.CustomType;
import entity.Post;
import entity.User;

public class CustomTypeRepository {

	EntityManager em;

	public CustomTypeRepository(EntityManager em) {
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

	private void insertAttributes(String attributesString, CustomType cusType,
			EntityManager em) {
		if (attributesString == null || attributesString == "")
			return;

		List<Attribute> attributes = ServiceHelper
				.parseListFromJsonString(attributesString);

		for (int i = 0; i < attributes.size(); i++) {
			Attribute attribute = new Attribute();
			attribute.setAttributeKey(attributes.get(i).getAttributeKey());
			// attribute.setAttributeValue(attributes.get(i).getAttributeValue());
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
			Query query = em
					.createQuery("FROM Post C WHERE C.postId = :postId")
					.setParameter("postId", postId);
			returnVal = (Post) query.getSingleResult();
		}
		return returnVal;
	}

	public Post createPost(User user, Long customTypeId, String title,
			String text, String attributeValuesJsonListString) {
		Post post = null;
		if (user == null || customTypeId == 0 || title.isEmpty())
			return null;

		post = new Post();
		post.setCustomType(getCustomTypeById(customTypeId));
		post.setTitle(title);
		post.setText(text);
		post.setTime(new Date());
		Post post1 = em.merge(post);

		mergeAttributeValues(attributeValuesJsonListString, post1);

		return post1;
	}

	private void mergeAttributeValues(String attributeValuesJsonListString,
			Post post1) {
		ArrayList<AttributeValue> attributeValues = ServiceHelper
				.parseListFromJsonString(attributeValuesJsonListString);
		Set<AttributeValue> attributeValuesSet = new HashSet<>(attributeValues);

		post1.setAttributeValues(attributeValuesSet);
		for (AttributeValue item : attributeValuesSet) {
			item.setPost(post1);
			em.merge(item);
		}
	}

	public Post editPost(User user, String postJsonString,
			String attributeValuesJsonListString) throws BusinessException {
		Post post = null;
		if (user == null || ValidationHelper.isNullOrWhitespace(postJsonString))
			return null;

		post = (Post) ServiceHelper.parseObjectFromJsonString(postJsonString);
		ValidationHelper.validatePost(post);

		Post post1 = em.merge(post);
		mergeAttributeValues(attributeValuesJsonListString, post1);

		return post;
	}

	public Attribute getAttributeById(Long attributeId) {
		Attribute returnVal = null;
		if (attributeId > 0) {
			Query query = em.createQuery(
					"FROM Attribute A WHERE A.attributeId = :attributeId")
					.setParameter("attributeId", attributeId);
			returnVal = (Attribute) query.getSingleResult();
		}
		return returnVal;
	}
}
