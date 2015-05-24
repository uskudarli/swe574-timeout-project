package dto;

import entity.Action;
import entity.User;
import enums.FeedType;

public class NewsFeedDTO {
	private User user;
	private Action action;
	private FeedType feedType;
	private Long feedId;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public FeedType getFeedType() {
		return feedType;
	}
	public void setFeedType(FeedType feedType) {
		this.feedType = feedType;
	}
	public Long getFeedId() {
		return feedId;
	}
	public void setFeedId(Long feedId) {
		this.feedId = feedId;
	}
}
