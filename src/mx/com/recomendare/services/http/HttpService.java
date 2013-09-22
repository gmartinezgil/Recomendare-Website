/**
 * 
 */
package mx.com.recomendare.services.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.services.cache.CacheService;
import net.sf.ehcache.CacheException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jerry
 *
 */
public final class HttpService extends AbstractService {
	//the multi threaded http service...
	private MultiThreadedHttpConnectionManager connectionManager;
	//the cache...
	private CacheService cacheService;
	
	//the log...
	private static final Log log = LogFactory.getLog(HttpService.class);

	/**
	 * 
	 */
	public HttpService() {
		doStart();
	}

	//********************************
	//*********SERVICE***************
	//********************************
	/**
	 * 
	 */
	public void doStart() {
		log.info("Starting - "+getClass().getName()+"...");
		if(!started)  {
			connectionManager =  new MultiThreadedHttpConnectionManager();
			try {
				cacheService = new CacheService("httpservice");
				started = true;
				log.info(getClass().getName()+"...started!");
			} catch (CacheException e) {
				log.error("Can't make use of the cache service - "+e.getMessage(), e);
				log.error(getClass().getName()+"...started failed!");
			}
		}
		else {
			log.error(getClass().getName()+"...already started!");
		}
	}

	/**
	 * 
	 */
	public void doStop() {
		if(started)  {
			log.info("Stoping - "+getClass().getName()+"...");
			connectionManager.shutdown();
			connectionManager = null;
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}
	
	/**
	 * 
	 * @param URL
	 * @return
	 */
	public String getContentAsStringFrom(final String URL)  {
		log.info("URL - "+URL);
		String response = (String)cacheService.getFromCache(URL);
		if(response != null)  {
			return response;
		}
		else  {
			HttpClient client = new HttpClient(connectionManager);
			//if behind a PROXY...development mode...
			//HostConfiguration hostConfiguration = client.getHostConfiguration();
			//hostConfiguration.setProxy("proxy.fonacot.gob.mx", 8080);
			//
	        GetMethod get = new GetMethod(URL);
	        get.addRequestHeader("Content-type", "text/xml; charset=UTF-8");
	        try {
	            int responseCode = client.executeMethod(get);
				log.info(responseCode);
				if(responseCode == 200)  {
					log.debug(get.getResponseCharSet());
					StringBuffer sb = new StringBuffer();
					String lineReaded = null;
					BufferedReader reader = new BufferedReader(new InputStreamReader(get.getResponseBodyAsStream(), "UTF-8"/*get.getResponseCharSet()*/));
					while((lineReaded = reader.readLine()) != null)  {
						sb.append(lineReaded);
					}
					response = sb.toString();
					cacheService.addToCache(URL, response);
					return response;
				}
	        } catch (HttpException e) {
				log.error("HTTP Error when getting the URL - "+URL+", message - "+e.getMessage(), e);
			} catch (IOException e) {
				log.error("Can't connect to the next URL - "+URL+", message - "+e.getMessage(), e);
			} finally {
	            get.releaseConnection();
	        }
		}
		return null;
	}
	
	/**
	 * 
	 * @param URL
	 * @return
	 */
	public byte[] getContenAsBytesFrom(final String URL)  {
		log.info("URL - "+URL);
		byte[] response = (byte[])cacheService.getFromCache(URL);
		if(response != null)  {
			return response;
		}
		else  {
			HttpClient client = new HttpClient(connectionManager);
			//if behind a PROXY...development mode...
			//HostConfiguration hostConfiguration = client.getHostConfiguration();
			//hostConfiguration.setProxy("proxy.fonacot.gob.mx", 8080);
			//
	        GetMethod get = new GetMethod(URL);
	        try {
	            int responseCode = client.executeMethod(get);
				log.info(responseCode);
				if(responseCode == 200)  {
					response = get.getResponseBody();
					cacheService.addToCache(URL, response);
					return response;
				}
	        } catch (HttpException e) {
				log.error("HTTP Error when getting the URL - "+URL+", message - "+e.getMessage(), e);
			} catch (IOException e) {
				log.error("Can't connect to the next URL - "+URL+", message - "+e.getMessage(), e);
			} finally {
	            get.releaseConnection();
	        }
		}
		return null;
	}
	
}//END OF FILE