/**
 * 
 */
package mx.com.recomendare.web.rss;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.model.IModel;
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
public final class ItemsFeedResource extends FeedResource {
	private static final long serialVersionUID = 1L;

	private IModel model;
	/**
	 * 
	 */
	public ItemsFeedResource(final IModel model) {
		this.model = model;
	}

	/* (non-Javadoc)
	 * @see org.wicketstuff.rome.FeedResource#getFeed()
	 */
	@SuppressWarnings("unchecked")
	protected SyndFeed getFeed() {
		SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle(Constants.SITE_NAME);
        feed.setLink(Constants.SITE_URL);
        feed.setDescription("");
        
        final List<ItemModel> items = (List<ItemModel>)model.getObject();
        final Iterator<ItemModel> iterator = items.iterator();
        List<SyndEntry> entries = new ArrayList<SyndEntry>(items.size());
        while (iterator.hasNext()) {
			ItemModel itemModel = iterator.next();
			SyndEntry entry = new SyndEntryImpl();
            entry.setTitle(itemModel.getName());
            entry.setLink(Constants.SITE_URL+"/place/i/"+itemModel.getId()+"/n/"+WebUtil.encodeURL(itemModel.getName())+"/");
            entry.setPublishedDate(new Date());
            SyndContent description = new SyndContentImpl();
            description.setType("text/plain");
            description.setValue(itemModel.getDescription());
            entry.setDescription(description);
            entries.add(entry);
        }
        feed.setEntries(entries);
        feed.setPublishedDate(new Date());
        return feed;
	}

}//END OF FILE