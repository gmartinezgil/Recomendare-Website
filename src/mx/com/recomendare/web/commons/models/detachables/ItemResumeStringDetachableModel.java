/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.ItemsDAO;
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
public final class ItemResumeStringDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	//the item id to obtain the resume of itself...
	private int itemId;
	//the name of the currency obtained...
	private String result;
	/**
	 * @param object
	 */
	public ItemResumeStringDetachableModel(final int itemId) {
		this.itemId =itemId;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		Session session = ((DatabaseRequestCycle)RequestCycle.get()).getDatabaseSession();
		ItemsDAO dao = ((Main)RequestCycle.get().getApplication()).getServices().getDatabaseService().getItemsDAO();
		dao.setSession(session);
		String languageCode = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode();
		return result = dao.getItemResume(itemId, languageCode);
	}

	public String toString() {
		return result;
	}
	
}//END OF FILE