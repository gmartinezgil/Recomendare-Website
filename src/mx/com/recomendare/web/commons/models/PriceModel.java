/**
 * 
 */
package mx.com.recomendare.web.commons.models;

import java.io.Serializable;

/**
 * @author jerry
 *
 */
public interface PriceModel extends Serializable {
	public double getMinPrice();
	public double getMaxPrice();
	public String getOriginalCurrencyCode();
	public int getOriginalCurrencyId();
}//END OF FILE