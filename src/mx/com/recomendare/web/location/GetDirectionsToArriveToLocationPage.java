/**
 * 
 */
package mx.com.recomendare.web.location;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.components.ShowLocationsDialogPanel;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.commons.models.SelectOption;
import mx.com.recomendare.web.commons.models.detachables.CitiesSelectOptionListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.CountriesListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.items.AddNewItemWizard;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import wicket.contrib.gmap.GMap2;
import wicket.contrib.gmap.api.GControl;
import wicket.contrib.gmap.api.GIcon;
import wicket.contrib.gmap.api.GLatLng;
import wicket.contrib.gmap.api.GMapType;
import wicket.contrib.gmap.api.GMarker;
import wicket.contrib.gmap.api.GMarkerOptions;
import wicket.contrib.gmap.api.GOverlay;
import wicket.contrib.gmap.api.GPoint;
import wicket.contrib.gmap.api.GPolyline;
import wicket.contrib.gmap.api.GSize;

import com.cloudmade.api.geometry.Point;
import com.cloudmade.api.routing.Route;

/**
 * @author jerry
 *
 */
public final class GetDirectionsToArriveToLocationPage extends BasePage {
	//the map where it's located the user...
	private GMap2 gMap;
	//street
	private RequiredTextField street;
	//zone
	private RequiredTextField zone;
	//postal code
	private RequiredTextField postalCode; 
	//dialog...
	private ModalWindow locationsModalWindow;
	/**
	 * 
	 */
	public GetDirectionsToArriveToLocationPage() {
		init();
	}
	
	private void init()  {
		//TITLE
		add(new Label("changePreferedLocationLabel", new LabelStringDetachableModel("GET_DIRECTIONS")));
		//MAP
		gMap = new GMap2("gmap", Constants.GMAP_LOCALHOST_8080_KEY);
		gMap.setZoom(Constants.CITY_VIEW_ZOOM_LEVEL);
		gMap.addControl(GControl.GMapTypeControl);
		gMap.addControl(GControl.GLargeMapControl);
		gMap.setMapType(GMapType.G_NORMAL_MAP);
		//the center it's the client's location...
		gMap.setCenter(new GLatLng(getLocation().getLatitude(), getLocation().getLongitude()));
		gMap.setOutputMarkupId(true);
		add(gMap);
		//FORM
		add(new SetLocationForm("setLocationForm", new CompoundPropertyModel(new LocationFormModel())));
	}

	/**
	 * SetLocationForm
	 * 
	 */
	final class SetLocationForm extends Form  {
		private static final long serialVersionUID = 1L;

		public SetLocationForm(String id, IModel model) {
			super(id, model);
			init();
		}

