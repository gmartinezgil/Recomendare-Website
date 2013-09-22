/**
 * 
 */
package mx.com.recomendare.web.users;

import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.commons.models.UserModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.UserDetachableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * @author jerry
 *
 */
public final class ConfirmUserRegistrationPage extends BasePage {

	//the log...
	private static final Log log = LogFactory.getLog(ConfirmUserRegistrationPage.class);
	/**
	 * @param parameters
	 */
	public ConfirmUserRegistrationPage(final PageParameters parameters) {
		final String userKey = parameters.getString("i");
		log.info(userKey);
		if (userKey != null && userKey.trim().length() > 0) {
			int userId = Integer.parseInt(userKey);
			IModel userModel = new CompoundPropertyModel(new UserDetachableModel(userId, UserDetachableModel.DETAIL_MODE));
			setModel(userModel);
			init();
		} else {//TODO:send to an 404 custom page...
		}
	}
	
	private void init()  {
		UserModel model = (UserModel)getModelObject();
		add(new Label("titleLabel", new LabelStringDetachableModel("USER_CREATED_SUCESSFULLY")));
		add(new Label("messageLabel", new LabelStringDetachableModel("EMAIL_SENDED_TO_YOUR_ADDRESS_FOR_CONFIRMATION")));
		add(new Label("userName", model.getName()));
		add(new Label("userEmailAddress", model.getAddress().getEmail()));
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.BasePage#getPageTitle()
	 */
	protected IModel getPageTitle() {
		return new LabelStringDetachableModel("CONFIRM_REGISTRATION", LabelStringDetachableModel.TITLE_LABEL);
	}

	protected String getMetaDescription() {
		return "";
	}

	protected String getMetaKeywords() {
		return "";
	}

}//END OF FILE