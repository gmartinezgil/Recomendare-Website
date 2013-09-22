package mx.com.recomendare.web.places;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.recomendare.web.commons.models.ImageModelImpl;
import mx.com.recomendare.web.commons.models.SelectOption;

/**
 * The model for the place to be used by the wizard and it's steps...
 * 
 *  @author jerry
 */
public final class WizardPlaceModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private SelectOption categories;
	private SelectOption subCategories;
	private String name;
	private String description;
	private SelectOption country;
	private SelectOption state;
	private SelectOption city;
	private String street;
	private String streetNumber;
	private String phoneNumber;
	private String email;
	private String url;
	private Date date;
	//the images...
	private List images;
	
	public WizardPlaceModel() {
		images = new ArrayList();
	}
	
	public SelectOption getCategories() {
		return categories;
	}
	public void setCategories(SelectOption category) {
		this.categories = category;
	}
	public SelectOption getSubCategories() {
		return subCategories;
	}
	public void setSubCategories(SelectOption subCategory) {
		this.subCategories = subCategory;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public SelectOption getCountry() {
		return country;
	}
	public void setCountry(SelectOption country) {
		this.country = country;
	}
	public void setState(SelectOption state) {
		this.state = state;
	}
	public SelectOption getState() {
		return state;
	}
	public void setCity(SelectOption city) {
		this.city = city;
	}
	public SelectOption getCity() {
		return city;
	}	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void addPicture(ImageModelImpl picture) {
		images.add(picture);
	}
	
	public List getPictures()  {
		return images;
	}
		
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(getClass().getName());
		sb.append("] - {categories = '");
		sb.append(categories);
		sb.append("', subCategories = '");
		sb.append(subCategories);
		sb.append("', name = '");
		sb.append(name);
		sb.append("', description = '");
		sb.append(description);
		sb.append("', country = '");
		sb.append(country);
		sb.append("', state = '");
		sb.append(state);
		sb.append("', city = '");
		sb.append(city);
		sb.append("', street = '");
		sb.append(street);
		sb.append("', streetNumber = '");
		sb.append(streetNumber);
		sb.append("', phoneNumber = '");
		sb.append(phoneNumber);
		sb.append("', email = '");
		sb.append(email);
		sb.append("', url = '");
		sb.append(url);
		sb.append("', images = '");
		sb.append(images);
		sb.append("'}");
		return sb.toString();
	}

}//END OF FILE