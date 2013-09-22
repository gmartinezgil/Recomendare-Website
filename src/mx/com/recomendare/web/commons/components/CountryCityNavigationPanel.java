/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.countries.ViewCityPage;
import mx.com.recomendare.web.countries.ViewCountryPage;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author jerry
 *
 */
public final class CountryCityNavigationPanel extends Panel {
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param model
	 */
	public CountryCityNavigationPanel(String id, IModel model) {
		super(id, model);
		init();
	}
	
	private void init()  {
		final LocationModel model = (LocationModel)getModelObject();
		//COUNTRY
		PageParameters parameters = new PageParameters();
		parameters.put("i", model.getCountryId());
		parameters.put("n", model.getCountryName());
		final BookmarkableStatisticsLink viewCountryLink = new BookmarkableStatisticsLink("viewCountryLink", ViewCountryPage.class, parameters);
		viewCountryLink.add(new Image("viewCountryLinkImage", WebUtil.getCountryFlagImageFromUserLocation(model.getCountryCode())));
		viewCountryLink.add(new Label("viewCountryLinkName", model.getCountryName()));
		add(viewCountryLink);
		//CITY
		parameters = new PageParameters();
		parameters.put("i", model.getCityId());
		parameters.put("n", model.getCityName());
		final BookmarkableStatisticsLink viewCityLink = new BookmarkableStatisticsLink("viewCityLink", ViewCityPage.class, parameters); 
		viewCityLink.add(new Label("viewCityLinkName", model.getCityName()));
		add(viewCityLink);
	}

}//END OF FILE