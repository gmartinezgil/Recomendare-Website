/**
 * 
 */
package mx.com.recomendare.services.cache;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * @author jerry
 *
 */
public final class CacheService {
    //the cache...
    private Cache cache;
    //the log...
    //private static final Log log = LogFactory.getLog(CacheService.class);
    /**
     * @throws CacheException 
     * 
     */
    public CacheService(final String cacheName) throws CacheException {
        CacheManager cacheManager = CacheManager.create();
        cache = cacheManager.getCache(cacheName);
    /*
    String[] cacheNames = CacheManager.getInstance().getCacheNames();
    for (int i = 0; i < cacheNames.length; i++) {
    log.info(cacheNames[i]);
    }
     */
    }

    /**
     * 
     * @param key
     * @param value
     */
    public void addToCache(final Serializable key, final Serializable value) {
        cache.put(new Element(key, value));
    }

    /**
     * 
     * @param key
     * @return
     * @throws IllegalStateException
     * @throws CacheException
     */
    public Serializable getFromCache(final Serializable key) throws IllegalStateException, CacheException {
        Element cachedElement = cache.get(key);
        return (cachedElement != null) ? cachedElement.getValue() : null;
    }

    /**
     * 
     * @param key
     */
    public void removeFromCache(final Serializable key) {
        cache.remove(key);
    }
    
}//END OF FILE