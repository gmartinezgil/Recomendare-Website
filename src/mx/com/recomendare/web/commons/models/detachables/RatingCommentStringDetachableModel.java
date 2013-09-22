/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.ItemCommentsDAO;
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
public final class RatingCommentStringDetachableModel extends 	LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	//the rating id for the comment to get of...
	private int ratingId;
	//the name of the currency obtained...
	private String result;
	/**
	 * @param object
	 */
	public RatingCommentStringDetachableModel(final int ratingId) {
		this.ratingId = ratingId;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		Session session = ((DatabaseRequestCycle)RequestCycle.get()).getDatabaseSession();
		ItemCommentsDAO dao = ((Main)RequestCycle.get().getApplication()).getServices().getDatabaseService().getRatingsDAO();
		dao.setSession(session);
		String languageCode = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode();
		return result = dao.getRatingComments(ratingId, languageCode);
	}

	/**
	 * 
	 */
	public String toString() {
		return result;
	}
	
}//END OF FILE