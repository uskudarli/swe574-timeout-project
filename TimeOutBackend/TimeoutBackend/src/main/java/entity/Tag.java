package entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Tag")
public class Tag {
	@Id
	@GeneratedValue(generator="incrementTag")
	@GenericGenerator(name="incrementTag", strategy = "increment")
	private Long tagId;

	private String tagName;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tag")
	private Set<ActionTag> actionTags = new HashSet<ActionTag>(0);

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}
	
	
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
}
