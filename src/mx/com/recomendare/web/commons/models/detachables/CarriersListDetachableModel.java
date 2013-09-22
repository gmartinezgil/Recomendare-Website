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
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.SelectOption;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Session;

/**
 * @author jerry
 *
 */
public final class CarriersListDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private String countryId;
	
	/**
	 * @param countryId 
	 * 
	 */
	public CarriersListDetachableModel(String countryId) {
		this.countryId = countryId;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		//TODO:have to parameterize well at database level this relationship...
		List<SelectOption> theCarriers = new ArrayList<SelectOption>();
		Session session = ((DatabaseRequestCycle)RequestCycle.get()).getDatabaseSession();
		ConstantsDAO carriersDAO = ((Main)RequestCycle.get().getApplication()).getServices().getDatabaseService().getConstantsDAO();
		carriersDAO.setSession(session);
		Iterator<Constant> iterator = carriersDAO.getConstantsWithKeyName("CARRIER_TYPE").iterator();
		while (iterator.hasNext()) {
			Constant carrier = iterator.next();
			SelectOption option = new SelectOption(String.valueOf(carrier.getId()), carrier.getKeyValue());
			theCarriers.add(option);
		}
		return theCarriers;
	}

}//END OF FILE