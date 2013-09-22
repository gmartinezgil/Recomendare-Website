package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

/**
 * UserFriend generated by hbm2java
 */
public class UserFriend implements java.io.Serializable {

	private int id;
	private User user;
	private Constant constant;

	public UserFriend() {
	}

	public UserFriend(int id) {
		this.id = id;
	}

	public UserFriend(int id, User user, Constant constant) {
		this.id = id;
		this.user = user;
		this.constant = constant;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Constant getConstant() {
		return this.constant;
	}

	public void setConstant(Constant constant) {
		this.constant = constant;
	}

}