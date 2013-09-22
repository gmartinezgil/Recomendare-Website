/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.recomendare.web.commons.models.detachables;

import java.util.List;

import mx.com.recomendare.db.entities.Category;
import mx.com.recomendare.services.db.CategoriesDAO;
import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Session;

/**
 *
 * @author jerry
 */
public abstract class AbstractCategoriesListDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
    
    /**
     * 
     * @return
     */
    protected Object load() {
        Session session = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
        CategoriesDAO dao = ((Main) RequestCycle.get().getApplication()).getServices().getDatabaseService().getCategoriesDAO();
        dao.setSession(session);
        String languageCode = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode();
        return getFormatToVisualize(dao.getAllCategories(languageCode));
    }
    
    protected abstract List<Object> getFormatToVisualize(final List<Category> categories);
    
}//END OF FILE