package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ItemSubmittedBy generated by hbm2java
 */
public class ItemSubmittedBy implements java.io.Serializable {

	private int id;
	private User user;
	private Date onDate;
	private Short rating;
	private Set items = new HashSet(0);

	public ItemSubmittedBy() {
	}

	public ItemSubmittedBy(int id) {
		this.id = id;
	}

	public ItemSubmittedBy(int id, User user, Date onDate, Short rating,
			Set items) {
		this.id = id;
		this.user = user;
		this.onDate = onDate;
		this.rating = rating;
		this.items = items;
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

	public Date getOnDate() {
		return this.onDate;
	}

	public void setOnDate(Date onDate) {
		this.onDate = onDate;
	}

	public Short getRating() {
		return this.rating;
	}

	public void setRating(Short rating) {
		this.rating = rating;
	}

	public Set getItems() {
		return this.items;
	}

	public void setItems(Set items) {
		this.items = items;
	}

}
