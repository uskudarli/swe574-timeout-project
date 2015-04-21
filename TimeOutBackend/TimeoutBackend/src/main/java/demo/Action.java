package demo;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;


/**
 * @author mac
 *
 */
@Entity
@Table(name = "Action")
public class Action {

	@Id
	@GeneratedValue(generator="incrementAction")
	@GenericGenerator(name="incrementAction", strategy = "increment")
	private Long actionId;


	private String title;
	private String description;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime")
	private Date createTime;
	
	private String privacy;
	private String actionType;
	//private Location location;
	private Date startTime;
	private Date endTime;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "action")
	private Set<ActionUser> actionUsers = new HashSet<ActionUser>(0);

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "action")
	private Set<ActionTag> actionTags = new HashSet<ActionTag>(0);
	
	public Action(){
	}
	
	public Action(String title, String description, String actionType){
		this.title = title;
		this.description = description;
		this.createTime = new Date();
		this.privacy = Params.PRIVACY_PUBLIC_TO_ALL;
		this.actionType = actionType;
		//this.location = null;
		this.startTime = new Date();
		if (actionType == "E"){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, 1);
			this.endTime = cal.getTime();
		}
	}
	public Action(String title, String description, String actionType, 
				  Date startTime, Date endTime){
		this.title = title;
		this.description = description;
		this.createTime = new Date();
		this.privacy = Params.PRIVACY_PUBLIC_TO_ALL;
		this.actionType = actionType;
		//this.location = null;
		if (actionType == "E"){
			this.startTime = startTime;
			this.endTime = endTime;
		}
	}
	

	public Long getActionId() {
		return actionId;
	}

	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getPrivacy() {
		return privacy;
	}
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
//	public Location getLocation() {
//		return location;
//	}
//	public void setLocation(Location location) {
//		this.location = location;
//	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	public Set<ActionUser> getActionUsers() {
		return actionUsers;
	}

	public void setActionUsers(Set<ActionUser> actionUsers) {
		this.actionUsers = actionUsers;
	}


	public Set<ActionTag> getActionTags() {
		return actionTags;
	}

	public void setActionTags(Set<ActionTag> actionTags) {
		this.actionTags = actionTags;
	}
}
