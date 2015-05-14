package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import entity.Friendship;
import entity.User;

public class FriendshipRepository {

	EntityManager em;
	
	public FriendshipRepository(EntityManager em){
		this.em = em;
	}

	public List<User> prepareFriendsForUser(User user) {
		Query query = em
				.createQuery(
						"FROM Friendship F WHERE F.person = :person")
				.setParameter("person", user);
		
		List<Friendship> list = query.getResultList();
		List<User> returnList = new ArrayList<User>();
		
		for (Friendship friendship : list) {
			returnList.add(friendship.getFriend());
		}

		return returnList;
	}
}
