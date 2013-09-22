/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import mx.com.recomendare.web.commons.models.ItemModel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * @author Jerry
 *
 */
public final class NavigationPanel extends Panel {
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param model
	 */
	public NavigationPanel(final String id, final IModel model) {
		super(id, model);
		init();
	}
	
	private void init()  {
		final ItemModel model = (ItemModel)getModelObject();
		//LOCATION
		final WebMarkupContainer locationContainer = new WebMarkupContainer("locationContainer");
		locationContainer.setVisible(false);
		if(model.getLocation() != null)  {
			locationContainer.add(new CountryCityNavigationPanel("countryCityNavigationPanel", new CompoundPropertyModel(model.getLocation())));
			locationContainer.setVisible(true);
		}
		add(locationContainer);
		add(new CategorySubcategoryNavigationPanel("categorySubcategoryNavigationPanel", new CompoundPropertyModel(model.getCategory())));
	}

}//END OF FILE