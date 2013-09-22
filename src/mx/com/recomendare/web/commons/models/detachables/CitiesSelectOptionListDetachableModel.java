/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.db.entities.City;
import mx.com.recomendare.services.db.CitiesDAO;
import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.SelectOption;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.RequestCycle;
import org.hibernate.Session;

/**
 * @author jerry
 *
 */
public final class CitiesSelectOptionListDetachableModel extends AbstractCitiesListDetachableModel {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public CitiesSelectOptionListDetachableModel(final String countryId) {
        super(countryId);
    }

    /**
     * 
     * @param cities
     * @return
     */
    protected List<Object> getFormatToVisualize(final List<City> cities) {
        if (cities != null && cities.size() > 0) {
        	Session session = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
            CitiesDAO dao = ((Main) RequestCycle.get().getApplication()).getServices().getDatabaseService().getCitiesDAO();
            dao.setSession(session);
            String languageCode = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode();
            List<Object> citiesToShow = new ArrayList<Object>(cities.size());
            Iterator<City> iterator = cities.iterator();
            while (iterator.hasNext()) {
                City city = iterator.next();
                String cityName = dao.getCityName(city.getId(), languageCode);
                SelectOption option = new SelectOption(String.valueOf(city.getId()), cityName);
                citiesToShow.add(option);
            }
            return citiesToShow;
        }
        return null;
    }
    
}//END OF FILE