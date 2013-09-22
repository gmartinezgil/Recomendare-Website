package mx.com.recomendare.web.session;

import mx.com.recomendare.db.entities.CurrencyName;
import mx.com.recomendare.db.entities.User;
import mx.com.recomendare.services.db.CitiesDAO;
import mx.com.recomendare.services.db.CountriesDAO;
import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.UsersDAO;
import mx.com.recomendare.services.security.EncryptService;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.LocationModelImpl;

import org.apache.wicket.Application;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.protocol.http.WebSession;
import org.hibernate.Session;

/**
 * 
 * @author jerry
 *
 */
public final class SignInSession extends WebSession {

    private static final long serialVersionUID = 1L;
    //the user stored in session...
    private UserSessionModel user;
    
    

    @SuppressWarnings("deprecation")
	public SignInSession(Application application, Request request) {
		super(application, request);
	}

    public final boolean authenticate(final String username, final String password) {
        if ((user == null) || (user != null && !user.itIsAuthenticated())) {
     
            Session dbSession = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
            UsersDAO usersDAO = ((Main) getApplication()).getServices().getDatabaseService().getUsersDAO();
            usersDAO.setSession(dbSession);
            
            EncryptService encryptService = ((Main) getApplication()).getServices().getEncryptService();
            String encryptedPassword = encryptService.encrypt(password);
            
            final User userDAO = usersDAO.getUserWithCredentials(username, (encryptedPassword != null)?encryptedPassword:password);
            if (userDAO != null) {
            	if(userDAO.getUserConfiguration().getIsActive()) {
            		user = new UserSessionModel();
                    user.setScreenName(userDAO.getUserConfiguration().getScreenName());
                    user.setId(userDAO.getId());
                    
                    CountriesDAO countriesDAO = ((Main)getApplication()).getServices().getDatabaseService().getCountriesDAO();
                    countriesDAO.setSession(dbSession);
                    CitiesDAO citiesDAO = ((Main)getApplication()).getServices().getDatabaseService().getCitiesDAO();
                    citiesDAO.setSession(dbSession);
                    
                    LocationModelImpl locationModel = new LocationModelImpl();
                    locationModel.setCityName(citiesDAO.getCityName(userDAO.getUserConfiguration().getCity().getId(), userDAO.getUserConfiguration().getLanguage().getCode()));
                    locationModel.setCityId(userDAO.getUserConfiguration().getCity().getId());
                    locationModel.setCountryName(countriesDAO.getCountryName(userDAO.getUserConfiguration().getCity().getCountry().getId(), userDAO.getUserConfiguration().getLanguage().getCode()));
                    locationModel.setCountryCode(userDAO.getUserConfiguration().getCity().getCountry().getCode());
                    locationModel.setCountryId(userDAO.getUserConfiguration().getCity().getCountry().getId());
                    locationModel.setLatitude(userDAO.getUserConfiguration().getCity().getLocation().getLatitude().floatValue());//from the city..not from the user...
                    locationModel.setLongitude(userDAO.getUserConfiguration().getCity().getLocation().getLongitude().floatValue());//from the city..not from the user...
                    locationModel.setLanguageId(userDAO.getUserConfiguration().getLanguage().getId());
                    locationModel.setLanguageCode(userDAO.getUserConfiguration().getLanguage().getCode());
                    locationModel.setLanguageName(userDAO.getUserConfiguration().getLanguage().getName());
                    locationModel.setCurrencyCode(userDAO.getUserConfiguration().getCity().getCountry().getCurrency().getCode());
                    locationModel.setCurrencyName(((CurrencyName)userDAO.getUserConfiguration().getCity().getCountry().getCurrency().getCurrencyNames().toArray()[0]).getName());//TODO: it should ask to the db...
                    user.setLocation(locationModel);
                    user.setItIsAuthenticated(true);
            	}
            }
        }
        return (user != null && user.itIsAuthenticated());
    }

    public boolean isSignedIn() {
        return (user != null && user.itIsAuthenticated());
    }

    public UserSessionModel getUser() {
        return user;
    }

    public void setUser(final UserSessionModel user) {
        this.user = user;
    }
    
}//END OF FILE