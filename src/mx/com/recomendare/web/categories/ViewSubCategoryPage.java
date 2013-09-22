/**
 * 
 */
package mx.com.recomendare.web.categories;

import java.util.List;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.commons.models.detachables.ItemsListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.places.ViewPlacePage;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.image.resource.ThumbnailImageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.aetrion.flickr.places.Place;

/**
 * @author jerry
 *
 */
public final class ViewSubCategoryPage extends BasePage {
    private static final long serialVersionUID = 1L;
    
    private String subcategoryName;
    private String categoryId;
    private String categoryName;
    
    /**
     * 
     * @param parameters
     */
    public ViewSubCategoryPage(PageParameters parameters) {
        super(parameters);
        subcategoryName = parameters.getString("n");
        categoryId = parameters.getString("ci");
        categoryName = parameters.getString("cn");
        String subcategoryKey = parameters.getString("i");
        if (subcategoryKey != null && subcategoryKey.trim().length() > 0) {
            IModel itemsModel = new CompoundPropertyModel(new ItemsListDetachableModel(subcategoryKey));
            setModel(itemsModel);
            init();
        } else {/*throw new SubCategoryNotFoundException(subcategoryKey);*/
        }
    }

    private void init() {
        //TITLE
        final Link categoryViewLink = new Link("categoryViewLink") {
            private static final long serialVersionUID = 1L;
            public void onClick() {
                PageParameters parameters = new PageParameters();
                parameters.put("i", categoryId);
                parameters.put("n", categoryName);
                setResponsePage(ViewCategoryPage.class, parameters);
            }
        };
        categoryViewLink.add(new Label("categoryNameLabel", categoryName));
        add(categoryViewLink);
        add(new Label("subcategoryNameLabel", subcategoryName));
        //ITEMS
        final PageableListView itemsPageableListView = new PageableListView("items", (List) getModelObject(), Constants.MAX_ITEMS_PER_PAGE) {
            private static final long serialVersionUID = 1L;
            protected void populateItem(final ListItem item) {
                final mx.com.recomendare.db.entities.Item itemToShow = (mx.com.recomendare.db.entities.Item) item.getModelObject();
                final Link itemViewDetailLink = new Link("itemViewDetailLink") {
                    private static final long serialVersionUID = 1L;
                    public void onClick() {
                        //TODO:statistics...?
                        final Place place = ((Place) itemToShow.getPlaces().toArray()[0]);
                        PageParameters parameters = new PageParameters();
                        parameters.put("i", place.getId());
                        parameters.put("n", place.getItem().getName());
                        setResponsePage(ViewPlacePage.class, parameters);
                    }
                };
                if (itemToShow.getPictureByPictureOneId() != null && itemToShow.getPictureByPictureOneId().getContent().length > 0) {
                    final DynamicImageResource imageResource = new DynamicImageResource() {
                        private static final long serialVersionUID = 1L;
                        protected byte[] getImageData() {
                            return itemToShow.getPictureByPictureOneId().getContent();
                        }
                    };
                    ThumbnailImageResource thumbnailImageResource = new ThumbnailImageResource(imageResource, 100);
                    thumbnailImageResource.setCacheable(false);
                    itemViewDetailLink.add(new Image("itemPicture", thumbnailImageResource));
                }
                else  {
                    itemViewDetailLink.add(new Image("itemPicture", WebUtil.QUESTIONMARK_IMAGE));
                }
                itemViewDetailLink.add(new Label("itemName", itemToShow.getName()));
                item.add(itemViewDetailLink);
            }
        };
        add(itemsPageableListView);
        add(new PagingNavigator("itemsNavigator", itemsPageableListView));
    }

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new Model(Constants.SITE_NAME + " - " +new LabelStringDetachableModel("SUBCATEGORY") + " - " +subcategoryName);
	}
    
}//END OF FILE