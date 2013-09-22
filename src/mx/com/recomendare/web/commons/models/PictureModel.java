/**
 * 
 */
package mx.com.recomendare.web.commons.models;

import java.io.Serializable;

/**
 * @author Jerry
 *
 */
public interface PictureModel extends Serializable {
	//picture one
	public int getId();
	public byte[] getContent();
	public String getContentType();
	public String getName();
	public String getComments();
}//END OF FILE