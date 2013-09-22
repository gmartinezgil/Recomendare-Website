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
public final class DaysListDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	
	int days[] = {
					1,2,3,4,5,6,7,8,9,10,
					11,12,13,14,15,16,17,18,19,20,
					21,22,23,24,25,26,27,28,29,30,31
				};

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		List<SelectOption> options = new ArrayList<SelectOption>();
		for (int i = 0; i < days.length; i++) {
			SelectOption option = new SelectOption(String.valueOf(days[i]), String.valueOf(days[i]));
			options.add(option);
		}
		return options;
	}

}
