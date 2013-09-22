/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.db.entities.Constant;
import mx.com.recomendare.services.db.ConstantsDAO;
import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.SelectOption;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Session;

/**
 * @author jerry
 *
 */
public final class GendersListDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		final Session session = ((DatabaseRequestCycle)RequestCycle.get()).getDatabaseSession();
		ConstantsDAO constantsDAO = ((Main)RequestCycle.get().getApplication()).getServices().getDatabaseService().getConstantsDAO();
		constantsDAO.setSession(session);
		final List<Constant> constants = constantsDAO.getConstantsWithKeyName(Constants.GENDER_TYPE);
		List<SelectOption> options = new ArrayList<SelectOption>(constants.size());
		final Iterator<Constant> iterator = constants.iterator();
		while (iterator.hasNext()) {
			final Constant constant = iterator.next();
			SelectOption option = new SelectOption(String.valueOf(constant.getId()), constant.getKeyValue());
			options.add(option);
		}
		return options;
	}

}//END OF FILE