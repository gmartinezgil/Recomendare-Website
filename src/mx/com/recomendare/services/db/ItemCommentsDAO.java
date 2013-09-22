/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.recomendare.services.db;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.db.entities.Item;
import mx.com.recomendare.db.entities.ItemComment;
import mx.com.recomendare.db.entities.ItemCommentText;
import mx.com.recomendare.db.entities.Language;
import mx.com.recomendare.db.entities.User;
import mx.com.recomendare.web.session.UserSessionModel;

import org.hibernate.Transaction;

import com.google.api.translate.Translate;

/**
 *
 * @author jerry
 */
public final class ItemCommentsDAO extends AbstractDAO {
	/**
	 * 
	 * @param cityId
	 * @param maxNumberOfElementsToShow
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemComment> getLatestRatings(final int cityId, final int maxNumberOfElementsToShow) {
        return session.createQuery(
									"FROM ItemComment ic " +
									"ORDER BY ic.onDate DESC"
									).setMaxResults(maxNumberOfElementsToShow).setCacheable(true).list();
    }
    
    /**
     * 
     * @param ratingId
     * @param languageCode
     * @return
     */
    public String getRatingComments(final int ratingId, final String languageCode)  {
    	return	(String)session.createQuery(
											"SELECT ict.comments "+
											"FROM ItemComment ic, ItemCommentText ict, Language l "+
											"WHERE ict.itemRating = ic "+
											"AND ict.language = l "+
											"AND ic = ? "+
											"AND l.code = ?"
											).setParameter(0, new ItemComment(ratingId)).setParameter(1, languageCode).uniqueResult();
    }
    
    /**
     * 
     * @param itemId
     * @param userSessionModel
     * @param rating
     * @param comments
     */
	public void saveRatingComment(final int itemId, final UserSessionModel userSessionModel, final int rating, final String comments) {
		Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            ItemsDAO itemsDAO = new ItemsDAO();
            itemsDAO.setSession(session);
            final Item item = itemsDAO.getItemById(itemId);
            UsersDAO usersDAO = new UsersDAO();
            usersDAO.setSession(session);
            final User user = usersDAO.getUserWithScreenName(userSessionModel.getScreenName());
            
            //create the rating...
            ItemComment itemComment = new ItemComment();
            itemComment.setItem(item);
            itemComment.setUser(user);
            itemComment.setCalification((short)rating);
            itemComment.setRatedFavorably((short)0);
            itemComment.setRatedNotFavorably((short)0);
            itemComment.setOnDate(new Date());
            session.save(itemComment);
            
            //and it's comments...if there it's any...
            //TODO:this is out of context...should be called in the business layer...
            if(comments != null && comments.trim().length() > 0)  {
            	LanguagesDAO languages = new LanguagesDAO();
	            languages.setSession(session);
	            //supported languages...
	            Iterator<Language> iterator =  languages.getAllLanguages().iterator();
	            while (iterator.hasNext()) {
					Language language = iterator.next();
					ItemCommentText itemCommentText = new ItemCommentText(); 
					itemCommentText.setLanguage(language);
					//itemCommentText.setComments(Translate.translate(comments, userSessionModel.getLocation().getLanguageCode(), language.getCode()));
					itemCommentText.setItemComment(itemComment);
	                session.save(itemCommentText);
				}
            }
            
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("Can't save this comment - " + comments + " on this place id - "+itemId+", because - " + e.getMessage(), e);
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