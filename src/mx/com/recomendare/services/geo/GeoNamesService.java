/**
 * 
 */
package mx.com.recomendare.services.geo;

import java.io.IOException;
import java.util.List;

import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.services.http.HttpService;
import mx.com.recomendare.web.commons.models.LocationModelImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * @author jerry
 *
 */
public final class GeoNamesService extends AbstractService {
	//the log...
	private static final Log log = LogFactory.getLog(GeoNamesService.class);
	//the URL...where we get the map image...
	private static final String URL = "http://ws.geonames.org/";
	
	//the http service...
	private HttpService httpService;
	//the digester service...
	private GeoNamesDigester digester;
	
	public GeoNamesService(final HttpService httpService) {
		this.httpService = httpService;
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
			digester = new GeoNamesDigester();
			started = true;
			log.info(getClass().getName()+"...started!");
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
			digester = null;
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param languageCode
	 * @return
	 */
	public LocationModelImpl getCountryInformationFromLocation(final float latitude, final float longitude, final String languageCode)  {
		final String urlCountrySubdivision = URL +"countrySubdivision?lat="+latitude+"&lng="+longitude+"&lang="+languageCode+"&type=xml";
		String responseBody = httpService.getContentAsStringFrom(urlCountrySubdivision);
		log.info(responseBody);
		try  {
			LocationModelImpl location = digester.digestGeoCountrySubdivision(responseBody);
			location.setLatitude(latitude);
			location.setLongitude(longitude);
			return location;
		} catch (IOException e) {
			log.error("Error getting - "+urlCountrySubdivision+", reason - "+e.getMessage(), e);
		} catch (SAXException e) {
			log.error("Couldn't parse XML - "+urlCountrySubdivision+", reason - "+e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public List<PlaceModel> getNearestPlacesFrom(final float latitude, final float longitude)  {
		final String urlFindNearBy = URL+"findNearby?lat="+latitude+"&lng="+longitude;
		String responseBody = httpService.getContentAsStringFrom(urlFindNearBy);
		log.info(responseBody);
		try  {
				return digester.digestGeoNamePlace(responseBody);
		} catch (IOException e) {
			log.error("Couldn't get io - "+urlFindNearBy+", reason - "+e.getMessage(), e);
		} catch (SAXException e) {
			log.error("Couldn't parse XML - "+urlFindNearBy+", reason - "+e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public List<PlaceModel> getWikipediaEntriesFrom(final float latitude, final float longitude)  {
		final String urlFindNearBy = URL+"findNearbyWikipedia?lat="+latitude+"&lng="+longitude+"&lang=es";
		String responseBody = httpService.getContentAsStringFrom(urlFindNearBy);
		log.info(responseBody);
		try  {
				return digester.digestWikipediaEntryPlace(responseBody);
		} catch (IOException e) {
			log.error("Couldn't get io - "+urlFindNearBy+", reason - "+e.getMessage(), e);
		} catch (SAXException e) {
			log.error("Couldn't parse XML - "+urlFindNearBy+", reason - "+e.getMessage(), e);
		}
		return null;
	}

}//END OF FILE