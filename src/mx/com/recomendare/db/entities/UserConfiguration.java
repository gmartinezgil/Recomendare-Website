package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

/**
 * UserConfiguration generated by hbm2java
 */
public class UserConfiguration implements java.io.Serializable {

	private int id;
	private City city;
	private Language language;
	private Picture picture;
	private String screenName;
	private Boolean isActive;
	private String confirmationKey;
	private Set users = new HashSet(0);
	private Set userConfigurationLocations = new HashSet(0);

	public UserConfiguration() {
	}

	public UserConfiguration(int id) {
		this.id = id;
	}

	public UserConfiguration(int id, City city, Language language,
			Picture picture, String screenName, Boolean isActive,
			String confirmationKey, Set users, Set userConfigurationLocations) {
		this.id = id;
		this.city = city;
		this.language = language;
		this.picture = picture;
		this.screenName = screenName;
		this.isActive = isActive;
		this.confirmationKey = confirmationKey;
		this.users = users;
		this.userConfigurationLocations = userConfigurationLocations;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public City getCity() {
		return this.city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Language getLanguage() {
		return this.language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Picture getPicture() {
		return this.picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public String getScreenName() {
		return this.screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getConfirmationKey() {
		return this.confirmationKey;
	}

	public void setConfirmationKey(String confirmationKey) {
		this.confirmationKey = confirmationKey;
	}

	public Set getUsers() {
		return this.users;
	}

	public void setUsers(Set users) {
		this.users = users;
	}

	public Set getUserConfigurationLocations() {
		return this.userConfigurationLocations;
	}

	public void setUserConfigurationLocations(Set userConfigurationLocations) {
		this.userConfigurationLocations = userConfigurationLocations;
	}

}