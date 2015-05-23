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
@Table(name = "AttributeValue")
public class AttributeValue {
	
	@Id
	@GeneratedValue(generator="incrementAttributeValue")
	@GenericGenerator(name="incrementAttributeValue", strategy = "increment")
	private Long attributeValueId;
	
	private String attributeValue;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attributeId")
	private Attribute attribute;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
	private Post post;

	public Long getAttributeValueId() {
		return attributeValueId;
	}

	public void setAttributeValueId(Long attributeValueId) {
		this.attributeValueId = attributeValueId;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}
	
	
}
