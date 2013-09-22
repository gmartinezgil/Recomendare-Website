/**
 * 
 */
package mx.com.recomendare.web.users;

import mx.com.recomendare.web.commons.models.SelectOption;

/**
 * @author jerry
 *
 */
public final class UserFormModel {
	//the name...
    private String name;
    //the first name...
    private String firstName;
    //the last name...
    private String lastName;
    //the login...
    private String login;
    //the password...
    private String password;
    //the password confirmation...
    private String passwordConfirmation;
    //the captcha...
    private String captcha;
    //countries...
    private SelectOption country;
    //city...
    private SelectOption city;
  //email...
    private String email;
    //the screen name...
    private String screenName;
    //the language
    private SelectOption language;
    //the gender
    private SelectOption gender;
    //day
    private SelectOption day;
    //month
    private SelectOption month;
    //month
    private SelectOption year;
  //the path to get the image...
    private String fileNameOfAvatarPictureToUpload;
  //latitude
    private float latitude;
    //longitude
    private float longitude;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}
	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	public SelectOption getCountry() {
		return country;
	}
	public void setCountry(SelectOption country) {
		this.country = country;
	}
	public SelectOption getCity() {
		return city;
	}
	public void setCity(SelectOption city) {
		this.city = city;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public SelectOption getLanguage() {
		return language;
	}
	public void setLanguage(SelectOption language) {
		this.language = language;
	}
	public SelectOption getGender() {
		return gender;
	}
	public void setGender(SelectOption gender) {
		this.gender = gender;
	}
	public SelectOption getDay() {
		return day;
	}
	public void setDay(SelectOption day) {
		this.day = day;
	}
	public SelectOption getMonth() {
		return month;
	}
	public void setMonth(SelectOption month) {
		this.month = month;
	}
	public SelectOption getYear() {
		return year;
	}
	public void setYear(SelectOption year) {
		this.year = year;
	}
	public String getFileNameOfAvatarPictureToUpload() {
		return fileNameOfAvatarPictureToUpload;
	}
	public void setFileNameOfAvatarPictureToUpload(
			String fileNameOfAvatarPictureToUpload) {
		this.fileNameOfAvatarPictureToUpload = fileNameOfAvatarPictureToUpload;
	}
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
    
    
}//END OF FILE