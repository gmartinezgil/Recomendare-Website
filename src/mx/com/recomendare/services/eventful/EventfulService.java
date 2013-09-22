/**
 * 
 */
package mx.com.recomendare.services.eventful;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.services.AbstractService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.evdb.javaapi.APIConfiguration;
import com.evdb.javaapi.EVDBAPIException;
import com.evdb.javaapi.EVDBRuntimeException;
import com.evdb.javaapi.data.Event;
import com.evdb.javaapi.data.Link;
import com.evdb.javaapi.data.SearchResult;
import com.evdb.javaapi.data.request.EventSearchRequest;
import com.evdb.javaapi.operations.EventOperations;

/**
 * @author jerry
 *
 */
public final class EventfulService extends AbstractService {
	private static final String API_KEY = "EVENTFUL_API_KEY";
	
	//the log...
	private static final Log log = LogFactory.getLog(EventfulService.class);
	
	//the event operations..
	private EventOperations eventOperations;
	/**
	 * 
	 */
	public EventfulService() {
		doStart();
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.Service#doStart()
	 */
	public void doStart() {
		log.info("Starting - "+getClass().getName()+"...");
		if(!started)  {
			APIConfiguration.setApiKey(API_KEY);
			//APIConfiguration.setBaseURL("http://api.evdb.com/rest/");
			APIConfiguration.setEvdbUser("LOGIN");
			APIConfiguration.setEvdbPassword("PASSWORD");
			eventOperations = new EventOperations();
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
			eventOperations = null;
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}

	/**
	 * 
	 * @param cityName
	 * @param countryName
	 * @param fromDate
	 * @param toDate
	 * @param categoryKeyword
	 * @return
	 */
	public List<EventfulModel> getEventsFromPlace(final String cityName, final String countryName, final Date fromDate, final Date toDate, final String categoryKeyword)  {
		log.info(cityName+", "+countryName);
		EventSearchRequest eventSearchRequest = new EventSearchRequest();
		eventSearchRequest.setLocation(cityName+", "+countryName);
		if(categoryKeyword != null) eventSearchRequest.setKeywords(categoryKeyword);
		if(fromDate != null && toDate != null)  eventSearchRequest.setDateRange(getRangeFromDates(fromDate, toDate));
		SearchResult searchResult = null;
		try {
			searchResult = eventOperations.search(eventSearchRequest);
			List<Event> events = searchResult.getEvents();
			if(events != null)  {
				List<EventfulModel> fullEvents = new ArrayList<EventfulModel>(events.size());
				Iterator<Event> iterator = events.iterator();
				while (iterator.hasNext()) {
					Event event = iterator.next();
					fullEvents.add(getEventfulModelFromEvent(eventOperations.get(event.getSeid())));
				}
				return fullEvents;
			}
		} catch (EVDBRuntimeException e) {
			log.error("Eventful runtime error, reason - "+e.getMessage(), e);
		} catch (EVDBAPIException e) {
			log.error("Eventful api error, reason - "+e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param fromDate
	 * @param toDate
	 * @param categoryKeyword
	 * @return
	 */
	public List<EventfulModel> getEventsFromLocation(final double latitude, final double longitude, final Date fromDate, final Date toDate, final String categoryKeyword)  {
		log.info(latitude+", "+longitude);
		EventSearchRequest eventSearchRequest = new EventSearchRequest();
		eventSearchRequest.setLocation(latitude+", "+longitude);
		if(categoryKeyword != null) eventSearchRequest.setKeywords(categoryKeyword);
		if(fromDate != null && toDate != null)  eventSearchRequest.setDateRange(getRangeFromDates(fromDate, toDate));
		SearchResult searchResult = null;
		try {
			searchResult = eventOperations.search(eventSearchRequest);
			List<Event> events = searchResult.getEvents();
			if(events != null)  {
				List<EventfulModel> fullEvents = new ArrayList<EventfulModel>(events.size());
				Iterator<Event> iterator = events.iterator();
				while (iterator.hasNext()) {
					Event event = iterator.next();
					fullEvents.add(getEventfulModelFromEvent(eventOperations.get(event.getSeid())));
				}
				return fullEvents;
			}
		} catch (EVDBRuntimeException e) {
			log.error("Eventful runtime error, reason - "+e.getMessage(), e);
		} catch (EVDBAPIException e) {
			log.error("Eventful api error, reason - "+e.getMessage(), e);
		}
		return null;
	}
	
	//to get the format of the dates needed by the api...
	private String getRangeFromDates(final Date from, final Date to)  {
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
		final String calculatedRange = simpleDateFormat.format(from) + "-" +simpleDateFormat.format(to);
		log.info(calculatedRange);
		return calculatedRange;
	}
	
	//gets from the event the final event model..
	private EventfulModel getEventfulModelFromEvent(final Event event)  {
		EventfulModel model = new EventfulModel();
		model.setTitle(event.getTitle());
		model.setDescription(event.getDescription());
		model.setPlace(event.getVenueName());
		model.setPrice(event.getPrice());
		model.setDirection(event.getVenueAddress());
		model.setCityName(event.getVenueCity());
		model.setCountryName(event.getVenueCountry());
		model.setLatitude(event.getVenueLatitude());
		model.setLongitude(event.getVenueLongitude());
		model.setOnDate(event.getStartTime());
		if(event.getLinks() != null && event.getLinks().size() > 0) {
			model.setURL(((Link)event.getLinks().get(0)).getUrl());
		}
		if(event.getImages() != null && event.getImages().size() > 0)  {
			model.setImageURL(event.getImages().get(0).getUrl());
		}
		log.info(model);
		return model;
	}

}//END OF FILE