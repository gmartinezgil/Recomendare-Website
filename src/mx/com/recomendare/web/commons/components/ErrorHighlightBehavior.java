/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * @author jerry
 *
 */
public final class ErrorHighlightBehavior extends AbstractBehavior {
	private static final long serialVersionUID = 1L;

	public void onComponentTag(Component c, ComponentTag tag) {
		FormComponent fc = (FormComponent)c;
		if (!fc.isValid()) {
			tag.put("class", "input.errormessage");
		}
	}

}//END OF FILE