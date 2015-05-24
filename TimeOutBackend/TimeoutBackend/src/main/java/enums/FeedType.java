package enums;

public enum FeedType {
	POST("P"), COMMENT("C"), MEMBER("M");

	private FeedType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public String value;
}
