/**
 * 
 */
package mx.com.recomendare.web.commons.models;

import java.io.Serializable;

/**
 * @author jerry
 *
 */
public interface AddressModel extends Serializable {
	//directions
	public String getStreetName();
	public String getStreetOutsideNumber();
	public String getTelephoneNumber();
	public String getHomePageURL();
	public String getEmail();
}//END OF FILE