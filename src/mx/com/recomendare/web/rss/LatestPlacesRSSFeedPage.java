/**
 * 
 */
package mx.com.recomendare.web.rss;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.commons.models.detachables.LatestPlacesListDetachableModel;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.aetrion.flickr.places.Place;

/**
 * @author jerry
 *
 */
public final class LatestPlacesRSSFeedPage extends BasePage {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public LatestPlacesRSSFeedPage() {
		super();
		init();
	}

	private void init()  {
		final LocationModel location = WebUtil.getUserActualLocation((SignInSession)getSession());
		add(new Image("rssFeedImage", WebUtil.ADD_RSS_FEED_IMAGE));
		add(new ResourceLink("latestPlacesFeed", new ResourceReference("mx.com.recomendare.web.rss.LatestPlacesFeedResource") {
			private static final long serialVersionUID = 1L;
			protected Resource newResource() {
				LoadableDetachableModel model = new LatestPlacesListDetachableModel((short)Constants.MAX_ITEMS_PER_PAGE);
				final List thePlaces = (List)model.getObject();
				List places = new ArrayList(thePlaces.size());
				Iterator iterator = thePlaces.iterator();
				while (iterator.hasNext()) {
					Place place = (Place) iterator.next();
					ItemRSSModel placeModel = new ItemRSSModel();
					placeModel.setId(place.getId());
					placeModel.setName(place.getItem().getName());
					places.add(placeModel);
				}
				return new LatestPlacesFeedResource(places);
			}
		}));
	}

	protected IModel getPageTitle() {
		return null;
	}

}//END OF FILE