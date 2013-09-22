/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.recomendare.services.db;

import java.util.List;

import mx.com.recomendare.db.entities.City;
import mx.com.recomendare.db.entities.Street;

/**
 *
 * @author jerry
 */
public final class StreetsDAO extends AbstractDAO {
    
    /**
     * 
     * @param cityId
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Street> getStreetsByCityId(int cityId) {
        return session.createQuery("FROM Street s WHERE s.city = ? ORDER BY s.name").setParameter(0, new City(cityId)).list();
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
