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
public final class MonthsListDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;

	int months[] = {
					1,2,3,4,5,6,7,8,9,10,11,12
					};

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		List<SelectOption> options = new ArrayList<SelectOption>();
		for (int i = 0; i < months.length; i++) {
			SelectOption option = new SelectOption(String.valueOf(months[i]), String.valueOf(months[i]));
			options.add(option);
		}
		return options;
	}

}
