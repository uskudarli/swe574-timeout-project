/**
 * 
 * User Extra Info Class holds extra information of users.
 * 
 */
package demo;

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
	
	private char gender;
	private Date birthDate;
	private String about;
	
	@OneToOne(mappedBy = "userExtraInfo")
	private User user;

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

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
	public UserExtraInfo(char gender, Date birthDate, String about) {
		this.gender = gender;
		this.birthDate = birthDate;
		this.about = about;
	}

}
