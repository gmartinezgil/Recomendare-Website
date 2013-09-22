package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Event generated by hbm2java
 */
public class Event implements java.io.Serializable {

	private int id;
	private User user;
	private Constant constant;
	private Date onDate;
	private Set eventDescriptions = new HashSet(0);

	public Event() {
	}

	public Event(int id) {
		this.id = id;
	}

	public Event(int id, User user, Constant constant, Date onDate,
			Set eventDescriptions) {
		this.id = id;
		this.user = user;
		this.constant = constant;
		this.onDate = onDate;
		this.eventDescriptions = eventDescriptions;
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

	public Date getOnDate() {
		return this.onDate;
	}

	public void setOnDate(Date onDate) {
		this.onDate = onDate;
	}

	public Set getEventDescriptions() {
		return this.eventDescriptions;
	}

	public void setEventDescriptions(Set eventDescriptions) {
		this.eventDescriptions = eventDescriptions;
	}

}