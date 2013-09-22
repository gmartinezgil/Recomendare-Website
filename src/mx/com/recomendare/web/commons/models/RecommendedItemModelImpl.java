/**
 * 
 */
package mx.com.recomendare.web.commons.models;

import java.io.Serializable;

/**
 * @author jerry
 *
 */
public final class RecommendedItemModelImpl implements RecommendedItemModel, Serializable {
	private static final long serialVersionUID = 1L;
	//the fields...
	private int id;
	private String name;
	private String description;
	private short rating;
	private double recommended;
	
	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.commons.models.ItemModel#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.commons.models.ItemModel#getId()
	 */
	public int getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.commons.models.ItemModel#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.commons.models.ItemModel#getRatingValue()
	 */
	public short getRatingValue() {
		return rating;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.commons.models.ItemModel#recommendedValue()
	 */
	public double getRecommendedValue() {
		return recommended;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.commons.models.ItemModel#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.commons.models.ItemModel#setId(int)
	 */
	public void setId(int id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.commons.models.ItemModel#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.commons.models.ItemModel#setRatingValue(short)
	 */
	public void setRatingValue(short ratingValue) {
		this.rating = ratingValue;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.commons.models.ItemModel#setRecommendedValue(double)
	 */
	public void setRecommendedValue(double recommendedValue) {
		this.recommended = recommendedValue;
	}

}//END OF FILE