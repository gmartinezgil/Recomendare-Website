package mx.com.recomendare.services.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

/**
 * 
 * @author jerry
 *
 */
public abstract class AbstractDAO implements DAO {
    //the session factory used by this DAO...
    protected Session session;
    
    //log...
    protected static final Log log = LogFactory.getLog(AbstractDAO.class);

    /**
     * 
     */
    public void setSession(Session session) {
        this.session = session;
    }
    
    /**
     * Helper method for retrieving hibernate session
     * 
     * @return Session
     */
    protected Session getSession() {
        return session;
    }

    /**
     * 
     */
    public void delete(long id) {
        getSession().delete(load(id));
    }

    public abstract int count();

    public abstract Object load(long id);

    public abstract Object save(Object toSave);

}//END OF FILE