/**
 * 
 */
package mx.com.recomendare.web.places;

import java.io.Serializable;
import java.util.Collections;

import mx.com.recomendare.services.eventful.EventfulModel;
import mx.com.recomendare.services.flickr.PhotoModel;
import mx.com.recomendare.services.opencellid.OpenCellIdModel;
import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.components.MessageOkDialogPanel;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.commons.models.SelectOption;
import mx.com.recomendare.web.commons.models.detachables.CarriersListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.CountriesListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import wicket.contrib.gmap.GMap2;
import wicket.contrib.gmap.api.GControl;
import wicket.contrib.gmap.api.GIcon;
import wicket.contrib.gmap.api.GLatLng;
import wicket.contrib.gmap.api.GMapType;
import wicket.contrib.gmap.api.GMarker;
import wicket.contrib.gmap.api.GMarkerOptions;
import wicket.contrib.gmap.api.GOverlay;
import wicket.contrib.gmap.api.GPoint;
import wicket.contrib.gmap.api.GSize;
import wicket.contrib.gmap.event.ClickListener;

/**
 * @author jerry
 *
 */
public final class ViewPlacesNearOfCellTowerPage extends BasePage {
	//the log...
	private static final Log log = LogFactory.getLog(ViewPlacesNearOfCellTowerPage.class);

	/**
	 * 
	 */
	public ViewPlacesNearOfCellTowerPage() {
		init();
	}
	
	private void init()  {
		//TITLE
		add(new Label("title", new LabelStringDetachableModel("PLACES_NEARBY_CELL")));
		add(new PlacesNearOfCellForm("placesNearOfCellForm", new CompoundPropertyModel(new PlacesNearOfCellFormModel())));
	}
	
	/**
	 * PlacesNearOfCellForm
	 */
	final class PlacesNearOfCellForm extends Form {
		private static final long serialVersionUID = 1L;
		//the carriers...
		private DropDownChoice carriers;
		//the map to watch nearby things...
		private GMap2 gMap;
		
		//TODO:you could make a location panel also...
		//the changing location...
		private NonCachingImage countryFlagImage;
		private NonCachingImage cityWeatherImage;
		private Label cityNameLabel;
		private Label countryNameLabel;
		private Label cityCurrentTemperature;
		
		private WebMarkupContainer nearByPicturesContainer; 
		private PageableListView nearByPictures;
		private WebMarkupContainer nearByEventsContainer;
		private PageableListView nearByEvents;
		//the location...
		private LocationModel location;
		
		//dialog...
		private ModalWindow locationNotFoundModalWindow;
		
		/**
		 * @param id
		 * @param model
		 */
		public PlacesNearOfCellForm(String id, IModel model) {
			super(id, model);
			init();
		}
		
