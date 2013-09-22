/**
 * 
 */
package mx.com.recomendare.services.flickr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import mx.com.recomendare.services.AbstractService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.photos.GeoData;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;
import com.aetrion.flickr.places.Place;
import com.aetrion.flickr.places.PlacesList;

/**
 * @author jerry
 *
 */
public final class FlickrService extends AbstractService {
	//api key...
	private static final String API_KEY = "FLICKR_API_KEY";
	//secret...
	private static final String SECRET = "FLICKR_API_SECRET";
	//flickr service...
	private Flickr flickr;
	
	
	//the log...
	private static final Log log = LogFactory.getLog(FlickrService.class);
	
	/**
	 * 
	 */
	public FlickrService() {
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
				flickr = new Flickr(API_KEY, SECRET, new REST());
				started = true;
				log.info(getClass().getName()+"...started!");
			} catch (ParserConfigurationException e) {
				log.error("Flickr service error, reason - "+e.getMessage(), e);
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
			flickr = null;
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PhotoModel> getPhotosFromLocation(final float latitude, final float longitude, final int numberOfPhotos)  {
		try {
			PlacesList places = flickr.getPlacesInterface().findByLatLon(latitude, longitude, Flickr.ACCURACY_STREET);
			Iterator<Place> iterator = places.iterator();
			while (iterator.hasNext()) {
				Place place = iterator.next();
				//Location location = flickr.getPlacesInterface().resolvePlaceId(place.getPlaceId());
				return getPhotosFromPlace(place.getPlaceId(), place.getWoeId(), numberOfPhotos);
			}
		} catch (FlickrException e) {
			log.error("Error accesing FLICKR service, reason - "+e.getMessage(), e);
		} catch (IOException e) {
			log.error("Can't connect to FLICKR service, reason - "+e.getMessage(), e);
		} catch (SAXException e) {
			log.error("Can't parse XML response, reason - "+e.getMessage(), e);
		}
		return null;
	}
	
	//once get the place...get each photo...
	@SuppressWarnings("unchecked")
	private List<PhotoModel> getPhotosFromPlace(final String placeId, final String woeId, final int numberOfPhotos)  {
		List<PhotoModel> photos = null;
		SearchParameters searchParameters = new SearchParameters();
		searchParameters.setPlaceId(placeId);
		searchParameters.setWoeId(woeId);
		searchParameters.setExtrasGeo(true);
		searchParameters.setExtrasDateUpload(true);
		PhotosInterface photosInterface = flickr.getPhotosInterface();
		int page = 1;
		try {
			PhotoList result = photosInterface.search(searchParameters, numberOfPhotos, page);
			log.info("results.size() - "+result.size());
			if(result.size() > 0)  {
				photos = new ArrayList<PhotoModel>();
				Iterator<Photo> iterator = result.iterator();
				while (iterator.hasNext()) {
					Photo photo = iterator.next();
					GeoData geoData = photo.getGeoData();
					PhotoModel model = new PhotoModel();
					model.setTitle(photo.getTitle());
					model.setThumbnailUrl(photo.getThumbnailUrl());
					model.setMediumUrl(photo.getMediumUrl());
					model.setLargeUrl(photo.getLargeUrl());
					model.setLatitude(geoData.getLatitude());
					model.setLongitude(geoData.getLongitude());
					model.setDatePosted(photo.getDatePosted());
					log.info(model);
					photos.add(model);
				}
				return photos;
			}
		} catch (FlickrException e) {
			log.error("Error accesing FLICKR service, reason - "+e.getMessage(), e);
		} catch (IOException e) {
			log.error("Can't connect to FLICKR service, reason - "+e.getMessage(), e);
		} catch (SAXException e) {
			log.error("Can't parse XML response, reason - "+e.getMessage(), e);
		}
		return null;
	}
	
	/*
	//only needed for the api...only one time ...
	public static void main(String[] args)  throws IOException, SAXException, FlickrException, ParserConfigurationException {
		flickr = new Flickr(API_KEY, SECRET, new REST());
		
		String frob = flickr.getAuthInterface().getFrob();

		URL authUrl = flickr.getAuthInterface().buildAuthenticationUrl(Permission.READ, frob);
		System.out.println("Please visit: " + authUrl.toExternalForm() + " then, hit enter.");

		System.in.read();

		Auth token = flickr.getAuthInterface().getToken(frob);
		RequestContext.getRequestContext().setAuth(token);
		System.out.println(token);
		System.out.println("Thanks.  You probably will not have to do this every time.  Now starting backup.");
	}
	*/
	
}//END OF FILE