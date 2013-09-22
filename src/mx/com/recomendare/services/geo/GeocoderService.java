/**
 * 
 */
package mx.com.recomendare.services.geo;

import geo.google.GeoAddressStandardizer;
import geo.google.GeoException;
import geo.google.datamodel.GeoAddress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.services.http.HttpService;
import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.commons.models.LocationModelImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.contrib.gmap.api.GLatLng;
import wicket.contrib.gmap.util.Geocoder;

/**
 * @author jerry
 *
 */
public final class GeocoderService extends AbstractService {
	//the geocoder...
	private Geocoder geocoder;
	//the http connection...
	private HttpService httpService;
	
	//the log...
	private static final Log log = LogFactory.getLog(GeocoderService.class);
	
	/**
	 * 
	 */
	public GeocoderService(final HttpService httpService) {
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
			geocoder = new Geocoder(Constants.GMAP_LOCALHOST_8080_KEY);
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
			geocoder = null;
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}
	
	/**
	 * 
	 * @param streetName
	 * @param cityName
	 * @param countryName
	 * @return
	 */
	public LocationModel getLocationFor(final String streetName, final String cityName, final String countryName)  {
		GLatLng placemark = null;
		try {
			String content = httpService.getContentAsStringFrom(geocoder.encode(streetName + ", " + cityName + "," + countryName));
			placemark = geocoder.decode(content);
		} catch (IOException e) {
			log.error("Can't connect to the service, reason - "+e.getMessage(), e);
		}
        if (placemark != null) {
            log.info("" + placemark.getLat());
            log.info("" + placemark.getLng());
            LocationModelImpl location = new LocationModelImpl();
            location.setLatitude(new Float(placemark.getLat()));
            location.setLongitude(new Float(placemark.getLng()));
            return location;
        }
        return null;
	}

	/**
	 * 
	 * @param streetNumber
	 * @param streetName
	 * @param cityName
	 * @param countryName
	 * @return
	 */
	public List<LocationModel> getMultipleLocationsFor(final String streetNumber, final String streetName, final String cityName, final String countryName)  {
		GeoAddressStandardizer geoAddressStandardizer = new GeoAddressStandardizer(Constants.GMAP_LOCALHOST_8080_KEY);
		List<GeoAddress> address = null;
		try {
			address = geoAddressStandardizer.standardizeToGeoAddresses(((streetNumber != null && streetNumber.trim().length() > 0)?streetNumber+" ":"") + streetName + ", " + cityName + "," + countryName);
		} 
		catch (GeoException e) {
			log.error("Can't parse the following address - '"+streetName +" "+ streetNumber + ", " + cityName + "," + countryName+"', reason("+e.getStatus()+") - "+e.getMessage(), e);
		}
		if(address != null && address.size() > 0)  {
			List<LocationModel> locations = new ArrayList<LocationModel>(address.size());
			Iterator<GeoAddress> iterator = address.iterator();
			int i = 0;
			while (iterator.hasNext()) {
				GeoAddress geoAddress = (GeoAddress) iterator.next();
				log.info(geoAddress);
				final String geoAddressSummary = geoAddress.getAddressLine();
				final StringTokenizer tokenizer = new StringTokenizer(geoAddressSummary, ",");
				if(tokenizer.countTokens() > 0 && tokenizer.countTokens() > 3/*6*/)  {//TODO: this is not good to have this value, it should be checked in another way...
					LocationModelImpl location = new LocationModelImpl();
					location.setId(i);
					location.setLatitude((float) geoAddress.getCoordinate().getLatitude());
					location.setLongitude((float) geoAddress.getCoordinate().getLongitude());
					location.setSummary(geoAddressSummary);
					if(streetNumber != null && streetNumber.length() > 0)  {
						String streetNameWithNumber = tokenizer.nextToken();
						location.setStreetName(streetNameWithNumber.substring(0, streetNameWithNumber.length() - streetNumber.length()).trim());
						location.setStreetNumber(streetNumber);
						location.setZoneName(tokenizer.nextToken());
						location.setCityName(cityName);tokenizer.nextToken();
						location.setCountryName(countryName);tokenizer.nextToken();
						//location.setPostalCodeNumber(tokenizer.nextToken());
					}
					else  {
						location.setStreetName(tokenizer.nextToken());
						location.setCityName(cityName);tokenizer.nextToken();
						location.setCountryName(countryName);tokenizer.nextToken();
					}
					locations.add(location);
					i++;
				}
			}
			return locations;
		}
		return null;
	}
	
}//END OF FILE