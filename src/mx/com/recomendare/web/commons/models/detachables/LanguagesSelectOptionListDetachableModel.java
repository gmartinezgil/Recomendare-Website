/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.db.entities.Language;
import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.LanguagesDAO;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.SelectOption;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Session;

/**
 * @author jerry
 *
 */
public final class LanguagesSelectOptionListDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		Session session = ((DatabaseRequestCycle)RequestCycle.get()).getDatabaseSession();
		LanguagesDAO dao = ((Main)RequestCycle.get().getApplication()).getServices().getDatabaseService().getLanguagesDAO();
		dao.setSession(session);
		List<Language> languages = dao.getAllLanguages();
		List<SelectOption> languagesToShow = new ArrayList<SelectOption>(languages.size());
		Iterator<Language> iterator = languages.iterator();
		while (iterator.hasNext()) {
			Language language = iterator.next();
			SelectOption option = new SelectOption(String.valueOf(language.getId()), language.getName());
			languagesToShow.add(option);
		}
		return languagesToShow;
	}

}//END OF FILE