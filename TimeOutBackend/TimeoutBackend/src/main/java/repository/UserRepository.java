package repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;

import common.BusinessException;
import common.DBUtility;
import common.ErrorMessages;
import common.ResponseHeader;
import entity.Session;
import entity.User;

public class UserRepository {
	
	EntityManager em;
	
	public UserRepository(EntityManager em){
		this.em = em;
	}

	public void insertUser(String userEmail, String password) {
		em.persist(new User(userEmail, password));
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
}
