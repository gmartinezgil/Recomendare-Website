/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import java.util.ArrayList;
import java.util.List;

import mx.com.recomendare.web.commons.models.SelectOption;

import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author jerry
 *
 */
public final class YearsListDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		List<SelectOption> options = new ArrayList<SelectOption>();
		for (int i = 1900; i < 2009; i++) {
			SelectOption option = new SelectOption(String.valueOf(i), String.valueOf(i));
			options.add(option);
		}
		return options;
	}

}//END OF FILE