/**
 * 
 */
package demo;

public class Invitation {

	private int invitationId;
	private int sendingUserId;
	private int receivingUserId;
	private char status;

	public int getInvitationId() {
		return invitationId;
	}

	public void setInvitationId(int invitationId) {
		this.invitationId = invitationId;
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
