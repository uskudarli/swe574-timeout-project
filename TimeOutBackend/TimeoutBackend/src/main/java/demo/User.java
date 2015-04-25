package demo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "User")
public class User {

	@Id
	@GeneratedValue(generator = "incrementUser")
	@GenericGenerator(name = "incrementUser", strategy = "increment")
	private Long userId;

	private String userName;
	private Date date;
	private String password;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private Set<ActionUser> actionUsers = new HashSet<ActionUser>(0);
	
	@ManyToMany
	@JoinTable(name="FriendShip",
	 joinColumns=@JoinColumn(name="personId"),
	 inverseJoinColumns=@JoinColumn(name="friendId")
	)
	private Set<User> friendShip1 = new HashSet<User>(0);
	
	@ManyToMany
	@JoinTable(name="FriendShip",
	 joinColumns=@JoinColumn(name="friendId"),
	 inverseJoinColumns=@JoinColumn(name="personId")
	)
	private Set<User> friendShip2 = new HashSet<User>(0);
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private Set<CustomType> customTypes = new HashSet<CustomType>(0);
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private Set<Post> posts = new HashSet<Post>(0);
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private Set<Comment> comments = new HashSet<Comment>(0);
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
	private Role role;
	
	@OneToOne
    @PrimaryKeyJoinColumn
	private UserCommInfo userCommInfo;
	
	@OneToOne
    @PrimaryKeyJoinColumn
	private UserExtraInfo userExtraInfo;

	public User() {
	}

	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public Long getUserId() {
		return userId;
	}

	private void setUserId(Long id) {
		this.userId = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
