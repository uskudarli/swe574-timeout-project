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
	
	
}
