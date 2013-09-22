package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

import java.util.Date;

/**
 * UserSessionHistoric generated by hbm2java
 */
public class UserSessionHistoric implements java.io.Serializable {

	private int id;
	private Constant constant;
	private UserSession userSession;
	private String ipAddress;
	private Date onDate;

	public UserSessionHistoric() {
	}

	public UserSessionHistoric(int id) {
		this.id = id;
	}

	public UserSessionHistoric(int id, Constant constant,
			UserSession userSession, String ipAddress, Date onDate) {
		this.id = id;
		this.constant = constant;
		this.userSession = userSession;
		this.ipAddress = ipAddress;
		this.onDate = onDate;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Constant getConstant() {
		return this.constant;
	}

	public void setConstant(Constant constant) {
		this.constant = constant;
	}

	public UserSession getUserSession() {
		return this.userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Date getOnDate() {
		return this.onDate;
	}

	public void setOnDate(Date onDate) {
		this.onDate = onDate;
	}

}
