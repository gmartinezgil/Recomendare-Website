/**
 * 
 */
package mx.com.recomendare.web.commons.models;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jerry
 *
 */
public interface ItemModel extends Serializable {
	public int getId();
	//description
	public String getName();
	public String getDescription();
	//ratings
	public int getNumberOfRatings();
	public short getOverallRating();
	//category
	public CategoryModel getCategory();
	//location
	public LocationModel getLocation();
	//pictures
	public List<PictureModel> getPictures();
	//price
	public PriceModel getPrice();
	//directions
	public AddressModel getAddress();
	//comments
	public List<CommentModel> getComments();
}//END OF FILE