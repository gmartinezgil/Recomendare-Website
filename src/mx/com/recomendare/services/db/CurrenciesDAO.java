/**
 * 
 */
package mx.com.recomendare.services.db;

import java.util.List;

import mx.com.recomendare.db.entities.Currency;

/**
 * @author jerry
 *
 */
public final class CurrenciesDAO extends AbstractDAO {
	
	/**
	 * 
	 * @param languageCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Currency> getCurrencies() {
		return session.createQuery(
									"FROM Currency c"
									).list();
	}
	
	/**
	 * 
	 * @param currencyId
	 * @param languageCode
	 * @return
	 */
	public String getCurrencyName(int currencyId, String languageCode) {
		return (String)session.createQuery(
															"SELECT cn.name "+
															"FROM Currency c, CurrencyName cn, Language l "+
															"WHERE cn.currency = c "+
															"AND cn.language = l "+
															"AND c = ? "+
															"AND l.code = ?"
															).setParameter(0, new Currency(currencyId)).setParameter(1, languageCode).uniqueResult();
	}
	
	/**
	 * 
	 * @param countryCode
	 * @return
	 */
	public String getCurrencyCodeFromCountry(final String countryCode) {
		return (String)session.createQuery(
															"SELECT c.code "+
															"FROM Currency c, Country co "+
															"WHERE co.currency = c "+
															"AND co.code = ?"
															).setParameter(0, countryCode).uniqueResult();
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
		return session.load(Currency.class, new Integer(String.valueOf(id)));
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