package demo;

public enum ActionUserStatus {

	INVITED("I"), CREATED("C"), MEMBER("M"), ADMIN("A");

	private ActionUserStatus(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public String value;

}