/**
 * 
 */
package mx.com.recomendare.web.users;

import java.io.Serializable;

import mx.com.recomendare.web.commons.models.SelectOption;

/**
 * @author jerry
 *
 */
public final class UserWizardModel implements Serializable {
    private static final long serialVersionUID = 1L;
    
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
    //street...
    private String street;
    //phone number...
    private String phoneNumber;
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
    //latitude
    private float latitude;
    //longitude
    private float longitude;
    //street number
    private String streetNumber;
    //the path to get the image...
    private String fileNameOfAvatarPictureToUpload;
    //
    private String zone;
    private String postalCode;

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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
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

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getFileNameOfAvatarPictureToUpload() {
		return fileNameOfAvatarPictureToUpload;
	}

	public void setFileNameOfAvatarPictureToUpload(
			String fileNameOfAvatarPictureToUpload) {
		this.fileNameOfAvatarPictureToUpload = fileNameOfAvatarPictureToUpload;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(getClass().getName());
        sb.append("] - {name = '");
        sb.append(name);
        sb.append("', firstName = '");
        sb.append(firstName);
        sb.append("', lastName = '");
        sb.append(lastName);
        sb.append("', login = '");
        sb.append(login);
        sb.append("', password = '");
        sb.append(password);
        sb.append("', passwordConfirmation = '");
        sb.append(passwordConfirmation);
        sb.append("', captcha = '");
        sb.append(captcha);
        sb.append("', country = '");
        sb.append(country);
        sb.append("', city = '");
        sb.append(city);
        sb.append("', street = '");
        sb.append(street);
        sb.append("', phoneNumber = '");
        sb.append(phoneNumber);
        sb.append("', email = '");
        sb.append(email);
        sb.append("', screenName = '");
        sb.append(screenName);
        sb.append("', language = '");
        sb.append(language);
        sb.append("'}");
        return sb.toString();
    }
    
}//END OF FILE