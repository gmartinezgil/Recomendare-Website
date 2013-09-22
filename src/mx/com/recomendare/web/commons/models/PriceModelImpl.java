/**
 * 
 */
package mx.com.recomendare.web.commons.models;

import java.io.Serializable;

/**
 * @author jerry
 *
 */
public final class PriceModelImpl implements PriceModel, Serializable {
	private static final long serialVersionUID = 1L;
	
	private double minPrice;
	private double maxPrice;
	private String originalCurrencyCode;
	private int originalCurrencyId;
	
	public double getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}
	public double getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}
	public String getOriginalCurrencyCode() {
		return originalCurrencyCode;
	}
	public void setOriginalCurrencyCode(String originalCurrencyCode) {
		this.originalCurrencyCode = originalCurrencyCode;
	}
	public int getOriginalCurrencyId() {
		return originalCurrencyId;
	}
	public void setOriginalCurrencyId(int originalCurrencyId) {
		this.originalCurrencyId = originalCurrencyId;
	}
}