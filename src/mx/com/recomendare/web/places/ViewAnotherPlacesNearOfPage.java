/**
 * 
 */
package mx.com.recomendare.web.places;

import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.util.Util;
import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.commons.models.detachables.ItemResumeStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.PlacesNearByListDetachableModel;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.markup.html.image.resource.ThumbnailImageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import wicket.contrib.gmap.GMap2;
import wicket.contrib.gmap.api.GControl;
import wicket.contrib.gmap.api.GLatLng;
import wicket.contrib.gmap.api.GMapType;
import wicket.contrib.gmap.api.GMarker;
import wicket.contrib.gmap.api.GMarkerOptions;

import com.aetrion.flickr.places.Place;

/**
 * @author jerry
 *
 */
public final class ViewAnotherPlacesNearOfPage extends BasePage {
    private static final long serialVersionUID = 1L;

    //the original place name...
    private String placeName;

    //the log...
    private static final Log log = LogFactory.getLog(ViewAnotherPlacesNearOfPage.class);
    
    /**
     * 
     * @param parameters
     */
    public ViewAnotherPlacesNearOfPage(PageParameters parameters) {
        super(parameters);
        placeName = parameters.getString("n");
        String placeKey = parameters.getString("i");
        if (placeKey != null && placeKey.trim().length() > 0) {
            int placeId = Integer.parseInt(placeKey);
            IModel placeModel = new CompoundPropertyModel(new PlacesNearByListDetachableModel(placeId));
            setModel(placeModel);
            init();
        } else {/*throws PlaceNotFoundException(placeKey);*//*setResponsePage(pageBack);*/
        }
    }

    private void init() {
        //TITLE
        add(new Label("nearbyPlacesLabel", placeName));
        //MAP
        add(new Label("mapLabel", new LabelStringDetachableModel("MAP")));
        final GMap2 gMap = new GMap2("gmap", Constants.GMAP_LOCALHOST_8080_KEY);
        gMap.setCenter(getCenterOfMap((List) getModelObject()));
        gMap.setZoom(Constants.STREET_VIEW_ZOOM_LEVEL);
        gMap.addControl(GControl.GMapTypeControl);
        gMap.addControl(GControl.GLargeMapControl);
        gMap.setMapType(GMapType.G_NORMAL_MAP);
        //markers...
        final Iterator iterator = ((List) getModelObject()).iterator();
        while (iterator.hasNext()) {
            Place place = (Place) iterator.next();
            final GLatLng placeLocation = new GLatLng(place.getLocation().getLatitude().floatValue(), place.getLocation().getLongitude().floatValue());
            final GMarker placeMarker = new GMarker(placeLocation, new GMarkerOptions(place.getItem().getName()));
            gMap.addOverlay(placeMarker);
        }
        gMap.setOutputMarkupId(true);
        add(gMap);
        //FORM
        add(new PlaceOptionsForm("placeOptionsForm"));
        //PLACES
        add(new Label("placesNearByLabel", new LabelStringDetachableModel("NEARBY_PLACES")));
        final PageableListView placesNearByPageableListView = new PageableListView("placesNearBy", (List)getModelObject(), Constants.MAX_ITEMS_PER_PAGE) {
            private static final long serialVersionUID = 1L;
            protected void populateItem(final ListItem item) {
                final Place place = (Place) item.getModelObject();
                final Link viewPlaceDetailLink = new Link("viewPlaceDetailLink") {
                    private static final long serialVersionUID = 1L;
                    public void onClick() {
                        PageParameters parameters = new PageParameters();
                        parameters.put("i", place.getId());
                        parameters.put("n", place.getItem().getName());
                        setResponsePage(ViewPlacePage.class, parameters);
                    }
                };
                if(place.getItem().getPictureByPictureOneId() != null && place.getItem().getPictureByPictureOneId().getContent().length > 0) {
                    final DynamicImageResource imageResource = new DynamicImageResource() {
                        private static final long serialVersionUID = 1L;
                        protected byte[] getImageData() {
                            return place.getItem().getPictureByPictureOneId().getContent();
                        }
                    };
                    final ThumbnailImageResource thumbnailImageResource = new ThumbnailImageResource(imageResource, 100);
                    thumbnailImageResource.setCacheable(false);
                    viewPlaceDetailLink.add(new Image("picturePlace", thumbnailImageResource));
                } else {
                    viewPlaceDetailLink.add(new Image("picturePlace", WebUtil.QUESTIONMARK_IMAGE));
                }
                viewPlaceDetailLink.add(new Label("namePlaceLabel", place.getItem().getName()));
                item.add(viewPlaceDetailLink);
                if (place.getItem().getItemRatings().size() > 0) {
                    //get the ratings...the average...
                    final Iterator iterator = place.getItem().getItemRatings().iterator();
                    int numberOfStars = 0;
                    while (iterator.hasNext()) {
                        ItemRating rating = (ItemRating) iterator.next();
                        numberOfStars += rating.getCalification().intValue();
                    }
                    item.add(new Label("placeRating", Util.getStarsFromRating(numberOfStars / place.getItem().getItemRatings().size())));
                } else {
                    item.add(new Label("placeRating", new LabelStringDetachableModel("NOT_RATED")));
                }
                item.add(new Label("placeDescription", new ItemResumeStringDetachableModel(place.getItem().getId())));
                final AjaxFallbackLink viewPlaceLocationOnMapLink = new AjaxFallbackLink("viewPlaceLocationOnMapLink") {

                    private static final long serialVersionUID = 1L;

                    public void onClick(AjaxRequestTarget target) {
                        final Iterator iterator = gMap.getOverlays().iterator();
                        while (iterator.hasNext()) {
                            GMarker marker = (GMarker) iterator.next();
                            float markerLatitude = (float) marker.getLatLng().getLat();
                            float markerLongitude = (float) marker.getLatLng().getLng();
                            float placeLatitude = place.getLocation().getLatitude().floatValue();
                            float placeLongitude = place.getLocation().getLongitude().floatValue();
                            log.info("GMarker(" + markerLatitude + ", " + markerLongitude + ") == Place(" + placeLatitude + ", " + placeLongitude + ")");
                            log.info("Distance " + Util.calculateDistanceBetweenTwoPoints(markerLatitude, markerLongitude, placeLatitude, placeLongitude) + " KM");
                            if (markerLatitude == placeLatitude && markerLongitude == placeLongitude) {
                                gMap.setCenter(marker.getLatLng());
                                /*
                                if (target != null) {
                                    target.addComponent(gMap);
                                }
                                */
                                return;
                            }
                        }
                    }
                };
                viewPlaceLocationOnMapLink.add(new Label("locateOnMapLabel", new LabelStringDetachableModel("LOCATE_ON_MAP")));
                viewPlaceLocationOnMapLink.add(new Image("pictureLocateOnMap", WebUtil.LOCATE_PLACE_ON_MAP_IMAGE));
                viewPlaceLocationOnMapLink.setOutputMarkupId(true);
                item.add(viewPlaceLocationOnMapLink);
            }
        };
        add(placesNearByPageableListView);
        add(new PagingNavigator("placesNearByNavigator", placesNearByPageableListView));
    }

