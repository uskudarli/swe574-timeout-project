package repository;

import helpers.ServiceHelper;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import common.DBUtility;
import entity.Action;
import entity.User;
import enums.ActionType;

public class ActionRepository {

	EntityManager em;
	
	public ActionRepository(EntityManager em){
		this.em = em;
	}
	
	public Action getActionById(Long actionId, String actionType) {
		Action returnVal = null;
		if (actionId > 0){
			Query query = null;
			if (actionType == ActionType.EVENT.toString() || actionType == ActionType.GROUP.toString()) {
				query = em
						.createQuery(
								"FROM Action A WHERE A.actionId = :actionId AND "
										+ "A.actionType = :actionType")
						.setParameter("actionId", actionId)
						.setParameter("actionType", actionType);
			} else {
				query = em
						.createQuery(
								"FROM Action A WHERE A.actionId = :actionId")
						.setParameter("actionId", actionId);
			}
			
			returnVal = (Action) query.getSingleResult();
		}
		
		return returnVal;
	}
}
