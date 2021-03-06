package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

import java.util.Date;

/**
 * ClickstreamStatistic generated by hbm2java
 */
public class ClickstreamStatistic implements java.io.Serializable {

	private int id;
	private User user;
	private ClientBrowserStatistic clientBrowserStatistic;
	private Location location;
	private String sessionId;
	private String onLink;
	private String onPage;
	private Date onDate;

	public ClickstreamStatistic() {
	}

	public ClickstreamStatistic(int id) {
		this.id = id;
	}

	public ClickstreamStatistic(int id, User user,
			ClientBrowserStatistic clientBrowserStatistic, Location location,
			String sessionId, String onLink, String onPage, Date onDate) {
		this.id = id;
		this.user = user;
		this.clientBrowserStatistic = clientBrowserStatistic;
		this.location = location;
		this.sessionId = sessionId;
		this.onLink = onLink;
		this.onPage = onPage;
		this.onDate = onDate;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ClientBrowserStatistic getClientBrowserStatistic() {
		return this.clientBrowserStatistic;
	}

	public void setClientBrowserStatistic(
			ClientBrowserStatistic clientBrowserStatistic) {
		this.clientBrowserStatistic = clientBrowserStatistic;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getOnLink() {
		return this.onLink;
	}

	public void setOnLink(String onLink) {
		this.onLink = onLink;
	}

	public String getOnPage() {
		return this.onPage;
	}

	public void setOnPage(String onPage) {
		this.onPage = onPage;
	}

	public Date getOnDate() {
		return this.onDate;
	}

	public void setOnDate(Date onDate) {
		this.onDate = onDate;
	}

}
