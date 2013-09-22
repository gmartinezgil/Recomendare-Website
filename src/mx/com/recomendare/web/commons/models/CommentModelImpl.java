/**
 * 
 */
package mx.com.recomendare.web.commons.models;

import java.io.Serializable;

/**
 * @author jerry
 *
 */
public final class CommentModelImpl implements CommentModel, Serializable {
	private static final long serialVersionUID = 1L;
	
	private short rating;
	private int userId;
	private String userScreenName;
	private PictureModel userAvatarPicture;
	private String userComments;
	private int thumbsUp;
	private int thumbsDown;
	
	public short getRating() {
		return rating;
	}
	public void setRating(short rating) {
		this.rating = rating;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserScreenName() {
		return userScreenName;
	}
	public void setUserScreenName(String userScreenName) {
		this.userScreenName = userScreenName;
	}
	public String getUserComment() {
		return userComments;
	}
	public void setUserComments(String userComments) {
		this.userComments = userComments;
	}
	public int getThumbsUp() {
		return thumbsUp;
	}
	public void setThumbsUp(int thumbsUp) {
		this.thumbsUp = thumbsUp;
	}
	public int getThumbsDown() {
		return thumbsDown;
	}
	public void setThumbsDown(int thumbsDown) {
		this.thumbsDown = thumbsDown;
	}
	public PictureModel getUserAvatarPicture() {
		return userAvatarPicture;
	}
	public void setUserAvatarPicture(PictureModel userAvatarPicture) {
		this.userAvatarPicture = userAvatarPicture;
	}
}