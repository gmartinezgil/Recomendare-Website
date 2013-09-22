/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mx.com.recomendare.db.entities.Item;
import mx.com.recomendare.db.entities.ItemComment;
import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.ItemsDAO;
import mx.com.recomendare.services.recommender.RecommenderServiceException;
import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.RecommendedItemModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.session.SignInSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.hibernate.Session;

/**
 * @author jerry
 *
 */
public final class RecommenderPanel extends Panel {
	private static final long serialVersionUID = 1L;

	//the log...
	private static final Log log = LogFactory.getLog(RecommenderPanel.class);

	/**
	 * @param id
	 */
	public RecommenderPanel(String id) {
		super(id);
		init();
	}

	private void init() {
		//RECOMMENDER
		add(new Label("recommendationsLabel", new LabelStringDetachableModel("RECOMMENDATIONS")));
		//TODO: it should be in a loadable detachable model...the recommendation list...
		final PageableListView itemsToRecommend = new PageableListView("recommendationsList", getRecommendations((SignInSession) getSession()), Constants.MAX_ITEMS_PER_PAGE) {
			private static final long serialVersionUID = 1L;
			protected void populateItem(final ListItem item) {
				final RecommendedItemModel itemModel = (RecommendedItemModel) item.getModelObject();
				final Link itemDetailLink = new Link("itemDetailLink") {
					private static final long serialVersionUID = 1L;
					public void onClick() {
						/*
						Session session = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
						ItemLocationsDAO places = ((Main) RequestCycle.get().getApplication()).getServices().getDatabaseService().getPlacesDAO();
						places.setSession(session);

						Place place = places.getPlaceById(itemModel.getId());
						PageParameters parameters = new PageParameters();
						parameters.put("i", place.getId());
						parameters.put("n", place.getItem().getName());
						setResponsePage(ViewPlacePage.class, parameters);
						*/
					}
				};
				itemDetailLink.add(new Label("itemName", itemModel.getName()));
				item.add(itemDetailLink);
				//item.add(new Label("itemRating", Util.getStarsFromRating(model.getRatingValue())));
				item.add(new StarsRatingPanel("itemRating", itemModel.getRatingValue(), StarsRatingPanel.ICON_STYLE));
			}
		};
		add(itemsToRecommend);
	}

	//utility...
	private List getRecommendations(SignInSession session) {
		List itemsToRecommend = null;
		try {
			itemsToRecommend = ((Main) getApplication()).getServices().getRecommenderService().getRecommendationsToUser(session.getUser().getScreenName());
		} catch (RecommenderServiceException e) {
			log.error("Failed to get the Item recommendations to User(" + session.getUser().getScreenName() + ") - " + e.getMessage(), e);
		}
		if (itemsToRecommend != null && itemsToRecommend.size() > 0) {//TODO: the recommender service has to deliver the full objects with this information...

			Session dbSession = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
			ItemsDAO itemsDAO = ((Main) getApplication()).getServices().getDatabaseService().getItemsDAO();
			itemsDAO.setSession(dbSession);

			final Iterator iterator = itemsToRecommend.iterator();
			while (iterator.hasNext()) {
				RecommendedItemModel itemToRecommend = (RecommendedItemModel) iterator.next();
				Item itemDAO = itemsDAO.getItemById(itemToRecommend.getId());
				if (itemDAO != null) {
					itemToRecommend.setName(itemDAO.getName());
					//itemToRecommend.setDescription(new ItemResumeStringDetachableModel(itemDAO.getId()));
					itemToRecommend.setRatingValue(calculateOverallRating(itemDAO.getItemComments()));
				}
			}
			return itemsToRecommend;
		}
		return null;
	}
	
	private static short calculateOverallRating(Set ratings)  {
		if(ratings != null && ratings.size() > 0)  {
			//get the ratings...the average...
			short numberOfStars = 0;
			Iterator iterator = ratings.iterator();
			while (iterator.hasNext()) {
				ItemComment rating = (ItemComment) iterator.next();
				numberOfStars += rating.getCalification().shortValue();
			}
			return (short) (numberOfStars / ratings.size());
		}
		return 0;
	}

}//END OF FILE