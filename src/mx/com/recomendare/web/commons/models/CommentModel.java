/**
 * 
 */
package mx.com.recomendare.web.commons.models;

import java.io.Serializable;

/**
 * @author jerry
 *
 */
public interface CommentModel extends Serializable {
	public short getRating();
	public int getUserId();
	public String getUserScreenName();
	public PictureModel getUserAvatarPicture();
	public String getUserComment();
	public int getThumbsUp();
	public int getThumbsDown();
}//END OF FILE