package dto;

import java.util.List;

import entity.Action;
import entity.User;

public class ActionMemberDTO {
	Action action;
	List<User> users;
	int count;

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
