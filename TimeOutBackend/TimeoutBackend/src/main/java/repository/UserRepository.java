package repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;

import common.BusinessException;
import common.DBUtility;
import common.ErrorMessages;
import common.ResponseHeader;
import entity.Role;
import entity.Session;
import entity.User;

public class UserRepository {
	
	EntityManager em;
	
	public UserRepository(EntityManager em){
		this.em = em;
	}

	public int getUserByUserNumberEmail(String userEmail) {
		List<User> users = em.createQuery("FROM User U WHERE U.userEmail = :userEmail")
        .setParameter("userEmail", userEmail)
        .getResultList();
		
		if (users == null)
			return 0;
		return users.size();
	}

	public ResponseHeader login(String userEmail, String password) throws BusinessException {
		List<User> result =
                em.createQuery("FROM User U WHERE U.userEmail = :userEmail AND U.password = :password")
                        .setParameter("userEmail", userEmail)
                        .setParameter("password", password)
                        .getResultList();
        if (result != null && result.size() > 0) {

            String sessionId = new BigInteger(130, new Random()).toString(32).toUpperCase();

            em.persist(new Session(result.get(0), sessionId));

            return new ResponseHeader(sessionId);
        } else {
            throw new BusinessException(
            		ErrorMessages.invalidLoginCode, ErrorMessages.invalidLogin);
        }
	}

	public Role getRoleByName(String role) {
		if (role.isEmpty())
			return null;
		Role roleDb;
		try {
			roleDb = (Role) em.createQuery("FROM Role R WHERE R.name = :name")
			        .setParameter("name", role)
			        .getSingleResult();
		} catch (Exception e) {
			return null;
		}
		return roleDb;		
	}

	public void insertUser(User user) {
//		User user1 = em.merge(user);
//		em.merge(user1.getUserBasicInfo());
//		em.merge(user1.getUserCommInfo());
//		em.merge(user1.getUserExtraInfo());
//		em.merge(user1.getRole());
		em.persist(user);
	}

	public void insertRole(Role role) {
		em.persist(role);
	}
}
