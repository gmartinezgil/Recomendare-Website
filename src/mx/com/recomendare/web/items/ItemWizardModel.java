/**
 * 
 */
package mx.com.recomendare.web.items;

import java.io.Serializable;

import mx.com.recomendare.web.commons.models.SelectOption;

/**
 * @author jerry
 *
 */
public final class ItemWizardModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//the rating...
    public static final int MAX_RATING = 5;

	private SelectOption category;
	private SelectOption subCategory;
	private String name;
	private String description;
	private double minimumPrice;
	private double maximumPrice;
	private SelectOption currency;
	private SelectOption country;
	private SelectOption city;
	private String street;
	private String streetNumber;
	private String phoneNumber;
	private String email;
	private String URL;
    private int rating;
    private float latitude;
    private float longitude;
	
	/**
	 * @return the category
	 */
	public SelectOption getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(SelectOption category) {
		this.category = category;
	}
	/**
	 * @return the subCategory
	 */
	public SelectOption getSubCategory() {
		return subCategory;
	}
	/**
	 * @param subCategory the subCategory to set
	 */
	public void setSubCategory(SelectOption subCategory) {
		this.subCategory = subCategory;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param desription the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	public double getMinimumPrice() {
		return minimumPrice;
	}
	public void setMinimumPrice(double minimumPrice) {
		this.minimumPrice = minimumPrice;
	}
	public double getMaximumPrice() {
		return maximumPrice;
	}
	public void setMaximumPrice(double maximumPrice) {
		this.maximumPrice = maximumPrice;
	}
	public SelectOption getCountry() {
		return country;
	}
	public void setCountry(SelectOption country) {
		this.country = country;
	}
	public SelectOption getCity() {
		return city;
	}
	public void setCity(SelectOption city) {
		this.city = city;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String url) {
		URL = url;
	}
	public SelectOption getCurrency() {
		return currency;
	}
	public void setCurrency(SelectOption currency) {
		this.currency = currency;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(getClass().getName());
		sb.append("] - {category = '");
		sb.append(category);
		sb.append("', subCategory = '");
		sb.append(subCategory);
		sb.append("', name = '");
		sb.append(name);
		sb.append("', description = '");
		sb.append(description);
		sb.append("', minimumPrice = '");
		sb.append(minimumPrice);
		sb.append("', maximumPrice = '");
		sb.append(maximumPrice);
		sb.append("', currency = '");
		sb.append(currency);
		sb.append("', country = '");
		sb.append(country);
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
		sb.append("', URL = '");
		sb.append(URL);
		sb.append("'}");
		return sb.toString();
	}
	
}//END OF FILE