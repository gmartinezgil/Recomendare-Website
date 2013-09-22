/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import java.util.List;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.items.ViewItemPage;
import mx.com.recomendare.web.rss.ItemsFeedResource;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.markup.html.image.resource.ThumbnailImageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.time.Time;

/**
 * @author jerry
 *
 */
public final class AjaxListPreviewItemsPanel extends Panel {
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param model
	 */
	public AjaxListPreviewItemsPanel(String id, IModel model) {
		super(id, model);
		init();
	}
	
	private void init()  {
		//feed
        final ResourceLink itemsRSSFeedLink = new ResourceLink("itemsRSSFeedLink", new ResourceReference("ItemsFeedResource") {
			private static final long serialVersionUID = 1L;
			public Time lastModifiedTime() {
				return super.lastModifiedTime();
			}
			@SuppressWarnings("unchecked")
			protected Resource newResource() {
				List<ItemModel> items = (List<ItemModel>)getModelObject();
				if(items != null && items.size() > 0)  {
					return new ItemsFeedResource(getModel());
				}
				else return super.newResource();
			}
			
		});
        itemsRSSFeedLink.add(new Label("itemsRSSFeedLinkLabel", new LabelStringDetachableModel("SUBSCRIBE_RSS_FEED")));
        itemsRSSFeedLink.add(new Image("itemsRSSFeedLinkImage", WebUtil.ADD_RSS_FEED_IMAGE));
        add(itemsRSSFeedLink);
        //list
        final WebMarkupContainer itemsListContainer = new WebMarkupContainer("itemsListContainer");
        final PageableListView itemsListView = new PageableListView("itemsListView", getModel(), Constants.MAX_ITEMS_PER_PAGE) {
            private static final long serialVersionUID = 1L;
            protected void populateItem(final ListItem item) {
            	final ItemModel model = (ItemModel)item.getModelObject();
            	//link
            	PageParameters parameters = new PageParameters();
                parameters.put("i", model.getId());
                parameters.put("n", WebUtil.encodeURL(model.getName()));
            	final BookmarkableStatisticsLink viewItemLink = new BookmarkableStatisticsLink("viewItemLink", ViewItemPage.class, parameters);
                //picture
                if(model.getPictures() != null && model.getPictures().get(0) != null && model.getPictures().get(0).getContent().length > 0) {
                    final DynamicImageResource imageResource = new DynamicImageResource() {
                        private static final long serialVersionUID = 1L;
                        protected byte[] getImageData() {
                            return model.getPictures().get(0).getContent();
                        }
                    };
                    final ThumbnailImageResource thumbnailImageResource = new ThumbnailImageResource(imageResource, 100);
                    //thumbnailImageResource.setCacheable(true);
                    viewItemLink.add(new Image("viewItemLinkPicture", thumbnailImageResource));
                } else {
                	viewItemLink.add(new Image("viewItemLinkPicture", WebUtil.NO_PICTURE_IMAGE));
                }
                viewItemLink.add(new Label("viewItemLinkName", model.getName()));
                item.add(viewItemLink);
                //description
                final String previewDescription = (model.getDescription().length() > 80)?model.getDescription().substring(0, 80):model.getDescription();
                Label itemDescription = new Label("itemDescription", previewDescription+" ..."); 
                itemDescription.setEscapeModelStrings(false);
                item.add(itemDescription);
                //navigation
                item.add(new NavigationPanel("itemNavigation", item.getModel()));
            }
        };
        itemsListContainer.add(itemsListView);
        itemsListContainer.setOutputMarkupId(true);
        add(itemsListContainer);
        //navigator...
        add(new AjaxPagingNavigator("itemListNavigator", itemsListView){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.addComponent(itemsListContainer);
			}
        });
	}

}//END OF FILE