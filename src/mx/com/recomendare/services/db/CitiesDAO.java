/**
 * 
 */
package mx.com.recomendare.services.db;

import java.util.List;

import mx.com.recomendare.db.entities.City;
import mx.com.recomendare.db.entities.Country;

/**
 * @author jerry
 *
 */
public final class CitiesDAO extends AbstractDAO {
	
	/**
	 * 
	 * @param keyCountry
	 * @param languageCode 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<City> getCitiesByCountryId(final String keyCountry, final String languageCode) {
		return session.createQuery(
													"SELECT c "+
													"FROM City c, CityName cn, Language l " +
													"WHERE cn.city = c " +
													"AND cn.language = l "+
													"AND c.country = ? "+
													"AND l.code = ?"+
													"ORDER BY cn.name"
													).setParameter(0, new Country(Integer.parseInt(keyCountry))).setParameter(1, languageCode).setCacheable(true).list();
	}
	
	/**
	 * 
	 * @param cityName
	 * @return
	 */
	public City getCityByServiceName(final String cityServiceName)  {
		return (City)session.createQuery(
										"SELECT c "+
										"FROM City c " +
										"WHERE c.byServiceName = ?"
										).setParameter(0, cityServiceName).uniqueResult();
	}
	
	/**
	 * 
	 * @param cityId
	 * @param languageCode
	 * @return
	 */
	public String getCityName(final int cityId, final String languageCode) {
		return (String)session.createQuery(
															"SELECT cn.name "+
															"FROM City c, CityName cn, Language l "+
															"WHERE cn.city = c "+
															"AND cn.language = l "+
															"AND c = ? "+
															"AND l.code = ?"
															).setParameter(0, new City(cityId)).setParameter(1, languageCode).uniqueResult();
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
	public Object load(long id) {//TODO:use another method...or at least don't use long fort the id field...
		return session.load(City.class, new Integer(String.valueOf(id)));
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.db.newschema.AbstractDAO#save(java.lang.Object)
	 */
	public Object save(Object toSave) {
		// TODO Auto-generated method stub
		return null;
	}

}//END OF FILE