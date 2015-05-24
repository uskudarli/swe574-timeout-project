package entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Attribute")
public class Attribute {

	@Id
	@GeneratedValue(generator = "incrementAttribute")
	@GenericGenerator(name = "incrementAttribute", strategy = "increment")
	private Long attributeId;

	private String attributeKey;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customTypeId")
	private CustomType customType;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "attribute")
	private Set<AttributeValue> attributeValues = new HashSet<AttributeValue>(0);

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
}
