package demo;

import helpers.ServiceHelper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import repository.CustomTypeRepository;

import common.BusinessException;
import common.DBUtility;
import common.ResponseHeader;

import entity.Attribute;
import entity.CustomType;
import entity.Post;
import entity.User;

@RestController
public class CustomTypeRestServices {

	@RequestMapping(value = "/customType/create")
	@ResponseBody
	public Object createCustomType(
            @RequestParam(value = "sessionId") String sessionId, 
            @RequestParam(value = "actionId") Long actionId,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "attributes", required = false) String attributesJsonListString,
            HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		CustomType customType;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			CustomTypeRepository cr = new CustomTypeRepository(em);
			
			customType = cr.createCustomType(user, actionId, name, attributesJsonListString);
			DBUtility.commitTransaction(em);
		} catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		} catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return customType;
	}
	
	@RequestMapping(value = "/customType/edit")
	@ResponseBody
	public Object editCustomType(
            @RequestParam(value = "sessionId") String sessionId, 
            @RequestParam(value = "customTypeId") Long customTypeId,
            @RequestParam(value = "name") String name,
            HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		CustomType customType;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			CustomTypeRepository cr = new CustomTypeRepository(em);
			
			customType = cr.editCustomType(customTypeId, name);
			DBUtility.commitTransaction(em);
		} catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		} catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return customType;
	}
	
	@RequestMapping(value = "/customType/getById")
	@ResponseBody
	public Object getCustomTypeById(
            @RequestParam(value = "sessionId") String sessionId, 
            @RequestParam(value = "customTypeId") Long customTypeId,
            HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		CustomType customType;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			CustomTypeRepository cr = new CustomTypeRepository(em);
			customType = cr.getCustomTypeById(customTypeId);
			DBUtility.commitTransaction(em);
		} catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		} catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return customType;
	}
	
	@RequestMapping(value = "/customType/getListByActionId")
	@ResponseBody
	public Object getCustomTypeListByActionId(
            @RequestParam(value = "sessionId") String sessionId, 
            @RequestParam(value = "actionId") Long actionId,
            HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		List<CustomType> customTypes;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			CustomTypeRepository cr = new CustomTypeRepository(em);
			customTypes = cr.getCustomTypeListByActionId(actionId);
			DBUtility.commitTransaction(em);
		} catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		} catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return customTypes;
	}
	
	@RequestMapping(value = "/post/create")
	@ResponseBody
	public Object createPost(
            @RequestParam(value = "sessionId") String sessionId, 
            @RequestParam(value = "customTypeId") Long customTypeId,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "attributeValues", required = false) String attributeValuesJsonListString,
            HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		Post post;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			CustomTypeRepository cr = new CustomTypeRepository(em);
			
			post = cr.createPost(user, customTypeId, title, text, attributeValuesJsonListString);
			DBUtility.commitTransaction(em);
		} catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		} catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return post;
	}
	
	@RequestMapping(value = "/post/edit")
	@ResponseBody
	public Object editPost(
            @RequestParam(value = "sessionId") String sessionId, 
            @RequestParam(value = "post") String postJsonString,
            @RequestParam(value = "attributeValues", required = false) String attributeValuesJsonListString,
            HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		Post post;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			CustomTypeRepository cr = new CustomTypeRepository(em);
			
			post = cr.editPost(user, postJsonString, attributeValuesJsonListString);
			DBUtility.commitTransaction(em);
		} catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		} catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return post;
	}
	
	@RequestMapping(value = "/post/getListByCustomTypeId")
	@ResponseBody
	public Object getPostListByCustomTypeId(
            @RequestParam(value = "sessionId") String sessionId, 
            @RequestParam(value = "customTypeId") Long customTypeId,
            HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		List<Post> posts;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			CustomTypeRepository cr = new CustomTypeRepository(em);
			posts = cr.getPostListByCustomTypeId(customTypeId);
			DBUtility.commitTransaction(em);
		} catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		} catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return posts;
	}
	
	@RequestMapping(value = "/post/getById")
	@ResponseBody
	public Object getPostById(
            @RequestParam(value = "sessionId") String sessionId, 
            @RequestParam(value = "postId") Long postId,
            HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		Post post;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			CustomTypeRepository cr = new CustomTypeRepository(em);
			post = cr.getPostById(postId);
			DBUtility.commitTransaction(em);
		} catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		} catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return post;
	}
	
	@RequestMapping(value = "/attribute/getListByCustomTypeId")
	@ResponseBody
	public Object getAttributeListByCustomTypeId(
            @RequestParam(value = "sessionId") String sessionId, 
            @RequestParam(value = "customTypeId") Long customTypeId,
            HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		List<Attribute> attributes;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			CustomTypeRepository cr = new CustomTypeRepository(em);
			attributes = cr.getAttributeListByCustomTypeId(customTypeId);
			DBUtility.commitTransaction(em);
		} catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		} catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return attributes;
	}
	
	@RequestMapping(value = "/attribute/getById")
	@ResponseBody
	public Object getAttributeById(
            @RequestParam(value = "sessionId") String sessionId, 
            @RequestParam(value = "attributeId") Long attributeId,
            HttpServletResponse resp) {
		EntityManager em = ServiceHelper.initialize(resp);
		
		Attribute attribute;
		try {
			User user = ServiceHelper.getSessionUser(em, sessionId);
			
			CustomTypeRepository cr = new CustomTypeRepository(em);
			attribute = cr.getAttributeById(attributeId);
			DBUtility.commitTransaction(em);
		} catch (BusinessException e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getCode(), e.getMessage());
		} catch (Exception e) {
			DBUtility.rollbackTransaction(em);
			return new ResponseHeader(false, e.getMessage());
		}
		return attribute;
	}
}
