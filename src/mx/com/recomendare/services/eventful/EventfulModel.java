/**
 * 
 */
package mx.com.recomendare.services.eventful;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jerry
 *
 */
public final class EventfulModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String description;
	private String URL;
	private Date onDate;
	private String place;
	private String price;
	private String direction;
	private String cityName;
	private String countryName;
	private double latitude;
	private double longitude;
	private String imageURL;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String url) {
		URL = url;
	}
	public Date getOnDate() {
		return onDate;
	}
	public void setOnDate(Date onDate) {
		this.onDate = onDate;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(getClass().getName());
		sb.append("] - {title = '");
		sb.append(title);
		sb.append("', description = '");
		sb.append(description);
		sb.append("', URL = '");
		sb.append(URL);
		sb.append("', onDate = '");
		sb.append(onDate);
		sb.append("', place = '");
		sb.append(place);
		sb.append("', price = '");
		sb.append(price);
		sb.append("', direction = '");
		sb.append(direction);
		sb.append("', countryName = '");
		sb.append(countryName);
		sb.append("', cityName = '");
		sb.append(cityName);
		sb.append("', latitude = '");
		sb.append(latitude);
		sb.append("', longitude = '");
		sb.append(longitude);
		sb.append("', imageURL = '");
		sb.append(imageURL);
		sb.append("'}");
		return sb.toString();
	}
}//END OF FILE