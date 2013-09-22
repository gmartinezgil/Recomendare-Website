/**
 * 
 */
package mx.com.recomendare.web.rss;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.util.Constants;

import org.wicketstuff.rome.FeedResource;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;

/**
 * @author jerry
 *
 */
public final class HighRankedPlacesFeed extends FeedResource {
	private static final long serialVersionUID = 1L;

	private List places;
	/**
	 * 
	 */
	public HighRankedPlacesFeed(final List places) {
		super();
		this.places = places;
	}

	/* (non-Javadoc)
	 * @see org.wicketstuff.rome.FeedResource#getFeed()
	 */
	protected SyndFeed getFeed() {
		SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle(Constants.SITE_NAME+" - Los mejores lugares");
        feed.setLink(Constants.SITE_URL);
        feed.setDescription("Los lugares mejor recomendados");
        
        List entries = new ArrayList();
        Iterator iterator = places.iterator();
        while (iterator.hasNext()) {
            ItemRSSModel place = (ItemRSSModel) iterator.next();
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle(place.getName());
            entry.setLink(Constants.SITE_URL+"/place/i/"+place.getId()+"/n/"+place.getName()+"/");
            entry.setPublishedDate(new Date());
            SyndContent description = new SyndContentImpl();
            description.setType("text/plain");
            //description.setValue(place.getItem().getResume());
            entry.setDescription(description);
            entries.add(entry);
        }
        feed.setEntries(entries);
        feed.setPublishedDate(new Date());
        return feed;
	}

}//END OF FILE