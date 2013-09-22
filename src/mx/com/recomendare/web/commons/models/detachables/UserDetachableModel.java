/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author jerry
 *
 */
public final class UserDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	
	public static final short PREVIEW_MODE = 0;
	public static final short DETAIL_MODE = 1;
	
	private int userId;
	private short type;

	public UserDetachableModel(int userId, short type) {
		this.userId = userId;
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		final LocationModel location = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession()));
    	return ((Main)RequestCycle.get().getApplication()).getServices().getMainService().getUserModelById(userId, location.getLanguageCode(), type);
	}

}//END OF FILE