/**
 * 
 */
package mx.com.recomendare.web.commons.models;

import java.io.Serializable;

/**
 * @author jerry
 *
 */
public interface BirthDateModel extends Serializable {
	public short getDay();
	public short getMonth();
	public short getYear();
}
