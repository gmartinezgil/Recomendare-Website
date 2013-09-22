/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.recomendare.web.commons.models.detachables;

import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 *
 * @author jerry
 */
public final class ItemDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	
	//modes...
	public static final short PREVIEW_MODE = 0;
	public static final short DETAIL_MODE = 1;
	
	//the parameters...
	private int itemId;
	private short type;

    public ItemDetachableModel(int itemId, short type) {
        this.itemId = itemId;
        this.type = type;
    }

    /**
     * 
     * @return
     */
    protected Object load() {
    	final LocationModel location = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession()));
    	return ((Main)RequestCycle.get().getApplication()).getServices().getMainService().getItemModelById(itemId, location.getLanguageCode(), location.getCountryCode(), type);
    }
}//END OF FILE
