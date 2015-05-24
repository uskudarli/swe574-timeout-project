package repository;

import helpers.ServiceHelper;
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

	public FriendshipRepository(EntityManager em) {
		this.em = em;
	}

	public List<Friendship> prepareFriendsForUser(User user) {
		Query query = em.createQuery(
				"FROM Friendship F WHERE " + "F.person = :person")
				.setParameter("person", user);

		List<Friendship> returnList = query.getResultList();

		return returnList;
	}

	public boolean inviteFriends(User user, String friendsString) {
		if (user == null)
			return false;
		if (ValidationHelper.isNullOrWhitespace(friendsString))
			return false;
		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<Integer>>() {
		}.getType();
		ArrayList<Integer> t = gson.fromJson(friendsString, listType);

		// parse json to List<Integer>
		List<Integer> friendIds = t;

		// find users from user id's and add friendship invite records.
		for (Integer item : friendIds) {
			Query query = em
					.createQuery("FROM User U WHERE U.userId = :userId")
					.setParameter("userId", item.longValue());

			User friend = (User) query.getSingleResult();

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

		}
		return true;
	}

	public boolean acceptFriends(User user, String friendshipsString) {
		if (user == null)
			return false;
		if (ValidationHelper.isNullOrWhitespace(friendshipsString))
			return false;
		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<Integer>>() {
		}.getType();
		ArrayList<Integer> t = gson.fromJson(friendshipsString, listType);

		// parse json to List<Integer>
		List<Integer> friendshipIds = t;

		// find users from user id's and add friendship invite records.
		for (int item : friendshipIds) {
			Query query = em.createQuery(
					"FROM Friendship F WHERE "
							+ "F.friendshipId = :friendshipId").setParameter(
					"friendshipId", item);

			Friendship friendship1 = (Friendship) query.getSingleResult();

			friendship1.setStatus(FriendshipStatus.ACCEPTED_BY_SELF.toString());
			em.persist(friendship1);

			query = em
					.createQuery(
							"FROM Friendship F WHERE "
									+ "F.person = :person AND "
									+ "F.friend = :friend")
					.setParameter("person", friendship1.getFriend())
					.setParameter("friend", friendship1.getPerson());

			Friendship friendship2 = (Friendship) query.getSingleResult();
			friendship2
					.setStatus(FriendshipStatus.ACCEPTED_BY_OTHER.toString());
			em.persist(friendship2);
		}
		return true;
	}

	public boolean rejectFriends(User user, String friendshipsString) {
		if (user == null)
			return false;
		if (ValidationHelper.isNullOrWhitespace(friendshipsString))
			return false;
		
		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<Integer>>() {
		}.getType();
		ArrayList<Integer> t = gson.fromJson(friendshipsString, listType);
		// parse json to List<Integer>
		List<Integer> friendshipIds = t;

		// find users from user id's and add friendship invite records.
		for (int item : friendshipIds) {
			Query query = em.createQuery(
					"FROM Friendship F WHERE "
							+ "F.friendshipId = :friendshipId").setParameter(
					"friendshipId", item);

			Friendship friendship1 = (Friendship) query.getSingleResult();

			friendship1.setStatus(FriendshipStatus.REJECTED_BY_SELF.toString());
			em.persist(friendship1);

			query = em
					.createQuery(
							"FROM Friendship F WHERE "
									+ "F.person = :person AND "
									+ "F.friend = :friend")
					.setParameter("person", friendship1.getFriend())
					.setParameter("friend", friendship1.getPerson());

			Friendship friendship2 = (Friendship) query.getSingleResult();
			friendship2
					.setStatus(FriendshipStatus.REJECTED_BY_OTHER.toString());
			em.persist(friendship2);
		}
		return true;
	}
}
