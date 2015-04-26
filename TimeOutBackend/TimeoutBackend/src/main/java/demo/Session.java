package demo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "Session")
public class Session {

	@Id
	@GeneratedValue(generator="incrementSession")
	@GenericGenerator(name="incrementSession", strategy = "increment")
	private Long sesssionId;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
	private User user;

	private String cookie;

	public Session() {

	}

	public Session(User user, String cookie) {
		this.user = user;
		this.cookie = cookie;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
