/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import mx.com.recomendare.services.db.CitiesDAO;
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
public final class CityNameStringDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	//id of the city to get it's name...
	private int cityId;
	//the name of the city obtained...
	private String result;
	
	/**
	 * @param object
	 */
	public CityNameStringDetachableModel(final int cityId) {
		this.cityId = cityId;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		Session session = ((DatabaseRequestCycle)RequestCycle.get()).getDatabaseSession();
		CitiesDAO dao = ((Main)RequestCycle.get().getApplication()).getServices().getDatabaseService().getCitiesDAO();
		dao.setSession(session);
		String languageCode = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode();
		return result = dao.getCityName(cityId, languageCode); 
	}

	/**
	 * 
	 */
	public String toString() {
		return result;
	}
	
}//END OF FILE