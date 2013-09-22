/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.FavoritesDAO;
import mx.com.recomendare.util.Util;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.favorites.AddToFavoritesPage;
import mx.com.recomendare.web.favorites.ViewMyFavoritesPage;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.hibernate.Session;

/**
 * @author jerry
 *
 */
public final class FavoritesLinkPanel extends Panel {
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param model
	 */
	public FavoritesLinkPanel(String id, IModel model) {
		super(id, model);
		init();
	}
	
	private void init()  {
		final ItemModel item = (ItemModel)getModelObject();
		
		//FAVORITES
        //add..
        final Link addToFavoritesLink = new Link("addToFavoritesLink") {
            private static final long serialVersionUID = 1L;
            public void onClick() {
                PageParameters parameters = new PageParameters();
                parameters.put("i", item.getId());
                setResponsePage(AddToFavoritesPage.class, parameters);
            }
        };
        addToFavoritesLink.add(new Image("addFavoritePicture", WebUtil.ADD_FAVORITE_IMAGE));
        addToFavoritesLink.add(new Label("addToFavoritesLabel", new LabelStringDetachableModel("ADD_TO_MY_FAVORITES")));
        add(addToFavoritesLink);
        //already in...
        final Link viewMyFavoritesLink = new Link("viewMyFavoritesLink") {
            private static final long serialVersionUID = 1L;
            public void onClick() {
            	setResponsePage(ViewMyFavoritesPage.class);
            }
        };
        viewMyFavoritesLink.add(new Image("viewFavoritesPicture", WebUtil.VIEW_FAVORITES_IMAGE));
        viewMyFavoritesLink.add(new Label("viewMyFavoritesLabel", new LabelStringDetachableModel("VIEW_MY_FAVORITES")));
        if(!((SignInSession)getSession()).isSignedIn()) viewMyFavoritesLink.setVisible(false);
        add(viewMyFavoritesLink);
        //search if it's already in my favorites...
        Favorite myOldFavorite = findInMyFavorites((SignInSession) getSession());
        if (myOldFavorite != null) {
            addToFavoritesLink.setVisible(false);
            add(new Label("favoriteStatus", new LabelStringDetachableModel("IN_YOUR_FAVORITES_SINCE") + Util.getDateFormat(myOldFavorite.getCreatedOn(), WebUtil.getUserActualLocation(((SignInSession)getSession())))));
        } else {
            addToFavoritesLink.setVisible(true);
            add(new Label("favoriteStatus", ""));
        }
	}
	
	//utility...
    private Favorite findInMyFavorites(SignInSession session) {//TODO:it should be in the database service... 
        if (session != null && session.isSignedIn()) {
            
            Session dbSession = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
            FavoritesDAO favoritesDAO = ((Main) getApplication()).getServices().getDatabaseService().getFavoritesDAO();
            favoritesDAO.setSession(dbSession);
            List userFavorites = favoritesDAO.getFavoritesByScreenName(session.getUser().getScreenName());
            
            Iterator iterator = userFavorites.iterator();
            while (iterator.hasNext()) {
                Favorite favorite = (Favorite) iterator.next();
                if (favorite.getItem() != null) {
                    if (((ItemModel)getModelObject()).getId() == favorite.getItem().getId()) {
                        return favorite;
                    }
                }
            }
        }
        return null;
    }

}//END OF FILE