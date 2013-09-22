/**
 * 
 */
package mx.com.recomendare.web.users;

import mx.com.recomendare.web.BasePage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;

/**
 * @author jerry
 *
 */
public class AddNewUserPage extends BasePage {
	//the log...
	private static final Log log = LogFactory.getLog(AddNewUserPage.class);
	
	public AddNewUserPage()  {
		init();
	}
	
	private void init()  {
		 
	}
	
	final class UserCreationForm extends Form  {
		private static final long serialVersionUID = 1L;
		public UserCreationForm(String id, IModel model) {
			super(id, model);
			init();
		}
		
		private void init()  {
			// Create and add feedback panel to page
	        final FeedbackPanel feedback = new FeedbackPanel("feedback");
	        add(feedback);
	        
		}
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.BasePage#getMetaDescription()
	 */
	@Override
	protected String getMetaDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.BasePage#getMetaKeywords()
	 */
	@Override
	protected String getMetaKeywords() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.BasePage#getPageTitle()
	 */
	@Override
	protected IModel getPageTitle() {
		// TODO Auto-generated method stub
		return null;
	}

}