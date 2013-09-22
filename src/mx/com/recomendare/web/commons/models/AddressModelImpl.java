/**
 * 
 */
package mx.com.recomendare.web.commons.models;

import java.io.Serializable;

/**
 * @author jerry
 *
 */
public final class AddressModelImpl implements AddressModel, Serializable {
	private static final long serialVersionUID = 1L;
	
	private String streetName;
	private String streetOutsideNumber;
	private String telephoneNumber;
	private String homePageURL;
	private String email;
	
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getStreetOutsideNumber() {
		return streetOutsideNumber;
	}
	public void setStreetOutsideNumber(String streetOutsideNumber) {
		this.streetOutsideNumber = streetOutsideNumber;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	public String getHomePageURL() {
		return homePageURL;
	}
	public void setHomePageURL(String homePageURL) {
		this.homePageURL = homePageURL;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}