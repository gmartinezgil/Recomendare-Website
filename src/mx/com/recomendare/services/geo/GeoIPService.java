/**
 * 
 */
package mx.com.recomendare.services.geo;

import java.io.IOException;

import mx.com.recomendare.db.entities.City;
import mx.com.recomendare.db.entities.Country;
import mx.com.recomendare.db.entities.Language;
import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.services.cache.CacheService;
import mx.com.recomendare.services.db.CitiesDAO;
import mx.com.recomendare.services.db.CountriesDAO;
import mx.com.recomendare.services.db.CurrenciesDAO;
import mx.com.recomendare.services.db.DatabaseService;
import mx.com.recomendare.services.db.LanguagesDAO;
import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.commons.models.LocationModelImpl;
import net.sf.ehcache.CacheException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.timeZone;

/**
 * @author jerry
 *
 */
public final class GeoIPService extends AbstractService {	
	//the reference to the lookup service...
	private LookupService lookupService;
	//the database to correlate the language...
	private DatabaseService db;
	//the cache...
	private CacheService cacheService;
	//the path to the GeoLiteCity.dat...
	private String filePath;

	//the log...
	private static final Log log = LogFactory.getLog(GeoIPService.class);

	/**
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public GeoIPService(final String filePath, final DatabaseService databaseService) {
		this.filePath = filePath;
		log.info(filePath);
		db = databaseService;
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
			try {
				lookupService = new LookupService(filePath, LookupService.GEOIP_STANDARD);
				cacheService = new CacheService("geoipservice");
				started = true;
				log.info(getClass().getName()+"...started!");
			} catch (IOException e) {
				log.error("Can't find path -  "+filePath+", reason - "+e.getMessage(), e);
			}
			catch (CacheException e) {
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
			lookupService.close();
			lookupService = null;
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}

	/**
	 * 
	 * @param ipAddress
	 * @return
	 */
	//TODO:it could become from the database imported records instead of the API...and it's file...because of clustering issues that could arise... 
	@SuppressWarnings("static-access")
	public LocationModelImpl getLocation(final String ipAddress)  {
		LocationModelImpl locationModel = (LocationModelImpl)cacheService.getFromCache(ipAddress);
		if(locationModel != null)  {
			return locationModel;
		}
		else {
			final Session session = db.getSessionFactory().openSession();
			Location foundedLocation = lookupService.getLocation(ipAddress);
			if(foundedLocation != null)  {
				log.info("founded location - (country='"+foundedLocation.countryName+"', countryCode ='"+foundedLocation.countryCode+"', city='"+foundedLocation.city+"', latitude='"+foundedLocation.latitude+"', longitude='"+foundedLocation.longitude+"')");
				locationModel = new LocationModelImpl();
				CountriesDAO countriesDAO = db.getCountriesDAO();
				countriesDAO.setSession(session);
				Country foundedCountry = countriesDAO.getCountryByCode(foundedLocation.countryCode);
				if(foundedCountry != null && foundedCountry.getIsSupported())  {
					locationModel.setCountryId(foundedCountry.getId());
					locationModel.setCountryCode(foundedCountry.getCode());
					locationModel.setCountryName(countriesDAO.getCountryName(foundedCountry.getId(), foundedCountry.getLanguage().getCode()));
					CitiesDAO citiesDAO = db.getCitiesDAO();
					citiesDAO.setSession(session);
					City supportedCity = citiesDAO.getCityByServiceName(foundedLocation.city);
					locationModel.setCityId(supportedCity.getId());
					locationModel.setCityName(citiesDAO.getCityName(supportedCity.getId(), foundedCountry.getLanguage().getCode()));
					//locationModel.setState(regionName.regionNameByCode(foundedLocation.countryCode, foundedLocation.region));
					LanguagesDAO languagesDAO = db.getLanguagesDAO();
					languagesDAO.setSession(session);
					Language language = languagesDAO.getLanguageByCountryCode(foundedCountry.getCode());
					locationModel.setLanguageId(language.getId());
					locationModel.setLanguageCode(language.getCode());
					locationModel.setLanguageName(language.getName());
					//clients ISP provider position...
					locationModel.setLatitude(foundedLocation.latitude);
					locationModel.setLongitude(foundedLocation.longitude);
					//time zone...
					locationModel.setTimeZone(timeZone.timeZoneByCountryAndRegion(foundedLocation.countryCode, foundedLocation.region));
					//currency...
					CurrenciesDAO currenciesDAO = new CurrenciesDAO();
					currenciesDAO.setSession(session);
					locationModel.setCurrencyCode(currenciesDAO.getCurrencyCodeFromCountry(foundedCountry.getCode()));
					//and the cache...
					cacheService.addToCache(ipAddress, locationModel);
					log.info("locationmodel - "+locationModel);
					return locationModel;
				}
				else  {
					locationModel = getLocationWithSupportOfLanguage(session, foundedLocation);
					cacheService.addToCache(ipAddress, locationModel);
					log.info("locationmodel - "+locationModel);
					return locationModel;
				}
			}
			else  {
				locationModel = getLocationWithDefaults(session);
				cacheService.addToCache(ipAddress, locationModel);
				log.info("locationmodel - "+locationModel);
				return locationModel;
			}
		}
	}

