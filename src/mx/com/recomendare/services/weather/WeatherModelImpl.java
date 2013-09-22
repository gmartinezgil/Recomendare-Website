/**
 * 
 */
package mx.com.recomendare.services.weather;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jerry
 *
 */
public final class WeatherModelImpl implements WeatherModel, Serializable {
	private static final long serialVersionUID = 1L;
	private String temperature;
	private String temperatureHigh;
	private String temperatureLow;
	private String urlForPicture;
	private Date timeUpdated;
	private double latitude;
	private double longitude;
	private String cityName;
	private String countryName;
	
	/**
	 * 
	 */

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.weather.WeatherModel#getTemperature()
	 */
	public String getTemperature() {
		return temperature;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.weather.WeatherModel#getTemperatureHigh()
	 */
	public String getTemperatureHigh() {
		return temperatureHigh;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.weather.WeatherModel#getTemperatureLow()
	 */
	public String getTemperatureLow() {
		return temperatureLow;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.weather.WeatherModel#getURLForPicture()
	 */
	public String getURLForPicture() {
		return urlForPicture;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.weather.WeatherModel#setTemperature(java.lang.String)
	 */
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.weather.WeatherModel#setTemperatureHigh(java.lang.String)
	 */
	public void setTemperatureHigh(String temperatureHigh) {
		this.temperatureHigh = temperatureHigh;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.weather.WeatherModel#setTemperatureLow(java.lang.String)
	 */
	public void setTemperatureLow(String temperatureLow) {
		this.temperatureLow = temperatureLow;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.weather.WeatherModel#setURLForPicture(java.lang.String)
	 */
	public void setURLForPicture(String url) {
		this.urlForPicture = url;
	}

	public Date getTimeUpdated() {
		return timeUpdated;
	}

	public void setTimeUpdated(Date timeUpdated) {
		this.timeUpdated = timeUpdated;
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

	public String toString()  {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(getClass().getName());
		sb.append("] - {cityName = '");
		sb.append(cityName);
		sb.append("', countryName = '");
		sb.append(countryName);
		sb.append("', latitude = '");
		sb.append(latitude);
		sb.append("', longitude = '");
		sb.append(longitude);
		sb.append("', temperature = '");
		sb.append(temperature);
		sb.append("', temperatureHigh = '");
		sb.append(temperatureHigh);
		sb.append("', temperatureLow = '");
		sb.append(temperatureLow);
		sb.append("', urlForPicture = '");
		sb.append(urlForPicture);
		sb.append("', timeUpdated = '");
		sb.append(timeUpdated);
		sb.append("'}");
		return sb.toString();
	}

}//END OF FILE