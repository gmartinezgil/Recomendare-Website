/**
 * 
 */
package mx.com.recomendare.web.items;

import org.apache.wicket.model.IModel;

import wicket.contrib.gmap.GMap2;

import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;

/**
 * @author jerry
 *
 */
public final class ViewItemsAroundMePage extends BasePage {
	
	private GMap2 gMap;
	/**
	 * 
	 */
	public ViewItemsAroundMePage()  {
		init();
	}
	
	private void init()  {
		
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.BasePage#getMetaDescription()
	 */
	@Override
	protected String getMetaDescription() {
		// TODO Auto-generated method stub
		return "";
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.BasePage#getMetaKeywords()
	 */
	@Override
	protected String getMetaKeywords() {
		// TODO Auto-generated method stub
		return "";
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.BasePage#getPageTitle()
	 */
	@Override
	protected IModel getPageTitle() {
		return new LabelStringDetachableModel("ITEMS_NEARBY_ME", LabelStringDetachableModel.TITLE_LABEL);
	}

}
