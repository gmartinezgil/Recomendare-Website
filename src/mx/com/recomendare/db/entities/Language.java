package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

/**
 * Language generated by hbm2java
 */
public class Language implements java.io.Serializable {

	private int id;
	private String code;
	private String name;
	private String characterSet;
	private Set countryNames = new HashSet(0);
	private Set constantNames = new HashSet(0);
	private Set userFavoriteComments = new HashSet(0);
	private Set cityNames = new HashSet(0);
	private Set requestComments = new HashSet(0);
	private Set labels = new HashSet(0);
	private Set userConfigurations = new HashSet(0);
	private Set currencyNames = new HashSet(0);
	private Set eventDescriptions = new HashSet(0);
	private Set subcategoryDescriptions = new HashSet(0);
	private Set pictureComments = new HashSet(0);
	private Set itemCommentTexts = new HashSet(0);
	private Set categoryDescriptions = new HashSet(0);
	private Set countries = new HashSet(0);
	private Set itemResumes = new HashSet(0);

	public Language() {
	}

	public Language(int id) {
		this.id = id;
	}

	public Language(int id, String code, String name, String characterSet,
			Set countryNames, Set constantNames, Set userFavoriteComments,
			Set cityNames, Set requestComments, Set labels,
			Set userConfigurations, Set currencyNames, Set eventDescriptions,
			Set subcategoryDescriptions, Set pictureComments,
			Set itemCommentTexts, Set categoryDescriptions, Set countries,
			Set itemResumes) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.characterSet = characterSet;
		this.countryNames = countryNames;
		this.constantNames = constantNames;
		this.userFavoriteComments = userFavoriteComments;
		this.cityNames = cityNames;
		this.requestComments = requestComments;
		this.labels = labels;
		this.userConfigurations = userConfigurations;
		this.currencyNames = currencyNames;
		this.eventDescriptions = eventDescriptions;
		this.subcategoryDescriptions = subcategoryDescriptions;
		this.pictureComments = pictureComments;
		this.itemCommentTexts = itemCommentTexts;
		this.categoryDescriptions = categoryDescriptions;
		this.countries = countries;
		this.itemResumes = itemResumes;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCharacterSet() {
		return this.characterSet;
	}

	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}

	public Set getCountryNames() {
		return this.countryNames;
	}

	public void setCountryNames(Set countryNames) {
		this.countryNames = countryNames;
	}

	public Set getConstantNames() {
		return this.constantNames;
	}

	public void setConstantNames(Set constantNames) {
		this.constantNames = constantNames;
	}

	public Set getUserFavoriteComments() {
		return this.userFavoriteComments;
	}

	public void setUserFavoriteComments(Set userFavoriteComments) {
		this.userFavoriteComments = userFavoriteComments;
	}

	public Set getCityNames() {
		return this.cityNames;
	}

	public void setCityNames(Set cityNames) {
		this.cityNames = cityNames;
	}

	public Set getRequestComments() {
		return this.requestComments;
	}

	public void setRequestComments(Set requestComments) {
		this.requestComments = requestComments;
	}

	public Set getLabels() {
		return this.labels;
	}

	public void setLabels(Set labels) {
		this.labels = labels;
	}

	public Set getUserConfigurations() {
		return this.userConfigurations;
	}

	public void setUserConfigurations(Set userConfigurations) {
		this.userConfigurations = userConfigurations;
	}

	public Set getCurrencyNames() {
		return this.currencyNames;
	}

	public void setCurrencyNames(Set currencyNames) {
		this.currencyNames = currencyNames;
	}

	public Set getEventDescriptions() {
		return this.eventDescriptions;
	}

	public void setEventDescriptions(Set eventDescriptions) {
		this.eventDescriptions = eventDescriptions;
	}

	public Set getSubcategoryDescriptions() {
		return this.subcategoryDescriptions;
	}

	public void setSubcategoryDescriptions(Set subcategoryDescriptions) {
		this.subcategoryDescriptions = subcategoryDescriptions;
	}

	public Set getPictureComments() {
		return this.pictureComments;
	}

	public void setPictureComments(Set pictureComments) {
		this.pictureComments = pictureComments;
	}

	public Set getItemCommentTexts() {
		return this.itemCommentTexts;
	}

	public void setItemCommentTexts(Set itemCommentTexts) {
		this.itemCommentTexts = itemCommentTexts;
	}

	public Set getCategoryDescriptions() {
		return this.categoryDescriptions;
	}

	public void setCategoryDescriptions(Set categoryDescriptions) {
		this.categoryDescriptions = categoryDescriptions;
	}

	public Set getCountries() {
		return this.countries;
	}

	public void setCountries(Set countries) {
		this.countries = countries;
	}

	public Set getItemResumes() {
		return this.itemResumes;
	}

	public void setItemResumes(Set itemResumes) {
		this.itemResumes = itemResumes;
	}

}