package mx.com.recomendare.web;

import mx.com.recomendare.web.commons.components.AjaxCarrouselItemsPanel;
import mx.com.recomendare.web.commons.components.AjaxListPreviewItemsPanel;
import mx.com.recomendare.web.commons.models.detachables.AdvertisedItemsListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LatestPlacesListDetachableModel;
import mx.com.recomendare.web.countries.ViewCityPage;
import mx.com.recomendare.web.countries.ViewCountryPage;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

/**
 * 
 * @author jerry
 *
 */
public final class Index extends BasePage {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public Index() {
        super();
        init();
    }

    private void init() {
    	//CITY NAME
    	final Link viewCityLink = new Link("viewCityLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				setResponsePage(ViewCityPage.class);
			}
    	};
    	viewCityLink.add(new Label("cityLinkName", getLocation().getCityName()));
		add(viewCityLink);
		//COUNTRY
		final Link viewCountryLink = new Link("viewCountryLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				PageParameters parameters = new PageParameters();
				parameters.put("i", getLocation().getCountryCode());
				parameters.put("n", getLocation().getCountryName());
				setResponsePage(ViewCountryPage.class, parameters);
			}
		};
		viewCountryLink.add(new Image("countryLinkImage", WebUtil.getCountryFlagImageFromUserLocation(getLocation().getCountryCode())));
		viewCountryLink.add(new Label("countryLinkName", getLocation().getCountryName()));
		add(viewCountryLink);
		//CARROUSEL ITEMS
		add(new Label("recommendItemsThisWeekLabel", new LabelStringDetachableModel("RECOMMENDED_THIS_WEEK")));
		add(new AjaxCarrouselItemsPanel("carrousel", new AdvertisedItemsListDetachableModel()));
        //HIGH RANKED
        add(new Label("highRankedPlacesLabel", new LabelStringDetachableModel("MOST_RECOMMENDED")));
        add(new AjaxListPreviewItemsPanel("highRankedItemsList", new LatestPlacesListDetachableModel((short)5)));
        //LATEST PLACES
        add(new Label("latestPlacesLabel", new LabelStringDetachableModel("LATEST_PLACES")));
        add(new AjaxListPreviewItemsPanel("latestItemsRecommendedList", new LatestPlacesListDetachableModel((short)5)));
    }
    
	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new LabelStringDetachableModel("SITE_SLOGAN", LabelStringDetachableModel.TITLE_LABEL);
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
	protected String getMetaKeywords() {
		return "";
	}
    
}//END OF FILE