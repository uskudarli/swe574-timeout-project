/**
 * 
 */
package entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Sezgi
 *
 */

@Entity
@Table(name = "UserRecommendation")
public class UserRecommendation {

	@Id
	@GeneratedValue(generator = "incrementUserRecommendation")
	@GenericGenerator(name = "incrementUserRecommendation", strategy = "increment")
	private int userRecommendationId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	private User userRecommended1;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "recommendedUserId")
	private User userRecommended2;

	private char status;

	public int getUserRecommendationId() {
		return userRecommendationId;
	}

	public void setUserRecommendationId(int userRecommendationId) {
		this.userRecommendationId = userRecommendationId;
	}

	public User getUserRecommended1() {
		return userRecommended1;
	}

	public void setUserRecommended1(User userRecommended1) {
		this.userRecommended1 = userRecommended1;
	}

	public User getUserRecommended2() {
		return userRecommended2;
	}

	public void setUserRecommended2(User userRecommended2) {
		this.userRecommended2 = userRecommended2;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

}
