package entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Friendship")
public class Friendship {

	@Id
	@GeneratedValue(generator = "incrementFriendship")
	@GenericGenerator(name = "incrementFriendship", strategy = "increment")
	private int friendshipId;

	@ManyToOne(fetch = FetchType.EAGER )
	@JoinColumn(name = "personId")
	private User person;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "friendId")
	private User friend;
	
	private String status;

	public int getFriendshipId() {
		return friendshipId;
	}

	public void setFriendshipId(int friendshipId) {
		this.friendshipId = friendshipId;
	}

	public User getPerson() {
		return person;
	}

	public void setPerson(User person) {
		this.person = person;
	}

	public User getFriend() {
		return friend;
	}

	public void setFriend(User friend) {
		this.friend = friend;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
