/**
 * 
 */
package mx.com.recomendare.services.yahoo.boss;

import java.io.IOException;
import java.util.List;

import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.services.http.HttpService;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * @author jerry
 *
 */
public final class YahooBossSearchService extends AbstractService {
	private static final String API_KEY  = "API_KEY";
	private static final String URL = "http://boss.yahooapis.com/ysearch/web/v1/";
	
	//the log...
	private static final Log log = LogFactory.getLog(YahooBossSearchService.class);

	//the http service...
	private HttpService httpService;
	//the digester...
	private YahooBossDigester digester;
	
	/**
	 * 
	 */
	public YahooBossSearchService(final HttpService httpService) {
		this.httpService = httpService;
		doStart();
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.Service#doStart()
	 */
	public void doStart() {
		log.info("Starting - "+getClass().getName()+"...");
		if(!started)  {
			digester = new YahooBossDigester();
			started = true;
			log.info(getClass().getName()+"...started!");
		}
		else  {
			log.error(getClass().getName()+"...already started!");
		}
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.Service#doStop()
	 */
	public void doStop() {
		if(started)  {
			log.info("Stoping - "+getClass().getName()+"...");
			digester = null;
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}
	
	public List<YahooBossSearchResultModel> search(final String query, final String languageCode, final String countryCode, int start, int end)  {
		final String newURL = URL+WebUtil.encodeURL(query)+"?appid="+API_KEY+"&format=xml&start="+start+"&count="+end+"&lang="+	languageCode+"&region="+countryCode;//view=keyterms 
		String responseBody = httpService.getContentAsStringFrom(newURL);
		log.info(responseBody);
		try {
			return digester.digest(responseBody);
		} catch (IOException e) {
			log.error("Couldn't get io - "+newURL+", reason - "+e.getMessage(), e);
		} catch (SAXException e) {
			log.error("Couldn't parse xml from - "+newURL+", reason - "+e.getMessage(), e);
		}
		return null;
	}

}//END OF FILE