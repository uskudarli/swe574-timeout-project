package demo;

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
	private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
	private Action action;
	
    public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
	private Tag tag;

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public ActionTag() {
	}
}
