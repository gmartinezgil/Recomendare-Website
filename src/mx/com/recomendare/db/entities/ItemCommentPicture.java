package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

/**
 * ItemCommentPicture generated by hbm2java
 */
public class ItemCommentPicture implements java.io.Serializable {

	private int id;
	private ItemComment itemComment;
	private Picture picture;

	public ItemCommentPicture() {
	}

	public ItemCommentPicture(int id) {
		this.id = id;
	}

	public ItemCommentPicture(int id, ItemComment itemComment, Picture picture) {
		this.id = id;
		this.itemComment = itemComment;
		this.picture = picture;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ItemComment getItemComment() {
		return this.itemComment;
	}

	public void setItemComment(ItemComment itemComment) {
		this.itemComment = itemComment;
	}

	public Picture getPicture() {
		return this.picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

}
