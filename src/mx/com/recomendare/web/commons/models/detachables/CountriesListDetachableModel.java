/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.db.entities.Country;
import mx.com.recomendare.services.db.CountriesDAO;
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
public final class CountriesListDetachableModel extends AbstractCountriesListDetachableModel {
	private static final long serialVersionUID = 1L;
	
	protected List<Object> getFormatToVisualize(List<Country> countries) {
		List<Object> countriesToShow = new ArrayList<Object>(countries.size());
		Iterator<Country> iterator = countries.iterator();
		Session session = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
        CountriesDAO dao = ((Main) RequestCycle.get().getApplication()).getServices().getDatabaseService().getCountriesDAO();
        dao.setSession(session);
        String languageCode = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode();
		while (iterator.hasNext()) {
			Country country = iterator.next();
			String countryName = dao.getCountryName(country.getId(), languageCode);
			SelectOption option = new SelectOption(String.valueOf(country.getId()), countryName);
			countriesToShow.add(option);
		}
		return countriesToShow;
	}

}//END OF FILE