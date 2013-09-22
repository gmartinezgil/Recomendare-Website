/**
 * 
 */
package mx.com.recomendare.web.location;

import mx.com.recomendare.web.commons.components.SearchPanel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.session.SignInPage;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.session.SignOutPage;
import mx.com.recomendare.web.users.AddNewUserWithWizardPage;
import mx.com.recomendare.web.users.ViewUserProfilePage;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author jerry
 *
 */
public final class LocalizationPanel extends Panel {
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 */
	public LocalizationPanel(String id) {
		super(id);
		init();
	}
	
	private void init()  {
		//SESSION & LOCATION & WEATHER...
		//get the information...
		final SignInSession session = (SignInSession) getSession(); 
        //LOGIN STATUS
        add(new Label("welcomeLabel",  new LabelStringDetachableModel("WELCOME")));
        add(new Label("sessionStatusLabel", (session.isSignedIn()) ? session.getUser().getScreenName() : " "));
        Link logLink = null;
        Link accountLink = null;
        if(session.isSignedIn())  {
        	logLink = new Link("logLink") {
        		private static final long serialVersionUID = 1L;
        		public void onClick() {
        			setResponsePage(SignOutPage.class);
        		}
        	};
        	logLink.add(new Label("logLinkLabel", new LabelStringDetachableModel("LOGOUT")));
        	add(logLink);
        	accountLink = new Link("accountLink") {
        		private static final long serialVersionUID = 1L;
        		public void onClick() {
        			PageParameters parameters = new PageParameters();
        			parameters.put("i", session.getUser().getId());
        			parameters.put("n", session.getUser().getScreenName());
        			setResponsePage(ViewUserProfilePage.class, parameters);
        		}
        	};
        	accountLink.add(new Label("accountLinkLabel", new LabelStringDetachableModel("ACCOUNT")));
        	add(accountLink);
        }
        else  {
        	logLink = new Link("logLink") {
        		private static final long serialVersionUID = 1L;
        		public void onClick() {
        			setResponsePage(SignInPage.class);
        		}
        	};
        	logLink.add(new Label("logLinkLabel", new LabelStringDetachableModel("LOGIN")));
        	add(logLink);
        	accountLink = new Link("accountLink") {
        		private static final long serialVersionUID = 1L;
        		public void onClick() {
        			setResponsePage(AddNewUserWithWizardPage.class);
        		}
        	};
        	accountLink.add(new Label("accountLinkLabel", new LabelStringDetachableModel("ADD_USER")));
        	add(accountLink);
        }
      //SEARCH
    	add(new SearchPanel("searchPanel"));
	}

}//END OF FILE