	/**
	 * 
	 * @param session
	 * @param foundedLocation
	 * @return
	 */
	private LocationModelImpl getLocationWithSupportOfLanguage(final Session session, final Location foundedLocation)  {
		LocationModelImpl locationModel = new LocationModelImpl();
		locationModel.setCountryCode(foundedLocation.countryCode);
		locationModel.setCountryName(foundedLocation.countryName);
		locationModel.setCityName(foundedLocation.city);
		LanguagesDAO languagesDAO = db.getLanguagesDAO();
		languagesDAO.setSession(session);
		Language defaultLanguage = languagesDAO.getLanguageByCountryCode(Constants.DEFAULT_INTERNATIONAL_COUNTRY_CODE);
		locationModel.setLanguageId(defaultLanguage.getId());
		locationModel.setLanguageCode(defaultLanguage.getCode());
		locationModel.setLanguageName(defaultLanguage.getName());
		locationModel.setLatitude(foundedLocation.latitude);
		locationModel.setLongitude(foundedLocation.longitude);
		return locationModel;
	}
	
	/**
	 * 
	 * @param location
	 * @return
	 */
	private LocationModelImpl getLocationWithDefaults(final Session session)  {
		LocationModelImpl locationModel = new LocationModelImpl(); 
		CountriesDAO countriesDAO = db.getCountriesDAO();
		countriesDAO.setSession(session);
		Country defaultCountry = countriesDAO.getCountryByCode(Constants.DEFAULT_COUNTRY_CODE);
		locationModel.setCountryId(defaultCountry.getId());
		locationModel.setCountryCode(defaultCountry.getCode());
		locationModel.setCountryName(countriesDAO.getCountryName(defaultCountry.getId(), defaultCountry.getLanguage().getCode()));
		CitiesDAO citiesDAO = db.getCitiesDAO();
		citiesDAO.setSession(session);
		City defaultCity = citiesDAO.getCityByServiceName(Constants.DEFAULT_CITY_NAME);
		locationModel.setCityId(defaultCity.getId());
		locationModel.setCityName(citiesDAO.getCityName(defaultCity.getId(), defaultCountry.getLanguage().getCode()));
		//locationModel.setState(regionName.regionNameByCode(foundedLocation.countryCode, foundedLocation.region));
		LanguagesDAO languagesDAO = db.getLanguagesDAO();
		languagesDAO.setSession(session);
		Language defaultLanguage = languagesDAO.getLanguageByCountryCode(Constants.DEFAULT_INTERNATIONAL_COUNTRY_CODE);
		locationModel.setLanguageId(defaultLanguage.getId());
		locationModel.setLanguageCode(defaultLanguage.getCode());
		locationModel.setLanguageName(defaultLanguage.getName());
		//city location of user...
		locationModel.setLatitude(defaultCity.getLocation().getLatitude().floatValue());
		locationModel.setLongitude(defaultCity.getLocation().getLongitude().floatValue());
		//currency...
		CurrenciesDAO currenciesDAO = new CurrenciesDAO();
		currenciesDAO.setSession(session);
		locationModel.setCurrencyCode(currenciesDAO.getCurrencyCodeFromCountry(defaultCountry.getCode()));
		return locationModel;
	}

}//END OF FILE