/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.recomendare.services.db;

import java.util.Date;
import java.util.Iterator;

import mx.com.recomendare.db.entities.Language;
import mx.com.recomendare.db.entities.Request;
import mx.com.recomendare.db.entities.RequestComment;
import mx.com.recomendare.db.entities.User;

import org.hibernate.Transaction;

import com.google.api.translate.Translate;

/**
 *
 * @author jerry
 */
public final class RequestsDAO extends AbstractDAO {

    /**
     * 
     * @param fromUserScreenName
     * @param toUser
     * @param comments
     */
    public void createFriendRequest(final String fromUserScreenName, final User toUser, final String comments) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            //get the user from the session who made the request...
            UsersDAO users = new UsersDAO();
            users.setSession(session);
            User fromUser = users.getUserWithScreenName(fromUserScreenName);

            Request request = new Request();
            request.setUserByFromUserId(fromUser);
            request.setUserByToUserId(toUser);
            request.setOnDate(new Date());
            session.save(request);
            
            //TODO:this is out of context...should be called in the business layer...
            LanguagesDAO languages = new LanguagesDAO();
            languages.setSession(session);
            final Iterator<Language> iterator = languages.getAllLanguages().iterator();
            while (iterator.hasNext()) {
				final Language language = iterator.next();
				RequestComment comment = new RequestComment();
				comment.setLanguage(language);
				//comment.setComments(Translate.translate(comments, fromUser.getUserConfiguration().getLanguage().getCode(), language.getCode()));
				comment.setRequest(request);
				session.save(comment);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("Can't save this Request from - " + fromUserScreenName + ", because - " + e.getMessage(), e);
        }
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