package demo;

import java.util.Date;
import java.util.List;

public class Event {

	private String eventName;
	private String eventDescription;
	private Date startTime;
	private Date endTime;
	private List<User> invitedPeople;
	private String tagString;

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public List<User> getInvitedPeople() {
		return invitedPeople;
	}

	public void setInvitedPeople(List<User> invitedPeople) {
		this.invitedPeople = invitedPeople;
	}

	public String getTagString() {
		return tagString;
	}

	public void setTagString(String tagString) {
		this.tagString = tagString;
	}

}
