/**
 * 
 */
package mx.com.recomendare.web.rss;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.recomendare.util.Constants;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeedImpl;

/**
 * @author jerry
 *
 */
public final class PlaceFeed extends SyndFeedImpl {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public PlaceFeed() {
		super();

		Place place = getPlace();
		
		setAuthor(Constants.SITE_NAME);
		setPublishedDate(new java.util.Date());
		setTitle("Place - "+place.getItem().getName());
		setFeedType(FeedManager.getDefaultFeedType());
		setDescription("");
		setLink(Constants.SITE_URL+"/place/i/"+place.getItem().getId()+"/n/"+place.getItem().getName()+"/");

		List entries = new ArrayList();
		SyndEntry entry = new SyndEntryImpl();
        entry.setTitle(place.getItem().getName());
        entry.setLink(Constants.SITE_URL+"/place/i/"+place.getItem().getId()+"/n/"+place.getItem().getName()+"/");
        entry.setPublishedDate(new Date());
        SyndContent description = new SyndContentImpl();
        description.setType("text/plain");
        //description.setValue(place.getItem().getResume());
        entry.setDescription(description);
        entries.add(entry);
        
		setEntries(entries);
	}

	/*
	private Place getPlace()  {
		Session session = ((DatabaseRequestCycle)RequestCycle.get()).getDatabaseSession();
		PlacesDAO dao = ((Main)RequestCycle.get().getApplication()).getDatabaseService().getPlacesDAO();
		dao.setSession(session);
		return dao.getPlaceById(1);
	}
	*/
	
}//END OF FILE