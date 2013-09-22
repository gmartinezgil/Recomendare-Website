package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

/**
 * Zone generated by hbm2java
 */
public class Zone implements java.io.Serializable {

	private int id;
	private City city;
	private String name;
	private Set streets = new HashSet(0);
	private Set postalCodes = new HashSet(0);

	public Zone() {
	}

	public Zone(int id) {
		this.id = id;
	}

	public Zone(int id, City city, String name, Set streets, Set postalCodes) {
		this.id = id;
		this.city = city;
		this.name = name;
		this.streets = streets;
		this.postalCodes = postalCodes;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set getStreets() {
		return this.streets;
	}

	public void setStreets(Set streets) {
		this.streets = streets;
	}

	public Set getPostalCodes() {
		return this.postalCodes;
	}

	public void setPostalCodes(Set postalCodes) {
		this.postalCodes = postalCodes;
	}

}