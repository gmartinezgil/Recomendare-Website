package mx.com.recomendare.web.session;

import mx.com.recomendare.web.BasePage;

import org.apache.wicket.PageParameters;

/**
 * Base class for all the pages that need authentication...I think... :)
 * ALL the pages that need to be authenticated against the user session should extend this page...
 * The mechanism it's already implemented in the Main application... 
 * @author jerry
 *
 */
public abstract class AuthenticatedPage extends BasePage {
    private static final long serialVersionUID = 1L;

    public AuthenticatedPage(PageParameters parameters) {
        super(parameters);
    }

    public AuthenticatedPage() {
        super();
    }
    
}//END OF FILE
