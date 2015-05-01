/**
 * 
 */
package demo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Sezgi
 *
 */

@Entity
@Table(name = "ActionRecommendation")
public class ActionRecommendation {

	@Id
	@GeneratedValue(generator = "incrementActionRecommendation")
	@GenericGenerator(name = "incrementActionRecommendation", strategy = "increment")
	private int actionRecommendationId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "actionId")
	private Action action;

	private char status;

	public int getActionRecommendationId() {
		return actionRecommendationId;
	}

	public void setActionRecommendationId(int actionRecommendationId) {
		this.actionRecommendationId = actionRecommendationId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

}
