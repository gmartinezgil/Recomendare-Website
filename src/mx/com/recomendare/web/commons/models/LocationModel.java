/**
 * 
 */
package mx.com.recomendare.web.commons.models;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author Jerry
 *
 */
public interface LocationModel extends Serializable {
	//id
	public int getId();
	public String getName();
	//city
	public int getCityId();
	public String getCityName();
	//country
	public int getCountryId();
	public String getCountryName();
	public String getCountryCode();
	//location
	public float getLatitude();
	public float getLongitude();
	//language
	public int getLanguageId();
	public String getLanguageName();
	public String getLanguageCode();
	//currency
	public int getCurrencyId();
	public String getCurrencyName();
	public String getCurrencyCode();
	//time zone
	public String getTimeZone();
	//zone 
	public int getZoneId();
	public String getZoneName();
	//street
	public int getStreetId();
	public String getStreetName();
	public String getStreetNumber();
	//postal code
	public int getPostalCodeId();
	public String getPostalCodeNumber();
	//locale
	public Locale getLocale();
	//summary
	public String getSummary();
}//END OF FILE