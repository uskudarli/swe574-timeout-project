package repository;

import helpers.ValidationHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entity.Friendship;
import entity.User;
import enums.FriendshipStatus;

public class FriendshipRepository {

	EntityManager em;
	
	public FriendshipRepository(EntityManager em){
		this.em = em;
	}

	public List<Friendship> prepareFriendsForUser(User user) {
		Query query = em
				.createQuery(
						"FROM Friendship F WHERE F.person = :person")
				.setParameter("person", user);
		
		List<Friendship> returnList = query.getResultList();

		return returnList;
	}

	public boolean inviteFriends(User user, String friendsString) {
		if (user == null)
			return false;
		if (ValidationHelper.isNullOrWhitespace(friendsString))
			return false;
		
		//parse json to List<Integer>
		List<Integer> friendIds = null;
		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<Integer>>() {
		}.getType();
		friendIds = gson.fromJson(friendsString, listType);
		
		//find users from user id's and add friendship invite records.
		for (Integer item : friendIds){
			Query query = em
					.createQuery(
							"FROM User U WHERE U.userId = :userId")
					.setParameter("userId", item.longValue());
			try {
				User friend = (User)query.getSingleResult();
				
				Friendship friendship1 = new Friendship();
				friendship1.setPerson(user);
				friendship1.setFriend(friend);
				friendship1.setStatus(FriendshipStatus.INVITED_BY_SELF.toString());
				em.persist(friendship1);
				
				Friendship friendship2 = new Friendship();
				friendship2.setPerson(friend);
				friendship2.setFriend(user);
				friendship2.setStatus(FriendshipStatus.INVITED_BY_OTHER.toString());
				em.persist(friendship2);
				
			} catch (Exception e) {
				//do nothing, continue
			}
		}
		return true;
	}

	public boolean acceptFriends(User user, String friendsString) {
//		if (user == null)
//			return false;
//		if (ValidationHelper.isNullOrWhitespace(friendsString))
//			return false;
//		
//		//parse json to List<Integer>
//		List<Integer> friendIds = null;
//		Gson gson = new Gson();
//		Type listType = new TypeToken<ArrayList<Integer>>() {
//		}.getType();
//		friendIds = gson.fromJson(friendsString, listType);
//		
//		//find users from user id's and add friendship invite records.
//		for (int item : friendIds){
//			Query query = em
//					.createQuery(
//							"FROM User U WHERE U.userId = :userId")
//					.setParameter("userId", item);
//			try {
//				User friend = (User)query.getSingleResult();
//				
//				Friendship friendship1 = new Friendship();
//				friendship1.setPerson(user);
//				friendship1.setFriend(friend);
//				friendship1.setStatus(FriendshipStatus.ACCEPTED_BY_SELF.toString());
//				em.persist(friendship1);
//				
//				Friendship friendship2 = new Friendship();
//				friendship2.setPerson(friend);
//				friendship2.setFriend(user);
//				friendship2.setStatus(FriendshipStatus.ACCEPTED_BY_OTHER.toString());
//				em.persist(friendship2);
//				
//			} catch (Exception e) {
//				//do nothing, continue
//			}
//		}
		return true;
	}
}
