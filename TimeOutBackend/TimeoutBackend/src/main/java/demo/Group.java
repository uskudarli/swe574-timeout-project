/**
 * 
 */
package demo;

import java.util.List;

public class Group {

	private String groupName;
	private String groupDescription;
	private List<User> invitedPeople;
	private String tagString;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
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
