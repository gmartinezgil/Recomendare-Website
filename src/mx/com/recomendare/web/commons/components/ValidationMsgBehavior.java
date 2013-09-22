/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * @author jerry
 *
 */
public final class ValidationMsgBehavior extends AbstractBehavior {

	private static final long serialVersionUID = 1L;

	public void onRendered(Component c) {
		FormComponent fc = (FormComponent)c;
		if (!fc.isValid()) {
			String error;
			if (fc.hasFeedbackMessage()) {
				error = fc.getFeedbackMessage().getMessage().toString();
			} else {
				error = "Your input is invalid.";
			}
			fc.getResponse().write(
					"<div class=\"errormessage\">" + error + "</div>"
			);
		}
	}

}//END OF FILE