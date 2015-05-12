package repository;

import javax.persistence.EntityManager;

import entity.User;

public class UserRepository {
	
	EntityManager em;
	
	public UserRepository(EntityManager em){
		this.em = em;
	}

	public void insertUser(String userEmail, String password) {
		em.persist(new User(userEmail, password));
	}

}