		private void init()  {
			//location
			location = getLocation();
			//DIALOG
			locationNotFoundModalWindow = new ModalWindow("locationNotFoundModalWindow");
			locationNotFoundModalWindow.setCookieName("modal-1");
			locationNotFoundModalWindow.setResizable(false);
			locationNotFoundModalWindow.setInitialWidth(20);
			locationNotFoundModalWindow.setInitialHeight(10);
			locationNotFoundModalWindow.setWidthUnit("em");
			locationNotFoundModalWindow.setHeightUnit("em");
			locationNotFoundModalWindow.setCssClassName(ModalWindow.CSS_CLASS_BLUE);
			add(locationNotFoundModalWindow);
			//COUNTRY
	        add(new Label("countryName", new LabelStringDetachableModel("COUNTRY")));
	        final DropDownChoice countries = new DropDownChoice("countries", new PropertyModel(getModelObject(), "country"), new CountriesListDetachableModel(), WebUtil.KEYVALUE_CHOICE_RENDERER) {
	            private static final long serialVersionUID = 1L;
	            protected CharSequence getDefaultChoice(final Object selected) {
	                return "<option value=\"\"></option>";
	            }
	        };
	        countries.add(new AjaxFormComponentUpdatingBehavior("onchange") {
	            private static final long serialVersionUID = 1L;
	            protected void onUpdate(AjaxRequestTarget target) {
	                if (target != null) {
	                	PlacesNearOfCellFormModel model = (PlacesNearOfCellFormModel) getModelObject();
	                    carriers.setChoices(new CarriersListDetachableModel(model.getCountry().getKey()));
	                    target.addComponent(carriers);
	                }
	            }
	        });
	        countries.setRequired(true);
	        add(countries);
			//CARRIER
	        add(new Label("carrierName", new LabelStringDetachableModel("CARRIER")));
			carriers = new DropDownChoice("carriers", new PropertyModel(getModelObject(), "carrier"), Collections.EMPTY_LIST, WebUtil.KEYVALUE_CHOICE_RENDERER){
				private static final long serialVersionUID = 1L;
	            protected CharSequence getDefaultChoice(final Object selected) {
	                return "<option value=\"\"></option>";
	            }
			};
			carriers.setOutputMarkupId(true);
			add(carriers);
			//CELLID
			add(new Label("cellIdName", new LabelStringDetachableModel("CELL_ID")));
			add(new RequiredTextField("cellIdText", new PropertyModel(getModelObject(), "cellId")));
			//LAC
			add(new Label("lacName", new LabelStringDetachableModel("LAC")));
			add(new RequiredTextField("lacText", new PropertyModel(getModelObject(), "lac")));
			//BUTTON
			final AjaxButton updateLocationButton = new AjaxButton("updateLocationButton", this){
				private static final long serialVersionUID = 1L;
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					PlacesNearOfCellFormModel model = (PlacesNearOfCellFormModel)getForm().getModelObject();
					if(target != null)  {
						//TODO:have to ask to the database for the MCC and MNC...
						final OpenCellIdModel cellPositionModel = ((Main)getApplication()).getServices().getOpenCellIdService().getLocationFromCellPosition(model.getCellId(), model.getLac(), "334", "20");
						log.info(cellPositionModel);
						if(cellPositionModel != null && cellPositionModel.getLac().trim().length() > 0)  {
							location = ((Main)getApplication()).getServices().getGeoNamesService().getCountryInformationFromLocation(Float.parseFloat(cellPositionModel.getLat()), Float.parseFloat(cellPositionModel.getLon()), location.getLanguageCode());
							log.info(location);
							//update map position...
							gMap.removeAllOverlays();
							final GLatLng position = new GLatLng(location.getLatitude(), location.getLongitude());
							final GMarker center = new GMarker(position, new GMarkerOptions("Your position ;)"));
							gMap.addOverlay(center);
							gMap.getInfoWindow().open(position, new Label("yourPositionLabel", "Cell Tower - lat("+location.getLatitude()+"), lon("+location.getLongitude()+")"));
				            gMap.setCenter(position);
							//target.addComponent(gMap);
				            //update location...
				            countryFlagImage.setImageResource(WebUtil.getCountryFlagImageFromUserLocation(location.getCountryCode()));
				            target.addComponent(countryFlagImage);
				            countryNameLabel.setModel(new Model(location.getCountryName()));
				            target.addComponent(countryNameLabel);
				            cityNameLabel.setModel(new Model(location.getCityName()));
				            target.addComponent(cityNameLabel);
				            /*
				            final WeatherModel newWeather = WebUtil.getWeatherFromUserLocation(location);
				            cityWeatherImage.setImageResource(new BufferedDynamicImageResource() {
								private static final long serialVersionUID = 1L;
								protected byte[] getImageData() {
									return ((Main)getApplication()).getServices().getHttpService().getContenAsBytesFrom(newWeather.getURLForPicture());
								}
							});
				            target.addComponent(cityWeatherImage);
				            cityCurrentTemperature.setModel(new Model(newWeather.getTemperature()));
				            target.addComponent(cityCurrentTemperature);
				            */
				            nearByPictures.setList(((Main)getApplication()).getServices().getFlickrService().getPhotosFromLocation(location.getLatitude(), location.getLongitude(), 5));
				            target.addComponent(nearByPicturesContainer);
				            nearByEvents.setList(((Main)getApplication()).getServices().getEventfulService().getEventsFromPlace(location.getCityName() + "City", location.getCountryName(), null, null, null));
				            target.addComponent(nearByEventsContainer);
						}
						else {
							locationNotFoundModalWindow.setTitle(new LabelStringDetachableModel("LOCATION_NOT_FOUND"));
							locationNotFoundModalWindow.setContent(new MessageOkDialogPanel(locationNotFoundModalWindow.getContentId(),new LabelStringDetachableModel("LOCATION_NOT_FOUND"), locationNotFoundModalWindow));
							locationNotFoundModalWindow.show(target);
						}
					}
				}
			};
			updateLocationButton.setModel(new LabelStringDetachableModel("UPDATE"));
			add(updateLocationButton);
			//MAP
			gMap = new GMap2("gmap", Constants.GMAP_LOCALHOST_8080_KEY);
			gMap.setCenter(new GLatLng(location.getLatitude(), location.getLongitude()));
	        gMap.setZoom(Constants.STREET_VIEW_ZOOM_LEVEL);
	        gMap.addControl(GControl.GMapTypeControl);
	        gMap.addControl(GControl.GLargeMapControl);
	        gMap.setMapType(GMapType.G_NORMAL_MAP);
	        gMap.setOutputMarkupId(true);
	        //center...
	        final GMarker center = new GMarker(new GLatLng(location.getLatitude(), location.getLongitude()), new GMarkerOptions("Your position ;)"));
            gMap.addOverlay(center);
            //
            gMap.add(new ClickListener() {
    			private static final long serialVersionUID = 1L;
    			protected void onClick(AjaxRequestTarget target, GLatLng gLatLng, GOverlay overlay) {
    				if (gLatLng != null)	{
    					gMap.getInfoWindow().open(gLatLng, new Label("newPositionLabel", "lat("+gLatLng.getLat()+"), lon("+gLatLng.getLng()+")"));
    				}
    			}
    		});
	        add(gMap);
	        //country, city and flag...
			add(new Label("youAreAtLabel", new LabelStringDetachableModel("YOU_ARE_AT")));
			countryFlagImage = new NonCachingImage("myPositionCountryFlag", WebUtil.getCountryFlagImageFromUserLocation(location.getCountryCode()));
			countryFlagImage.setOutputMarkupId(true);
			add(countryFlagImage);
			countryNameLabel = new Label("myPositionCountryName", new Model(location.getCountryName()));
			countryNameLabel.setOutputMarkupId(true);
			add(countryNameLabel);
			add(new Label("cityOfLabel", new LabelStringDetachableModel("CITY_OF")));
			cityNameLabel = new Label("myPositionCityName", new Model(location.getCityName()));
			cityNameLabel.setOutputMarkupId(true);
			add(cityNameLabel);
			//weather
			/*
			final WeatherModel weather = WebUtil.getWeatherFromUserLocation(location);
			final ExternalLink externalWeatherLink = new ExternalLink("weatherExternalnfoLink", "http://www.weatherbug.com/");
			cityWeatherImage = new NonCachingImage("myPositionCurrentWeatherImage", new BufferedDynamicImageResource(){
				private static final long serialVersionUID = 1L;
				protected byte[] getImageData() {
					return ((Main)getApplication()).getServices().getHttpService().getContenAsBytesFrom(weather.getURLForPicture());
				}
			});
			cityWeatherImage.setOutputMarkupId(true);
			cityCurrentTemperature = new Label("myPositionCurrentWeatherLabel", new Model(weather.getTemperature()));
			cityCurrentTemperature.setOutputMarkupId(true);
			externalWeatherLink.add(cityWeatherImage);
			externalWeatherLink.add(cityCurrentTemperature);
	        add(externalWeatherLink);
	        */
	        //FLICKR PICTURES
	        add(new Label("nearByPicturesLabel", new LabelStringDetachableModel("PICTURES")));
	        nearByPictures = new PageableListView("nearByPictures", ((Main)getApplication()).getServices().getFlickrService().getPhotosFromLocation(location.getLatitude(), location.getLongitude(), 5), Constants.MAX_ITEMS_PER_PAGE) {
				private static final long serialVersionUID = 1L;
				protected void populateItem(ListItem item) {
					final PhotoModel photo = (PhotoModel)item.getModelObject();
					final Link viewFullPhotoImageLink = new Link("viewFullPhotoImageLink") {
						private static final long serialVersionUID = 1L;
						public void onClick() {
							final GLatLng photoPosition = new GLatLng(photo.getLatitude(), photo.getLongitude());
							final GIcon photoPositionIcon = new GIcon(urlFor(new ResourceReference(ViewPlacesNearOfCellTowerPage.class, "bandera-roja.png")).toString(), urlFor(new ResourceReference(ViewPlacesNearOfCellTowerPage.class, "sombra-bandera2.png")).toString()).iconSize(new GSize(17,17)).shadowSize(new GSize(22,18)).iconAnchor(new GPoint(11,16)).infoWindowAnchor(new GPoint(9, 2)).infoShadowAnchor(new GPoint(18, 25));
							final GMarker photoMarker = new GMarker(photoPosition, new GMarkerOptions(photo.getTitle(), photoPositionIcon));
							gMap.addOverlay(photoMarker);
							gMap.getInfoWindow().open(photoPosition, new Label("photoPositionLabel", photo.getTitle()+" - lat("+location.getLatitude()+"), lon("+location.getLongitude()+")"));
				            gMap.setCenter(photoPosition);
						}
					};
					final NonCachingImage viewFullPhotoImage = new NonCachingImage("viewFullPhotoImage", new BufferedDynamicImageResource(){
						private static final long serialVersionUID = 1L;
						protected byte[] getImageData() {
							return ((Main)getApplication()).getServices().getHttpService().getContenAsBytesFrom(photo.getThumbnailUrl());
						}
					});
					viewFullPhotoImageLink.add(new AttributeModifier("title", true, new Model(photo.getLargeUrl())));
					viewFullPhotoImageLink.add(viewFullPhotoImage);
					viewFullPhotoImageLink.add(new Label("viewFullPhotoImageTitle", photo.getTitle()));
					item.add(viewFullPhotoImageLink);
				}
	        };
	        nearByPicturesContainer = new WebMarkupContainer("nearByPicturesContainer");
	        nearByPicturesContainer.add(nearByPictures);
	        nearByPicturesContainer.setOutputMarkupId(true);
	        add(nearByPicturesContainer);
	        //EVENTS
	        add(new Label("nearByEventsLabel", new LabelStringDetachableModel("EVENTS")));
	        nearByEvents = new PageableListView("nearByEvents", ((Main)getApplication()).getServices().getEventfulService().getEventsFromPlace(location.getCityName() + " City", location.getCountryName(), null, null, null), 5){
				private static final long serialVersionUID = 1L;
				protected void populateItem(ListItem item) {
					final EventfulModel event = (EventfulModel)item.getModelObject();
					final Link viewEventLink = new Link("viewEventLink"){
						private static final long serialVersionUID = 1L;
						public void onClick() {
							
						}
					};//, event.getURL(), event.getTitle()
					if(event.getImageURL() != null)  {
						final NonCachingImage viewEventLinkImage = new NonCachingImage("viewEventLinkImage", new BufferedDynamicImageResource(){
							private static final long serialVersionUID = 1L;
							protected byte[] getImageData() {
								return ((Main)getApplication()).getServices().getHttpService().getContenAsBytesFrom(event.getImageURL());
							}
						});
						viewEventLink.add(viewEventLinkImage);
					}
					else  {
						viewEventLink.add(new Image("viewEventLinkImage", WebUtil.PICTURE_EMPTY_IMAGE));
					}
					viewEventLink.add(new AttributeModifier("title", true, new Model(event.getURL())));
					item.add(viewEventLink);
					final Label eventDescription = new Label("eventDescriptionLabel", event.getDescription());
					eventDescription.setEscapeModelStrings(false);
					item.add(eventDescription);
				}
	        };
	        nearByEventsContainer = new WebMarkupContainer("nearByEventsContainer");
	        nearByEventsContainer.add(nearByEvents);
	        nearByEventsContainer.setOutputMarkupId(true);
	        add(nearByEventsContainer);
		}

	}
	
	/**
	 * PlacesNearOfCellFormModel
	 */
	final class PlacesNearOfCellFormModel implements Serializable {
		private static final long serialVersionUID = 1L;
		private SelectOption country;
		private SelectOption carrier;
		private String cellId;
		private String lac;
		private boolean hexadecimal;
		
		public SelectOption getCountry() {
			return country;
		}
		public void setCountry(SelectOption country) {
			this.country = country;
		}
		public SelectOption getCarrier() {
			return carrier;
		}
		public void setCarrier(SelectOption carrier) {
			this.carrier = carrier;
		}
		public String getCellId() {
			return cellId;
		}
		public void setCellId(String cellId) {
			this.cellId = cellId;
		}
		public String getLac() {
			return lac;
		}
		public void setLac(String lac) {
			this.lac = lac;
		}
		public boolean isHexadecimal() {
			return hexadecimal;
		}
		public void setHexadecimal(boolean hexadecimal) {
			this.hexadecimal = hexadecimal;
		}

	}

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new LabelStringDetachableModel("PLACES_NEARBY_CELL", LabelStringDetachableModel.TITLE_LABEL);
	}

}//END OF FILE