/**
 * 
 * Every user has a role and this class assigns roles to the users.
 * 
 */
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
@Table(name = "Role")
public class Role {

	@Id
	@GeneratedValue(generator = "incrementRole")
	@GenericGenerator(name = "incrementRole", strategy = "increment")
	private int roleId;
	
	private String name;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "role")
	private Set<User> users = new HashSet<User>(0);
	
	public Role() {
	}
	
	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param roleId
	 * @param name
	 */
	public Role(int roleId, String name) {
		super();
		this.roleId = roleId;
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

}
