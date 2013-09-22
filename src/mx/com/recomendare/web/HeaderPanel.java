/**
 * 
 */
package mx.com.recomendare.web;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.location.LocalizationPanel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author jerry
 *
 */
public final class HeaderPanel extends Panel {
    private static final long serialVersionUID = 1L;
    
    /**
     * @param id
     */
    public HeaderPanel(String id) {
        super(id);
        init();
    }

    private void init() {
        //SITE NAME
        final Link homeLink = new Link("homeLink") {
            private static final long serialVersionUID = 1L;
            public void onClick() {
                setResponsePage(Index.class);
            }
        };
        homeLink.add(new Label("sitename", Constants.SITE_NAME));
        add(homeLink);
        //SLOGAN
        add(new Label("slogan", new LabelStringDetachableModel("SITE_SLOGAN")));//Constants.SITE_SLOGAN
        //LOCALIZATION
        add(new LocalizationPanel("localization"));
        
        //RSS
        /*
	    add(new Link("rssLink"){
	    private static final long serialVersionUID = 1L;
	    public void onClick() {
	    setResponsePage(LatestPlacesRSSFeedPage.class);
	    }
	    });
         */
    }
    
}//END OF FILE