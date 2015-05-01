/**
 * 
 */
package demo;

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
@Table(name = "FriendshipInvitation")
public class FriendshipInvitation {

	@Id
	@GeneratedValue(generator = "incrementFriendshipInvitation")
	@GenericGenerator(name = "incrementFriendshipInvitation", strategy = "increment")
	private int friendshipInvitationId;

	private int sendingUserId;
	private int receivingUserId;
	private char status;

	public int getFriendshipInvitationId() {
		return friendshipInvitationId;
	}

	public void setFriendshipInvitationId(int friendshipInvitationId) {
		this.friendshipInvitationId = friendshipInvitationId;
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
