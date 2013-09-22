/**
 * 
 */
package mx.com.recomendare.services.db;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.WebResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.jamonapi.MonitorFactory;

/**
 * @author jerry
 *
 */
public final class DatabaseRequestCycle extends WebRequestCycle {
	//the session...database
	private Session hibernateSession;
	
	//metrics...
	static final String UNIT = "ms.";
	 
    private long startTime;


	public DatabaseRequestCycle(WebApplication application, Request request, Response response) {
		super(application, (WebRequest)request, (WebResponse)response);
		this.startTime = 0;
	}

	/* (non-Javadoc)
	 * @see wicket.RequestCycle#onBeginRequest()
	 */
	protected void onBeginRequest() {
		super.onBeginRequest();
		startTime = System.currentTimeMillis();
		//connect to the database...
    	SessionFactory factory = DatabaseService.getSessionFactory();
    	hibernateSession = factory.openSession();
	}

	/* (non-Javadoc)
	 * @see wicket.RequestCycle#onEndRequest()
	 */
	protected void onEndRequest() {
		super.onEndRequest();
		calculateDurationAndAddToMonitor();
		if(hibernateSession != null) hibernateSession.close();
	}

	/**
	 * @return the session
	 */
	public Session getDatabaseSession() {
		return hibernateSession;
	}
	
	@SuppressWarnings("unchecked")
	private void calculateDurationAndAddToMonitor() {
        if(startTime != 0) {
            Class pageClass = null;
            if(getWebResponse().isAjax() && getWebRequest().getPage() != null) {
                pageClass = getWebRequest().getPage().getClass();
            } else {
                pageClass = getResponsePageClass();
            }
 
            if(pageClass != null) {
                MonitorFactory.add(pageClass.toString(), UNIT,
                                          System.currentTimeMillis() - startTime);
            }
        }
    }

}//END OF FILE