    /**
     *  ViewAnotherPlacesForm
     */
    final class PlaceOptionsForm extends Form {

        private static final long serialVersionUID = 1L;

        public PlaceOptionsForm(String id) {
            super(id);
            init();
        }

        private void init() {
            //PLACES
            add(new Label("mapOptionsLabel", new LabelStringDetachableModel("MAP_OPTIONS")));
            //final AjaxCheckBox viewSameCategory, viewAllCategories
        }
    }

    //utility...
    private GLatLng getCenterOfMap(final List places) {
        double maxLng = 0.0;
        double maxLat = 0.0;
        double minLng = 0.0;
        double minLat = 0.0;
        boolean firstTime = true;
        final Iterator iterator = places.iterator();
        while (iterator.hasNext()) {
            Place place = (Place) iterator.next();
            double lat = place.getLocation().getLatitude();
            double lng = place.getLocation().getLongitude();
            log.info("lat = " + lat + ", lng = " + lng);
            //TODO: set the bounds properly...this it's just a hack...
            if (firstTime) {
                minLat = lat;
                maxLng = lng;
                firstTime = false;
            }
            if (lng > maxLng) {
                maxLng = lng;
            } else if (lng < minLng) {
                minLng = lng;
            }
            if (lat > maxLat) {
                maxLat = lat;
            } else if (lat < minLat) {
                minLat = lat;
            }
        }
        log.info("minLat = " + minLat + ", maxLat = " + maxLat + ", minLng = " + minLng + ", maxLng = " + maxLng);
        return new GLatLng(minLat + ((maxLat - minLat) / 2), minLng + ((maxLng - minLng) / 2));
    }

    /**
     * 
     */
	protected IModel getPageTitle() {
		return new Model(Constants.SITE_NAME + " - " +new LabelStringDetachableModel("NEARBY_PLACES") + " - " +placeName);
	}
    
}//END OF FILE