/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.recomendare.services.db;

import java.util.Date;
import java.util.List;

import mx.com.recomendare.db.entities.City;
import mx.com.recomendare.db.entities.Constant;
import mx.com.recomendare.db.entities.Language;
import mx.com.recomendare.db.entities.Location;
import mx.com.recomendare.db.entities.Person;
import mx.com.recomendare.db.entities.Picture;
import mx.com.recomendare.db.entities.User;
import mx.com.recomendare.db.entities.UserConfiguration;
import mx.com.recomendare.db.entities.UserConfigurationLocation;
import mx.com.recomendare.db.entities.UserProfile;
import mx.com.recomendare.db.entities.UserSession;
import mx.com.recomendare.web.commons.models.UserModel;

import org.hibernate.Transaction;

/**
 *
 * @author jerry
 */
public final class UsersDAO extends AbstractDAO {
	
	/**
	 * 
	 * @param login
	 * @return
	 */
	public boolean existsUserWithThisLogin(final String login) {
		UserSession userSession = (UserSession)session.createQuery(
																	"FROM UserSession us " +
																	"WHERE us.login = ?"
																	).setParameter(0, login).uniqueResult(); 
		return (userSession != null)?true:false;
	}
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	public int createUser(final UserModel model)  {
		Transaction tx = null;
        try {
            tx = session.beginTransaction();

            //PERSON
            Person person = new Person();
            person.setName(model.getName());
            person.setFirstname(model.getFirstName());
            person.setLastname(model.getLastName());
            person.setBirthDay(model.getBirthDate().getDay());
            person.setBirthMonth(model.getBirthDate().getMonth());
            person.setBirthYear(model.getBirthDate().getYear());
            ConstantsDAO constantsDAO = new ConstantsDAO();
            constantsDAO.setSession(session);
            Constant gender = (Constant)constantsDAO.load(model.getGenderId());
            person.setConstant(gender);
            session.save(person);
            
            //CONTACT
            

            //USER_CONFIGURATION
            UserConfiguration configuration = new UserConfiguration();
            CitiesDAO citiesDAO = new CitiesDAO();
			citiesDAO.setSession(session);
			City city = (City)citiesDAO.load(model.getLocation().getCityId());
            configuration.setCity(city);
            LanguagesDAO languagesDAO = new LanguagesDAO();
			languagesDAO.setSession(session);
			Language language = (Language)languagesDAO.load(model.getLocation().getLanguageId());
            configuration.setLanguage(language);
            configuration.setScreenName(model.getScreenName());
            Picture avatarPicture = new Picture();
            avatarPicture.setContent(model.getAvatarPicture().getContent());
            session.save(avatarPicture);
            configuration.setPicture(avatarPicture);
            configuration.setIsActive(false);
            session.save(configuration);
            
            //USER_CONFIGURATION_LOCATION
            Location location = new Location();
            location.setLatitude(new Double(model.getLocation().getLatitude()));
            location.setLongitude(new Double(model.getLocation().getLongitude()));
            session.save(location);
            UserConfigurationLocation configurationLocation = new UserConfigurationLocation();
            configurationLocation.setOnDate(new Date());
            configurationLocation.setLocation(location);
            configurationLocation.setUserConfiguration(configuration);
            session.save(configurationLocation);
            
            //USER_SESSION
            UserSession userSession = new UserSession();
            userSession.setLogin(model.getLogin());
            userSession.setPassword(model.getPassword());
            session.save(userSession);

            //USER
            User user = new User();
            user.setPerson(person);
            user.setUserProfile(new UserProfile(1));
            user.setUserConfiguration(configuration);
            user.setUserSession(userSession);
            session.save(user);

            tx.commit();
            
            return user.getId();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("Can't save this UserModel - " + model + ", reason - " + e.getMessage(), e);
        }
		return -1;
	}

    /**
     * Gets the user from the persistent storage...
     * @param username
     * @param password
     * @return
     */
    public User getUserWithCredentials(final String username, final String password) {
        return (User) session.createQuery(
        									"SELECT u " +
        									"FROM User u, UserSession us " +
        									"WHERE u.userSession = us " +
        									"AND us.login = ? " +
        									"AND us.password = ?"
        									).setParameter(0, username).setParameter(1, password).uniqueResult();//only one user .. ;)
    }

    /**
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
        return session.createQuery(
        							"FROM User u " +
        							"ORDER BY u.id"
        							).list();
    }

    /**
     * 
     * @param screenName
     * @return
     */
    public UserConfiguration getUserConfigurationByScreenName(final String screenName) {    	
        return (UserConfiguration) session.createQuery(
        												"SELECT uc " +
        												"FROM User u, UserConfiguration uc " +
        												"WHERE u.userConfiguration = uc " +
        												"AND u = ?"
        												).setParameter(0, getUserWithScreenName(screenName)).uniqueResult();
    }

    /**
     * 
     * @param userKey
     * @return
     */
    public UserConfiguration getUserConfigurationByUserId(final String userKey) {
    	return (UserConfiguration) session.createQuery(
    													"SELECT uc " +
    													"FROM User u, UserConfiguration uc " +
    													"WHERE u.userConfiguration = uc " +
    													"AND u = ?"
    													).setParameter(0, new User(new Integer(userKey))).uniqueResult();
	}
    
    /**
     * 
     * @param screenName
     * @return
     */
    public User getUserWithScreenName(final String screenName) {
        return (User) session.createQuery(
        									"SELECT u " +
        									"FROM User u, UserConfiguration uc " +
        									"WHERE u.userConfiguration = uc " +
        									"AND uc.screenName = ?"
        									).setParameter(0, screenName).uniqueResult();
    }
    
    /**
     * 
     * @return
     */
    public UserProfile getDefaultUserProfile()  {
    	return (UserProfile)session.createQuery(
    											"FROM UserProfile up " +
    											"WHERE up = ?"
    											).setParameter(0, new UserProfile(1));
    }

    /* (non-Javadoc)
     * @see mx.com.recomendare.services.db.newschema.AbstractDAO#count()
     */
    public int count() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see mx.com.recomendare.services.db.newschema.AbstractDAO#load(long)
     */
    public Object load(long id) {
    	return session.load(User.class, new Integer(String.valueOf(id)));
    }

    /* (non-Javadoc)
     * @see mx.com.recomendare.services.db.newschema.AbstractDAO#save(java.lang.Object)
     */
    public Object save(Object toSave) {
        // TODO Auto-generated method stub
        return null;
    }

}//END OF FILE