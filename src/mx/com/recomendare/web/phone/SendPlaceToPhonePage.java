/**
 * 
 */
package mx.com.recomendare.web.phone;

import mx.com.recomendare.web.session.AuthenticatedPage;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

/**
 * @author jerry
 *
 */
public final class SendPlaceToPhonePage extends AuthenticatedPage {

	/**
	 * @param parameters
	 */
	public SendPlaceToPhonePage(PageParameters parameters) {
		super(parameters);
		init();
	}

	private void init()  {
		/*WebClientInfo info = (WebClientInfo) getRequestCycle().getClientInfo();
		ClientProperties client = info.getProperties();
		if(	
			!client.isBrowserInternetExplorer() || 
			!client.isBrowserKonqueror() ||
			!client.isBrowserMozilla() ||
			!client.isBrowserMozillaFirefox() ||
			!client.isBrowserOpera() ||
			!client.isBrowserSafari()
			)  {
		}*/
		final DropDownChoice mobilePhoneBrand = new DropDownChoice("mobileBrand");
		final DropDownChoice mobilePhone = new DropDownChoice("mobilePhone");
		
	}

	protected IModel getPageTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getMetaDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getMetaKeywords() {
		// TODO Auto-generated method stub
		return null;
	}

}//END OF FILE