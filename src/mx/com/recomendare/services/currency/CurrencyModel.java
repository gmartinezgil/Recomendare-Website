/**
 * 
 */
package mx.com.recomendare.services.currency;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jerry
 *
 */
public final class CurrencyModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private double amount;
	private String originalCountryCode;
	private Map<String, Double> conversions;
	
	public CurrencyModel() {
		conversions = new HashMap<String, Double>();
	}
	
	public String getOriginalCountryCode() {
		return originalCountryCode;
	}

	public void setOriginalCountryCode(String originalCountryCode) {
		this.originalCountryCode = originalCountryCode;
	}

	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public void addConversion(final String toCountryCode, final double amount)  {
		conversions.put(toCountryCode, new Double(amount));
	}
	
	public boolean isAlreadyConverted(final String countryCode)  {
		return conversions.containsKey(countryCode);
	}
	
	public double getConversionFromCountry(final String countryCode)  {
		return ((Double)conversions.get(countryCode)).doubleValue();
	}
	
}//END OF FILE