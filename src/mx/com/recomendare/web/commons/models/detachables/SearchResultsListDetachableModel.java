/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author jerry
 *
 */
public final class SearchResultsListDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;

	private String query;
	/**
	 * 
	 */
	public SearchResultsListDetachableModel(final String query) {
		this.query = query;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		final LocationModel location = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession()));
		return ((Main) RequestCycle.get().getApplication()).getServices().getMainService().getItemModelsByKeyword(query, location.getLanguageCode(), location.getCountryCode());
	}

}//END OF FILE