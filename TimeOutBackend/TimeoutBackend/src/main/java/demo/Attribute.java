package demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Attribute")
public class Attribute {

	@Id
	@GeneratedValue(generator="incrementAttribute")
	@GenericGenerator(name="incrementAttribute", strategy = "increment")
	private Long attributeId;
	
	private String attributeKey;
	private String attributeValue;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customTypeId")
	private CustomType customType;
	
	
	public Long getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}

	public CustomType getCustomType() {
		return customType;
	}

	public void setCustomType(CustomType customType) {
		this.customType = customType;
	}

	public String getAttributeKey() {
		return attributeKey;
	}

	public void setAttributeKey(String attributeKey) {
		this.attributeKey = attributeKey;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}



}
