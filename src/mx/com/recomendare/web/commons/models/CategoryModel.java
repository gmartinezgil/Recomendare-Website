/**
 * 
 */
package mx.com.recomendare.web.commons.models;

import java.io.Serializable;

/**
 * @author jerry
 *
 */
public interface CategoryModel extends Serializable {
	//category
	public int getCategoryId();
	public String getCategoryName();
	//sub category
	public int getSubcategoryId();
	public String getSubcategoryName();
}//END OF FILE