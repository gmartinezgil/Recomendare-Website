/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.recomendare.services.db;

import java.util.Date;
import java.util.List;

import mx.com.recomendare.db.entities.Item;
import mx.com.recomendare.db.entities.UserFavorite;

import org.hibernate.Transaction;

/**
 *
 * @author jerry
 */
public final class FavoritesDAO extends AbstractDAO {

    /**
     * 
     * @param item
     * @param screenName
     * @return
     */
    public UserFavorite createFavorite(final int itemId, final String screenName) {
        UserFavorite favorite = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            favorite = new UserFavorite();
            UsersDAO users = new UsersDAO();
            users.setSession(session);
            favorite.setUser(users.getUserWithScreenName(screenName));
            ItemsDAO itemsDAO = new ItemsDAO();
            itemsDAO.setSession(session);
            Item item = (Item)itemsDAO.load(itemId);
            favorite.setItem(item);
            favorite.setCreatedOn(new Date());
            session.save(favorite);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("Can't save this Favorite with the Item - " + itemId + ", because - " + e.getMessage(), e);
        }
        return favorite;
    }
    
    /**
     * 
     * @param screenName
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<UserFavorite> getFavoritesByScreenName(final String screenName) {
        return session.createQuery(
        							"SELECT uf " +
        							"FROM UserFavorite uf, UserConfiguration uc " +
        							"WHERE uf.userConfiguration = uc " +
        							"AND uc.screenName = ?"
        							).setParameter(0, screenName).list();
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
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see mx.com.recomendare.services.db.newschema.AbstractDAO#save(java.lang.Object)
     */
    public Object save(Object toSave) {
        // TODO Auto-generated method stub
        return null;
    }
    
}//END OF FILE