package demo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Post")
public class Post {

	@Id
	@GeneratedValue(generator="incrementPost")
	@GenericGenerator(name="incrementPost", strategy = "increment")
	private Long postId;
	
	private String title;
	private String text;
	private Date time;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customTypeId")
	private CustomType customType;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
	private User user;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
	private Set<Comment> comments = new HashSet<Comment>(0);
	
	public Long getPostId() {
		return postId;
	}
	public void setPostId(Long postId) {
		this.postId = postId;
	}
	public CustomType getCustomType() {
		return customType;
	}
	public void setCustomType(CustomType customType) {
		this.customType = customType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
}
