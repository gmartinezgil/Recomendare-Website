/**
 * 
 */
package mx.com.recomendare.services.feed;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.services.cache.CacheService;
import net.sf.ehcache.CacheException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @author jery
 *
 */
public final class FeedParserService extends AbstractService {
	//the log...
	private static final Log log = LogFactory.getLog(FeedParserService.class);
	//the parser input..
	private SyndFeedInput input;
	//the cache...
	private CacheService cacheService;
	/**
	 * 
	 */
	public FeedParserService() {
		doStart();
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.Service#doStart()
	 */
	public void doStart() {
		log.info("Starting - "+getClass().getName()+"...");
		if(!started)  {
			input =  new SyndFeedInput();
			try {
				cacheService = new CacheService("feedparserservice");
				started = true;
				log.info(getClass().getName()+"...started!");
			}
			catch (CacheException e) {
				log.error("Can't make use of the cache service - "+e.getMessage(), e);
			}
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
			input = null;
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}

	/**
	 * 
	 * @param urlFeedToParse
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SyndEntry> parseFeed(final String urlFeedToParse)  {
		log.info(urlFeedToParse);
		List<SyndEntry> feedEntries = (List<SyndEntry>)cacheService.getFromCache(urlFeedToParse);
		if(feedEntries != null)  {
			return feedEntries;
		}
		else  {
			try {
				final URL feedUrl = new URL(urlFeedToParse); 
				final SyndFeed feed = input.build(new XmlReader(feedUrl));
				feedEntries = feed.getEntries();
				cacheService.addToCache(urlFeedToParse, (Serializable)feedEntries);
				return feedEntries;
			} catch (MalformedURLException e) {
				log.error("Can't parse the site address - "+urlFeedToParse+", reason - "+e.getMessage(), e);
			} catch (IllegalArgumentException e) {
				log.error("Illegal argument with the site address - "+urlFeedToParse+", reason - "+e.getMessage(), e);
			} catch (FeedException e) {
				log.error("Can't read site with address - "+urlFeedToParse+", reason - "+e.getMessage(), e);
			} catch (IOException e) {
				log.error("Can't connect with the site with address - "+urlFeedToParse+", reason - "+e.getMessage(), e);
			}
			return null;
		}
	}
	
}//END OF FILE