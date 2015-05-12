package entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import enums.ActionUserStatus;

@Entity
@Table(name = "ActionUser")
public class ActionUser {
	
	@Id
	@GeneratedValue(generator="incrementActionUser")
	@GenericGenerator(name="incrementActionUser", strategy = "increment")
	private Long actionUserId;



	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
	private User user;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "actionId")
	private Action action;

	@Enumerated
	private ActionUserStatus actionUserStatus;

	public Long getActionUserId() {
		return actionUserId;
	}

	public void setActionUserId(Long actionUserId) {
		this.actionUserId = actionUserId;
	}
	public ActionUserStatus getActionUserStatus() {
		return actionUserStatus;
	}

	public void setActionUserStatus(ActionUserStatus actionUserStatus) {
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
