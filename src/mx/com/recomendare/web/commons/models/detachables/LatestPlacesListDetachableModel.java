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
public final class LatestPlacesListDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	
	private short maxNumberOfElementsToShow;
	
	public LatestPlacesListDetachableModel(final short maxNumberOfElementsToShow) {
		this.maxNumberOfElementsToShow = maxNumberOfElementsToShow;
	}

	/* (non-Javadoc)
	 * @see wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		final LocationModel location = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession()));
    	return ((Main)RequestCycle.get().getApplication()).getServices().getMainService().getLatestItemModelsAdded(location.getCityId(), maxNumberOfElementsToShow, location.getLanguageCode(), location.getCountryCode());
	}

}//END OF FILE