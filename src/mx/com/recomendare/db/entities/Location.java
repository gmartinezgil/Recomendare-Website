package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

/**
 * Location generated by hbm2java
 */
public class Location implements java.io.Serializable {

	private int id;
	private Double latitude;
	private Double longitude;
	private Set itemLocations = new HashSet(0);
	private Set countries = new HashSet(0);
	private Set clickstreamStatistics = new HashSet(0);
	private Set userConfigurationLocations = new HashSet(0);
	private Set hotspotStatistics = new HashSet(0);
	private Set searchStatistics = new HashSet(0);
	private Set cities = new HashSet(0);
	private Set cellTowers = new HashSet(0);

	public Location() {
	}

	public Location(int id) {
		this.id = id;
	}

	public Location(int id, Double latitude, Double longitude,
			Set itemLocations, Set countries, Set clickstreamStatistics,
			Set userConfigurationLocations, Set hotspotStatistics,
			Set searchStatistics, Set cities, Set cellTowers) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.itemLocations = itemLocations;
		this.countries = countries;
		this.clickstreamStatistics = clickstreamStatistics;
		this.userConfigurationLocations = userConfigurationLocations;
		this.hotspotStatistics = hotspotStatistics;
		this.searchStatistics = searchStatistics;
		this.cities = cities;
		this.cellTowers = cellTowers;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Set getItemLocations() {
		return this.itemLocations;
	}

	public void setItemLocations(Set itemLocations) {
		this.itemLocations = itemLocations;
	}

	public Set getCountries() {
		return this.countries;
	}

	public void setCountries(Set countries) {
		this.countries = countries;
	}

	public Set getClickstreamStatistics() {
		return this.clickstreamStatistics;
	}

	public void setClickstreamStatistics(Set clickstreamStatistics) {
		this.clickstreamStatistics = clickstreamStatistics;
	}

	public Set getUserConfigurationLocations() {
		return this.userConfigurationLocations;
	}

	public void setUserConfigurationLocations(Set userConfigurationLocations) {
		this.userConfigurationLocations = userConfigurationLocations;
	}

	public Set getHotspotStatistics() {
		return this.hotspotStatistics;
	}

	public void setHotspotStatistics(Set hotspotStatistics) {
		this.hotspotStatistics = hotspotStatistics;
	}

	public Set getSearchStatistics() {
		return this.searchStatistics;
	}

	public void setSearchStatistics(Set searchStatistics) {
		this.searchStatistics = searchStatistics;
	}

	public Set getCities() {
		return this.cities;
	}

	public void setCities(Set cities) {
		this.cities = cities;
	}

	public Set getCellTowers() {
		return this.cellTowers;
	}

	public void setCellTowers(Set cellTowers) {
		this.cellTowers = cellTowers;
	}

}