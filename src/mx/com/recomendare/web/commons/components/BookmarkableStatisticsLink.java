/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import java.util.Date;
import java.util.Iterator;

import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.session.UserSessionModel;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.target.component.BookmarkablePageRequestTarget;

/**
 * @author Jerry
 *
 */
public final class BookmarkableStatisticsLink extends Link {
	private static final long serialVersionUID = 1L;
	
	//the page to forward to...
	private Class<?> pageToForward;
	//the parameters send to this page...
	private PageParameters parametersOfPageToForward;

	/**
	 * @param id
	 */
	public BookmarkableStatisticsLink(final String id, final Class<?> pageToForward, final PageParameters parametersOfPageToForward) {
		super(id);
		this.pageToForward = pageToForward;
		this.parametersOfPageToForward = parametersOfPageToForward;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.link.Link#onClick()
	 */
	@SuppressWarnings("unchecked")
	public void onClick() {
		StringBuffer sb = new StringBuffer();
		sb.append(getPage().getPageClass().getSimpleName());
		sb.append('/');
		PageParameters parameters = getPage().getPageParameters(); 
		if(parameters != null && parameters.size() > 0)  {
			Iterator iterator = parameters.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String)iterator.next();
				sb.append(key);
				sb.append('/');
				sb.append(parameters.getString(key));
				sb.append('/');
			}
		}
		final Date now = new Date();
		final String ipRemoteUserAddress = WebUtil.getUserRemoteAddress();
		final String userSessionId = getSession().getId();
		final UserSessionModel user = ((SignInSession)getSession()).getUser();
		final String pagePath = sb.toString();
		final String linkName = getId() + '/' + urlFor(pageToForward, parametersOfPageToForward);
		final String userAgent = ((WebClientInfo)getRequestCycle().getClientInfo()).getUserAgent();
		final LocationModel location = WebUtil.getUserActualLocation((SignInSession)getSession());
		final String referer = ((WebRequest)getRequestCycle().getRequest()).getHttpServletRequest().getHeader("referer");
		((Main)getRequestCycle().getApplication()).getServices().getStatisticsService().saveLinkClickStatistic(
								now, 
								ipRemoteUserAddress, 
								userSessionId, 
								user, 
								pagePath,
								linkName,
								userAgent,
								location,
								referer
								);
		if(parametersOfPageToForward != null)  {
			getRequestCycle().setRequestTarget(new BookmarkablePageRequestTarget(pageToForward, parametersOfPageToForward));
			//setResponsePage(pageToForward, parametersOfPageToForward);
		}
		else  {
			getRequestCycle().setRequestTarget(new BookmarkablePageRequestTarget(pageToForward));
			//setResponsePage(pageToForward);
		}
	}

}//END OF FILE