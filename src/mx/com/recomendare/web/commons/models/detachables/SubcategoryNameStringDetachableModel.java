/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.SubcategoriesDAO;
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
public final class SubcategoryNameStringDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	//the sub category id from where the name located we want... 
	private int subcategoryId;
	//the name of the currency obtained...
	private String result;
	
	/**
	 * @param subcategoryId
	 */
	public SubcategoryNameStringDetachableModel(final int subcategoryId) {
		this.subcategoryId = subcategoryId;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		Session session = ((DatabaseRequestCycle)RequestCycle.get()).getDatabaseSession();
		SubcategoriesDAO dao = ((Main)RequestCycle.get().getApplication()).getServices().getDatabaseService().getSubcategoriesDAO();
		dao.setSession(session);
		String languageCode = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode();
		return result = dao.getSubcategoryName(subcategoryId, languageCode);
	}

	/**
	 * 
	 */
	public String toString() {
		return result;
	}
	
}//END OF FILE