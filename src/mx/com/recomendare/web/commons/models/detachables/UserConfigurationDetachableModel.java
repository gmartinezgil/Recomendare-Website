/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.recomendare.web.commons.models.detachables;

import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.UsersDAO;
import mx.com.recomendare.web.Main;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Session;

/**
 *
 * @author jerry
 */
public final class UserConfigurationDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	
	private String userKey;

    /**
     * 
     * @param userKey
     */
    public UserConfigurationDetachableModel(String userKey) {
        this.userKey = userKey;
    }

    /**
     * 
     * @return
     */
    protected Object load() {
        Session session = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
        UsersDAO dao = ((Main) RequestCycle.get().getApplication()).getServices().getDatabaseService().getUsersDAO();
        dao.setSession(session);
        return dao.getUserConfigurationByUserId(userKey);
    }
    
}//END OF FILE