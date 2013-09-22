package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

/**
 * UserProfile generated by hbm2java
 */
public class UserProfile implements java.io.Serializable {

	private int id;
	private String name;
	private Set users = new HashSet(0);

	public UserProfile() {
	}

	public UserProfile(int id) {
		this.id = id;
	}

	public UserProfile(int id, String name, Set users) {
		this.id = id;
		this.name = name;
		this.users = users;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set getUsers() {
		return this.users;
	}

	public void setUsers(Set users) {
		this.users = users;
	}

}
