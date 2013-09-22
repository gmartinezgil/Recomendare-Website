/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.recomendare.web.commons.models.detachables;

import java.util.List;

/**
 *
 * @author jerry
 */
public final class SubcategoriesListDetachableModel extends AbstractSubcategoriesListDetachableModel {
	private static final long serialVersionUID = 1L;

	/**
     * 
     * @param categoryId
     */
    public SubcategoriesListDetachableModel(final int categoryId) {
        super(categoryId);
    }
    
    /**
     * 
     * @param subcategories
     * @return
     */
    @SuppressWarnings("unchecked")
	protected List getFormatToVisualize(final List subcategories) {
        return subcategories;
    }

}//END OF FILE