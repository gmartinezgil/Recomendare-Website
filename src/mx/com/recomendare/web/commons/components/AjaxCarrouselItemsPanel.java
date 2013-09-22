/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import java.util.ArrayList;
import java.util.List;

import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.commons.models.ItemModelImpl;
import mx.com.recomendare.web.items.ViewItemPage;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Resource;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.image.resource.ThumbnailImageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

/**
 * @author jerry
 *
 */
public final class AjaxCarrouselItemsPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	//the actual counter
	private int counter;
	
	//and the components to be refreshed...
	private WebMarkupContainer itemContainer;
	private NonCachingImage itemPicture;
	private Label itemTitle;
	private Label itemDescription;
	//the proper items to show...
	private List<ItemModel> items;

	/**
	 * @param id
	 * @param model
	 */
	public AjaxCarrouselItemsPanel(String id, IModel model) {
		super(id, model);
		init();
	}
	
	@SuppressWarnings("unchecked")
	private void init()  {
		items = (List<ItemModel>)getModelObject();
		if(items == null) {
			items = new ArrayList<ItemModel>(1);
		}
		if(items.size() == 0)  {
			ItemModelImpl item = new ItemModelImpl();
			item.setName("");
			item.setDescription("");
			items.add(item);
		}
		//showing panel
		itemContainer = new WebMarkupContainer("itemContainer");
		final Link viewItemLink = new Link("viewItemLink"){
			private static final long serialVersionUID = 1L;
			public void onClick() {
				PageParameters parameters = new PageParameters();
                parameters.put("i", items.get(counter).getId());
                parameters.put("n", WebUtil.encodeURL(items.get(counter).getName()));
                setResponsePage(ViewItemPage.class, parameters);
			}
		};
		itemPicture = new NonCachingImage("itemPicture");
		itemPicture.setImageResource(getScaledImageOfItemModel(items.get(counter), 225));
		viewItemLink.add(itemPicture);
		itemContainer.add(viewItemLink);
		itemTitle = new Label("itemTitle", items.get(counter).getName());
		itemTitle.setEscapeModelStrings(false);
		itemContainer.add(itemTitle);
		final String previewDescription = (items.get(counter).getDescription().length() > 80)?items.get(counter).getDescription().substring(0, 80):items.get(counter).getDescription();
		itemDescription = new Label("itemDescription", previewDescription);
		itemDescription.setEscapeModelStrings(false);
		itemContainer.add(itemDescription);
		itemContainer.setOutputMarkupId(true);
		add(itemContainer);
		//previous button
		final AjaxLink previousLink = new AjaxLink("previousLink"){
			private static final long serialVersionUID = 1L;
			public void onClick(AjaxRequestTarget target) {
				counter--;
				if(counter < 0)  {
					counter = 0;
				}
				refreshTarget(target, items.get(counter));
			}
		};
		previousLink.add(new Image("previousLinkImage", WebUtil.LEFT_ARROW_IMAGE));
		add(previousLink);
		//next button
		final AjaxLink nextLink = new AjaxLink("nextLink"){
			private static final long serialVersionUID = 1L;
			public void onClick(AjaxRequestTarget target) {
				counter++;
				if(counter == items.size())  {
					counter = 0;
				}
				refreshTarget(target, items.get(counter));
			}
		};
		nextLink.add(new Image("nextLinkImage", WebUtil.RIGHT_ARROW_IMAGE));
		add(nextLink);
		//updating in time
		add(new AbstractAjaxTimerBehavior(Duration.seconds(15)){
			private static final long serialVersionUID = 1L;
			protected void onTimer(AjaxRequestTarget target) {
				counter++;
				if(counter == items.size())  {
					counter = 0;
				}
				refreshTarget(target, items.get(counter));
			}
		});
	}
	
	//gets the scaled image of the item...the first picture...
	private Resource getScaledImageOfItemModel(final ItemModel model, int scale)  {
		if(model.getPictures() != null && model.getPictures().size() > 0)  {
			final BufferedDynamicImageResource bufferedImage = new BufferedDynamicImageResource() {
				private static final long serialVersionUID = 1L;
				protected byte[] getImageData() {
					return model.getPictures().get(0).getContent();
				}
			};
			final ThumbnailImageResource thumbnail = new ThumbnailImageResource(bufferedImage, scale);
			return thumbnail;
		}
		return WebUtil.NO_PICTURE_IMAGE;
	}
	
	//refresh the item that pass on the carrousel... 
	private void refreshTarget(final AjaxRequestTarget target, final ItemModel model)  {
		itemPicture.setImageResource(getScaledImageOfItemModel(model, 225));
		itemTitle.setModel(new Model(model.getName()));
		final String previewDescription = (model.getDescription().length() > 80)?model.getDescription().substring(0, 80):model.getDescription();
		itemDescription.setModel(new Model(previewDescription));
		target.addComponent(itemContainer);
	}

}//END OF FILE