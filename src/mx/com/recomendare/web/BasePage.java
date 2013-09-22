package mx.com.recomendare.web;

import mx.com.recomendare.web.ads.AdsPanel;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * 
 * It's the base page for all other pages...
 * @author jerry
 *
 */
public abstract class BasePage extends WebPage {
    private static final long serialVersionUID = 1L;
    
    //access the panels from the child pages...if necessary...
    protected HeaderPanel headerPanel;
    protected SideBarPanel sideBarPanel;
    protected MenuPanel menuPanel;
    protected AdsPanel adsPanel;
    protected FooterPanel footerPanel;
    
    /**
     * 
     */
    public BasePage() {
        super();
        init();
    }

    /**
     * 
     * @param parameters
     */
    public BasePage(PageParameters parameters) {
        super(parameters);
        init();
    }

    private void init() {
        //HEADER
    	headerPanel = new HeaderPanel("headerPanel");
        add(headerPanel);
        //MENU
        menuPanel = new MenuPanel("menuPanel"); 
        add(menuPanel);
        //SIDE BAR
        sideBarPanel = new SideBarPanel("sidebarPanel"); 
        add(sideBarPanel);
        //ADS
        adsPanel = new AdsPanel("adsPanel"); 
        add(adsPanel);
        //FOOTER
        footerPanel = new FooterPanel("footerPanel"); 
        add(footerPanel);
    }
    
    /**
     * 
     */
    protected void onBeforeRender() {
    	// add the <title> tag
    	addOrReplace(new Label("mainTitle", getPageTitle()));
    	//meta description
    	final WebMarkupContainer metaDescription = new WebMarkupContainer("metaDescription");
    	metaDescription.add(new SimpleAttributeModifier("content", getMetaDescription()));
    	addOrReplace(metaDescription);
    	//meta keywords
    	final WebMarkupContainer metaKeyWords = new WebMarkupContainer("metaKeywords");
    	metaKeyWords.add(new SimpleAttributeModifier("content", getMetaKeywords()));
    	addOrReplace(metaKeyWords);
    	//super
    	super.onBeforeRender();
    }
    
    /**
     * 
     * @return
     */
    protected LocationModel getLocation()  {
    	return WebUtil.getUserActualLocation((SignInSession)getSession());
    }
    
    /**
     * Defines the page title in every child...
     * @return
     */
    protected abstract IModel getPageTitle();
    
    /**
     * The META keywords used on the page
     * @return
     */
    protected abstract String getMetaKeywords();
    
    /**
     * The META description used on the page
     * @return
     */
    protected abstract String getMetaDescription();
}//END OF FILE