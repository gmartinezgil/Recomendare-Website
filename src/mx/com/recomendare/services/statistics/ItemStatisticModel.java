package mx.com.recomendare.services.statistics;

import java.io.Serializable;
import java.util.Date;

import mx.com.recomendare.web.session.UserSessionModel;

public interface ItemStatisticModel extends Serializable{
	public int getId();
	public int getItemId();
	public String getSessionId();
	public String getPagePath();
	public String getReferrer();
	public UserSessionModel getUserSession();
	public String getUserIpAddress();
	public Date getDate();
	public String getLinkName();
	public String getBrowserName();
	public String getBrowserVersion();
	public String getBrowserPlatform();
}//END OF FILE