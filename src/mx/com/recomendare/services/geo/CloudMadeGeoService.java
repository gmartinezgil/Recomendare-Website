/**
 * 
 */
package mx.com.recomendare.services.geo;

import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.web.commons.models.LocationModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cloudmade.api.CMClient;
import com.cloudmade.api.CMClient.MeasureUnit;
import com.cloudmade.api.CMClient.RouteType;
import com.cloudmade.api.geocoding.GeoResult;
import com.cloudmade.api.geocoding.GeoResults;
import com.cloudmade.api.geometry.Point;
import com.cloudmade.api.routing.Route;
import com.cloudmade.api.routing.RouteNotFoundException;

/**
 * @author jerry
 *
 */
public final class CloudMadeGeoService extends AbstractService {
	private static final String CLOUD_MATE_KEY = "CLOUD_MATE_API_KEY";
	//the client...
	private CMClient client;

	//the log...
	private static final Log log = LogFactory.getLog(CloudMadeGeoService.class);
	
	/**
	 * 
	 */
	public CloudMadeGeoService() {
		doStart();
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.Service#doStart()
	 */
	public void doStart() {
		log.info("Starting - "+getClass().getName()+"...");
		if(!started)  {
			client =  new CMClient(CLOUD_MATE_KEY);
			started = true;
			log.info(getClass().getName()+"...started!");
		}
		else  {
			log.error(getClass().getName()+"...already started!");
		}
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.Service#doStop()
	 */
	public void doStop() {
		if(started)  {
			log.info("Stoping - "+getClass().getName()+"...");
			client = null;
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}
	
	/**
	 * 
	 * @param startLatitude
	 * @param startLongitude
	 * @param endLatitude
	 * @param endLongitude
	 * @param languageCode
	 * @param transportType
	 * @param measureType
	 * @return
	 */
	public Route getRouteFromStartEndPoints(final float startLatitude, final float startLongitude, final float endLatitude, final float endLongitude, final String languageCode, final short transportType, final short measureType)  {
		try {
			return client.route(
			        new Point(startLatitude, startLongitude),
			        new Point(endLatitude, endLongitude),
			        RouteType.CAR,
			        null,
			        null,
			        languageCode,
			        MeasureUnit.KM);
		} catch (RouteNotFoundException e) {
			log.error("Route not found between these points, reason - "+e.getMessage(), e);
		}
		return null;
	}
	
	public List<LocationModel> getMultipleLocationsFor(final String streetNumber, final String streetName, final String cityName, final String countryName)  {
		GeoResults results = client.find(
														", " + //point of interest
														((streetNumber != null && streetNumber.trim().length() > 0)?streetNumber:"") + ", " + //street number	
														streetName + ", " + //street name
														cityName + ", " + //city
														", " + //county
														countryName, //country
														10, //number of results
														0, //skip after first result
														null, //bbox
														true, //search results inside bbox
														false, //return geometry
														true
														);
		GeoResult[] allResults = results.results;
		if(allResults != null && allResults.length > 0) {
			for (int i = 0; i < allResults.length; i++) {
				GeoResult geoResult = allResults[i];
				log.info(geoResult.location.city);
				log.info(geoResult.location.country);
				log.info(geoResult.location.county);
				log.info(geoResult.location.postcode);
				log.info(geoResult.location.road);
				log.info(geoResult.centroid);
				log.info("-----------------------------------");
			}
		}
        /*
        try {
            result = client.findClosest("pub", new Point(51.66117, 13.37654));

            System.out.println(result.properties);
            System.out.println(result.centroid);
        } catch (ObjectNotFoundException e) {
        }
        */
		return null;
	}
	
	public static void main(String[] args) {
		CloudMadeGeoService service = new CloudMadeGeoService();
		Route route = service.getRouteFromStartEndPoints(0.00000f, 0.00000f, 0.00000f, 0.00000f, "es", (short)0, (short)0);
		for (final Iterator<Point> iterator = route.geometry.points.iterator(); iterator.hasNext();) {
			Point point = (Point)iterator.next();
			System.out.println(point.lat + " - " + point.lon);
		}
	}

}//END OF FILE