/**
 * 
 */
package mx.com.recomendare.web.xml;

import java.util.List;

import mx.com.recomendare.db.entities.User;
import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.UsersDAO;
import mx.com.recomendare.web.Main;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.hibernate.Session;

public class XMLPage extends WebPage {
	
	public XMLPage() {
		Session session = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
        UsersDAO dao = ((Main) RequestCycle.get().getApplication()).getServices().getDatabaseService().getUsersDAO();
        dao.setSession(session);
		add(new PersonsListView("persons", dao.getAllUsers()));
	}

	public String getMarkupType() {
		return "xml";
	}

	private static final class PersonsListView extends ListView {
		private static final long serialVersionUID = 1L;

		public PersonsListView(String id, List<User> list) {
			super(id, list);
		}

		protected void populateItem(ListItem item)  {
			User user = (User)item.getModelObject();
			item.add(new Label("firstName", user.getPerson().getFirstname()));
			item.add(new Label("lastName", user.getPerson().getLastname()));
		}
	}
	
}//END OF FILE