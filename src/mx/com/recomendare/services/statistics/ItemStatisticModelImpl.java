/**
 * 
 */
package mx.com.recomendare.services.statistics;

import java.util.Date;

import mx.com.recomendare.web.session.UserSessionModel;

/**
 * @author jerry
 *
 */
public final class ItemStatisticModelImpl implements ItemStatisticModel {
	private static final long serialVersionUID = 1L;
	
	//the properties...
	private Date date;
	private int id;
	private int itemId;
	private String referrer;
	private String sessionId;
	private UserSessionModel user;
	private String ipAddress;
	private String linkName;
	private String browserName;
	private String browserVersion;
	private String browserPlatform;
	private String pagePath;
	

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.statistics.ItemStatisticModel#getDate()
	 */
	public Date getDate() {
		return date;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.statistics.ItemStatisticModel#getId()
	 */
	public int getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.statistics.ItemStatisticModel#getReferrer()
	 */
	public String getReferrer() {
		return referrer;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.statistics.ItemStatisticModel#getSessionId()
	 */
	public String getSessionId() {
		return sessionId;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.statistics.ItemStatisticModel#getUserIpAddress()
	 */
	public String getUserIpAddress() {
		return ipAddress;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.statistics.ItemStatisticModel#getUserSession()
	 */
	public UserSessionModel getUserSession() {
		return user;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.statistics.ItemStatisticModel#setDate(java.util.Date)
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.statistics.ItemStatisticModel#setId(int)
	 */
	public void setId(int id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.statistics.ItemStatisticModel#setIpAddress(java.lang.String)
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.statistics.ItemStatisticModel#setRefererrer(java.lang.String)
	 */
	public void setRefererrer(String referrer) {
		this.referrer = referrer;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.statistics.ItemStatisticModel#setSessionId(java.lang.String)
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.statistics.ItemStatisticModel#setUserSession(mx.com.recomendare.web.session.UserSessionModel)
	 */
	public void setUserSession(UserSessionModel user) {
		this.user = user;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public String getBrowserPlatform() {
		return browserPlatform;
	}

	public void setBrowserPlatform(String browserPlatform) {
		this.browserPlatform = browserPlatform;
	}

	public String getPagePath() {
		return pagePath;
	}

	public void setPagePath(String pagePath) {
		this.pagePath = pagePath;
	}

}//END OF FILE