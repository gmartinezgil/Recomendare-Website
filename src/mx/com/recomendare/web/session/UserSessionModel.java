package mx.com.recomendare.web.session;

import java.io.Serializable;

import mx.com.recomendare.web.commons.models.LocationModel;

public final class UserSessionModel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    //the screen name of the user...
    private String screenName;
    //the location of the user...
    private LocationModel location;
    //the id
    private int id;//TODO:not in favor of this but needed...
    //to see if it's really...
    private boolean authenticated;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public void setItIsAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public boolean itIsAuthenticated() {
        return authenticated;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(getClass().getName());
        sb.append("] - {screenName = '");
        sb.append(screenName);
        sb.append("', location = '");
        sb.append(location);
        sb.append("', authenticated = '");
        sb.append(authenticated);
        sb.append("'}");
        return sb.toString();
    }
    
}//END OF FILE