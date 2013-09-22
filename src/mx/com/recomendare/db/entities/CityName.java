package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

/**
 * CityName generated by hbm2java
 */
public class CityName implements java.io.Serializable {

	private int id;
	private City city;
	private Language language;
	private String name;

	public CityName() {
	}

	public CityName(int id) {
		this.id = id;
	}

	public CityName(int id, City city, Language language, String name) {
		this.id = id;
		this.city = city;
		this.language = language;
		this.name = name;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}