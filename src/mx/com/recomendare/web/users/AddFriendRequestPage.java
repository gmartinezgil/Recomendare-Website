/**
 * 
 */
package mx.com.recomendare.web.users;

import java.io.Serializable;

import mx.com.recomendare.db.entities.User;
import mx.com.recomendare.db.entities.UserConfiguration;
import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.RequestsDAO;
import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.Index;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.UserConfigurationDetachableModel;
import mx.com.recomendare.web.session.AuthenticatedPage;
import mx.com.recomendare.web.session.SignInSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hibernate.Session;

/**
 * @author jerry
 *
 */
public final class AddFriendRequestPage extends AuthenticatedPage {
    private static final long serialVersionUID = 1L;

    //the log...
    private static final Log log = LogFactory.getLog(AddFriendRequestPage.class);

    private String userScreenName;
    private UserConfiguration userConfiguration;
    
    /**
     * 
     * @param parameters
     */
    public AddFriendRequestPage(PageParameters parameters) {
        super(parameters);
        userScreenName = parameters.getString("n");
        String userKey = parameters.getString("i");
        if(userKey != null && userKey.trim().length() > 0)  {
             IModel userConfigurationModel = new CompoundPropertyModel(new UserConfigurationDetachableModel(userKey));
            setModel(userConfigurationModel);
            userConfiguration = (UserConfiguration)getModelObject();
            init();
        }
        else  {
            //throw new UserConfigurationNotFound(userKey);
        }
    }

    private void init() {
        //TITLE
        add(new Label("addFriendRequestLabel", new LabelStringDetachableModel("ADD_AS_FRIEND")));
        //FORM
        add(new AddFriendRequestForm("addFriendRequestForm", new CompoundPropertyModel(new FriendRequestModel())));
    }

    /**
     * AddFriendRequestForm
     */
    final class AddFriendRequestForm extends Form {
        private static final long serialVersionUID = 1L;

        public AddFriendRequestForm(String id, IModel model) {
            super(id, model);
            init();
        }

        private void init() {
            //COMMENTS
            add(new Label("toFriendLabel", new LabelStringDetachableModel("COMMENTS_TO") +" "+ userScreenName));
            final TextField commentsToUser = new TextField("comments", new PropertyModel(getModelObject(), "comments"));
            add(commentsToUser);
            //BUTTONS
            final Button sendRequestButton = new Button("sendRequest", new LabelStringDetachableModel("SEND_REQUEST")) {
                private static final long serialVersionUID = 1L;
                public void onSubmit() {
                    FriendRequestModel model = (FriendRequestModel) getForm().getModelObject();
                    log.info(model);
                    SignInSession session = (SignInSession) getSession();

                    Session dbSession = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
                    RequestsDAO requestsDAO = ((Main) getApplication()).getServices().getDatabaseService().getRequestsDAO();
                    requestsDAO.setSession(dbSession);
                    requestsDAO.createFriendRequest(session.getUser().getScreenName(), (User)userConfiguration.getUsers().toArray()[0], (model.getComments() != null) ? model.getComments() : null);

                    setResponsePage(Index.class);
                }
            };
            add(sendRequestButton);
            final Button cancelButton = new Button("cancel", new LabelStringDetachableModel("CANCEL")) {
                private static final long serialVersionUID = 1L;
                public void onSubmit() {
                    setResponsePage(Index.class);
                }
            };
            cancelButton.setDefaultFormProcessing(false);
            add(cancelButton);
        }
    }

    /**
     *  FriendRequestModel
     */
    final class FriendRequestModel implements Serializable {
        private static final long serialVersionUID = 1L;
        
        //the comments...
        private String comments;

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }
    }

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new Model(Constants.SITE_NAME + " - " +new LabelStringDetachableModel("FRIEND_REQUEST"));
	}

	protected String getMetaDescription() {
		return "";
	}

	protected String getMetaKeywords() {
		return "";
	}
    
}//END OF FILE