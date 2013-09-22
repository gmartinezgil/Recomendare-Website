/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.db.entities.Category;
import mx.com.recomendare.db.entities.CategoryDescription;
import mx.com.recomendare.web.commons.models.SelectOption;



/**
 * @author jerry
 *
 */
public final class CategoriesSelectOptionListDetachableModel extends AbstractCategoriesListDetachableModel {
	private static final long serialVersionUID = 1L;
	
    /**
     * 
     * @param categories
     * @return
     */
    protected List<Object> getFormatToVisualize(final List<Category> categories) {
        List<Object> categoriesToShow = new ArrayList<Object>(categories.size());
        Iterator<Category> iterator = categories.iterator();
		while (iterator.hasNext()) {
			Category category = iterator.next();
			SelectOption option = new SelectOption(String.valueOf(category.getId()), ((CategoryDescription)category.getCategoryDescriptions().toArray()[0]).getName());
			categoriesToShow.add(option);
		}
        return categoriesToShow;
    }
    
}//END OF FILE