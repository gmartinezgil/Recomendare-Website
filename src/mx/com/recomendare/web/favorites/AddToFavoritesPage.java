/**
 * 
 */
package mx.com.recomendare.web.favorites;

import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.FavoritesDAO;
import mx.com.recomendare.util.Util;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.commons.models.detachables.ItemDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.items.ViewItemPage;
import mx.com.recomendare.web.session.AuthenticatedPage;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.extensions.markup.html.image.resource.ThumbnailImageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.hibernate.Session;

/**
 * @author jerry
 *
 */
public final class AddToFavoritesPage extends AuthenticatedPage {
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @param parameters
     */
    public AddToFavoritesPage(PageParameters parameters) {
        super(parameters);
        String itemKey = parameters.getString("i");
        if (itemKey != null && itemKey.trim().length() > 0) {
            int itemId = Integer.parseInt(itemKey);
            IModel itemModel = new CompoundPropertyModel(new ItemDetachableModel(itemId, ItemDetachableModel.PREVIEW_MODE));
            setModel(itemModel);
            init();
        } else {/*throw new ItemNotFounException(itemKey);*/ //TODO:better send to a 404 custom page...
        }
    }

    private void init() {
        final ItemModel model = (ItemModel)getModelObject();
        //TODO:these code should be in another part...
        Session dbSession = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
        FavoritesDAO favorites = ((Main) getApplication()).getServices().getDatabaseService().getFavoritesDAO();
        favorites.setSession(dbSession);
        Favorite newFavorite = favorites.createFavorite(model.getId(), ((SignInSession) getSession()).getUser().getScreenName());//TODO: it should present a form for the comments and then add this item...to favorites..

        //TITLE
        add(new Label("myFavoritesLabel", new LabelStringDetachableModel("MY_FAVORITES")));
        //RECENT ADDED...
        add(new Label("newFavoriteLabel", new LabelStringDetachableModel("NEW_FAVORITE")));
        add(new Label("newFavoriteDate", Util.getDateFormat(newFavorite.getCreatedOn(), WebUtil.getUserActualLocation(((SignInSession)getSession())))));
        add(new Image("favoritePicture", WebUtil.VIEW_FAVORITES_IMAGE));
        final Link viewRecentAddedItemLink = new Link("viewRecentAddedItemLink") {
            private static final long serialVersionUID = 1L;
            public void onClick() {
                PageParameters parameters = new PageParameters();
                parameters.put("i", model.getId());
                parameters.put("n", model.getName());
                setResponsePage(ViewItemPage.class, parameters);
            }
        };
        if(model.getPictures() != null && model.getPictures().get(0) != null && model.getPictures().get(0).getContent().length > 0) {
            final DynamicImageResource imageResource = new DynamicImageResource() {
                private static final long serialVersionUID = 1L;
                protected byte[] getImageData() {
                    return model.getPictures().get(0).getContent();
                }
            };
            final ThumbnailImageResource thumbnailImageResource = new ThumbnailImageResource(imageResource, 100);
            thumbnailImageResource.setCacheable(false);
            viewRecentAddedItemLink.add(new Image("recentItemPicture", thumbnailImageResource));
        } else {
            viewRecentAddedItemLink.add(new Image("recentItemPicture", WebUtil.QUESTIONMARK_IMAGE));
        }
        viewRecentAddedItemLink.add(new Label("recentItemName", new PropertyModel(getModel(), "name")));
        add(viewRecentAddedItemLink);
        //ALL MY FAVORITES...
        
    }

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new LabelStringDetachableModel("ADD_FAVORITE");
	}
    
}//END OF FILE