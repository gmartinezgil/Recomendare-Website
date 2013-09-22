/**
 * 
 */
package mx.com.recomendare.services.db;

import java.util.List;

import mx.com.recomendare.db.entities.Country;

/**
 * @author jerry
 *
 */
public final class CountriesDAO extends AbstractDAO {
	/**
	 * Gets a list of countries supported by the system...
	 * @param languageCode 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Country> getAllCountries(final String languageCode)  {
		return session.createQuery(
									"SELECT c " +
									"FROM Country c, CountryName cn, Language l " +
									"WHERE cn.country = c "+
									"AND cn.language = l "+
									"AND l.code = ? "+
									"ORDER BY cn.name"
								).setParameter(0, languageCode).list();
	}

	/**
	 * 
	 * @param countryCode
	 * @return
	 */
	public Country getCountryByCode(final String countryCode) {
		return (Country)session.createQuery(
											"FROM Country c " +
											"WHERE c.code = ?"
											).setParameter(0, countryCode).setCacheable(true).uniqueResult();
	}
	
	/**
	 * 
	 * @param countryId
	 * @param languageCode
	 * @return
	 */
	public String getCountryName(int countryId, String languageCode) {
		return (String)session.createQuery(
												"SELECT cn.name "+
												"FROM Country c, CountryName cn, Language l "+
												"WHERE cn.country = c "+
												"AND cn.language = l "+
												"AND c = ? "+
												"AND l.code = ?"
											).setParameter(0, new Country(countryId)).setParameter(1, languageCode).uniqueResult();
	}

	/**
	 * 
	 * @param countryCode
	 * @return
	 */
	public boolean isSupportedCountry(final String countryCode)  {
		Boolean isCountrySupported = (Boolean)session.createQuery(
																	"SELECT c.isSupported "+
																	"FROM Country c "+
																	"WHERE c.code = ? "
																	).setParameter(0, countryCode).uniqueResult(); 
		return isCountrySupported.booleanValue();
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