/**
 * 
 */
package mx.com.recomendare.services.weather;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.services.cache.CacheService;
import mx.com.recomendare.util.Constants;
import mx.com.recomendare.util.Util;
import net.sf.ehcache.CacheException;
import net.wxbug.api.ApiLocationData;
import net.wxbug.api.LiveWeatherData;
import net.wxbug.api.UnitType;
import net.wxbug.api.WeatherBugWebServicesSoapProxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jerry
 *
 */
public final class WeatherService extends AbstractService {
	//required to use the service...
	private static final String ACTIVATION_CODE = "ACTIVATION_CODE";
	//the images...
	private static final String URL_IMAGES_ADDRESS = "http://deskwx.weatherbug.com/images/Forecast/icons/";
	
	//the call to the web service...
	private WeatherBugWebServicesSoapProxy proxy;
	//we use a cache to the code cities used when asking for them...
	private Map<String,String> citiesCode;
	
	//the cache...
	private CacheService cacheService;
	
	//the log...
    private static final Log log = LogFactory.getLog(WeatherService.class);
    
	/**
	 * 
	 */
	public WeatherService() {
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
			citiesCode = new HashMap<String,String>();
			proxy = new WeatherBugWebServicesSoapProxy();
			try {
				cacheService = new CacheService("weather");
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
			proxy = null;
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}
	
	/**
	 * 
	 * @param cityName
	 * @param countryName
	 * @return
	 * @throws RemoteException 
	 */
	public WeatherModel getWeatherConditionsByCityCountry(final String cityName, final String countryName) throws RemoteException  {
		if(cityName != null && cityName.trim().length() > 0)  {
			final String cityCode = getWeatherCityCountryCode(cityName, countryName);
			if(cityCode != null)  {
				WeatherModel weatherModel = null;
				try {
					weatherModel = (WeatherModel)cacheService.getFromCache(cityCode);
					if(weatherModel != null)  {
						if(!Util.hasPassedTime(weatherModel.getTimeUpdated(), Constants.ONE_HOUR_OF_TIME))  {
							return weatherModel;
						}
						else  {
							cacheService.removeFromCache(cityCode);
							weatherModel = getWeather(cityCode);
							cacheService.addToCache(cityCode, weatherModel);
							return weatherModel;
						}
					}
					else if(weatherModel == null) {
						weatherModel = getWeather(cityCode);
						cacheService.addToCache(cityCode, weatherModel);
						return weatherModel;
					}
				} catch (IllegalStateException e) {
					log.error("Can't access the cache, reason - "+e.getMessage(), e);
				} catch (CacheException e) {
					log.error("Can't retrieve from the cache the following city - "+cityName, e);
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param cityName
	 * @param countryName
	 * @return
	 * @throws RemoteException 
	 */
	public WeatherModel getWeatherConditionsByCountry(final String countryName) throws RemoteException  {
		if(countryName != null && countryName.trim().length() > 0)  {
			final String countryCode = getWeatherCountryCode(countryName);
			if(countryCode != null)  {
				WeatherModel weatherModel = null;
				try {
					weatherModel = (WeatherModel)cacheService.getFromCache(countryCode);
					if(weatherModel != null)  {
						if(!Util.hasPassedTime(weatherModel.getTimeUpdated(), Constants.ONE_HOUR_OF_TIME))  {
							return weatherModel;
						}
						else  {
							cacheService.removeFromCache(countryCode);
							weatherModel = getWeather(countryCode);
							cacheService.addToCache(countryCode, weatherModel);
							return weatherModel;
						}
					}
					else if(weatherModel == null) {
						weatherModel = getWeather(countryCode);
						cacheService.addToCache(countryCode, weatherModel);
						return weatherModel;
					}
				} catch (IllegalStateException e) {
					log.error("Can't access the cache, reason - "+e.getMessage(), e);
				} catch (CacheException e) {
					log.error("Can't retrieve from the cache the following country - "+countryName, e);
				}
			}
		}
		return null;
	}

	//utility...
	private String getWeatherCityCountryCode(final String cityName, final String countryName) throws RemoteException {
		log.info(cityName+", "+countryName);
		if(citiesCode.get(cityName) == null) {
			Object[] locations = proxy.getLocationList(cityName + ", " +countryName, ACTIVATION_CODE);
			if(locations != null && locations.length > 0)  {
				for(int i=0; i<locations.length; i++)  {
					ApiLocationData location = (ApiLocationData)locations[i];
					log.info("weather location - city - "+location.getCity()+", cityCode - "+location.getCityCode()+", country - "+location.getCountry()+", state - "+location.getState());
				}
				String cityCode = ((ApiLocationData)locations[0]).getCityCode();
				citiesCode.put(cityName, cityCode);
				return cityCode;
			}
			return null;
		}
		return (String)citiesCode.get(cityName);
	}
	
	//utility...
	private String getWeatherCountryCode(final String countryName) throws RemoteException {
		log.info(countryName);
		if(citiesCode.get(countryName) == null) {
			Object[] locations = proxy.getLocationList(countryName, ACTIVATION_CODE);
			if(locations != null && locations.length > 0)  {
				for(int i=0; i<locations.length; i++)  {
					ApiLocationData location = (ApiLocationData)locations[i];
					log.info("weather location - city - "+location.getCity()+", cityCode - "+location.getCityCode()+", country - "+location.getCountry()+", state - "+location.getState());
				}
				String cityCode = ((ApiLocationData)locations[0]).getCityCode();
				citiesCode.put(countryName, cityCode);
				return cityCode;
			}
			return null;
		}
		return (String)citiesCode.get(countryName);
	}
	
	//utility...
	private WeatherModel getWeather(final String cityCode) throws RemoteException  {
		WeatherModel weatherModel = null;
		final LiveWeatherData weather = proxy.getLiveWeatherByCityCode(cityCode, UnitType.Metric, ACTIVATION_CODE);
		if(weather != null)  {
			weatherModel= new WeatherModelImpl();
			weatherModel.setCityName(weather.getCity());
			weatherModel.setCountryName(weather.getCountry());
			weatherModel.setLatitude(weather.getLatitude());
			weatherModel.setLongitude(weather.getLongitude());
			weatherModel.setTemperature(weather.getTemperature());
			weatherModel.setTemperatureHigh(weather.getTemperatureHigh());
			weatherModel.setTemperatureLow(weather.getTemperatureLow());
			weatherModel.setURLForPicture(URL_IMAGES_ADDRESS + weather.getCurrIcon());
			weatherModel.setTimeUpdated(new Date());
		}
		return weatherModel; 
	}

}//END OF FILE