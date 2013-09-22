/**
 * 
 */
package mx.com.recomendare.services;

import mx.com.recomendare.services.amazon.AmazonAffiliateService;
import mx.com.recomendare.services.currency.CurrencyConverterService;
import mx.com.recomendare.services.db.DatabaseService;
import mx.com.recomendare.services.eventful.EventfulService;
import mx.com.recomendare.services.feed.FeedParserService;
import mx.com.recomendare.services.flickr.FlickrService;
import mx.com.recomendare.services.geo.CloudMadeGeoService;
import mx.com.recomendare.services.geo.GeoIPService;
import mx.com.recomendare.services.geo.GeoNamesService;
import mx.com.recomendare.services.geo.GeocoderService;
import mx.com.recomendare.services.http.HttpService;
import mx.com.recomendare.services.iata.IATAService;
import mx.com.recomendare.services.kayak.KayakService;
import mx.com.recomendare.services.mail.MailService;
import mx.com.recomendare.services.opencellid.OpenCellIdService;
import mx.com.recomendare.services.recommender.RecommenderService;
import mx.com.recomendare.services.security.EncryptService;
import mx.com.recomendare.services.statistics.StatisticsService;
import mx.com.recomendare.services.translation.TranslationService;
import mx.com.recomendare.services.weather.WeatherService;
import mx.com.recomendare.services.yahoo.boss.YahooBossSearchService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jerry
 *
 */
public final class ServiceFacade extends AbstractService {
	//the log...
    private static final Log log = LogFactory.getLog(ServiceFacade.class);
	
    //the security services...
    private EncryptService encryptService;
    //the database service...
    private DatabaseService databaseService;
    //the geographic IP location service...
    private GeoIPService geoIpService;
    //the geographic address location service... 
    private GeocoderService geoCoderService;
    //the translation service...
    private TranslationService translationService;
    //the recommender service...
    private RecommenderService recommenderService;
    //the mail service...
    private MailService mailService;
    //the statistics
    private StatisticsService statisticsService;
    //the weather...
    private WeatherService weatherService;
    //the currency converter service...
    private CurrencyConverterService currencyConverterService;
    //the amazon affiliate service...
    private AmazonAffiliateService amazonAffiliateService;
    //the open cell id service...
    private OpenCellIdService openCellIdService;
    //the geo naming service...
    private GeoNamesService geoNamesService;
    //the HTTP services...
    private HttpService httpService;
    //flickr service...
    private FlickrService flickrService;
    //feed parse service...
    private FeedParserService feedParserService;
    //the iata service...
    private IATAService iataService;
	//the kayac service...
    private KayakService kayakService;
    //the eventful service...
    private EventfulService eventfulService;
    //the yahoo search boss service...
    private YahooBossSearchService yahooBossSearchService;
    //the cloudmade service
    private CloudMadeGeoService cloudMadeGeoService;
    
    //and the resume of all ones...
    private MainService mainService;
    
	//the paths...
    private String pathToGeoLiteCityFile, pathToMailPropertiesFile, pathToIATAFile;

