/**
 * 
 */
package mx.com.recomendare.services.db;

import java.util.List;

import mx.com.recomendare.db.entities.City;
import mx.com.recomendare.db.entities.Zone;

/**
 * @author jerry
 *
 */
public final class ZonesDAO extends AbstractDAO {
	
	/**
	 * 
	 * @param cityId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Zone> getZones(final int cityId) {
		return session.createQuery(
									"SELECT z " +
									"FROM Zone z, City c " +
									"WHERE z.city = c " +
									"AND c = ?"
									).setParameter(0, new City(cityId)).list();
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