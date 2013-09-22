/**
 * 
 */
package mx.com.recomendare.web.location;

import java.io.Serializable;
import java.util.Collections;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.Index;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.commons.models.SelectOption;
import mx.com.recomendare.web.commons.models.detachables.CitiesSelectOptionListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.CountriesListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LanguagesSelectOptionListDetachableModel;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
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
import wicket.contrib.gmap.api.GLatLng;
import wicket.contrib.gmap.api.GMapType;
import wicket.contrib.gmap.api.GMarker;

/**
 * @author jerry
 *
 */
public final class SetPreferedLocationPage extends BasePage {
	//the map where it's located the user...
	private GMap2 gMap;
	
	private final LocationModel location = WebUtil.getUserActualLocation(((SignInSession)getSession())); 
	/**
	 * 
	 */
	public SetPreferedLocationPage() {
		super();
		init();
	}

	private void init()  {
		//TITLE
		add(new Label("changePreferedLocationLabel", new LabelStringDetachableModel("SET_LOCATION")));
		//MAP
		gMap = new GMap2("gmap", Constants.GMAP_LOCALHOST_8080_KEY);
		gMap.setZoom(Constants.STREET_VIEW_ZOOM_LEVEL);
		gMap.addControl(GControl.GMapTypeControl);
		gMap.addControl(GControl.GLargeMapControl);
		gMap.setMapType(GMapType.G_NORMAL_MAP);
		//the center it's the client's location...
		final GMarker center = new GMarker(new GLatLng(location.getLatitude(), location.getLongitude()));
		gMap.setCenter(new GLatLng(location.getLatitude(), location.getLongitude()));
		gMap.addOverlay(center);
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

		//city field...
		private DropDownChoice city;	//i.e. San Francisco/Ciudad de Mexico

		public SetLocationForm(String id, IModel model) {
			super(id, model);
			init();
		}

		private void init()  {
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
			//CITY
			add(new Label("cityName", new LabelStringDetachableModel("CITY")));
			city = new DropDownChoice("city", new PropertyModel(getModelObject(), "city"), Collections.EMPTY_LIST, WebUtil.KEYVALUE_CHOICE_RENDERER) {
				private static final long serialVersionUID = 1L;
				protected CharSequence getDefaultChoice(final Object selected) {
					return "<option value=\"\"></option>";
				}
			};
			city.setRequired(true);
			city.setOutputMarkupId(true);
			add(city);
			//LANGUAGE
			add(new Label("languageLabel", new LabelStringDetachableModel("LANGUAGE")));
			final DropDownChoice language = new DropDownChoice("language", new PropertyModel(getModelObject(), "language"), new LanguagesSelectOptionListDetachableModel(), WebUtil.KEYVALUE_CHOICE_RENDERER){
				private static final long serialVersionUID = 1L;
				protected CharSequence getDefaultChoice(final Object selected) {
					return "<option value=\"\"></option>";
				}
			};
			add(language);
			//STREET
			add(new Label("streetName", new LabelStringDetachableModel("STREET")));
			final RequiredTextField street = new RequiredTextField("street", new PropertyModel(getModelObject(), "street"));
			/*
			final IAutoCompleteRenderer informativeRenderer = new AbstractAutoCompleteRenderer() {
				private static final long serialVersionUID = 1L;
				protected String getTextValue(Object object) {
					return ((Street) object).getName();
				}
				protected void renderChoice(Object object, Response response, String arg2) {
					response.write("<div style='float:left; color:orange;'>");
					response.write(((Street) object).getName());
					response.write("</div>");
				}
			};
			street.add(new AutoCompleteBehavior(informativeRenderer) {
				private static final long serialVersionUID = 1L;
				protected Iterator getChoices(String input) {
					List choicesToBeRendered = new ArrayList();
					//TODO:should be only initialized once...if not there are many database sessions hanging around...
					Session dbSession = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
					StreetsDAO streetsDAO = ((Main) getApplication()).getServices().getDatabaseService().getStreetsDAO();
					streetsDAO.setSession(dbSession);
					List streets = streetsDAO.getStreetsByCityId(location.getCityId());
					if (streets != null) {
						final Iterator iterator = streets.iterator();
						while (iterator.hasNext()) {
							Street street = (Street) iterator.next();
							if (street.getName().startsWith(input.toLowerCase().trim())) {
								choicesToBeRendered.add(street);
							}
						}
					}
					return choicesToBeRendered.iterator();
				}
			});
			*/
			street.add(StringValidator.maximumLength(30));
			add(street);
			final AjaxButton updateLocationButton = new AjaxButton("updateLocationButton", this){
				private static final long serialVersionUID = 1L;
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					LocationFormModel model = (LocationFormModel)getForm().getModelObject();
					LocationModel location = ((Main)getApplication()).getServices().getGeoCoderService().getLocationFor(model.getStreet(), model.getCity().getValue(), model.getCountry().getValue());
					if(location != null)  {
						if(target != null)  {
							//gMap.getOverlays().clear();
							GMarker center = new GMarker(new GLatLng(location.getLatitude(), location.getLongitude()));
							gMap.addOverlay(center);
							gMap.setCenter(new GLatLng(location.getLatitude(), location.getLongitude()));
						}
					}
				}
			};
			updateLocationButton.setModel(new LabelStringDetachableModel("UPDATE"));
			add(updateLocationButton);
			final Button saveLocationButton = new Button("saveLocationButton", new LabelStringDetachableModel("SAVE")){
				private static final long serialVersionUID = 1L;
				public void onSubmit() {
					//TODO:save all the reference of the location on the session and update in the database...and return to the home page...
				}
			};
			add(saveLocationButton);
			final Button cancelButton = new Button("cancelButton", new LabelStringDetachableModel("CANCEL")){
				private static final long serialVersionUID = 1L;
				public void onSubmit() {
					setResponsePage(Index.class);
				}
			};
			cancelButton.setDefaultFormProcessing(false);
			add(cancelButton);
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
		private SelectOption language;
		private String street;

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

		public SelectOption getLanguage() {
			return language;
		}
		public void setLanguage(SelectOption language) {
			this.language = language;
		}
		
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			sb.append(getClass().getName());
			sb.append("] - {country = '");
			sb.append(country);
			sb.append("', city = '");
			sb.append(city);
			sb.append("', language = '");
			sb.append(language);
			sb.append("', street = '");
			sb.append(street);
			sb.append("'}");
			return sb.toString();
		}

	}

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new Model(Constants.SITE_NAME + " - " +new LabelStringDetachableModel("SET_LOCATION"));
	}

	protected String getMetaDescription() {
		return "";
	}

	protected String getMetaKeywords() {
		return "";
	}

}//END OF FILE