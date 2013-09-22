/**
 * 
 */
package mx.com.recomendare.services.geo;

import java.io.Serializable;

import mx.com.recomendare.web.commons.models.LocationModelImpl;

/**
 * @author jerry
 *
 */
public final class PlaceModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String URL;
	private String imageURL;
	private LocationModelImpl location;
	
	public PlaceModel() {
		location = new LocationModelImpl();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String url) {
		URL = url;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getLatitude() {
		return String.valueOf(location.getLatitude());
	}

	public void setLatitude(String latitude) {
		location.setLatitude(Float.parseFloat(latitude));
	}

	public String getLongitude() {
		return String.valueOf(location.getLongitude());
	}

	public void setLongitude(String longitude) {
		location.setLongitude(Float.parseFloat(longitude));
	}

	public String getCountryCode() {
		return location.getCountryCode();
	}

	public void setCountryCode(String countryCode) {
		location.setCountryCode(countryCode);
	}

	public String getCountryName() {
		return location.getCountryName();
	}

	public void setCountryName(String countryName) {
		location.setCountryName(countryName);
	}
	
}//END OF FILE