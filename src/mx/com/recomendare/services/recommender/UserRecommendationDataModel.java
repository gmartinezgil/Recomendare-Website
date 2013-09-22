package mx.com.recomendare.services.recommender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.db.entities.ItemComment;
import mx.com.recomendare.services.db.DatabaseService;
import mx.com.recomendare.services.db.UsersDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.planetj.taste.common.TasteException;
import com.planetj.taste.impl.model.GenericDataModel;
import com.planetj.taste.impl.model.GenericItem;
import com.planetj.taste.impl.model.GenericPreference;
import com.planetj.taste.impl.model.GenericUser;
import com.planetj.taste.model.DataModel;
import com.planetj.taste.model.Item;
import com.planetj.taste.model.Preference;
import com.planetj.taste.model.User;

/**
 * @author jerry
 *
 */
final class UserRecommendationDataModel implements DataModel {
    //the reference to the DAO service...
    private UsersDAO usersDAO;
    //and finally the model...
    private DataModel model;
    //the log...
    private static final Log log = LogFactory.getLog(UserRecommendationDataModel.class);

    @SuppressWarnings("static-access")
	public UserRecommendationDataModel(final DatabaseService databaseService) {
        usersDAO = databaseService.getUsersDAO();
        usersDAO.setSession(databaseService.getSessionFactory().openSession());
        model = new GenericDataModel(getUsersList());
    }

    public Item getItem(Object arg0) throws TasteException {
        return model.getItem(arg0);
    }

    @SuppressWarnings("unchecked")
	public Iterable getItems() throws TasteException {
        return model.getItems();
    }

    public int getNumItems() throws TasteException {
        return model.getNumItems();
    }

    public int getNumUsers() throws TasteException {
        return model.getNumUsers();
    }

    @SuppressWarnings("unchecked")
	public Iterable getPreferencesForItem(Object arg0) throws TasteException {
        return model.getPreferencesForItem(arg0);
    }

    public User getUser(Object arg0) throws TasteException {
        return model.getUser(arg0);//TODO:NoSuchElementException
    }

    @SuppressWarnings("unchecked")
	public Iterable getUsers() throws TasteException {
        return model.getUsers();
    }

    public void removePreference(Object arg0, Object arg1) throws TasteException {
        model.removePreference(arg0, arg1);
    }

    public void setPreference(Object arg0, Object arg1, double arg2) throws TasteException {
        model.setPreference(arg0, arg1, arg2);
    }
    
    public Preference[] getPreferencesForItemAsArray(Object arg0) throws TasteException {
		return model.getPreferencesForItemAsArray(arg0);
	}

    public void refresh() {
        log.info("refreshing datamodel...");
        model.refresh();//TODO:have to update every time we have a new user, for user based recommendations...
        log.info("datamodel refreshed...");
    }

    //utility...
    @SuppressWarnings("unchecked")
	private List<User> getUsersList() {
        long start = System.currentTimeMillis();
        List<User> users = new ArrayList<User>();
        final List<mx.com.recomendare.db.entities.User> usersFromDAO = usersDAO.getAllUsers();
        Iterator<mx.com.recomendare.db.entities.User> usersIterator = usersFromDAO.iterator();
        while (usersIterator.hasNext()) {
            mx.com.recomendare.db.entities.User userDAO = (mx.com.recomendare.db.entities.User) usersIterator.next();
            List<Preference> preferences = new ArrayList<Preference>(userDAO.getItemComments().size());
            final Iterator<ItemComment> itemCommentIterator = userDAO.getItemComments().iterator();
            while (itemCommentIterator.hasNext()) {
                ItemComment itemComment = itemCommentIterator.next();
                mx.com.recomendare.db.entities.Item itemDAO = itemComment.getItem();
                Item item = new GenericItem(new Integer(itemDAO.getId()));
                Preference preference = new GenericPreference(null, item, itemComment.getCalification().doubleValue());
                preferences.add(preference);
            }
            User user = new GenericUser(userDAO.getUserConfiguration().getScreenName(), preferences);
            users.add(user);
        }
        log.info("datamodel took in building it - " + (System.currentTimeMillis() - start) + " ms");
        return users;
    }

}//END OF FILE