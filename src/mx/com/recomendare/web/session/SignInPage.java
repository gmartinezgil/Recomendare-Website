package mx.com.recomendare.web.session;

import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * This class presents to the user the sign in panel to authenticate the user...
 * TODO: should be used the panel in all the pages until it's already signed... 
 * @author jerry
 *
 */
public final class SignInPage extends BasePage {
    private static final long serialVersionUID = 1L;

    public SignInPage() {
        super();
        init();
    }

    private void init() {
    	//TITLE
    	add(new Label("titleLabel", new LabelStringDetachableModel("SIGNIN")));
    	//SIGNIN PANEL
        add(new SignInPanel("signInPanel") {
            private static final long serialVersionUID = 1L;
            public boolean signIn(String username, String password) {
                return ((SignInSession) getSession()).authenticate(username, password);
            }
        });
    }

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new LabelStringDetachableModel("SIGNIN", LabelStringDetachableModel.TITLE_LABEL);
	}

	protected String getMetaDescription() {
		return "";
	}

	protected String getMetaKeywords() {
		return "";
	}
    
}//END OF FILE