package mx.com.recomendare.web.session;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public final class SignOutPage extends BasePage {
	private static final long serialVersionUID = 1L;

	public SignOutPage() {
		super();
		init();
	}
	
	private void init()  {
		getSession().invalidate();
		//MESSAGE
		add(new Label("sessionExpiredMessage", new LabelStringDetachableModel("YOUR_SESSION_HAS_EXPIRED")));
	}

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new Model(Constants.SITE_NAME + " - " +new LabelStringDetachableModel("SIGNOUT"));
	}

	protected String getMetaDescription() {
		return "";
	}

	protected String getMetaKeywords() {
		return "";
	}

}//END OF FILE