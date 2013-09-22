/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.recomendare.web.commons.models.detachables;

import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.ItemLocationsDAO;
import mx.com.recomendare.web.Main;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Session;

/**
 *
 * @author jerry
 */
public final class PlacesNearByListDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	
	private int placeId;

    /**
     * 
     * @param cityId
     */
    public PlacesNearByListDetachableModel(int placeId) {
        this.placeId = placeId;
    }

    /**
     * 
     * @return
     */
    protected Object load() {
        Session session = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
        ItemLocationsDAO dao = ((Main) RequestCycle.get().getApplication()).getServices().getDatabaseService().getPlacesDAO();
        dao.setSession(session);
        return dao.getItemLocationsNearByItem(placeId);
    }
}//END OF FILE

