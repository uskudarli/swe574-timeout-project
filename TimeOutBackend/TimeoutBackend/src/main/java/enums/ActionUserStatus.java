package enums;

public enum ActionUserStatus {
	
	CREATED("C"), //created the action
	INVITED("I"), //somebody has sent invite to user
	REQUESTED("R"), //user requested to be a member
	MEMBER("M"), //invitation accepted or, request approved
	ADMIN("A"); //has privilege of approval in the action

	private ActionUserStatus(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public String value;

}