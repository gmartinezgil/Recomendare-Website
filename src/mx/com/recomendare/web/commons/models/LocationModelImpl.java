/**
 * 
 */
package mx.com.recomendare.web.commons.models;

import java.io.Serializable;
import java.util.Locale;


/**
 * @author jerry
 *
 */
public final class LocationModelImpl implements LocationModel, Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private float latitude;
	private float longitude;
	private String countryName;
	private String countryCode;
	private int countryId;
	private String cityName;
	private int cityId;
	private String timeZone;
	private int languageId;
	private String languageCode;
	private String languageName;
	private String currencyCode;
	private String currencyName;
	private int currencyId;
	private Locale locale;
	private String summary;
	private int postalCodeId;
	private String postalCodeNumber;
	private int streetId;
	private String streetName;
	private String streetNumber;
	private int zoneId;
	private String zoneName;
	
	
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
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String country) {
		this.countryName = country;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String city) {
		this.cityName = city;
	}

	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}
	public int getLanguageId() {
		return languageId;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}
	public String getLanguageName() {
		return languageName;
	}
	
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public int getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPostalCodeId() {
		return postalCodeId;
	}
	public void setPostalCodeId(int postalCodeId) {
		this.postalCodeId = postalCodeId;
	}
	public String getPostalCodeNumber() {
		return postalCodeNumber;
	}
	public void setPostalCodeNumber(String postalCodeNumber) {
		this.postalCodeNumber = postalCodeNumber;
	}
	public int getStreetId() {
		return streetId;
	}
	public void setStreetId(int streetId) {
		this.streetId = streetId;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getStreetNumber() {
		return streetNumber;
	}
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
	public int getZoneId() {
		return zoneId;
	}
	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(getClass().getName());
		sb.append("] - {countryName = '");
		sb.append(countryName);
		sb.append("', countryCode = '");
		sb.append(countryCode);
		sb.append("', countryId = '");
		sb.append(countryId);
		sb.append("', cityName = '");
		sb.append(cityName);
		sb.append("', cityId = '");
		sb.append(cityId);
		sb.append("', latitude = '");
		sb.append(latitude);
		sb.append("', longitude = '");
		sb.append(longitude);
		sb.append("', languageId = '");
		sb.append(languageId);
		sb.append("', languageCode = '");
		sb.append(languageCode);
		sb.append("', languageName = '");
		sb.append(languageName);
		sb.append("', currencyCode = '");
		sb.append(currencyCode);
		sb.append("', currencyName = '");
		sb.append(currencyName);
		sb.append("', locale = '");
		sb.append(locale);
		sb.append("', timeZone = '");
		sb.append(timeZone);
		sb.append("', summary = '");
		sb.append(summary);
		sb.append("'}");
		return sb.toString();
	}

}//END OF FILE