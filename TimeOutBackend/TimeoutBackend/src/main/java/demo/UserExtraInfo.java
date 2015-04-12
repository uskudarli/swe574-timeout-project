/**
 * 
 * User Extra Info Class holds extra information of users.
 * 
 */
package demo;

import java.util.Date;

public class UserExtraInfo {

	private char gender;
	private Date birthDate;
	private String about;

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
