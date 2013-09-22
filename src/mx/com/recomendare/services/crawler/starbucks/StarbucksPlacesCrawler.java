/**
 * 
 */
package mx.com.recomendare.services.crawler.starbucks;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.web.places.WizardPlaceModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.xml.sax.SAXException;

/**
 * @author jerry
 *
 */
public final class StarbucksPlacesCrawler {
	//the dapp application published on the site...that parses the Starbucks locations in Mexico... 
	//private Dapp dapp;
	//the digester...
	private StarbucksPlacesDigester digester;
	
	//the log...
    private static final Log log = LogFactory.getLog(StarbucksPlacesCrawler.class);
    
	/**
	 * @throws DappExecutionErrorException 
	 * @throws JSONException 
	 * @throws NetworkErrorException 
	 * @throws InvalidVariableArgumentsException 
	 * @throws DappNotFoundException 
	 * 
	 */
	public StarbucksPlacesCrawler() /*throws DappNotFoundException, InvalidVariableArgumentsException, NetworkErrorException, JSONException, DappExecutionErrorException */{
		//System.setProperty("http.proxyHost", "HOST");
		//System.setProperty("http.proxyPort", "PORT");
		//dapp = new Dapp("StarbucksMexico");
		digester = new StarbucksPlacesDigester();
	}
	
	/**
	 * @throws SAXException 
	 * @throws IOException 
	 * 
	 */
	public void crawl() throws IOException, SAXException  {
		List<WizardPlaceModel> starbucksStores = digester.digest(/*dapp.getXML()*/);
		Iterator<WizardPlaceModel> iterator = starbucksStores.iterator();
		while (iterator.hasNext()) {
			WizardPlaceModel place = iterator.next();
			String streetName = place.getStreet();
			if(streetName.indexOf("No.") > 0) {
				int index = streetName.indexOf("No.");
				place.setStreet(streetName.substring(0, index));
				place.setStreetNumber(streetName.substring(index + 3, index + 6));
			}
			if(streetName.indexOf("Loc.") > 0) {
				int index = streetName.indexOf("Loc.");
				place.setStreet(streetName = streetName.substring(0, index));
				place.setStreetNumber(streetName.substring(index + 4, index + 8));
			}
			log.info(place.getName());
			log.info(place.getStreet());
			log.info(place.getStreetNumber());
			log.info(place.getPhoneNumber());
		}
	}

}//END OF FILE