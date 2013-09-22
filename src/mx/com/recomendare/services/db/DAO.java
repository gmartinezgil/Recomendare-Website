package mx.com.recomendare.services.db;

import org.hibernate.Session;

/**
 * 
 * @author jerry
 *
 */
public interface DAO {
    /**
     * 
     * @param session
     */
    public void setSession(Session session);
    /**
     * 
     * @param id
     * @return Object
     */
    public Object load(long id);
    /**
     * 
     * @param toSave
     * @return Object
     */
    public Object save(Object toSave);
    /**
     * 
     * @param id
     */
    public void delete(long id);
    /**
     * 
     * @return int
     */
    public int count();
}//END OF FILE