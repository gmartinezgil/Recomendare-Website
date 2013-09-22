/**
 * 
 */
package mx.com.recomendare.web.commons.models;

import java.io.Serializable;

/**
 * @author jerry
 *
 */
public interface RecommendedItemModel extends Serializable {
	public int getId();
	public void setId(int id);
	public String getName();
	public void setName(String name);
	public String getDescription();
	public void setDescription(String description);
	public double getRecommendedValue();
	public void setRecommendedValue(double recommendedValue);
	public short getRatingValue();
	public void setRatingValue(short ratingValue);
}