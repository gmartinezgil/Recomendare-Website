/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.recomendare.web.commons.models.detachables;

import java.util.List;

import mx.com.recomendare.db.entities.City;
import mx.com.recomendare.services.db.CitiesDAO;
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
public abstract class AbstractCitiesListDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	//the country id parent key for this cities...
    protected String countryId;

    /**
     * 
     * @param countryId
     */
    public AbstractCitiesListDetachableModel(final String countryId) {
        this.countryId = countryId;
    }

    /**
     * 
     * @return
     */
    protected Object load() {
        if (countryId != null && countryId.trim().length() > 0) {
            Session session = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
            CitiesDAO dao = ((Main) RequestCycle.get().getApplication()).getServices().getDatabaseService().getCitiesDAO();
            dao.setSession(session);
            String languageCode = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode();
            return getFormatToVisualize(dao.getCitiesByCountryId(countryId, languageCode));
        }
        return null;
    }

    protected abstract List<Object> getFormatToVisualize(final List<City> cities);
}//END OF FILE

