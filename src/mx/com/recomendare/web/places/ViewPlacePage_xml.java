/**
 * 
 */
package mx.com.recomendare.web.places;

import mx.com.recomendare.web.BasePage;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * @author jerry
 *
 */
public final class ViewPlacePage_xml extends BasePage {

	/**
	 * 
	 */
	public ViewPlacePage_xml() {
		init();
	}
	
	/**
	 * 
	 * @param parameters
	 */
	public ViewPlacePage_xml(PageParameters parameters) {
		init();
	}

	private void init()  {
		add(new Label("LABEL", "hola"));
	}

	/**
	 * 
	 */
	public String getMarkupType() {
		return "text/xml";
	}
	
	/**
	 * 
	 */
	protected IModel getPageTitle() {
		// TODO Auto-generated method stub
		return null;
	}

}//END OF FILE