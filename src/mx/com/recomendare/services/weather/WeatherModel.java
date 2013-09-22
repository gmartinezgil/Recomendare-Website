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
public interface WeatherModel extends Serializable{
	public String getTemperature();
	public void setTemperature(String temperature);
	public String getTemperatureHigh();
	public void setTemperatureHigh(String temperatureHigh);
	public String getTemperatureLow();
	public void setTemperatureLow(String temperatureLow);
	public String getURLForPicture();
	public void setURLForPicture(String url);
	public Date getTimeUpdated();
	public void setTimeUpdated(Date date);
	public String getCityName();
	public void setCityName(String city);
	public String getCountryName();
	public void setCountryName(String country);
	public double getLatitude();
	public void setLatitude(double latitude);
	public double getLongitude();
	public void setLongitude(double longitude);
}//END OF FILE