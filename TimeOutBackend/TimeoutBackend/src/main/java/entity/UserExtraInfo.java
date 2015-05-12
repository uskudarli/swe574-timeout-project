/**
 * 
 * User Extra Info Class holds extra information of users.
 * 
 */
package entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "UserExtraInfo")
public class UserExtraInfo {

	@Id
	@GeneratedValue(generator = "incrementUser")
	@GenericGenerator(name = "incrementUser", strategy = "increment")
	private Long userId;
	
	private Date birthDate;
	private String about;
	private String interests;
	private String languages;
	
	@OneToOne(mappedBy = "userExtraInfo")
	private User user;

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public UserExtraInfo() {
	}

	/**
	 * @param gender
	 * @param birthDate
	 * @param about
	 */
	public UserExtraInfo(Date birthDate, String about) {
		this.birthDate = birthDate;
		this.about = about;
	}

	public String getInterests() {
		return interests;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
