package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

import java.util.Date;

/**
 * ItemHistoric generated by hbm2java
 */
public class ItemHistoric implements java.io.Serializable {

	private int id;
	private Item item;
	private Constant constant;
	private User user;
	private Date onDate;

	public ItemHistoric() {
	}

	public ItemHistoric(int id) {
		this.id = id;
	}

	public ItemHistoric(int id, Item item, Constant constant, User user,
			Date onDate) {
		this.id = id;
		this.item = item;
		this.constant = constant;
		this.user = user;
		this.onDate = onDate;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Constant getConstant() {
		return this.constant;
	}

	public void setConstant(Constant constant) {
		this.constant = constant;
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

}
