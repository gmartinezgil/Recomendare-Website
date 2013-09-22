/**
 * 
 */
package mx.com.recomendare.services.opencellid;

import java.io.IOException;

import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.services.http.HttpService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * @author jerry
 *
 */
public final class OpenCellIdService extends AbstractService {
	//the log...
	private static final Log log = LogFactory.getLog(OpenCellIdService.class);
	//the URL to connect...
	private static final String URL = "http://www.opencellid.org/cell/get?";
	
	//the http service...
	private HttpService httpService;
	//the digester...
	private OpenCellIdDigester digester;
	
	/**
	 * 
	 */
	public OpenCellIdService(final HttpService httpService) {
		this.httpService = httpService;
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
			digester = new OpenCellIdDigester();
			started = true;
			log.info(getClass().getName()+"...started!");
		}
		else  {
			log.error(getClass().getName()+"...already started!");
		}
	}
	
	/**
	 * 
	 */
	public void doStop() {
		if(started)  {
			log.info("Stoping - "+getClass().getName()+"...");
			digester = null;
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}
	
	public OpenCellIdModel getLocationFromCellPosition(final String cellId, final String lac, final String mcc, final String mnc)  {
		final String newURL = URL+"cellid="+cellId+"&mcc="+mcc+"&mnc="+mnc+"&lac="+lac;
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