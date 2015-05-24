package enums;

public enum FriendshipStatus {

	INVITED_BY_SELF("IS"), // user invited somebody to be a friend
	INVITED_BY_OTHER("IO"), // somebody invited user to be a friend
	ACCEPTED_BY_SELF("AS"), // user accepted the invitation sent by somebody
	ACCEPTED_BY_OTHER("AO"), // somebody accepted invitation sent by user
	REJECTED_BY_SELF("RS"), // user rejected the invitation sent by somebody
	REJECTED_BY_OTHER("RO"); // somebody rejected invitation sent by user

	private FriendshipStatus(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public String value;
}
