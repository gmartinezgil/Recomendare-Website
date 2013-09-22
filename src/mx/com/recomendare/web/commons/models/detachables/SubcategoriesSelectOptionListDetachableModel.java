/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.db.entities.Subcategory;
import mx.com.recomendare.db.entities.SubcategoryDescription;
import mx.com.recomendare.web.commons.models.SelectOption;

/**
 * @author jerry
 *
 */
public final class SubcategoriesSelectOptionListDetachableModel extends AbstractSubcategoriesListDetachableModel {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public SubcategoriesSelectOptionListDetachableModel(final int categoryId) {
        super(categoryId);
    }

    /**
     * 
     * @param subcategories
     * @return
     */
    protected List<Object> getFormatToVisualize(final List<Subcategory> subcategories) {
        if (subcategories != null && subcategories.size() > 0) {
            List<Object> subCategoriesToShow = new ArrayList<Object>(subcategories.size());
            Iterator<Subcategory> iterator = subcategories.iterator();
            while (iterator.hasNext()) {
                Subcategory subCategory = iterator.next();
                SelectOption option = new SelectOption(String.valueOf(subCategory.getId()), ((SubcategoryDescription)subCategory.getSubcategoryDescriptions().toArray()[0]).getName());
                subCategoriesToShow.add(option);
            }
            return subCategoriesToShow;
        }
        return null;
    }
    
}//END OF FILE