/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.db.entities.Zone;
import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.ZonesDAO;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.components.TagCloud;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Session;

/**
 * @author jerry
 *
 */
public final class ZonesTagListDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		final Session session = ((DatabaseRequestCycle)RequestCycle.get()).getDatabaseSession();
		ZonesDAO dao = ((Main)RequestCycle.get().getApplication()).getServices().getDatabaseService().getZonesDAO();
		dao.setSession(session);
		final LocationModel location = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession()));
		final List<Zone> zones = dao.getZones(location.getCityId());
		List<TagCloud.Tag> tags = new ArrayList<TagCloud.Tag>(zones.size());
		final Iterator<Zone> iterator = zones.iterator();
		while (iterator.hasNext()) {
			final Zone zone = (Zone) iterator.next();
			final String name = zone.getName();
			final int weight = zone.getStreets().size();//TODO:it should have better be from the items that have...but this suffice by the moment...
			tags.add(new TagCloud.Tag(name, weight){
				private static final long serialVersionUID = 1L;
				public Link getLink(String id) {
					return new Link(id){
						private static final long serialVersionUID = 1L;
						public void onClick() {
							PageParameters parameters = new PageParameters();
							parameters.put("s", name);
							//setResponsePage(ViewZonePage.class, parameters);						
						}
					};
				}
			});
		}
		return tags;
	}

}//END OF FILE