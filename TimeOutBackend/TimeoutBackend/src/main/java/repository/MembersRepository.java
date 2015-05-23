package repository;

import helpers.ValidationHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entity.Action;
import entity.ActionUser;
import entity.User;
import enums.ActionUserStatus;

public class MembersRepository {

	EntityManager em;

	public MembersRepository(EntityManager em) {
		this.em = em;
	}
	
	public List<ActionUser> getMembersOfAction(Action action) {
		String hql = "FROM ActionUser AU WHERE AU.action = :action";
		Query query = em.createQuery(hql);
		query.setParameter("action", action);
		List<ActionUser> result = query.getResultList();
		
		return result;
	}
	
	public void insertInvitedPeople(String invitedPeopleString, Action action) {
		if (ValidationHelper.isNullOrWhitespace(invitedPeopleString))
			return;

		List<Integer> invitedPeople = null;
		Gson gson = new Gson();

		Type listType = new TypeToken<ArrayList<Integer>>() {
		}.getType();

		invitedPeople = gson.fromJson(invitedPeopleString, listType);

		for (int i = 0; i < invitedPeople.size(); i++) {

			ActionUser actionUser = new ActionUser();
			Query query = em
					.createQuery("FROM User U WHERE U.userId = :userId");
			query.setParameter("userId", invitedPeople.get(i).longValue());
			actionUser.setUser((User) query.getSingleResult());

			actionUser.setAction(action);
			actionUser.setActionUserStatus(ActionUserStatus.INVITED);
			em.persist(actionUser);
		}

	}
}
