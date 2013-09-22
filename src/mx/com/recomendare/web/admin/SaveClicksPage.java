/**
 * 
 */
package mx.com.recomendare.web.admin;

import java.util.Date;
import java.util.Enumeration;

import mx.com.recomendare.util.Util;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.request.WebClientInfo;

/**
 * @author jerry
 *
 */
public final class SaveClicksPage extends WebPage {
	/**
	 * @param parameters
	 */
	@SuppressWarnings("unchecked")
	public SaveClicksPage(PageParameters parameters) {
		final Date now = new Date();
		System.out.println("date="+now);
		if(((SignInSession)getSession()).isSignedIn()) System.out.println("user - "+((SignInSession)getSession()).getUser().getScreenName());
		System.out.println("x="+parameters.getString("x"));
		System.out.println("y="+parameters.getString("y"));
		System.out.println("l="+parameters.getString("l"));
		System.out.println("w="+parameters.getString("w"));
		System.out.println("h="+parameters.getString("h"));
		final WebClientInfo info = (WebClientInfo) getRequestCycle().getClientInfo();
		System.out.println("session-id="+getSession().getId());
        System.out.println("user-agent="+info.getUserAgent());
        System.out.println("browser-locale="+getSession().getLocale());
        System.out.println("IP-address="+WebUtil.getUserRemoteAddress());
        final LocationModel location = WebUtil.getUserActualLocation((SignInSession)getSession());
        System.out.println("latitude="+location.getLatitude());
        System.out.println("longitude="+location.getLongitude());
        System.out.println("city="+location.getCityName());
        System.out.println("country="+location.getCountryName());
        System.out.println("timezone="+location.getTimeZone());
        System.out.println("timezone-date="+Util.getDateInTimeZone(now, location.getTimeZone()));
        System.out.println("-------------------------------------------------------------");
        Enumeration<String> e = ((WebRequest) RequestCycle.get().getRequest()).getHttpServletRequest().getHeaderNames();
        while (e.hasMoreElements()) {
			String keyHeaderName = (String) e.nextElement();
			System.out.println(keyHeaderName +"=" +((WebRequest) RequestCycle.get().getRequest()).getHttpServletRequest().getHeader(keyHeaderName));
		}
	}

}//END OF FILE