/**
 * 
 */
package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Sezgi
 *
 */
@Entity
@Table(name = "ActionInvitation")
public class ActionInvitation {

	@Id
	@GeneratedValue(generator = "incrementActionInvitation")
	@GenericGenerator(name = "incrementActionInvitation", strategy = "increment")
	private int actionInvitationId;

	private int sendingUserId;
	private int receivingUserId;
	private char status;

	public int getActionInvitationId() {
		return actionInvitationId;
	}

	public void setActionInvitationId(int actionInvitationId) {
		this.actionInvitationId = actionInvitationId;
	}

	public int getSendingUserId() {
		return sendingUserId;
	}

	public void setSendingUserId(int sendingUserId) {
		this.sendingUserId = sendingUserId;
	}

	public int getReceivingUserId() {
		return receivingUserId;
	}

	public void setReceivingUserId(int receivingUserId) {
		this.receivingUserId = receivingUserId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

}
