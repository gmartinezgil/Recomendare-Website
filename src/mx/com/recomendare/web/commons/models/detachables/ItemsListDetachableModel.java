/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.recomendare.web.commons.models.detachables;

import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.ItemsDAO;
import mx.com.recomendare.web.Main;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Session;

/**
 *
 * @author jerry
 */
public final class ItemsListDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	//the subcategory id...
    private String subcategoryKey;

    public ItemsListDetachableModel(String subcategoryKey) {
        this.subcategoryKey = subcategoryKey;
    }

    /**
     * 
     * @return
     */
    protected Object load() {
        Session session = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
        ItemsDAO dao = ((Main) RequestCycle.get().getApplication()).getServices().getDatabaseService().getItemsDAO();
        dao.setSession(session);
        return dao.getItemsBySubcategory(subcategoryKey);
    }
}//END OF FILE
