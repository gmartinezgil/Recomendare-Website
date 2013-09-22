/**
 * 
 */
package mx.com.recomendare.services.db;

import java.util.List;

import mx.com.recomendare.db.entities.Language;


/**
 * @author jerry
 *
 */
public final class LanguagesDAO extends AbstractDAO {
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Language> getAllLanguages() {
		return session.createQuery(
										"FROM Language l " +
										"ORDER BY l.name"
										).setCacheable(true).list();
	}
	
	/**
	 * 
	 * @param countryCode
	 * @return
	 */
	public Language getLanguageByCountryCode(final String countryCode) {
		return (Language)session.createQuery(
												"SELECT l " +
												"FROM Language l, Country c " +
												"WHERE c.language = l " +
												"AND c.code = ?"
												).setParameter(0, countryCode).uniqueResult();
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
		return session.load(Language.class, new Integer(String.valueOf(id)));
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.db.newschema.AbstractDAO#save(java.lang.Object)
	 */
	public Object save(Object toSave) {
		// TODO Auto-generated method stub
		return null;
	}

}//END OF FILE