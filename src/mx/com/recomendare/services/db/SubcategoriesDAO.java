/**
 * 
 */
package mx.com.recomendare.services.db;

import java.util.List;

import mx.com.recomendare.db.entities.Category;
import mx.com.recomendare.db.entities.Subcategory;

/**
 * @author jerry
 *
 */
public final class SubcategoriesDAO extends AbstractDAO {
	/**
	 * 
	 * @param categoryId
	 * @param languageCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Subcategory> getSubcategoriesByCategoryId(final int categoryId, final String languageCode)  {
		return session.createQuery(
									"SELECT s "+
									"FROM Subcategory s, SubcategoryDescription sd, Language l "+
									"WHERE sd.subcategory = s "+
									"AND sd.language = l "+
									"AND s.category = ? "+
									"AND l.code = ? "+
									"ORDER BY sd.name"
									).setParameter(0, new Category(categoryId)).setParameter(1, languageCode).list();
	}
	
	/**
	 * 
	 * @param subcategoryId
	 * @param languageCode
	 * @return
	 */
	public String getSubcategoryName(final int subcategoryId, final String languageCode)  {
		return (String)session.createQuery(
											"SELECT sd.name "+
											"FROM Subcategory s, SubcategoryDescription sd, Language l "+
											"WHERE sd.subcategory = s "+
											"AND sd.language = l "+
											"AND s = ? "+
											"AND l.code = ?"
											).setParameter(0, new Subcategory(subcategoryId)).setParameter(1, languageCode).uniqueResult();
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
		return session.load(Subcategory.class, new Integer(String.valueOf(id)));
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.db.newschema.AbstractDAO#save(java.lang.Object)
	 */
	public Object save(Object toSave) {
		// TODO Auto-generated method stub
		return null;
	}

}//END OF FILE