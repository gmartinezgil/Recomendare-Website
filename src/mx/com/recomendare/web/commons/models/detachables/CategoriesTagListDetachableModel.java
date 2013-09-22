/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.recomendare.web.commons.models.detachables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.db.entities.Category;
import mx.com.recomendare.db.entities.CategoryDescription;
import mx.com.recomendare.web.categories.ViewCategoryPage;
import mx.com.recomendare.web.commons.components.TagCloud;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.link.Link;

/**
 *
 * @author jerry
 */
public final class CategoriesTagListDetachableModel extends AbstractCategoriesListDetachableModel {
	private static final long serialVersionUID = 1L;

    /**
     * 
     */
    protected List<Object> getFormatToVisualize(final List<Category> categories) {
    	List<Object> tags = new ArrayList<Object>(categories.size());
        final Iterator<Category> iterator = categories.iterator();
        while (iterator.hasNext()) {
            final Category category = iterator.next();
            final String categoryName = getCategoryDescription(category); 
            tags.add(new TagCloud.Tag(categoryName, category.getSubcategories().size()) {//TODO:it should be the category items size...
                private static final long serialVersionUID = 1L;
                public Link getLink(final String id) {
                    return new Link(id) {
                        private static final long serialVersionUID = 1L;
                        public void onClick() {
                            PageParameters parameters = new PageParameters();
                            parameters.put("i", category.getId());
                            parameters.put("n", categoryName);
                            setResponsePage(ViewCategoryPage.class, parameters);
                        }
                    };
                }
            });
        }
        return tags;
    }
    
    //to resolve the category name by it's language code...
    @SuppressWarnings("unchecked")
	private String getCategoryDescription(final Category category)  {//TODO:instead of this, try to access directly the dao with the language parameter...
    	Iterator<CategoryDescription> iterator = category.getCategoryDescriptions().iterator();
    	while (iterator.hasNext()) {
			CategoryDescription description = iterator.next();
			if(description.getLanguage().getCode().equals(WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode())) {
				return description.getName();
			}
		}
    	return null;
    }

    
}//END OF FILE