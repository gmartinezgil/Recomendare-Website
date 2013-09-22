/**
 * 
 */
package mx.com.recomendare.services.currency;

import java.io.IOException;
import java.text.ParseException;

import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.services.cache.CacheService;
import mx.com.recomendare.services.db.CurrenciesDAO;
import mx.com.recomendare.services.db.DatabaseService;
import net.sf.ehcache.CacheException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

/**
 * @author jerry
 *
 */
public final class CurrencyConverterService extends AbstractService {
	//the log...
    private static final Log log = LogFactory.getLog(CurrencyConverterService.class);
	
	//the converter...
	private CurrencyConverter converter;
	//the cache...
	private CacheService cacheService;
	//the database to correlate the currency used in the country...
	private DatabaseService database;
	
	public static final String MXN_PESO = "MXN";
	public static final String USD_DOLLAR = "USD";
	public static final String BRL_REAL = "BRL";
	public static final String CAD_DOLLAR = "CAD";
	
	/**
	 * 
	 */
	public CurrencyConverterService(final DatabaseService databaseService) {
		database = databaseService;
		doStart();
	}
	
	//********************************
	//*********SERVICE***************
	//********************************
	/**
	 * 
	 */
	public void doStart() {
		log.info("Starting - "+getClass().getName()+"...");
		if(!started)  {
			converter = CurrencyConverter.getInstance();
			try {
				cacheService = new CacheService("currencyservice");
				started = true;
				log.info(getClass().getName()+"...started!");
			} catch (CacheException e) {
				log.error("Can't make use of the cache service - "+e.getMessage(), e);
			}
		}
		else  {
			log.error(getClass().getName()+"...already started!");
		}
	}
	
