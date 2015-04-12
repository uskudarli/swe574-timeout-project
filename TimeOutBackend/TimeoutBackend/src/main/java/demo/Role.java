/**
 * 
 * Every user has a role and this class assigns roles to the users.
 * 
 */
package demo;

public class Role {

	private int roleId;
	private String name;

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

}
