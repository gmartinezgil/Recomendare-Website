package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

/**
 * ItemPicture generated by hbm2java
 */
public class ItemPicture implements java.io.Serializable {

	private int id;
	private Item item;
	private Picture picture;

	public ItemPicture() {
	}

	public ItemPicture(int id) {
		this.id = id;
	}

	public ItemPicture(int id, Item item, Picture picture) {
		this.id = id;
		this.item = item;
		this.picture = picture;
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

	public Picture getPicture() {
		return this.picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

}