	/**
	 * 
	 */
	public void doStop() {
		if(started)  {
			log.info("Stoping - "+getClass().getName()+"...");
			converter.clearCache();
			converter = null;
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}
	
	/**
	 * 
	 * @param amount
	 * @param fromCountryCode
	 * @param toCountryCode
	 * @return
	 */
	//TODO: missing fromCountryCode validation...
	public double convertCurrencyFromCountryTo(final double amount, final String fromCountryCode, final String toCountryCode)  {
		if(fromCountryCode.equalsIgnoreCase(toCountryCode)) return amount;
		if(
				"MX".equalsIgnoreCase(toCountryCode) || 
				"US".equalsIgnoreCase(toCountryCode) || 
				"BR".equalsIgnoreCase(toCountryCode) || 
				"CA".equalsIgnoreCase(toCountryCode) || 
				"ES".equalsIgnoreCase(toCountryCode) ||
				"AT".equalsIgnoreCase(toCountryCode) ||
				"BE".equalsIgnoreCase(toCountryCode) ||
				"CY".equalsIgnoreCase(toCountryCode) ||
				"FI".equalsIgnoreCase(toCountryCode) ||
				"FR".equalsIgnoreCase(toCountryCode) ||
				"DE".equalsIgnoreCase(toCountryCode) ||
				"ES".equalsIgnoreCase(toCountryCode) ||
				"GR".equalsIgnoreCase(toCountryCode) ||
				"IE".equalsIgnoreCase(toCountryCode) ||
				"IT".equalsIgnoreCase(toCountryCode) ||
				"LU".equalsIgnoreCase(toCountryCode) ||
				"MT".equalsIgnoreCase(toCountryCode) ||
				"NL".equalsIgnoreCase(toCountryCode) ||
				"PT".equalsIgnoreCase(toCountryCode) ||
				"SI".equalsIgnoreCase(toCountryCode) 
			)  {
			CurrencyModel currencyModel = (CurrencyModel)cacheService.getFromCache(String.valueOf(amount)); 
			if(currencyModel != null)  {
				if(currencyModel.isAlreadyConverted(toCountryCode)) return currencyModel.getConversionFromCountry(toCountryCode);
				else  {
					String fromCurrencyCode = getCurrencyFromCountry(currencyModel.getOriginalCountryCode());
					String toCurrencyCode = getCurrencyFromCountry(toCountryCode);
					double newAmount = convert(amount, fromCurrencyCode, toCurrencyCode);
					currencyModel.addConversion(toCountryCode, newAmount);
					cacheService.removeFromCache(String.valueOf(amount));
					cacheService.addToCache(String.valueOf(amount), currencyModel);
					return newAmount;
				}
			}
			else  {
				currencyModel = new CurrencyModel();
				currencyModel.setOriginalCountryCode(fromCountryCode);
				String fromCurrencyCode = getCurrencyFromCountry(fromCountryCode);
				String toCurrencyCode = getCurrencyFromCountry(toCountryCode);
				double newAmount = convert(amount, fromCurrencyCode, toCurrencyCode);
				currencyModel.addConversion(toCountryCode, newAmount);
				cacheService.addToCache(String.valueOf(amount), currencyModel);
				return newAmount;
			}			
		}
		else return amount;
	}

	/**
	 * 
	 * @param amount
	 * @param fromCurrencyCode
	 * @param toCurrencyCode
	 * @return
	 */
	public double convertCurrencyFromCodeTo(final double amount, final String fromCurrencyCode, final String toCurrencyCode)  {
		if(fromCurrencyCode.equalsIgnoreCase(toCurrencyCode)) return amount;
		if(
				MXN_PESO.equalsIgnoreCase(toCurrencyCode) || 
				USD_DOLLAR.equalsIgnoreCase(toCurrencyCode) || 
				BRL_REAL.equalsIgnoreCase(toCurrencyCode) || 
				CAD_DOLLAR.equalsIgnoreCase(toCurrencyCode) 
			)  {
			CurrencyModel currencyModel = (CurrencyModel)cacheService.getFromCache(String.valueOf(amount)); 
			if(currencyModel != null)  {
				if(currencyModel.isAlreadyConverted(toCurrencyCode)) return currencyModel.getConversionFromCountry(toCurrencyCode);
				else  {
					double newAmount = convert(amount, fromCurrencyCode, toCurrencyCode);
					currencyModel.addConversion(toCurrencyCode, newAmount);
					cacheService.removeFromCache(String.valueOf(amount));
					cacheService.addToCache(String.valueOf(amount), currencyModel);
					return newAmount;
				}
			}
			else  {
				currencyModel = new CurrencyModel();
				currencyModel.setOriginalCountryCode(fromCurrencyCode);
				double newAmount = convert(amount, fromCurrencyCode, toCurrencyCode);
				currencyModel.addConversion(toCurrencyCode, newAmount);
				cacheService.addToCache(String.valueOf(amount), currencyModel);
				return newAmount;
			}			
		}
		else return amount;
	}
	
	
	/**
	 * 
	 * @param amount
	 * @param fromCurrency
	 * @param toCurrency
	 * @return
	 */
	private double convert(final double amount, final String fromCurrency, final String toCurrency)  {
		try {
			return converter.convert(amount, fromCurrency, toCurrency);
		} catch (IllegalArgumentException e) {
			log.error("illegal arguments when called", e);
		} catch (IOException e) {
			log.error("can't connect to the service, reason - "+e.getMessage(), e);
		} catch (ParseException e) {
			log.error("can't parse the XML, reason - "+e.getMessage(), e);
		}
		return amount;
	}
	
	/**
	 * 
	 * @param countryCode
	 * @return
	 */
	@SuppressWarnings("static-access")
	private String getCurrencyFromCountry(final String countryCode)  {
		final Session session = database.getSessionFactory().openSession();
		CurrenciesDAO currenciesDAO = database.getCurrenciesDAO();
		currenciesDAO.setSession(session);
		return currenciesDAO.getCurrencyCodeFromCountry(countryCode);
	}

}//END OF FILE