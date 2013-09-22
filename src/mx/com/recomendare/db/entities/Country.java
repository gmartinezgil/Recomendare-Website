package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

/**
 * Country generated by hbm2java
 */
public class Country implements java.io.Serializable {

	private int id;
	private Language language;
	private Location location;
	private Currency currency;
	private String code;
	private String mcc;
	private String byServiceName;
	private Boolean isSupported;
	private Set countryNames = new HashSet(0);
	private Set cities = new HashSet(0);

	public Country() {
	}

	public Country(int id) {
		this.id = id;
	}

	public Country(int id, Language language, Location location,
			Currency currency, String code, String mcc, String byServiceName,
			Boolean isSupported, Set countryNames, Set cities) {
		this.id = id;
		this.language = language;
		this.location = location;
		this.currency = currency;
		this.code = code;
		this.mcc = mcc;
		this.byServiceName = byServiceName;
		this.isSupported = isSupported;
		this.countryNames = countryNames;
		this.cities = cities;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Language getLanguage() {
		return this.language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMcc() {
		return this.mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getByServiceName() {
		return this.byServiceName;
	}

	public void setByServiceName(String byServiceName) {
		this.byServiceName = byServiceName;
	}

	public Boolean getIsSupported() {
		return this.isSupported;
	}

	public void setIsSupported(Boolean isSupported) {
		this.isSupported = isSupported;
	}

	public Set getCountryNames() {
		return this.countryNames;
	}

	public void setCountryNames(Set countryNames) {
		this.countryNames = countryNames;
	}

	public Set getCities() {
		return this.cities;
	}

	public void setCities(Set cities) {
		this.cities = cities;
	}

}
