/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.ItemLocationsDAO;
import mx.com.recomendare.web.Main;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Session;

/**
 * @author jerry
 *
 */
public final class ItemLocationDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;

	//the identifier of the place...
	private int placeId;

	public ItemLocationDetachableModel(int placeId) {
		this.placeId = placeId;
	}

	/* (non-Javadoc)
	 * @see wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		Session session = ((DatabaseRequestCycle)RequestCycle.get()).getDatabaseSession();
		ItemLocationsDAO dao = ((Main)RequestCycle.get().getApplication()).getServices().getDatabaseService().getPlacesDAO();
		dao.setSession(session);
		return dao.load(placeId);
	}

}//END OF FILE