/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import mx.com.recomendare.services.db.CategoriesDAO;
import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Session;

/**
 * @author jerry
 *
 */
public final class CategoryNameStringDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	//the category id to show the name of the category located...
	private int categoryId;
	//the name of the category obtained...
	private String result;
	
	/**
	 * @param categoryId
	 */
	public CategoryNameStringDetachableModel(final int categoryId) {
		this.categoryId = categoryId;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		Session session = ((DatabaseRequestCycle)RequestCycle.get()).getDatabaseSession();
		CategoriesDAO dao = ((Main)RequestCycle.get().getApplication()).getServices().getDatabaseService().getCategoriesDAO();
		dao.setSession(session);
		String languageCode = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode();
		return result = dao.getCategoryName(categoryId, languageCode);
	}

	/**
	 * 
	 */
	public String toString() {
		return result;
	}

}//END OF FILE