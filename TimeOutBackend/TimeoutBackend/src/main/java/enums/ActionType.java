package enums;

public enum ActionType {

	EVENT("E"), GROUP("G");

	private ActionType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public String value;

}