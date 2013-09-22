package mx.com.recomendare.web;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import mx.com.recomendare.services.ServiceFacade;
import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.web.admin.MemoryStatusChartPage;
import mx.com.recomendare.web.admin.SaveClicksPage;
import mx.com.recomendare.web.categories.ViewCategoryPage;
import mx.com.recomendare.web.categories.ViewSubCategoryPage;
import mx.com.recomendare.web.countries.ViewCountryPage;
import mx.com.recomendare.web.items.AddNewItemWithWizardPage;
import mx.com.recomendare.web.items.ViewItemPage;
import mx.com.recomendare.web.items.ViewItemsAroundMePage;
import mx.com.recomendare.web.location.BrunoPartyPage;
import mx.com.recomendare.web.location.GetDirectionsToArriveToLocationPage;
import mx.com.recomendare.web.location.SetPreferedLocationPage;
import mx.com.recomendare.web.mail.SendPlaceToMailPage;
import mx.com.recomendare.web.phone.SendPlaceToPhonePage;
import mx.com.recomendare.web.places.ViewAnotherPlacesNearOfPage;
import mx.com.recomendare.web.places.ViewPlacesNearOfCellTowerPage;
import mx.com.recomendare.web.rss.LatestPlacesRSSFeedPage;
import mx.com.recomendare.web.search.SearchResultsPage;
import mx.com.recomendare.web.session.AuthenticatedPage;
import mx.com.recomendare.web.session.SignInPage;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.session.SignOutPage;
import mx.com.recomendare.web.users.AddNewUserWithWizardPage;
import mx.com.recomendare.web.users.ViewUserProfilePage;
import mx.com.recomendare.web.util.WebUtil;
import mx.com.recomendare.web.xml.XMLPage;

import org.apache.wicket.Component;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.protocol.http.BufferedWebResponse;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;

/**
 * 
 * @author jerry
 *
 */
/*
 * After the page is rendered, it is put into a PageMap. The PageMap instance lives in session and keeps the
last n pages ( this number is configurable through Wicket ApplicationSettings object). When a form is
submitted, the page is brought back from PageMap and the form handler is executed on it. The PageMap
uses a Least Recently Used (LRU) algorithm by default to evict pages to reduce space taken up in session.
You can configure Wicket with your own implementation of the eviction strategy. Wicket specifies the strategy
through the interface wicket.session.pagemap.IPageMapEvictionStrategy. You can configure
your implementation by invoking getSessionSettings().setPageMapEvictionStrategy
(yourPageMapEvicationStrategyInstance) in the WebApplication.init() method. This could
prove to be extremely crucial when tuning Wicket to suit your application needs.
 */
public final class Main extends WebApplication {
	//the reference to the services...
	private ServiceFacade services;
    
    /**
     * 
     */
    protected void init() {
        super.init();
        // Due to Firefox 3.0 we strip the wicket tags,
		// even in develop mode.
		// http://www.nabble.com/Gmap2-problem-with-Firefox-3.0-to18137475.html
		getMarkupSettings().setStripWicketTags(true);

        //if behind a PROXY...development mode...
        //System.setProperty("http.proxyHost", "proxy.com");
        //System.setProperty("http.proxyPort", "8080");
        //initializes the information gathering from the client's browser...
        //getRequestCycleSettings().setGatherExtendedBrowserInfo(true);
        
		//SERVICES
		final String pathToGeoLiteCityFile = getServletContext().getRealPath("WEB-INF" + File.separator + "classes" + File.separator + "mx" + File.separator + "com" + File.separator + "recomendare" + File.separator + "services" + File.separator + "geo" + File.separator + "GeoLiteCity.dat");
		final String pathToMailPropertiesFile = getServletContext().getRealPath("WEB-INF" + File.separator + "classes" + File.separator + "mx" + File.separator + "com" + File.separator + "recomendare" + File.separator + "services" + File.separator + "mail" + File.separator + "mail.properties");
		final String pathToIATAFile = getServletContext().getRealPath("WEB-INF" + File.separator + "classes" + File.separator + "mx" + File.separator + "com" + File.separator + "recomendare" + File.separator + "services" + File.separator + "iata" + File.separator + "IATA.txt");
		services = new ServiceFacade(pathToGeoLiteCityFile, pathToMailPropertiesFile, pathToIATAFile);

        //SECURITY
        getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy() {
            public boolean isActionAuthorized(Component component, Action action) {
                return true;
            }
            @SuppressWarnings("unchecked")
			public boolean isInstantiationAuthorized(Class componentClass) {
                if (AuthenticatedPage.class.isAssignableFrom(componentClass)) {
                    // Is user signed in?
                    if (((SignInSession) Session.get()).isSignedIn()) {
                        // okay to proceed
                        return true;
                    }
                    // Force sign in
                    throw new RestartResponseAtInterceptPageException(SignInPage.class);
                }
                return true;
            }
        });
        
