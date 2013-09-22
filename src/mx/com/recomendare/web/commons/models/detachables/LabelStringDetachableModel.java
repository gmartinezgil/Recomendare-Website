/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.LabelsDAO;
import mx.com.recomendare.util.Constants;
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
public final class LabelStringDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	//key code to search label translated for...
	private String labelCode;
	//the label type for the label searched...
	private int labelType;
	//the label imported in the proper location...
	private String result; 
	
	//the label types used by this class...
	public static final int NORMAL_LABEL = 0;
	public static final int TITLE_LABEL = 1;
	
	/**
	 * 
	 * @param key
	 */
	public LabelStringDetachableModel(final String labelCode) { 
		this.labelCode = labelCode;
	}
	
	/**
	 * 
	 * @param labelCode
	 * @param labelType
	 */
	public LabelStringDetachableModel(final String labelCode, final int labelType) { 
		this.labelCode = labelCode;
		this.labelType = labelType;
	}


	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		if(labelCode != null && labelCode.trim().length() > 0)  {
			Session session = ((DatabaseRequestCycle)RequestCycle.get()).getDatabaseSession();
			LabelsDAO dao = ((Main)RequestCycle.get().getApplication()).getServices().getDatabaseService().getLabelsDAO();
			dao.setSession(session);
			String languageCode = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode();
			result = dao.getLabelByCodeLanguage(labelCode, languageCode);
			if(NORMAL_LABEL == labelType)  {
				return result = (result == null || result.trim().length() == 0)?result=labelCode:result;
			}
			else if(TITLE_LABEL == labelType)  {
				return result = (result == null || result.trim().length() == 0)?result=Constants.SITE_NAME + " - " + labelCode:Constants.SITE_NAME + " - " + result;
			}
		}
		return labelCode;
	}

	/**
	 * 
	 */
	public String toString() {
		return result;
	}

}//END OF FILE