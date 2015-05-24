package entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CustomType")
public class CustomType {

	@Id
	@GeneratedValue(generator = "incrementCustomType")
	@GenericGenerator(name = "incrementCustomType", strategy = "increment")
	private Long customTypeId;

	private String name;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customType")
	private Set<Attribute> attributes = new HashSet<Attribute>(0);

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customType")
	private Set<Post> posts = new HashSet<Post>(0);

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "actionId")
	private Action action;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;

	public Long getCustomTypeId() {
		return customTypeId;
	}

	public void setCustomTypeId(Long customTypeId) {
		this.customTypeId = customTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<Attribute> attributes) {
		this.attributes = attributes;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

}
