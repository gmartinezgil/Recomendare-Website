/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.db.entities.Currency;
import mx.com.recomendare.services.db.CurrenciesDAO;
import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.SelectOption;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Session;

/**
 * @author Jerry
 *
 */
public final class CurrenciesListDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		List<SelectOption> currencies = new ArrayList<SelectOption>(); 
		Session session = ((DatabaseRequestCycle)RequestCycle.get()).getDatabaseSession();
		CurrenciesDAO currenciesDAO = ((Main)RequestCycle.get().getApplication()).getServices().getDatabaseService().getCurrenciesDAO();
		currenciesDAO.setSession(session);
		String languageCode = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode();
		Iterator<Currency> iterator = currenciesDAO.getCurrencies().iterator();
		while (iterator.hasNext()) {
			Currency currency = (Currency) iterator.next();
			String currencyName = currenciesDAO.getCurrencyName(currency.getId(), languageCode);
			SelectOption option = new SelectOption(String.valueOf(currency.getId()), currencyName);
			currencies.add(option);
		}
		return currencies;
	}

}//END OF FILE