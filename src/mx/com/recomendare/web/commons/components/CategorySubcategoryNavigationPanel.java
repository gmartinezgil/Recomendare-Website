/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import mx.com.recomendare.web.categories.ViewCategoryPage;
import mx.com.recomendare.web.categories.ViewSubCategoryPage;
import mx.com.recomendare.web.commons.models.CategoryModel;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author jerry
 *
 */
public final class CategorySubcategoryNavigationPanel extends Panel {
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param model
	 */
	public CategorySubcategoryNavigationPanel(String id, IModel model) {
		super(id, model);
		init();
	}
	
	private void init()  {
		final CategoryModel model = (CategoryModel)getModelObject();
		//CATEGORY
		PageParameters parameters = new PageParameters();
		parameters.put("i", model.getCategoryId());
		parameters.put("n", model.getCategoryName());
		final BookmarkableStatisticsLink viewCategoryLink = new BookmarkableStatisticsLink("viewCategoryLink", ViewCategoryPage.class, parameters);
		viewCategoryLink.add(new Label("viewCategoryLinkName", model.getCategoryName()));
		add(viewCategoryLink);
		//SUBCATEGORY
		parameters = new PageParameters();
		parameters.put("i", model.getSubcategoryId());
		parameters.put("n", model.getSubcategoryName());
		parameters.put("ci", model.getCategoryId());
		parameters.put("cn", model.getCategoryName());
		final BookmarkableStatisticsLink viewSubcategoriesLink = new BookmarkableStatisticsLink("viewSubcategoriesLink", ViewSubCategoryPage.class, parameters);
		viewSubcategoriesLink.add(new Label("viewSubcategoriesLinkName", model.getSubcategoryName()));
		add(viewSubcategoriesLink);
	}

}//END OF FILE