/**
 * 
 */
package mx.com.recomendare.web.ads;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.commons.components.ExternalImage;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author jerry
 *
 */
public final class AdsPanel extends Panel {
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 */
	public AdsPanel(String id) {
		super(id);
		init();
	}

	private void init()  {
		String link = null;
		String imageLink = null;
		final LocationModel location = WebUtil.getUserActualLocation((SignInSession)getSession());
		if(Constants.SPANISH_LANGUAGE_CODE.equalsIgnoreCase(location.getLanguageCode()))  {
			link = "http://www.spreadfirefox.com/node&id=0&t=281";
			imageLink = "http://www.spreadfirefox.com/files/images/affiliates_banners/180x150_02_es.png";
		}
		if(Constants.ENGLISH_LANGUAGE_CODE.equalsIgnoreCase(location.getLanguageCode()))  {
			//link = "http://www.spreadfirefox.com/node&id=0&t=271";
			//imageLink = "http://www.spreadfirefox.com/files/images/affiliates_banners/180x150_02c_en.png";
			link = "http://www.spreadfirefox.com/node&id=0&t=323";
			imageLink = "http://images.spreadfirefox.com/affiliates/Buttons/firefox3/foxkeh-fx3-125x125.png";
		}
		if(Constants.FRENCH_LANGUAGE_CODE.equalsIgnoreCase(location.getLanguageCode()))  {
			link = "http://www.spreadfirefox.com/node&id=0&t=276";
			imageLink = "http://www.spreadfirefox.com/files/images/affiliates_banners/180x150_02b_fr.png";
		}
		if(Constants.PORTUGESE_LANGUAGE_CODE.equalsIgnoreCase(location.getLanguageCode()))  {
			//link = "http://www.spreadfirefox.com/node&id=0&t=295";
			//imageLink = "http://www.spreadfirefox.com/files/images/affiliates_banners/180x150_02b_pt-br.png";
			link = "http://www.spreadfirefox.com/node&id=0&t=326";
			imageLink = "http://www.spreadfirefox.com/files/images/affiliates_banners/foxkeh-fx3-125x125.png";
		}
		final ExternalLink firefoxAffiliateDownloadDayLink = new ExternalLink("firefoxAffiliateDownloadDayLink", link);
		firefoxAffiliateDownloadDayLink.add(new ExternalImage("firefoxAffiliateDownloadDayImage", imageLink));
		add(firefoxAffiliateDownloadDayLink);
	}
	
}//END OF FILE