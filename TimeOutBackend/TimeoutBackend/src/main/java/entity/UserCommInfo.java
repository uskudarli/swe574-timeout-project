/**
 * 
 * User Communication Info Class holds communication information of users.
 * 
 */
package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "UserCommInfo")
@org.hibernate.annotations.GenericGenerator(name = "incrementUser", strategy = "foreign", parameters = { @org.hibernate.annotations.Parameter(name = "property", value = "user") })
public class UserCommInfo {

	@Id
	@GeneratedValue(generator = "incrementUser")
	private Long userId;

	private long mobilePhone;
	private String address;

	@JsonIgnore
	@OneToOne()
	@PrimaryKeyJoinColumn
	private User user;

	public long getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(long mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public UserCommInfo() {
	}

	/**
	 * @param mobilePhone
	 * @param address
	 */
	public UserCommInfo(long mobilePhone, String address) {
		super();
		this.mobilePhone = mobilePhone;
		this.address = address;
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
