/**
 * 
 */
package mx.com.recomendare.web.users;

import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * @author jerry
 *
 */
public final class AddNewUserWithWizardPage extends BasePage {
	
	public AddNewUserWithWizardPage()  {
		init();
	}
	
	private void init()  {
		//TITLE
    	add(new Label("createNewUserLabel", new LabelStringDetachableModel("CREATE_USER")));
    	//DIALOG
    	//the modal window...
    	final ModalWindow window = new ModalWindow("newUserWizardModalWindow");
    	window.setCookieName("userwizard-1");
    	window.setResizable(false);
    	window.setWidthUnit("em");
        window.setHeightUnit("em");
        window.setCssClassName(ModalWindow.CSS_CLASS_BLUE);
    	add(window);
    	//WIZARD
    	final AddNewUserWizard wizard = new AddNewUserWizard("addNewUserWizard", window);
    	wizard.setOutputMarkupId(true);
        add(wizard);
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.BasePage#getPageTitle()
	 */
	protected IModel getPageTitle() {
		return new LabelStringDetachableModel("CREATE_USER", LabelStringDetachableModel.TITLE_LABEL);
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