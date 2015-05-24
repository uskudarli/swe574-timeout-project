package entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "User")
public class User {

	@Id
	@GeneratedValue(generator = "incrementUser")
	@GenericGenerator(name = "incrementUser", strategy = "increment")
	private Long userId;

	@Column(name = "userName")
	private String userEmail;
	private Date date;
	private String password;

	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private Set<ActionUser> actionUsers = new HashSet<ActionUser>(0);

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<UserTag> userTags = new HashSet<UserTag>(0);

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userRecommended1")
	private Set<UserRecommendation> userRecommendations1 = new HashSet<UserRecommendation>(
			0);

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userRecommended2")
	private Set<UserRecommendation> userRecommendations2 = new HashSet<UserRecommendation>(
			0);

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "action")
	private Set<ActionRecommendation> actionRecommendations = new HashSet<ActionRecommendation>(
			0);

	// @ManyToMany
	// @JoinTable(name = "FriendShip", joinColumns = @JoinColumn(name =
	// "personId"), inverseJoinColumns = @JoinColumn(name = "friendId"))
	// private Set<User> friendShip1 = new HashSet<User>(0);
	//
	// @ManyToMany
	// @JoinTable(name = "FriendShip", joinColumns = @JoinColumn(name =
	// "friendId"), inverseJoinColumns = @JoinColumn(name = "personId"))
	// private Set<User> friendShip2 = new HashSet<User>(0);

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
	private Set<Friendship> friendShip1 = new HashSet<Friendship>(0);

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "friend")
	private Set<Friendship> friendShip2 = new HashSet<Friendship>(0);

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<CustomType> customTypes = new HashSet<CustomType>(0);

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<Post> posts = new HashSet<Post>(0);

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<Comment> comments = new HashSet<Comment>(0);

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "roleId")
	private Role role;

	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "user")
	@PrimaryKeyJoinColumn
	private UserBasicInfo userBasicInfo;

	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "user")
	@PrimaryKeyJoinColumn
	private UserCommInfo userCommInfo;

	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "user")
	@PrimaryKeyJoinColumn
	private UserExtraInfo userExtraInfo;

	public Set<ActionUser> getActionUsers() {
		return actionUsers;
	}

	public void setActionUsers(Set<ActionUser> actionUsers) {
		this.actionUsers = actionUsers;
	}

	public Set<CustomType> getCustomTypes() {
		return customTypes;
	}

	public void setCustomTypes(Set<CustomType> customTypes) {
		this.customTypes = customTypes;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public UserBasicInfo getUserBasicInfo() {
		return userBasicInfo;
	}

	public void setUserBasicInfo(UserBasicInfo userBasicInfo) {
		this.userBasicInfo = userBasicInfo;
		userBasicInfo.setUser(this);
	}

	public UserCommInfo getUserCommInfo() {
		return userCommInfo;
	}

	public void setUserCommInfo(UserCommInfo userCommInfo) {
		this.userCommInfo = userCommInfo;
		userCommInfo.setUser(this);
	}

	public UserExtraInfo getUserExtraInfo() {
		return userExtraInfo;
	}

	public void setUserExtraInfo(UserExtraInfo userExtraInfo) {
		this.userExtraInfo = userExtraInfo;
		userExtraInfo.setUser(this);
	}

	public User() {
	}

	public User(String userEmail, String password) {
		this.userEmail = userEmail;
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

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<UserRecommendation> getUserRecommendations1() {
		return userRecommendations1;
	}

	public void setUserRecommendations1(
			Set<UserRecommendation> userRecommendations1) {
		this.userRecommendations1 = userRecommendations1;
	}

	public Set<UserRecommendation> getUserRecommendations2() {
		return userRecommendations2;
	}

	public void setUserRecommendations2(
			Set<UserRecommendation> userRecommendations2) {
		this.userRecommendations2 = userRecommendations2;
	}

	public Set<ActionRecommendation> getActionRecommendations() {
		return actionRecommendations;
	}

	public void setActionRecommendations(
			Set<ActionRecommendation> actionRecommendations) {
		this.actionRecommendations = actionRecommendations;
	}

	public Set<Friendship> getFriendShip1() {
		return friendShip1;
	}

	public void setFriendShip1(Set<Friendship> friendShip1) {
		this.friendShip1 = friendShip1;
	}

	public Set<Friendship> getFriendShip2() {
		return friendShip2;
	}

	public void setFriendShip2(Set<Friendship> friendShip2) {
		this.friendShip2 = friendShip2;
	}

}
