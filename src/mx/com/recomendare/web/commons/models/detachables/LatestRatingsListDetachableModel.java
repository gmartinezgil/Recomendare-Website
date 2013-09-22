/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.recomendare.web.commons.models.detachables;

import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 *
 * @author jerry
 */
public final class LatestRatingsListDetachableModel extends LoadableDetachableModel {

    private static final long serialVersionUID = 1L;
    private int maxNumberOfElementsToShow;
    private int cityId;

    public LatestRatingsListDetachableModel(int cityId, int maxNumberOfElementsToShow) {
        this.maxNumberOfElementsToShow = maxNumberOfElementsToShow;
        this.cityId = cityId;
    }

    /**
     * 
     * @return
     */
    protected Object load() {
    	LocationModel location = WebUtil.getUserActualLocation((SignInSession)RequestCycle.get().getSession());
    	return ((Main) RequestCycle.get().getApplication()).getServices().getMainService().getLatestItemsComments(cityId, maxNumberOfElementsToShow, location.getLanguageCode());
    }
}//END OF FILE