/**
 * 
 */
package mx.com.recomendare.web;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.categories.ViewAllCategoriesPage;
import mx.com.recomendare.web.commons.components.BlankPanel;
import mx.com.recomendare.web.commons.components.CountryCityNavigationPanel;
import mx.com.recomendare.web.commons.components.RecommenderPanel;
import mx.com.recomendare.web.commons.components.StarsRatingPanel;
import mx.com.recomendare.web.commons.components.TagCloud;
import mx.com.recomendare.web.commons.models.CommentModel;
import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.commons.models.detachables.CategoriesTagListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LatestRatingsListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.ZonesTagListDetachableModel;
import mx.com.recomendare.web.items.ViewItemPage;
import mx.com.recomendare.web.location.ViewAllLocationsPage;
import mx.com.recomendare.web.ratings.ViewAllRatingsPage;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.users.ViewUserProfilePage;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.image.resource.ThumbnailImageResource;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * @author jerry
 *
 */
public final class SideBarPanel extends Panel {
    private static final long serialVersionUID = 1L;

    /**
     * @param id
     */
    public SideBarPanel(String id) {
        super(id);
        init();
    }

    private void init() {
        //RECOMMENDER
        Panel recommenderPanel = null;
        if(	((SignInSession)getSession()).isSignedIn())  {
        	recommenderPanel = new RecommenderPanel("recommenderPanel");
        }
        else  {
        	recommenderPanel = new BlankPanel("recommenderPanel");
        }
        add(recommenderPanel);
        //CATEGORIES
        add(new Label("categoriesLabel", new LabelStringDetachableModel("BY_CATEGORY")));
        final TagCloud categoriesTagCloud = new TagCloud("categoriesTagCloud", new CategoriesTagListDetachableModel());
        add(categoriesTagCloud);
        final Link viewAllCategoriesLink = new Link("viewAllCategoriesLink") {
            private static final long serialVersionUID = 1L;
            public void onClick() {
            	//TODO:clickstream statistics...
                setResponsePage(ViewAllCategoriesPage.class);
            }
        };
        viewAllCategoriesLink.add(new Label("viewAllCategoriesLinkLabel", new LabelStringDetachableModel("MORE")));
        add(viewAllCategoriesLink);
        //ZONES
        add(new Label("locationsLabel",  new LabelStringDetachableModel("BY_ZONE")));
        final TagCloud locationsTagCloud = new TagCloud("locationsTagCloud", new ZonesTagListDetachableModel());
        add(locationsTagCloud);
        final Link viewAllLocationsLink = new Link("viewAllLocationsLink") {
            private static final long serialVersionUID = 1L;
            public void onClick() {
            	
            	//TODO:clickstream statistics...
                setResponsePage(ViewAllLocationsPage.class);
            }
        };
        viewAllLocationsLink.add(new Label("viewAllLocationsLinkLabel", new LabelStringDetachableModel("MORE")));
        add(viewAllLocationsLink);
        //LOCATION MAP
        final LocationModel location = WebUtil.getUserActualLocation(((SignInSession)getSession()));
        add(new Label("placeLocation", new LabelStringDetachableModel("MY_LOCATION")));//TODO: it should be called an image from the data base...
        //links
        add(new CountryCityNavigationPanel("myLocationCountryCityNavigationPanel", new CompoundPropertyModel(location)));
        //map
        final WebMarkupContainer mapContainer = new WebMarkupContainer("mapContainer") {
			private static final long serialVersionUID = 1L;
			protected void onComponentTag(ComponentTag tag) {
				//tag.put("style", "background-image:url(" +	RequestCycle.urlFor("images/") + ");"); 
				tag.put("style", "background-image:url(\"images/staticmap.gif\")");
			}
        };
        //form
        final Form form = new Form("locationForm");
        form.add(new Label("streetLabel", new LabelStringDetachableModel("STREET")));
        form.add(new TextField("street"));
        form.add(new Button("update", new LabelStringDetachableModel("UPDATE")));
        mapContainer.add(form);
        add(mapContainer);
		/*
		final Image googleStaticPlaceMapImage = new Image("pictureMapLocation", WebUtil.getStaticMapImageResource(
																						location.getLatitude(), 
																						location.getLongitude(), 
																						Constants.STREET_VIEW_ZOOM_LEVEL, 
																						210, 
																						210
																						));
		final Link viewPlaceOnInteractiveMap = new Link("viewPlaceOnInteractiveMap"){
			private static final long serialVersionUID = 1L;
			public void onClick() {
				PageParameters parameters = new PageParameters();
				setResponsePage(ViewAnotherPlacesNearOfPage.class, parameters);
			}
		};
		viewPlaceOnInteractiveMap.add(googleStaticPlaceMapImage);
		viewPlaceOnInteractiveMap.add(new Label("viewPlaceOnInteractiveMapLabel", new LabelStringDetachableModel("SET_LOCATION")));
		add(viewPlaceOnInteractiveMap);
		*/
        //LAST COMMENTS
        add(new Label("lastCommentsLabel", new LabelStringDetachableModel("LAST_COMMENTS")));
        final PageableListView lastCommentsPageableListView = new PageableListView("lastComments", new LatestRatingsListDetachableModel(WebUtil.getUserActualLocation(((SignInSession)getSession())).getCityId(), Constants.MAX_ITEMS_PER_PAGE), Constants.MAX_COMMENTS_FRONT_PAGE) {
            private static final long serialVersionUID = 1L;
            protected void populateItem(final ListItem item) {
            	final ItemModel model = (ItemModel)item.getModelObject();
            	final CommentModel comment = (CommentModel)model.getComments().toArray()[0];
            	//item
                final Link placeDetailLink = new Link("placeDetailLink") {
                    private static final long serialVersionUID = 1L;
                    public void onClick() {
                        //TODO:clickstream statistics...
                        PageParameters parameters = new PageParameters();
                        parameters.put("i", model.getId());
                        parameters.put("n", model.getName());
                        setResponsePage(ViewItemPage.class, parameters);
                    }
                };
                placeDetailLink.add(new Label("ratingPlace", model.getName()));
                item.add(placeDetailLink);
                //user rating
                item.add(new StarsRatingPanel("ratingValue", comment.getRating(), StarsRatingPanel.NORMAL_STYLE));
            	//user detail
                final Link userDetailLink = new Link("userDetailLink") {
                    private static final long serialVersionUID = 1L;
                    public void onClick() {
                        PageParameters parameters = new PageParameters();
                        parameters.put("i", comment.getUserId());
                        parameters.put("n", comment.getUserScreenName());
                        setResponsePage(ViewUserProfilePage.class, parameters);
                    }
                };
                if(comment.getUserAvatarPicture() != null && comment.getUserAvatarPicture().getContent() != null) {
                	final DynamicImageResource imageResource = new DynamicImageResource() {
        				private static final long serialVersionUID = 1L;
        				protected byte[] getImageData() {
        					return comment.getUserAvatarPicture().getContent();
        				}
        			};
        			final ThumbnailImageResource thumbnailImageResource = new ThumbnailImageResource(imageResource, 50);
                	userDetailLink.add(new Image("userPicture", thumbnailImageResource));
                }
                else  {
                	userDetailLink.add(new Image("userPicture", WebUtil.USER_STATUS_OFFLINE_IMAGE));
                }
                userDetailLink.add(new Label("userScreenName", comment.getUserScreenName()));
                item.add(userDetailLink);
                //user comment
                final Label ratingComment = new Label("ratingComment", comment.getUserComment());
                ratingComment.setEscapeModelStrings(false);
                item.add(ratingComment);
                
                final Link viewRatingDetailLink = new Link("viewRatingDetailLink") {
                    private static final long serialVersionUID = 1L;
                    public void onClick() {
                        //TODO:clickstream statistics...
                        PageParameters parameters = new PageParameters();
                        parameters.put("i", model.getId());
                        parameters.put("n", model.getName());
                        setResponsePage(ViewItemPage.class, parameters);
                    }
                };
                viewRatingDetailLink.add(new Label("viewRatingDetailLinkLabel", new LabelStringDetachableModel("DETAIL")));
                item.add(viewRatingDetailLink);
            }
        };
        add(lastCommentsPageableListView);
        //add(new PagingNavigator("lastCommentsNavigator", lastCommentsPageableListView));
        final Link viewAllRatingsLink = new Link("viewAllRatingsLink") {
            private static final long serialVersionUID = 1L;
            public void onClick() {
            	//TODO:clickstream statistics...
                setResponsePage(ViewAllRatingsPage.class);
            }
        };
        viewAllRatingsLink.add(new Label("viewAllRatingsLinkLabel", new LabelStringDetachableModel("MORE")));
        add(viewAllRatingsLink);
    }
        
}//END OF FILE