/**
 * 
 */
package mx.com.recomendare.services.db;

import java.util.List;

import mx.com.recomendare.db.entities.Category;

/**
 * @author jerry
 *
 */
public final class CategoriesDAO extends AbstractDAO {

	/**
	 * 
	 * @param languageCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Category> getAllCategories(final String languageCode)  {
		return session.createQuery(
													"SELECT c "+
													"FROM Category c, CategoryDescription cd, Language l "+
													"WHERE cd.category = c "+
													"AND cd.language = l "+
													"AND l.code = ? "+
													"ORDER BY cd.name"
													).setParameter(0, languageCode).setCacheable(true).list();
	}

	/**
	 * 
	 * @param categoryId
	 * @param languageCode
	 * @return
	 */
	public String getCategoryName(final int categoryId, final String languageCode)  {
		return (String)session.createQuery(
																"SELECT cd.name "+
																"FROM Category c, CategoryDescription cd, Language l "+
																"WHERE cd.category = c "+
																"AND cd.language = l "+
																"AND c = ? "+
																"AND l.code = ?"
																).setParameter(0, new Category(categoryId)).setParameter(1, languageCode).uniqueResult();
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