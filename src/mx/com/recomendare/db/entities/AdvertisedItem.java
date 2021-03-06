package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

import java.util.Date;

/**
 * AdvertisedItem generated by hbm2java
 */
public class AdvertisedItem implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private Item item;
	private Date displayFrom;
	private Date displayUntil;

	public AdvertisedItem() {
	}

	public AdvertisedItem(int id) {
		this.id = id;
	}

	public AdvertisedItem(int id, Item item, Date displayFrom, Date displayUntil) {
		this.id = id;
		this.item = item;
		this.displayFrom = displayFrom;
		this.displayUntil = displayUntil;
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

	public Date getDisplayFrom() {
		return this.displayFrom;
	}

	public void setDisplayFrom(Date displayFrom) {
		this.displayFrom = displayFrom;
	}

	public Date getDisplayUntil() {
		return this.displayUntil;
	}

	public void setDisplayUntil(Date displayUntil) {
		this.displayUntil = displayUntil;
	}

}
