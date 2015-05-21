package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "UserBasicInfo")
@org.hibernate.annotations.GenericGenerator(name="incrementUser", strategy="foreign",
parameters={@org.hibernate.annotations.Parameter(name="property", value="user")
})
public class UserBasicInfo {

	@Id
	@GeneratedValue(generator = "incrementUser")
	private Long userId;
	
	private String firstName;
	private String lastName;
	private String gender;
	
	@JsonIgnore
	@OneToOne()
	@PrimaryKeyJoinColumn
	private User user;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
