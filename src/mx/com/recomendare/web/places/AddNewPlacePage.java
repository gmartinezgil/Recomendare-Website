package mx.com.recomendare.web.places;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.session.AuthenticatedPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * 
 * @author jerry
 *
 */
public final class AddNewPlacePage extends AuthenticatedPage {

    private static final long serialVersionUID = 1L;

    public AddNewPlacePage() {
        super();
        init();
    }

    private void init() {
    	//TITLE
    	add(new Label("addNewPlaceTitleLabel", new LabelStringDetachableModel("ADD_PLACE")));
        //WIZARD
        add(new NewPlaceWizard("addNewPlaceWizard"));
    }

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new Model(Constants.SITE_NAME + " - " +new LabelStringDetachableModel("ADD_PLACE"));
	}

}//END OF FILE