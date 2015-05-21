package enums;

public enum FriendshipStatus {

	INVITED_BY_SELF("IS"), INVITED_BY_OTHER("IO"), 
	ACCEPTED_BY_SELF("AS"), ACCEPTED_BY_OTHER("AO"), 
	REJECTED_BY_SELF("RS"), REJECTED_BY_OTHER("RO");

	private FriendshipStatus(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public String value;
}
