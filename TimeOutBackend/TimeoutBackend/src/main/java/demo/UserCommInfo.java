/**
 * 
 * User Communication Info Class holds communication information of users.
 * 
 */
package demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "UserCommInfo")
public class UserCommInfo {

	@Id
	@GeneratedValue(generator = "incrementUser")
	@GenericGenerator(name = "incrementUser", strategy = "increment")
	private Long userId;
	
	private long mobilePhone;
	private String address;
	
	@OneToOne(mappedBy = "userCommInfo")
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
