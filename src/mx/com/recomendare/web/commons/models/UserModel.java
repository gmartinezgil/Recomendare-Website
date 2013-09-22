/**
 * 
 */
package mx.com.recomendare.web.commons.models;

import java.io.Serializable;

/**
 * @author jerry
 *
 */
public interface UserModel extends Serializable {
	public int getId();
	//description
	public String getName();
	public String getFirstName();
	public String getLastName();
	public String getScreenName();
	public String getLogin();
	public String getPassword();
	public int getGenderId();
	public String getGenderName();
	public BirthDateModel getBirthDate();
	public String getLastIPAddress();
	//address
	public AddressModel getAddress();
	//location
	public LocationModel getLocation();
	//picture
	public PictureModel getAvatarPicture();
}