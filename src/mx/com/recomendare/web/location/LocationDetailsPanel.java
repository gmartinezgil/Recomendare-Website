/**
 * 
 */
package mx.com.recomendare.web.location;

import mx.com.recomendare.web.commons.models.LocationModel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author gerardomartinezgil
 *
 */
public final class LocationDetailsPanel extends Panel {
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param model
	 */
	public LocationDetailsPanel(String id, IModel model) {
		super(id, model);
		init();
	}

	private void init() {
		final LocationModel location = (LocationModel)getModelObject();
		add(new Label("name", location.getName()));
		add(new Label("street", location.getStreetName()));
		add(new Label("streetNumber", location.getStreetNumber()));
		add(new Label("zone", location.getZoneName()));
		add(new Label("city", location.getCityName()));
		add(new Label("country", location.getCountryName()));
	}
}