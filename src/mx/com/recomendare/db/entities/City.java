package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

/**
 * City generated by hbm2java
 */
public class City implements java.io.Serializable {

	private int id;
	private Location location;
	private Country country;
	private String timezone;
	private String byServiceName;
	private Set userConfigurations = new HashSet(0);
	private Set zones = new HashSet(0);
	private Set cityNames = new HashSet(0);

	public City() {
	}

	public City(int id) {
		this.id = id;
	}

	public City(int id, Location location, Country country, String timezone,
			String byServiceName, Set userConfigurations, Set zones,
			Set cityNames) {
		this.id = id;
		this.location = location;
		this.country = country;
		this.timezone = timezone;
		this.byServiceName = byServiceName;
		this.userConfigurations = userConfigurations;
		this.zones = zones;
		this.cityNames = cityNames;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getTimezone() {
		return this.timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getByServiceName() {
		return this.byServiceName;
	}

	public void setByServiceName(String byServiceName) {
		this.byServiceName = byServiceName;
	}

	public Set getUserConfigurations() {
		return this.userConfigurations;
	}

	public void setUserConfigurations(Set userConfigurations) {
		this.userConfigurations = userConfigurations;
	}

	public Set getZones() {
		return this.zones;
	}

	public void setZones(Set zones) {
		this.zones = zones;
	}

	public Set getCityNames() {
		return this.cityNames;
	}

	public void setCityNames(Set cityNames) {
		this.cityNames = cityNames;
	}

}
