package entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ActionTag")
public class ActionTag {
	@Id
	@GeneratedValue(generator="incrementActionTag")
	@GenericGenerator(name="incrementActionTag", strategy = "increment")
	private Long actionTagId;

  

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actionId")
	private Action action;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tagId")
	private Tag tag;
	
	public Long getActionTagId() {
		return actionTagId;
	}

	public void setActionTagId(Long actionTagId) {
		this.actionTagId = actionTagId;
	}

    public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public ActionTag() {
	}
}
