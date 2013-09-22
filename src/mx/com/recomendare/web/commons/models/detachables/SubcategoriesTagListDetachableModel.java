/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.db.entities.Subcategory;
import mx.com.recomendare.db.entities.SubcategoryDescription;
import mx.com.recomendare.web.categories.ViewSubCategoryPage;
import mx.com.recomendare.web.commons.components.TagCloud;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.link.Link;

/**
 * @author jerry
 *
 */
public final class SubcategoriesTagListDetachableModel extends AbstractSubcategoriesListDetachableModel {
	private static final long serialVersionUID = 1L;

	public SubcategoriesTagListDetachableModel(int categoryId) {
		super(categoryId);
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.web.commons.models.detachables.AbstractSubcategoriesListDetachableModel#getFormatToVisualize(java.util.List)
	 */
	protected List<Object> getFormatToVisualize(List<Subcategory> subcategories) {
		List<Object> tags = new ArrayList<Object>(subcategories.size());
        final Iterator<Subcategory> iterator = subcategories.iterator();
        while (iterator.hasNext()) {
            final Subcategory subcategory = (Subcategory) iterator.next();
            final String subcategoryName = getSubcategoryName(subcategory);
            tags.add(new TagCloud.Tag(subcategoryName, subcategory.getItems().size()) {
                private static final long serialVersionUID = 1L;
                public Link getLink(final String id) {
                    return new Link(id) {
                        private static final long serialVersionUID = 1L;
                        public void onClick() {
                            PageParameters parameters = new PageParameters();
                            parameters.put("i", subcategory.getId());
                            parameters.put("n", subcategoryName);
                            setResponsePage(ViewSubCategoryPage.class, parameters);
                        }
                    };
                }
            });
        }
        return tags;
	}
	
	//to resolve the country name by it's language code...
    @SuppressWarnings("unchecked")
	private String getSubcategoryName(final Subcategory subcategory)  {//TODO:instead of this, try to access directly the dao with the language...
    	Iterator<SubcategoryDescription> iterator = subcategory.getSubcategoryDescriptions().iterator();
    	while (iterator.hasNext()) {
			SubcategoryDescription description = iterator.next();
			if(description.getLanguage().getCode().equals(WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode())) {
				return description.getName();
			}
		}
    	return null;
    }

}//END OF FILE