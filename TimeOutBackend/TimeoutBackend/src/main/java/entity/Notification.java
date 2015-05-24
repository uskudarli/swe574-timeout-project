package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Sezgi
 *
 */
@Entity
@Table(name = "Notification")
public class Notification {

	@Id
	@GeneratedValue(generator = "incrementNotification")
	@GenericGenerator(name = "incrementNotification", strategy = "increment")
	private int notificationId;

	private String nname;
	private String detail;

	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public String getNname() {
		return nname;
	}

	public void setNname(String nname) {
		this.nname = nname;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
