/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import mx.com.recomendare.services.db.CurrenciesDAO;
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
public final class CurrencyNameStringDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	//the currency id to get it's name located...
	private int currencyId;
	//the name of the currency obtained...
	private String result;
	
	/**
	 * @param object
	 */
	public CurrencyNameStringDetachableModel(final int currencyId) {
		this.currencyId = currencyId;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		Session session = ((DatabaseRequestCycle)RequestCycle.get()).getDatabaseSession();
		CurrenciesDAO dao = ((Main)RequestCycle.get().getApplication()).getServices().getDatabaseService().getCurrenciesDAO();
		dao.setSession(session);
		String languageCode = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode();
		return result = dao.getCurrencyName(currencyId, languageCode);
	}

	/**
	 * 
	 */
	public String toString() {
		return result;
	}
	
}//END OF FILE