/**
 * 
 */
package mx.com.recomendare.services.db;

import java.util.List;

import mx.com.recomendare.db.entities.Constant;

/**
 * @author jerry
 *
 */
public final class ConstantsDAO extends AbstractDAO {

	/**
	 * 
	 * @param keyName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Constant> getConstantsWithKeyName(final String keyName)  {
		return session.createQuery("FROM Constant c WHERE c.keyName = ?").setParameter(0, keyName).list();
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.db.AbstractDAO#count()
	 */
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.db.AbstractDAO#load(long)
	 */
	public Object load(long id) {
		return session.load(Constant.class, new Integer(String.valueOf(id)));
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.db.AbstractDAO#save(java.lang.Object)
	 */
	public Object save(Object toSave) {
		return null;
	}

}//END OF FILE