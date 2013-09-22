/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.Component.IVisitor;

/**
 * @author jerry
 *
 */
public final class FormValidatorVisitor implements IVisitor, Serializable {
	private static final long serialVersionUID = 1L;

	Set<Component> visited = new HashSet<Component>();

	@Override
	public Object component(Component c) {
		if (!visited.contains(c)) {
			visited.add(c);
			c.add(new ValidationMsgBehavior());
			c.add(new ErrorHighlightBehavior());
		}
		return IVisitor.CONTINUE_TRAVERSAL;
	}

}//END OF FILE