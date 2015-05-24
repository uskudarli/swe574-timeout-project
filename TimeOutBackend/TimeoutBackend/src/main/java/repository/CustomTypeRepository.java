package repository;

import helpers.ServiceHelper;
import helpers.ValidationHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import common.BusinessException;
import common.DBUtility;
import entity.Action;
import entity.Attribute;
import entity.AttributeValue;
import entity.Comment;
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
		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<Attribute>>() {
		}.getType();
		ArrayList<Attribute> t = gson.fromJson(attributesString, listType);
		List<Attribute> attributes = t;

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
			CustomType customType = getCustomTypeById(customTypeId);
			
			Query query = em.createQuery(
					"FROM Post P WHERE P.customType = :customType")
					.setParameter("customType", customType);
			returnVal = query.getResultList();
		}
		return returnVal;
	}
	
	public List<Post> getPostListByUser(User user) {
		List<Post> returnVal = null;
		if (user != null) {
			Query query = em.createQuery(
					"FROM Post P WHERE P.user = :user")
					.setParameter("user", user);
			returnVal = query.getResultList();
		}
		return returnVal;
	}
	
	public List<Post> getNewPostsByCustomTypeId(Long customTypeId) {
		List<Post> newPosts = null;
		if (customTypeId > 0) {
			Query query = em.createQuery(
					"FROM Post P order by P.postId desc").setMaxResults(15);
			newPosts = query.getResultList();
			
			for (int i = 0; i < newPosts.size(); i++) {
				if (newPosts.get(i).getCustomType().getCustomTypeId() != customTypeId) {
					newPosts.remove(i);
				}
			}
		}
		return newPosts;
	}
	
	public List<Comment> getNewCommentsByPost(Post post) {
		List<Comment> newComments = null;
		if (post != null) {
			Query query = em.createQuery(
					"FROM Comment C order by C.commentId desc").setMaxResults(15);
			newComments = query.getResultList();
			
			for (int i = 0; i < newComments.size(); i++) {
				if (newComments.get(i).getPost().getPostId() != post.getPostId()) {
					newComments.remove(i);
				}
			}
		}
		return newComments;
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
		
		Gson gson = new Gson();
				Type listType = new TypeToken<ArrayList<AttributeValue>>() {
				}.getType();
				ArrayList<AttributeValue> t = gson.fromJson(attributeValuesJsonListString, listType);
		ArrayList<AttributeValue> attributeValues = t;
		
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

		Gson gson = new Gson();
		Type type = new TypeToken<Post>() {}.getType();
		post = (Post)gson.fromJson(postJsonString, type);
		
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
