/**
 * 
 */
package mx.com.recomendare.web.users;

import java.util.ArrayList;

import mx.com.recomendare.db.entities.UserConfiguration;
import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.commons.models.detachables.CityNameStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.CountryNameStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.UserConfigurationDetachableModel;
import mx.com.recomendare.web.places.ViewPlacePage;
import mx.com.recomendare.web.session.AuthenticatedPage;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.image.resource.ThumbnailImageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.aetrion.flickr.places.Place;

/**
 * @author jerry
 *
 */
//TODO:maybe be better have a simple profile page with name and favorites...and a secure version with all these details...
public final class ViewUserProfilePage extends AuthenticatedPage {
    private static final long serialVersionUID = 1L;
    
    /**
     * 
     * @param parameters
     */
    public ViewUserProfilePage(PageParameters parameters) {
        super(parameters);
        String userId = parameters.getString("i");
        if (userId != null && userId.trim().length() > 0) {
            IModel userprofileModel = new CompoundPropertyModel(new UserConfigurationDetachableModel(userId));
            setModel(userprofileModel);
            init();
        } else {/*throw new UserProfileNotFoundException(userProfileKey);*/
        }
    }

    private void init() {
    	final UserConfiguration userConfiguration = (UserConfiguration) getModelObject();
        //TITLE
        add(new Label("userProfileLabel", new LabelStringDetachableModel("USER_PROFILE")));
        //AVATAR IMAGE
        if (userConfiguration.getPicture() != null && userConfiguration.getPicture().getContent().length > 0) {
            final DynamicImageResource imageResource = new DynamicImageResource() {
                private static final long serialVersionUID = 1L;
                protected byte[] getImageData() {
                    return userConfiguration.getPicture().getContent();
                }
            };
            add(new Image("userProfilePicture", imageResource));
        } else {
            add(new Image("userProfilePicture", WebUtil.QUESTIONMARK_IMAGE));
        }
        //NAME
        add(new Label("userProfileName", new PropertyModel(getModel(), "screenName")));
        //ADD FRIEND LINK
        final Link addAsFriendLink = new Link("addAsFriendLink") {
            private static final long serialVersionUID = 1L;
            public void onClick() {
            	PageParameters parameters = new PageParameters();
            	parameters.put("i", userConfiguration.getId());
            	parameters.put("n", userConfiguration.getScreenName());
                setResponsePage(AddFriendRequestPage.class, parameters);
            }
        };
        addAsFriendLink.add(new Image("addPicture", WebUtil.ADD_IMAGE));
        addAsFriendLink.add(new Label("addAsFriendLabel", new LabelStringDetachableModel("ASK_TO_BE_MY_FRIEND")));
        add(addAsFriendLink);
        SignInSession session = (SignInSession) getSession();
        if (session != null && session.isSignedIn()) {
            if (session.getUser().getScreenName().equals(userConfiguration.getScreenName())) {
                addAsFriendLink.setVisible(false);
            }
        }
        //LOCATION
        //country...
        final Link viewCountryProfilesLink = new Link("viewCountryProfilesLink") {
            private static final long serialVersionUID = 1L;
            public void onClick() {
            	PageParameters parameters = new PageParameters();
            	parameters.put("i", userConfiguration.getCity().getCountry().getId());
            	parameters.put("n", new CountryNameStringDetachableModel(userConfiguration.getCity().getCountry().getId()));
                setResponsePage(ViewUserProfilesBaseOnCountryLocation.class, parameters);//TODO:base on city better...and there on country...
            }
        };
        viewCountryProfilesLink.add(new Label("countryLabel", new CountryNameStringDetachableModel(userConfiguration.getCity().getCountry().getId())));
        add(viewCountryProfilesLink);
        //city...
        final Link viewCityProfilesLink = new Link("viewCityProfilesLink") {
            private static final long serialVersionUID = 1L;
            public void onClick() {
            	PageParameters parameters = new PageParameters();
            	parameters.put("i", userConfiguration.getCity().getId());
            	parameters.put("n", new CityNameStringDetachableModel(userConfiguration.getCity().getId()));
                setResponsePage(ViewUserProfilesBaseOnCityLocation.class, parameters);
            }
        };
        viewCityProfilesLink.add(new Label("cityLabel", new CityNameStringDetachableModel(userConfiguration.getCity().getId())));
        add(viewCityProfilesLink);
        //FAVORITES
        add(new Label("favoritesTitleLabel", new LabelStringDetachableModel("MY_FAVORITES")));
        final DataView favoritesDataView = new DataView("favorites", new ListDataProvider(new ArrayList(userConfiguration.getFavorites()))) {//TODO:change to pageable list view...
            private static final long serialVersionUID = 1L;

            protected void populateItem(final Item item) {
                final Favorite favorite = (Favorite) item.getModelObject();
                final Link viewFavoriteItemLink = new Link("viewFavoriteItemLink") {

                    private static final long serialVersionUID = 1L;

                    public void onClick() {
                        Place place = ((Place) favorite.getItem().getPlaces().toArray()[0]);
                        PageParameters parameters = new PageParameters();
                        parameters.put("i", place.getId());
                        parameters.put("n", place.getItem().getName());
                        setResponsePage(ViewPlacePage.class, parameters);
                    }
                };
                if (favorite.getItem().getPictureByPictureOneId() != null && favorite.getItem().getPictureByPictureOneId().getContent().length > 0) {
                    final DynamicImageResource imageResource = new DynamicImageResource() {
                        private static final long serialVersionUID = 1L;
                        protected byte[] getImageData() {
                            return favorite.getItem().getPictureByPictureOneId().getContent();
                        }
                    };
                    ThumbnailImageResource thumbnailImageResource = new ThumbnailImageResource(imageResource, 100);
                    thumbnailImageResource.setCacheable(false);
                    viewFavoriteItemLink.add(new Image("itemPicture", thumbnailImageResource));
                }
                else  {
                    viewFavoriteItemLink.add(new Image("itemPicture", WebUtil.QUESTIONMARK_IMAGE));
                }
                viewFavoriteItemLink.add(new Label("itemName", favorite.getItem().getName()));
                item.add(viewFavoriteItemLink);
            }
        };
        add(favoritesDataView);
        add(new PagingNavigator("itemsNavigator", favoritesDataView));
        //FRIENDS//TODO:enable these features on the database design...
    }

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new Model(Constants.SITE_NAME + " - " + new LabelStringDetachableModel("PROFILE") +" - " +((UserConfiguration)getModelObject()).getScreenName());
	}

	protected String getMetaDescription() {
		return "";
	}

	protected String getMetaKeywords() {
		return "";
	}
    
}//END OF FILE