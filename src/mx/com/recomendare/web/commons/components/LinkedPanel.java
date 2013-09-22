/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.mail.SendPlaceToMailPage;
import mx.com.recomendare.web.phone.SendPlaceToPhonePage;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author jerry
 *
 */
public final class LinkedPanel extends Panel {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param id
	 * @param model
	 */
	public LinkedPanel(String id, IModel model) {
		super(id, model);
		init();
	}

	private void init()  {
		final ItemModel item = (ItemModel)getModelObject();

		//promote in another sites...
		//DELICIOUS
		final ExternalLink bookmarkOnDeliciousLink = new ExternalLink("bookmarkOnDeliciousLink", "http://del.icio.us/post?url="+Constants.SITE_URL+"&title="+WebUtil.encodeURL(item.getName()));//TODO:missing the next part...
		bookmarkOnDeliciousLink.add(new Image("bookmarkOnDeliciousLinkImage", WebUtil.DELICIOUS_IMAGE));
		bookmarkOnDeliciousLink.add(new AttributeModifier("title", true, new LabelStringDetachableModel("BOOKMARK_ON_DELICIOUS")));
		add(bookmarkOnDeliciousLink);
		//DIGG
		final ExternalLink diggThisPageOnDiggLink = new ExternalLink("diggThisPageOnDiggLink", "http://digg.com/submit?phase=2&url="+Constants.SITE_URL+"&title="+WebUtil.encodeURL(item.getName()));//TODO:missing the next part...
		diggThisPageOnDiggLink.add(new Image("diggThisPageOnDiggLinkImage", WebUtil.DIGG_IMAGE));
		diggThisPageOnDiggLink.add(new AttributeModifier("title", true, new LabelStringDetachableModel("DIGG_ON_DIGG")));
		add(diggThisPageOnDiggLink);
		//STUMBLEUPON
		final ExternalLink thumbThisPageOnStumbleUponLink = new ExternalLink("thumbThisPageOnStumbleUponLink", "http://www.stumbleupon.com/submit?url="+Constants.SITE_URL+"&title="+WebUtil.encodeURL(item.getName()));//TODO:missing the next part...
		thumbThisPageOnStumbleUponLink.add(new Image("thumbThisPageOnStumbleUponLinkImage", WebUtil.STUMBLE_IMAGE));
		thumbThisPageOnStumbleUponLink.add(new AttributeModifier("title", true, new LabelStringDetachableModel("THUMB_ON_STUMBLEUPON")));
		add(thumbThisPageOnStumbleUponLink);
		//REDDIT
		final ExternalLink postThisPageOnRedditLink = new ExternalLink("postThisPageOnRedditLink", "http://programming.reddit.com/submit?url="+Constants.SITE_URL+"&title="+WebUtil.encodeURL(item.getName()));//TODO:missing the next part...
		postThisPageOnRedditLink.add(new Image("postThisPageOnRedditLinkImage", WebUtil.REDDIT_IMAGE));
		postThisPageOnRedditLink.add(new AttributeModifier("title", true, new LabelStringDetachableModel("POST_ON_REDDIT")));
		add(postThisPageOnRedditLink);
		
		//internal...
		//EMAIL
        final Link sendPageByMailLink = new Link("sendPageByMailLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				PageParameters parameters = new PageParameters();
                parameters.put("i", item.getId());
                parameters.put("n", item.getName());
                setResponsePage(SendPlaceToMailPage.class, parameters);
			}
        };
        sendPageByMailLink.add(new Image("sendPageByMailPicture", WebUtil.SEND_EMAIL_IMAGE));
        sendPageByMailLink.add(new AttributeModifier("title", true, new LabelStringDetachableModel("SEND_PLACE_TO_MAIL")));
        //sendPageByMailLink.add(new Label("sendPageByMailLabel", new LabelStringDetachableModel("SEND_PLACE_TO_MAIL")));
        add(sendPageByMailLink);
        //PHONE
        final Link sendPlaceToPhoneLink = new Link("sendPlaceToPhoneLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				PageParameters parameters = new PageParameters();
                parameters.put("i", item.getId());
                parameters.put("n", item.getName());
                setResponsePage(SendPlaceToPhonePage.class, parameters);
			}
        };
        sendPlaceToPhoneLink.add(new Image("sendPlaceToPhonePicture", WebUtil.SEND_TO_PHONE_IMAGE));
        sendPlaceToPhoneLink.add(new AttributeModifier("title", true, new LabelStringDetachableModel("SEND_PLACE_TO_PHONE")));
        //sendPlaceToPhoneLink.add(new Label("sendPlaceToPhoneLabel", new LabelStringDetachableModel("SEND_PLACE_TO_PHONE")));
        add(sendPlaceToPhoneLink);
	}

}//END OF FILE