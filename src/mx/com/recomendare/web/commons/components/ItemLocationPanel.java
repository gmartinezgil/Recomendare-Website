/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.places.ViewAnotherPlacesNearOfPage;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author jerry
 *
 */
public final class ItemLocationPanel extends Panel {
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param model
	 */
	public ItemLocationPanel(String id, IModel model) {
		super(id, model);
		init();
	}
	
	private void init()  {
		final ItemModel model = (ItemModel)getModelObject();
		//LOCATION
		add(new Label("locationLabel", new LabelStringDetachableModel("LOCATION")));
		add(new Label("addressLabel", new LabelStringDetachableModel("ADDRESS")));
		//TODO: it should be called an image from the data base...
		final Image googleStaticPlaceMapImage = new Image("mapLocationImage", WebUtil.getStaticMapImageResource(
																						model.getLocation().getLatitude(), 
																						model.getLocation().getLongitude(), 
																						Constants.STREET_VIEW_ZOOM_LEVEL, 
																						255, 
																						300
																						));
		final Link viewOnInteractiveMapLink = new Link("viewOnInteractiveMapLink"){
			private static final long serialVersionUID = 1L;
			public void onClick() {
				PageParameters parameters = new PageParameters();
				parameters.put("i", model.getId());
				parameters.put("n", model.getName());
				setResponsePage(ViewAnotherPlacesNearOfPage.class, parameters);
			}
		};
		viewOnInteractiveMapLink.add(googleStaticPlaceMapImage);
		add(viewOnInteractiveMapLink);
		final Link viewAnotherItemsNearOfThisLink = new Link("viewAnotherItemsNearOfThisLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				PageParameters parameters = new PageParameters();
				parameters.put("i", model.getId());
				parameters.put("n", model.getName());
				setResponsePage(ViewAnotherPlacesNearOfPage.class, parameters);
			}
		};
		viewAnotherItemsNearOfThisLink.add(new Image("viewAnotherItemsNearOfThisLinkImage", WebUtil.NEARBY_PLACES_MAP_IMAGE));
		viewAnotherItemsNearOfThisLink.add(new Label("viewAnotherItemsNearOfThisLinkLabel", new LabelStringDetachableModel("VIEW_NEARBY_PLACES")));
		add(viewAnotherItemsNearOfThisLink);
		//ADRESS...
		add(new Label("streetOfLabel", new LabelStringDetachableModel("STREET_OF")));
		add(new Label("streetName",model.getAddress().getStreetName()));
		add(new Label("streetNumberLabel", new LabelStringDetachableModel("NUMBER")));
		add(new Label("streetNumber", model.getAddress().getStreetOutsideNumber()));
		add(new Label("phoneNumberLabel", new LabelStringDetachableModel("PHONE_NUMBER")));
		add(new Label("phoneNumber", model.getAddress().getTelephoneNumber()));
		add(new Label("addressCityOfLabel", new LabelStringDetachableModel("CITY_OF")));
		add(new Label("addressCityName", model.getLocation().getCityName()));//new PropertyModel(getModel(), "location.cityName")
		add(new Label("addressCountryName", model.getLocation().getCountryName()));//new PropertyModel(getModel(), "location.countryName")
		//HOMEPAGE
		//TODO:still you have to get statistics from here...also...use a normal Link...
		final ExternalLink internetAddressLink = new ExternalLink("homePageAddressLink", model.getAddress().getHomePageURL());
		internetAddressLink.add(new Label("homePageAddressLinkLabel", new LabelStringDetachableModel("OFFICIAL_SITE")));
		internetAddressLink.add(new Image("homePageAddressLinkImage", WebUtil.GO_TO_PAGE_IMAGE));
		internetAddressLink.add(new AttributeModifier("title", true, new Model(model.getAddress().getHomePageURL())));
		add(internetAddressLink);
	}

}//END OF FILE