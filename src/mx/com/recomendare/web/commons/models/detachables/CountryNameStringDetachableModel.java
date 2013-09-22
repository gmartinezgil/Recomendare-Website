/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import mx.com.recomendare.services.db.CountriesDAO;
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
public final class CountryNameStringDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	//the id of the country to get it's name located...
	private int countryId;
	//the name of the country obtained...
	private String result;
	
	/**
	 * @param object
	 */
	public CountryNameStringDetachableModel(final int countryId) {
		this.countryId = countryId;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		Session session = ((DatabaseRequestCycle)RequestCycle.get()).getDatabaseSession();
		CountriesDAO dao = ((Main)RequestCycle.get().getApplication()).getServices().getDatabaseService().getCountriesDAO();
		dao.setSession(session);
		String languageCode = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode();
		return result = dao.getCountryName(countryId, languageCode);
	}

	/**
	 * 
	 */
	public String toString() {
		return result;
	}
	
}//END OF FILE