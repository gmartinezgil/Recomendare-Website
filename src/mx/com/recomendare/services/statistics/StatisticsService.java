/**
 * 
 */
package mx.com.recomendare.services.statistics;

import java.util.Date;

import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.services.db.DatabaseService;
import mx.com.recomendare.services.db.StatisticsDAO;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.session.UserSessionModel;
import mx.com.recomendare.web.util.UserAgentDetector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

/**
 * @author jerry
 *
 */
public final class StatisticsService extends AbstractService {
	//the log...
    private static final Log log = LogFactory.getLog(StatisticsService.class);

    //the database service...
    private DatabaseService databaseService;
	//the dao...
	private StatisticsDAO statisticsDAO;
	
	//the user agent parser...
	private UserAgentDetector userAgentDetector;
        
	/**
	 * 
	 */
	public StatisticsService(final DatabaseService databaseService) {
		this.databaseService = databaseService;
		doStart();
	}
	
	//********************************
	//*********SERVICE***************
	//********************************
	/**
	 * 
	 */
	@SuppressWarnings("static-access")
	public void doStart() {
		log.info("Starting - "+getClass().getName()+"...");
		if(!started)  {
			userAgentDetector = new UserAgentDetector();
			statisticsDAO = databaseService.getStatisticsDAO();
			Session session = databaseService.getSessionFactory().openSession();
	        statisticsDAO.setSession(session);
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
	 * @param now
	 * @param ipRemoteUserAddress
	 * @param userSessionId
	 * @param userSession
	 * @param url
	 * @param location 
	 * @param userAgent 
	 * @param linkName 
	 * @param referer 
	 */
	@SuppressWarnings("static-access")
	public void saveLinkClickStatistic(final Date now, final String ipRemoteUserAddress, final String userSessionId, final UserSessionModel userSession, final String url, final String linkName, final String userAgent, final LocationModel location, final String referer)  {
		ItemStatisticModelImpl statistic = new ItemStatisticModelImpl();
		statistic.setDate(now);
		statistic.setIpAddress(ipRemoteUserAddress);
		statistic.setSessionId(userSessionId);
		statistic.setPagePath(url);
		statistic.setUserSession(userSession);
		statistic.setLinkName(linkName);
		statistic.setBrowserName(userAgentDetector.getBrowser(userAgent)[1]);
		statistic.setBrowserVersion(userAgentDetector.getBrowser(userAgent)[2]);
		statistic.setBrowserPlatform(userAgentDetector.getOS(userAgent)[2]);
		statistic.setRefererrer(referer);
		statisticsDAO.createClickStreamStatistic(statistic);
	}
	
}//END OF FILE