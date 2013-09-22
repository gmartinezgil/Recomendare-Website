/**
 * 
 */
package mx.com.recomendare.web.categories;


import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.commons.components.TagCloud;
import mx.com.recomendare.web.commons.models.detachables.SubcategoriesTagListDetachableModel;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author jerry
 *
 */
public final class ViewCategoryPage extends BasePage {
    private static final long serialVersionUID = 1L;

    //the category name...
    private String categoryName;

    /**
     * 
     * @param parameters
     */
    public ViewCategoryPage(PageParameters parameters) {
        super(parameters);
        categoryName = parameters.getString("n");
        String categoryKey = parameters.getString("i");
        if (categoryKey != null && categoryKey.trim().length() > 0) {
            IModel subcategoriesModel = new CompoundPropertyModel(new SubcategoriesTagListDetachableModel(Integer.parseInt(categoryKey)));
            setModel(subcategoriesModel);
            init();
        } else {
        	/*throw new CategoryNotFoundException(categoryKey);*/
        }
    }

    private void init() {
        //TITLE
        add(new Label("categoryNameLabel", categoryName));
        //SUBCATEGORIES
        add(new TagCloud("subcategories", getModel()));
        /*
        final PageableListView subcategoriesPageableListView = new PageableListView("subcategories", (List)getModelObject(), Constants.MAX_ITEMS_PER_PAGE) {
            private static final long serialVersionUID = 1L;
            protected void populateItem(final ListItem item) {
                final Subcategory subcategory = (Subcategory) item.getModelObject();
                final Link subcategoryViewLink = new Link("subcategoryViewLink") {
                    private static final long serialVersionUID = 1L;
                    public void onClick() {
                        PageParameters parameters = new PageParameters();
                        parameters.put("i", subcategory.getId());
                        parameters.put("n", new SubcategoryNameStringDetachableModel(subcategory.getId()));
                        parameters.put("ci", subcategory.getCategory().getId());
                        parameters.put("cn", new CategoryNameStringDetachableModel(subcategory.getCategory().getId()));
                        setResponsePage(ViewSubCategoryPage.class, parameters);
                    }
                };
                final IModel subcategoryNameModel = new SubcategoryNameStringDetachableModel(subcategory.getId());
                subcategoryViewLink.add(new Label("subcategoryLabel", subcategoryNameModel.getObject() + " (" + ((subcategory.getItems() != null) ? subcategory.getItems().size() + "" : "0") + ")"));
                item.add(subcategoryViewLink);
            }
        };
        add(subcategoriesPageableListView);
        add(new PagingNavigator("subCategoriesNavigator", subcategoriesPageableListView));
        */
    }

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new Model(Constants.SITE_NAME + " - " + categoryName + " - " + getLocation().getCityName());
	}

	/**
	 * 
	 */
	protected String getMetaDescription() {
		return "";
	}

	/**
	 * 
	 */
	//@SuppressWarnings("unchecked")
	protected String getMetaKeywords() {
		/*
		StringBuffer buffer = new StringBuffer();
		buffer.append(getLocation().getCityName());
		buffer.append(',');
		buffer.append(categoryName);
		buffer.append(',');
		Iterator<Subcategory> iterator = ((List)getModelObject()).iterator();
		while (iterator.hasNext()) {
			Subcategory subcategory = (Subcategory) iterator.next();
			final IModel subcategoryNameModel = new SubcategoryNameStringDetachableModel(subcategory.getId());
			buffer.append(subcategoryNameModel.getObject());
			buffer.append(',');
		}
		return buffer.toString();
		*/
		return "";
	}
    
}//END OF FILE