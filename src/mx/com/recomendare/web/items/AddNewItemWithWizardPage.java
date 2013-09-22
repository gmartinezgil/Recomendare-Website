/**
 * 
 */
package mx.com.recomendare.web.items;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.session.AuthenticatedPage;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author jerry
 *
 */
public final class AddNewItemWithWizardPage extends AuthenticatedPage {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public AddNewItemWithWizardPage() {
		super();
		init();
	}
	
	private void init()  {
		//WIZARD
		add(new AddNewItemWizard("addNewItemWizard"));
	}

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new Model(Constants.SITE_NAME + " - " +new LabelStringDetachableModel("ADD_ITEM"));
	}

	/**
	 * 
	 */
	protected String getMetaDescription() {
		return "";
	}

	/**
	 * 
	 */
	protected String getMetaKeywords() {
		return "";
	}

}//END OF FILE