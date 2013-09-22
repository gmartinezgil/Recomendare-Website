package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

import java.util.Date;

/**
 * ItemCommentHistoric generated by hbm2java
 */
public class ItemCommentHistoric implements java.io.Serializable {

	private String id;
	private Constant constant;
	private ItemComment itemComment;
	private Date onDate;

	public ItemCommentHistoric() {
	}

	public ItemCommentHistoric(String id) {
		this.id = id;
	}

	public ItemCommentHistoric(String id, Constant constant,
			ItemComment itemComment, Date onDate) {
		this.id = id;
		this.constant = constant;
		this.itemComment = itemComment;
		this.onDate = onDate;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Constant getConstant() {
		return this.constant;
	}

	public void setConstant(Constant constant) {
		this.constant = constant;
	}

	public ItemComment getItemComment() {
		return this.itemComment;
	}

	public void setItemComment(ItemComment itemComment) {
		this.itemComment = itemComment;
	}

	public Date getOnDate() {
		return this.onDate;
	}

	public void setOnDate(Date onDate) {
		this.onDate = onDate;
	}

}
