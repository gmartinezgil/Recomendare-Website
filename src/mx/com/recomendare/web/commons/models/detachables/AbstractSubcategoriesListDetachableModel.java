/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.recomendare.web.commons.models.detachables;

import java.util.List;

import mx.com.recomendare.db.entities.Subcategory;
import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.SubcategoriesDAO;
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
public abstract class AbstractSubcategoriesListDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	//the parent category for these sub categories child's...
    protected int categoryId;

    /**
     * 
     * @param categoryId
     */
    public AbstractSubcategoriesListDetachableModel(final int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 
     * @return
     */
    protected Object load() {
    	Session session = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
    	SubcategoriesDAO dao = ((Main) RequestCycle.get().getApplication()).getServices().getDatabaseService().getSubcategoriesDAO();
    	dao.setSession(session);
    	String languageCode = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode();
    	return getFormatToVisualize(dao.getSubcategoriesByCategoryId(categoryId, languageCode));
    }

    /**
     * 
     * @param subcategories
     * @return
     */
    protected abstract List<Object> getFormatToVisualize(final List<Subcategory> subcategories);
    
}//END OF FILE