    /**
     * 
     * @param pathToGeoLiteCityFile
     * @param pathToMailPropertiesFile
     */
	public ServiceFacade(final String pathToGeoLiteCityFile, final String pathToMailPropertiesFile, final String pathToIATAFile) {
		this.pathToGeoLiteCityFile = pathToGeoLiteCityFile;
		this.pathToMailPropertiesFile = pathToMailPropertiesFile;
		this.pathToIATAFile = pathToIATAFile;
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
			encryptService = new EncryptService();
			databaseService = new DatabaseService();
			recommenderService = new RecommenderService(databaseService);
			geoIpService = new GeoIPService(pathToGeoLiteCityFile, databaseService);
			currencyConverterService = new CurrencyConverterService(databaseService);
			statisticsService = new StatisticsService(databaseService);
			mainService = new MainService(databaseService, currencyConverterService, encryptService);
			httpService = new HttpService();
	        geoCoderService = new GeocoderService(httpService);
	        geoNamesService = new GeoNamesService(httpService);
	        openCellIdService = new OpenCellIdService(httpService);
	        cloudMadeGeoService = new CloudMadeGeoService();
	        yahooBossSearchService = new YahooBossSearchService(httpService);
	        translationService = new TranslationService();
	        feedParserService = new FeedParserService();
	        iataService = new IATAService(pathToIATAFile);
	        kayakService = new KayakService(feedParserService, iataService);
	        mailService = new MailService(pathToMailPropertiesFile);
	        weatherService = new WeatherService();
	        amazonAffiliateService = new AmazonAffiliateService();
	        flickrService = new FlickrService();
	        eventfulService = new EventfulService();
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
			encryptService.doStop();
			recommenderService.doStop();
			geoIpService.doStop();
			currencyConverterService.doStop();
			statisticsService.doStop();
			geoCoderService.doStop();
			geoNamesService.doStop();
			openCellIdService.doStop();
			cloudMadeGeoService.doStop();
			yahooBossSearchService.doStop();
			translationService.doStop();
			mailService.doStop();
			weatherService.doStop();
			amazonAffiliateService.doStop();
			flickrService.doStop();
			eventfulService.doStop();
			mainService.doStop();
			//the fundamentals...
			httpService.doStop();
			databaseService.doStop();
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}

	/**
	 * 
	 * @return
	 */
	public EncryptService getEncryptService() {
		return encryptService;
	}

	/**
	 * 
	 * @return
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * 
	 * @return
	 */
	public GeoIPService getGeoIpService() {
		return geoIpService;
	}

	/**
	 * 
	 * @return
	 */
	public GeocoderService getGeoCoderService() {
		return geoCoderService;
	}

	/**
	 * 
	 * @return
	 */
	public TranslationService getTranslationService() {
		return translationService;
	}

	/**
	 * 
	 * @return
	 */
	public RecommenderService getRecommenderService() {
		return recommenderService;
	}

	/**
	 * 
	 * @return
	 */
	public MailService getMailService() {
		return mailService;
	}

	/**
	 * 
	 * @return
	 */
	public WeatherService getWeatherService() {
		return weatherService;
	}

	/**
	 * 
	 * @return
	 */
	public CurrencyConverterService getCurrencyConverterService() {
		return currencyConverterService;
	}

	/**
	 * 
	 * @return
	 */
	public StatisticsService getStatisticsService() {
		return statisticsService;
	}

	/**
	 * 
	 * @return
	 */
	public AmazonAffiliateService getAmazonAffiliateService() {
		return amazonAffiliateService;
	}

	/**
	 * 
	 * @return
	 */
	public OpenCellIdService getOpenCellIdService() {
		return openCellIdService;
	}

	/**
	 * 
	 * @return
	 */
	public GeoNamesService getGeoNamesService() {
		return geoNamesService;
	}

	/**
	 * 
	 * @return
	 */
	public HttpService getHttpService() {
		return httpService;
	}

	/**
	 * 
	 * @return
	 */
	public FlickrService getFlickrService() {
		return flickrService;
	}
	
	/**
	 * 
	 * @return
	 */
	public FeedParserService getFeedParserService() {
		return feedParserService;
	}
	
	/**
	 * 
	 * @return
	 */
	public IATAService getIataService() {
		return iataService;
	}

	/**
	 * 
	 * @return
	 */
	public KayakService getKayakService() {
		return kayakService;
	}

	/**
	 * 
	 * @return
	 */
	public EventfulService getEventfulService() {
		return eventfulService;
	}

	/**
	 * 
	 * @return
	 */
	public YahooBossSearchService getYahooBossSearchService() {
		return yahooBossSearchService;
	}

	/**
	 * 
	 * @return
	 */
	public MainService getMainService() {
		return mainService;
	}

	public CloudMadeGeoService getCloudMadeGeoService() {
		return cloudMadeGeoService;
	}

}//END OF FILE