package demo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ActionUser")
public class ActionUser {
	
	@Id
	@GeneratedValue(generator="incrementActionUser")
	@GenericGenerator(name="incrementActionUser", strategy = "increment")
	private Long actionUserId;



	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
	private User user;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actionId")
	private Action action;
    
	private String actionUserStatus;

	public ActionUser() {
	}

	public Long getActionUserId() {
		return actionUserId;
	}

	public void setActionUserId(Long actionUserId) {
		this.actionUserId = actionUserId;
	}
	public String getActionUserStatus() {
		return actionUserStatus;
	}

	public void setActionUserStatus(String actionUserStatus) {
		this.actionUserStatus = actionUserStatus;
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
}
