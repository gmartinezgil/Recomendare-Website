/**
 * 
 */
package mx.com.recomendare.services.kayak;

import java.util.List;

import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.services.feed.FeedParserService;
import mx.com.recomendare.services.iata.IATAService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.syndication.feed.synd.SyndEntry;

/**
 * @author jerry
 *
 */
public final class KayakService extends AbstractService {
	//the log...
	private static final Log log = LogFactory.getLog(KayakService.class);

	//the feed parser service...
	private FeedParserService feedParserService;
	//the IATA service...
	private IATAService iataService;
	
	//where we take our fares...
	private static final String BASE_URL = "http://www.kayak.com/h/rss/"; 
	/**
	 * 
	 */
	public KayakService(final FeedParserService feedParserService, final IATAService iataService) {
		this.feedParserService = feedParserService;
		this.iataService = iataService;
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
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}
	
	/**
	 * 
	 * @param originCityName
	 * @param destinationCityName
	 * @param currencyCode
	 * @return
	 */
	public List<SyndEntry> getAirFaresForDestination(final String originCityName, final String destinationCityName, final String currencyCode)  {
		return feedParserService.parseFeed(
											BASE_URL+
											"fare?code="+
											iataService.getCodeFromCityName(originCityName)+
											"&dest="+
											iataService.getCodeFromCityName(destinationCityName)+
											"&mc="+
											currencyCode
											);
	}
	
	public List<SyndEntry> getHotelFaresFromDestination(final String destinationCountryCode, final String destinationCityName, final String currencyCode)  {
		return feedParserService.parseFeed(
											BASE_URL+
											"hotelrss/"+
											destinationCountryCode+
											"/"+
											destinationCityName+
											"?mc="+
											currencyCode
											);
	}
	
}//END OF FILE