/**
 * 
 * User Communication Info Class holds communication information of users.
 * 
 */
package demo;

public class UserCommInfo {

	private long mobilePhone;
	private String address;

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

}