        //PAGES
        mountBookmarkablePage("/home", Index.class);
        //mountBookmarkablePage("/place", ViewPlacePage.class);
        mountBookmarkablePage("/item", ViewItemPage.class);
        mountBookmarkablePage("/nearby-places", ViewAnotherPlacesNearOfPage.class);
        mountBookmarkablePage("/nearby-items", ViewItemsAroundMePage.class);
        mountBookmarkablePage("/country", ViewCountryPage.class);
        mountBookmarkablePage("/category", ViewCategoryPage.class);
        mountBookmarkablePage("/subcategory", ViewSubCategoryPage.class);
        mountBookmarkablePage("/set-location", SetPreferedLocationPage.class);
        mountBookmarkablePage("/signin", SignInPage.class);
        mountBookmarkablePage("/signout", SignOutPage.class);
        mountBookmarkablePage("/user", ViewUserProfilePage.class);
        mountBookmarkablePage("/register", AddNewUserWithWizardPage.class);
        //mountBookmarkablePage("/add-place", AddNewPlacePage.class);
        //mountBookmarkablePage("/add-item", AddNewItemPage.class);
        mountBookmarkablePage("/recommend", AddNewItemWithWizardPage.class);
        mountBookmarkablePage("/send-by-mail", SendPlaceToMailPage.class);
        mountBookmarkablePage("/send-by-phone", SendPlaceToPhonePage.class);
        mountBookmarkablePage("/rss-latest-places", LatestPlacesRSSFeedPage.class);
        mountBookmarkablePage("/search", SearchResultsPage.class);
        
        //of use of my own...
        mountBookmarkablePage("/monitor", MemoryStatusChartPage.class);
        mountBookmarkablePage("/cell", ViewPlacesNearOfCellTowerPage.class);
        mountBookmarkablePage("/xml", XMLPage.class);
        mountBookmarkablePage("/saveclick", SaveClicksPage.class);
        mountBookmarkablePage("/directions", GetDirectionsToArriveToLocationPage.class);
        mountBookmarkablePage("/bruno", BrunoPartyPage.class);
        
        getApplicationSettings().setPageExpiredErrorPage(Index.class);
        /*
        getApplicationSettings().setAccessDeniedPage(AccessDeniedPage.class);
        getApplicationSettings().setInternalErrorPage(InternalErrorPage.class);

        // show internal error page instead of default developer page...
        getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
        */
    }

    /**
     * 
     */
	protected WebResponse newWebResponse(HttpServletResponse servletResponse) {
		return new BufferedWebResponse(servletResponse) {
			public CharSequence encodeURL(final CharSequence url) {
				final String agent = ((WebRequest) RequestCycle.get().getRequest()).getHttpServletRequest().getHeader("User-Agent");
				return WebUtil.isAgent(agent) ? url : super.encodeURL(url);
			}
		};
	}

	/**
     * 
     */
	public Session newSession(Request request, Response response) {
		return new SignInSession(Main.this, request);
	}

    /**
     * @see wicket.protocol.http.WebApplication#newRequestCycleProcessor()
     */
    /*
    protected IRequestCycleProcessor newRequestCycleProcessor() {
    return new CompoundRequestCycleProcessor(
    new CryptedUrlWebRequestCodingStrategy(new WebRequestCodingStrategy()),null, null, null, null);
    }
     */
    
    /**
     * 
     */
	public RequestCycle newRequestCycle(Request request, Response response) {
		return new DatabaseRequestCycle(this, (WebRequest) request, (WebResponse) response);
	}
	
	/**
	 * 
	 * @return
	 */
	public ServiceFacade getServices() {
		return services;
	}

	/**
     * 
     */
	protected void onDestroy() {
		services.doStop();
		super.onDestroy();
	}

	/**
     * Our home page...
     */
    @SuppressWarnings("unchecked")
	public Class getHomePage() {
        return Index.class;
    }

}//END OF FILE