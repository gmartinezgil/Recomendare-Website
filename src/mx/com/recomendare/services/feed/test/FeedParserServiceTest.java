package mx.com.recomendare.services.feed.test;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import mx.com.recomendare.services.feed.FeedParserService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.syndication.feed.synd.SyndEntry;

public class FeedParserServiceTest extends TestCase {
	//the log...
	private static final Log log = LogFactory.getLog(FeedParserServiceTest.class);
	
	public void testParseFeed() {
		FeedParserService feedService = new FeedParserService();
		List<SyndEntry> feeds = feedService.parseFeed("http://www.kayak.com/h/rss/fare?code=MEX&dest=CUN&mc=MXN");
		Iterator<SyndEntry> iterator = feeds.iterator();
		while (iterator.hasNext()) {
			SyndEntry entry = iterator.next();
			log.info("-------------------------------------------------------------------------------");
			log.info(entry.getUri());
			log.info(entry.getLink());
			log.info(entry.getTitle());
			log.info(entry.getPublishedDate());
			if (entry.getDescription() != null) {
				log.info(" Description: "+ entry.getDescription().getValue());
			}
		}
	}
	
	/*
	public void testTimeZones()  {
		String[] timezones = TimeZone.getAvailableIDs();
		for (int i = 0; i < timezones.length; i++) {
			String timezone = timezones[i];
			System.out.println(timezone);
		}
	}
	*/

}