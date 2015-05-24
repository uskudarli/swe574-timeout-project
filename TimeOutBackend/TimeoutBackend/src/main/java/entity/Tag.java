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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Tag")
public class Tag {
	@Id
	@GeneratedValue(generator = "incrementTag")
	@GenericGenerator(name = "incrementTag", strategy = "increment")
	private Long tagId;

	private String tagName;
	private String contextId;
	private String url;
	private String alias;
	private String description;
	private String label;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tag")
	private Set<UserTag> userTags = new HashSet<UserTag>(0);

	@JsonIgnore
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

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Set<UserTag> getUserTags() {
		return userTags;
	}

	public void setUserTags(Set<UserTag> userTags) {
		this.userTags = userTags;
	}

	public Set<ActionTag> getActionTags() {
		return actionTags;
	}

	public void setActionTags(Set<ActionTag> actionTags) {
		this.actionTags = actionTags;
	}
}
