package mx.com.recomendare.db.entities;

// Generated May 13, 2009 5:10:26 PM by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

/**
 * Picture generated by hbm2java
 */
public class Picture implements java.io.Serializable {

	private int id;
	private Constant constantByTypeId;
	private Constant constantByExtensionId;
	private byte[] content;
	private Set contacts = new HashSet(0);
	private Set itemPictures = new HashSet(0);
	private Set banners = new HashSet(0);
	private Set itemCommentPictures = new HashSet(0);
	private Set pictureComments = new HashSet(0);
	private Set userConfigurations = new HashSet(0);

	public Picture() {
	}

	public Picture(int id) {
		this.id = id;
	}

	public Picture(int id, Constant constantByTypeId,
			Constant constantByExtensionId, byte[] content, Set contacts,
			Set itemPictures, Set banners, Set itemCommentPictures,
			Set pictureComments, Set userConfigurations) {
		this.id = id;
		this.constantByTypeId = constantByTypeId;
		this.constantByExtensionId = constantByExtensionId;
		this.content = content;
		this.contacts = contacts;
		this.itemPictures = itemPictures;
		this.banners = banners;
		this.itemCommentPictures = itemCommentPictures;
		this.pictureComments = pictureComments;
		this.userConfigurations = userConfigurations;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Constant getConstantByTypeId() {
		return this.constantByTypeId;
	}

	public void setConstantByTypeId(Constant constantByTypeId) {
		this.constantByTypeId = constantByTypeId;
	}

	public Constant getConstantByExtensionId() {
		return this.constantByExtensionId;
	}

	public void setConstantByExtensionId(Constant constantByExtensionId) {
		this.constantByExtensionId = constantByExtensionId;
	}

	public byte[] getContent() {
		return this.content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public Set getContacts() {
		return this.contacts;
	}

	public void setContacts(Set contacts) {
		this.contacts = contacts;
	}

	public Set getItemPictures() {
		return this.itemPictures;
	}

	public void setItemPictures(Set itemPictures) {
		this.itemPictures = itemPictures;
	}

	public Set getBanners() {
		return this.banners;
	}

	public void setBanners(Set banners) {
		this.banners = banners;
	}

	public Set getItemCommentPictures() {
		return this.itemCommentPictures;
	}

	public void setItemCommentPictures(Set itemCommentPictures) {
		this.itemCommentPictures = itemCommentPictures;
	}

	public Set getPictureComments() {
		return this.pictureComments;
	}

	public void setPictureComments(Set pictureComments) {
		this.pictureComments = pictureComments;
	}

	public Set getUserConfigurations() {
		return this.userConfigurations;
	}

	public void setUserConfigurations(Set userConfigurations) {
		this.userConfigurations = userConfigurations;
	}

}
