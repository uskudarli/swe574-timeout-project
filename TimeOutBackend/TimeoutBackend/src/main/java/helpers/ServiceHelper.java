package helpers;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import common.BusinessException;
import common.DBUtility;
import common.ErrorMessages;
import common.ResponseHeader;
import entity.Session;
import entity.User;

public class ServiceHelper {

	public static void setResponseHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Set-Cookie");
    }
	
	public static User getSessionUser(EntityManager em, String cookie) throws BusinessException {
		Session result = (Session) em
				.createQuery("FROM Session S WHERE S.cookie = :cookie")
				.setParameter("cookie", cookie).getSingleResult();
		User user = result.getUser();
		authorize(user);
		return user;
	}

	public static void authorize(User user) throws BusinessException {
		if (user == null) {
			businessError(ErrorMessages.notAuthorizedCode, ErrorMessages.notAuthorized);
		}
	}

	public static EntityManager initialize(HttpServletResponse resp) {
		ServiceHelper.setResponseHeaders(resp);
		EntityManager em = DBUtility.startTransaction();
		return em;
	}

	public static void businessError(String code,
			String message) throws BusinessException {
		throw new BusinessException(code, message);
	}
	
	/**
     * Format a time from a given format to given target format
     */
    public static Date dateParser(String inputTimeStamp)
            throws ParseException {
    	String format = "yyyy-MM-DD_HH:mm:ss";
    	DateFormat formatter = new SimpleDateFormat(format);
    	return formatter.parse(inputTimeStamp);
    }
    
    public static <T> ArrayList<T> parseListFromJsonString(String jsonString){
    	Gson gson = new Gson();
    	Type listType = new TypeToken<ArrayList<T>>() {}.getType();
    	ArrayList<T> t = gson.fromJson(jsonString, listType);
		return t;
    }
    
    public static <T> T parseObjectFromJsonString(String jsonString){
    	Gson gson = new Gson();
    	Type type = new TypeToken<T>() {}.getType();
    	T t = gson.fromJson(jsonString, type);
		return t;
    }
    
}
