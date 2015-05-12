package enums;

public enum PrivacyType {

	PUBLIC("P"), CLOSED("C");

	private PrivacyType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public String value;
}
