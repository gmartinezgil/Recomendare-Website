/**
 * 
 */
package mx.com.recomendare.services.db;


/**
 * @author jerry
 *
 */
public final class LabelsDAO extends AbstractDAO {
	/**
	 * 
	 * @param labelCode
	 * @param languageCode
	 * @return
	 */
	public String getLabelByCodeLanguage(final String labelCode, final String languageCode) {
		return (String)session.createQuery(
																"SELECT label.value " +
																"FROM Label label, Language language " +
																"WHERE label.language = language " +
																"AND label.name = ? " +
																"AND language.code = ?"
																).setParameter(0, labelCode).setParameter(1, languageCode).setCacheable(true).uniqueResult();
	}
	
	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.db.AbstractDAO#count()
	 */
	@Override
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.db.AbstractDAO#load(long)
	 */
	@Override
	public Object load(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.db.AbstractDAO#save(java.lang.Object)
	 */
	@Override
	public Object save(Object toSave) {
		// TODO Auto-generated method stub
		return null;
	}

}//END OF FILE