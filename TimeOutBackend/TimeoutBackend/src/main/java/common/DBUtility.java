package common;

import com.google.appengine.api.utils.SystemProperty;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class DBUtility {
	public static Map<String, String> properties = new HashMap<>();

	public static EntityManagerFactory entityManagerFactory;

	public static EntityManager em;

	public static Map<String, String> putProperties() {

	//	For Cloud Usage
		if (SystemProperty.environment.value() ==
				SystemProperty.Environment.Value.Production) {
			properties.put("javax.persistence.jdbc.driver",
					"com.mysql.jdbc.GoogleDriver");
			properties.put("javax.persistence.jdbc.url",
					"jdbc:google:mysql://timeout5742:timeout5742db/demo?user=root");
			return properties;
		}

		// For Local Usage
		properties.put("javax.persistence.jdbc.driver",
				"com.mysql.jdbc.Driver");
		properties.put("javax.persistence.jdbc.url",
				"jdbc:mysql://localhost:3306/demo?user=root?password=password");

		return properties;
	}

	public static EntityManager createEntityManager(){
		if(properties.isEmpty()) {
			Map<String, String> properties = new HashMap<>();
			properties = DBUtility.putProperties();
		}
		entityManagerFactory = Persistence.createEntityManagerFactory(
				"Demo", properties);
		return entityManagerFactory.createEntityManager();
	}

	public static EntityManager createTransaction(){
		em = createEntityManager();
		return em;
	}

	public static EntityManager startTransaction(){
		em = createEntityManager();
		em.getTransaction().begin();
		return em;
	}

	public static void commitTransaction(EntityManager em){
		em.getTransaction().commit();
		em.close();
		entityManagerFactory.close();
	}
	
	public static void rollbackTransaction(EntityManager em){
		em.getTransaction().rollback();
		em.close();
		entityManagerFactory.close();
	}

	public static EntityManager getCurrentEntityManager(){
		em.getTransaction().begin();
		return em;
	}

	public static void commitTransactionOnly(EntityManager em){
		em.getTransaction().commit();
//		em.close();
//		entityManagerFactory.close();
	}
}
