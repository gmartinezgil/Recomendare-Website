/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

/**
 * @author jerry
 *
 */
public final class ImageButtonPagingNavigator extends PagingNavigator {
	private static final long serialVersionUID = 1L;

	public ImageButtonPagingNavigator(String id, IPageable pageable) {
		super(id, pageable);
	}
	
}//END OF FILE