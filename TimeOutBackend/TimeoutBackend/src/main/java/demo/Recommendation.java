package demo;

public class Recommendation {
	
	private int recommendationId;
	private int userId;
	private char recommendationType;
	private char status;
	public int getRecommendationId() {
		return recommendationId;
	}
	public void setRecommendationId(int recommendationId) {
		this.recommendationId = recommendationId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public char getRecommendationType() {
		return recommendationType;
	}
	public void setRecommendationType(char recommendationType) {
		this.recommendationType = recommendationType;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}

}