		private void init()  {
			final LocationModel location = getLocation();
			LocationFormModel model = (LocationFormModel) getModelObject();
			model.setCountry(new SelectOption(String.valueOf(location.getCountryId()), location.getCountryName())); //yes, it works... :)
            model.setCity(new SelectOption(String.valueOf(location.getCityId()), location.getCityName()));
            //DIALOG
    		locationsModalWindow = new ModalWindow("locationsModalWindow");
    		locationsModalWindow.setCookieName("location-1");
    		locationsModalWindow.setResizable(false);
    		locationsModalWindow.setInitialWidth(60);
    		locationsModalWindow.setInitialHeight(8);
    		locationsModalWindow.setWidthUnit("em");
    		locationsModalWindow.setHeightUnit("em");
    		locationsModalWindow.setCssClassName(ModalWindow.CSS_CLASS_BLUE);
    		locationsModalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback(){
    			private static final long serialVersionUID = 1L;
    			public void onClose(AjaxRequestTarget target) {
    				final LocationModel foundedLocation = (LocationModel)locationsModalWindow.getModelObject();
    				LocationFormModel model = (LocationFormModel) getModelObject();
    				model.setLatitude(foundedLocation.getLatitude());
    				model.setLongitude(foundedLocation.getLongitude());
    				street.setModel(new Model(foundedLocation.getStreetName()));
    				target.addComponent(street);
    				zone.setModel(new Model(foundedLocation.getZoneName()));
    				target.addComponent(zone);
    				postalCode.setModel(new Model(foundedLocation.getPostalCodeNumber()));
    				target.addComponent(postalCode);
    				GOverlay center = getCurrentPositionOverlay(foundedLocation.getLatitude(), foundedLocation.getLongitude(), model.getStreet());
    				gMap.setOverlays(Collections.singletonList(center));
    				gMap.setCenter(new GLatLng(foundedLocation.getLatitude(), foundedLocation.getLongitude()));
    				gMap.setZoom(Constants.STREET_VIEW_ZOOM_LEVEL);
    			}
    		});
    		add(locationsModalWindow);
			//CITY
			add(new Label("cityName", new LabelStringDetachableModel("CITY")));
			final DropDownChoice city = new DropDownChoice("city", new PropertyModel(getModelObject(), "city"), new CitiesSelectOptionListDetachableModel(model.getCountry().getKey()), WebUtil.KEYVALUE_CHOICE_RENDERER) {
				private static final long serialVersionUID = 1L;
				protected CharSequence getDefaultChoice(final Object selected) {
					return "<option value=\"\"></option>";
				}
			};
			city.setRequired(true);
			city.setOutputMarkupId(true);
			add(city);
			//COUNTRY
			add(new Label("countryName", new LabelStringDetachableModel("COUNTRY")));
			final DropDownChoice country = new DropDownChoice("country", new PropertyModel(getModelObject(), "country"), new CountriesListDetachableModel(), WebUtil.KEYVALUE_CHOICE_RENDERER) {
				private static final long serialVersionUID = 1L;
				protected CharSequence getDefaultChoice(final Object selected) {
					return "<option value=\"\"></option>";
				}
			};
			country.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;
				protected void onUpdate(AjaxRequestTarget target) {
					if (target != null) {
						LocationFormModel model = (LocationFormModel) getModelObject();
						city.setChoices(new CitiesSelectOptionListDetachableModel(model.getCountry().getKey()));
						target.addComponent(city);
					}
				}
			});
			country.setRequired(true);
			add(country);
			//STREET
			add(new Label("streetName", new LabelStringDetachableModel("STREET")));
			street = new RequiredTextField("street", new PropertyModel(getModelObject(), "street"));
			street.add(new AjaxFormComponentUpdatingBehavior("onblur"){
				private static final long serialVersionUID = 1L;
				protected void onUpdate(AjaxRequestTarget target) {
					LocationFormModel model = (LocationFormModel) getModelObject();
					final String streetName = model.getStreet();
					final String streetNumber = model.getStreetNumber();
					if(streetName != null && streetName.trim().length() > 0)  {
						final String cityName = model.getCity().getValue();
						final String countryName = model.getCountry().getValue();
						List<LocationModel> locations = ((Main)getApplication()).getServices().getGeoCoderService().getMultipleLocationsFor(streetNumber, streetName, cityName, countryName);
						if(locations != null && locations.size() > 0)  {
							if(locations.size() == 1)  {
								if(target != null)  {
									updateTarget(model, locations.get(0), target);
								}
							}
							else  {
								if(target != null)  {
									locationsModalWindow.setTitle(new LabelStringDetachableModel("LOCATIONS_FOUNDED"));
									locationsModalWindow.setContent(new ShowLocationsDialogPanel(locationsModalWindow.getContentId(), new CompoundPropertyModel(locations), locationsModalWindow));
									locationsModalWindow.show(target);
								}
							}
						}
					}
				}
            });
			street.add(StringValidator.maximumLength(30));
			street.setOutputMarkupId(true);
			add(street);
			//STREET NUMBER
            add(new Label("streetNumberName", "#"));
            final RequiredTextField streetNumber = new RequiredTextField("streetNumber", new PropertyModel(getModelObject(), "streetNumber"));
            streetNumber.add(new AjaxFormComponentUpdatingBehavior("onblur"){
				private static final long serialVersionUID = 1L;
				protected void onUpdate(AjaxRequestTarget target) {
					LocationFormModel model = (LocationFormModel) getModelObject();
					final String streetName = model.getStreet();
					final String streetCapturedNumber = model.getStreetNumber();
					if(streetName != null && streetName.trim().length() > 0)  {
						final String cityName = model.getCity().getValue();
						final String countryName = model.getCountry().getValue();
						List<LocationModel> locations = ((Main)getApplication()).getServices().getGeoCoderService().getMultipleLocationsFor(streetCapturedNumber, streetName, cityName, countryName);
						if(locations != null && locations.size() > 0)  {
							if(locations.size() == 1)  {
								if(target != null)  {
									updateTarget(model, locations.get(0), target);
								}
							}
							else  {
								if(target != null)  {
									locationsModalWindow.setTitle(new LabelStringDetachableModel("LOCATIONS_FOUNDED"));
									locationsModalWindow.setContent(new ShowLocationsDialogPanel(locationsModalWindow.getContentId(), new CompoundPropertyModel(locations), locationsModalWindow));
									locationsModalWindow.show(target);
								}
							}
						}
					}
				}
            });
            streetNumber.setOutputMarkupId(true);
            add(streetNumber);
            //ZONE
            add(new Label("zoneName", new LabelStringDetachableModel("ZONE")));
            zone = new RequiredTextField("zone", new PropertyModel(getModelObject(), "zone"));
            zone.setEnabled(false);
            zone.setOutputMarkupId(true);
            add(zone);
            //POSTAL CODE
            add(new Label("postalCodeName", new LabelStringDetachableModel("POSTAL_CODE")));
            postalCode = new RequiredTextField("postalCode", new PropertyModel(getModelObject(), "postalCode"));
            postalCode.setEnabled(false);
            postalCode.setOutputMarkupId(true);
            add(postalCode);
			//CALCULATE_RUTE_BUTTON
			final AjaxButton updateLocationButton = new AjaxButton("updateLocationButton", this) {
				private static final long serialVersionUID = 1L;
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					final LocationModel location = getLocation();
					final LocationFormModel model = (LocationFormModel)getForm().getModelObject();
					final Route route = ((Main)getApplication()).getServices().getCloudMadeGeoService().getRouteFromStartEndPoints(
																															model.getLatitude(), 
																															model.getLongitude(), 
																															location.getLatitude(), 
																															location.getLongitude(), 
																															location.getLanguageCode(), 
																															(short)0, 
																															(short)0
																															);
					if(route != null)  {
						if(target != null)  {//TODO:show the progress bar...when updating this...
							GLatLng[] gLatLngs = new GLatLng[route.geometry.points.size()];
							int i = 0;
							for (final Iterator<Point> iterator = route.geometry.points.iterator(); iterator.hasNext();) {
								Point point = (Point)iterator.next();
								gLatLngs[i] = new GLatLng(point.lat, point.lon);
								i++;
							}
							final GPolyline polyline = new GPolyline("#fb0303", 7, 1.0f, gLatLngs);
							gMap.addOverlay(polyline);
							final GOverlay destination = getCurrentPositionOverlay((float)gLatLngs[i - 1].getLat(), (float)gLatLngs[i - 1].getLng(), "end");
							gMap.addOverlay(destination);
							gMap.setZoom(Constants.CITY_VIEW_ZOOM_LEVEL);
						}
					}
					else  {
						if(target != null)  {
							//finish it..
							target.appendJavascript("alert('NO_ROUTE_FOUNDED!')");
						}
					}
				}
			};
			updateLocationButton.setModel(new LabelStringDetachableModel("CALCULATE_ROUTE"));
			add(updateLocationButton);
		}

		//updates the target and the form model upon selection on combo box...
		private void updateTarget(LocationFormModel model, final LocationModel foundedLocation, AjaxRequestTarget target)  {
			model.setLatitude(foundedLocation.getLatitude());
			model.setLongitude(foundedLocation.getLongitude());
			street.setModel(new Model(foundedLocation.getStreetName()));
			target.addComponent(street);
			zone.setModel(new Model(foundedLocation.getZoneName()));
			target.addComponent(zone);
			postalCode.setModel(new Model(foundedLocation.getPostalCodeNumber()));
			target.addComponent(postalCode);
			final GOverlay center = getCurrentPositionOverlay(foundedLocation.getLatitude(), foundedLocation.getLongitude(), model.getStreet());
			gMap.setOverlays(Collections.singletonList(center));
			gMap.setCenter(new GLatLng(foundedLocation.getLatitude(), foundedLocation.getLongitude()));
			gMap.setZoom(Constants.STREET_VIEW_ZOOM_LEVEL);
		}
		
		//gets a new overlay picture for the location passed on...
		private GOverlay getCurrentPositionOverlay(final float latitude, final float longitude, final String title)  {
			final GIcon icon = new GIcon(urlFor(new ResourceReference(AddNewItemWizard.class, "image2.gif"))
					.toString(), urlFor(new ResourceReference(AddNewItemWizard.class, "shadow2.png"))
					.toString()).iconSize(new GSize(64, 64)).shadowSize(new GSize(64, 64)).iconAnchor(
					new GPoint(19, 40)).infoWindowAnchor(new GPoint(9, 2)).infoShadowAnchor(
					new GPoint(18, 25));
			final GMarkerOptions options = new GMarkerOptions(title, icon).draggable(true);
			final GLatLng latLng = new GLatLng(latitude, longitude); 
			return new GMarker(latLng, options);
		}
	}

	/**
	 *LocationFormModel
	 *
	 */
	final class LocationFormModel implements Serializable {
		private static final long serialVersionUID = 1L;
		private SelectOption country;
		private SelectOption city;
		private String street;
		private String streetNumber;
		private float latitude;
		private float longitude;
		private String zone;
		private String postalCode;

		public SelectOption getCountry() {
			return country;
		}
		public void setCountry(SelectOption country) {
			this.country = country;
		}

		public void setCity(SelectOption city) {
			this.city = city;
		}
		public SelectOption getCity() {
			return city;
		}

		public String getStreet() {
			return street;
		}
		public void setStreet(String street) {
			this.street = street;
		}

		public float getLatitude() {
			return latitude;
		}
		public void setLatitude(float latitude) {
			this.latitude = latitude;
		}
		public float getLongitude() {
			return longitude;
		}
		public void setLongitude(float longitude) {
			this.longitude = longitude;
		}
		public String getStreetNumber() {
			return streetNumber;
		}
		public void setStreetNumber(String streetNumber) {
			this.streetNumber = streetNumber;
		}
		public String getZone() {
			return zone;
		}
		public void setZone(String zone) {
			this.zone = zone;
		}
		public String getPostalCode() {
			return postalCode;
		}
		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			sb.append(getClass().getName());
			sb.append("] - {country = '");
			sb.append(country);
			sb.append("', city = '");
			sb.append(city);
			sb.append("', street = '");
			sb.append(street);
			sb.append("'}");
			return sb.toString();
		}

	}


	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.BasePage#getMetaDescription()
	 */
	protected String getMetaDescription() {
		return "";
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.BasePage#getMetaKeywords()
	 */
	protected String getMetaKeywords() {
		return "";
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.BasePage#getPageTitle()
	 */
	protected IModel getPageTitle() {
		return new LabelStringDetachableModel("GET_DIRECTIONS", LabelStringDetachableModel.TITLE_LABEL);
	}

}//END OF FILE