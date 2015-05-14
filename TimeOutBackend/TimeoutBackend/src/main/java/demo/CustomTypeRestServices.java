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
import repository.CustomTypeRepository;
import common.BusinessException;
import common.DBUtility;
import common.ResponseHeader;
import entity.Action;
import entity.Attribute;
import entity.CustomType;
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


}
