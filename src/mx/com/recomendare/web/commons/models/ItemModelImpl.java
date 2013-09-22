/**
 * 
 */
package mx.com.recomendare.web.commons.models;

import java.io.Serializable;
import java.util.List;

/**
 * @author jerry
 *
 */
public final class ItemModelImpl implements ItemModel, Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private String description;
	private int numberOfRatings;
	private short overallRating;
	private CategoryModel category;
	private LocationModel location;
	private List<PictureModel> pictures;
	private PriceModel price;
	private AddressModel address;
	private List<CommentModel> comments;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getNumberOfRatings() {
		return numberOfRatings;
	}
	public void setNumberOfRatings(int numberOfRatings) {
		this.numberOfRatings = numberOfRatings;
	}
	public short getOverallRating() {
		return overallRating;
	}
	public void setOverallRating(short overallRating) {
		this.overallRating = overallRating;
	}
	public CategoryModel getCategory() {
		return category;
	}
	public void setCategory(CategoryModel category) {
		this.category = category;
	}
	public LocationModel getLocation() {
		return location;
	}
	public void setLocation(LocationModel location) {
		this.location = location;
	}
	public List<PictureModel> getPictures() {
		return pictures;
	}
	public void setPictures(List<PictureModel> pictures) {
		this.pictures = pictures;
	}
	public PriceModel getPrice() {
		return price;
	}
	public void setPrice(PriceModel price) {
		this.price = price;
	}
	public AddressModel getAddress() {
		return address;
	}
	public void setAddress(AddressModel address) {
		this.address = address;
	}
	public List<CommentModel> getComments() {
		return comments;
	}
	public void setComments(List<CommentModel> comments) {
		this.comments = comments;
	}

}//END OF FILE