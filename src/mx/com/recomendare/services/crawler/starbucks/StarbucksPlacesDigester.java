/**
 * 
 */
package mx.com.recomendare.services.crawler.starbucks;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import mx.com.recomendare.web.places.WizardPlaceModel;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * @author jerry
 *
 */
public final class StarbucksPlacesDigester {

	private Digester digester;
	/**
	 * 
	 */
	public StarbucksPlacesDigester() {
		digester = new Digester();
		digester.setValidating(false);
		setRules();
	}
	
	@SuppressWarnings("unchecked")
	public List<WizardPlaceModel> digest(final String xmlContent) throws IOException, SAXException  {
		return (List<WizardPlaceModel>)digester.parse(new StringReader(xmlContent));
	}
	
	@SuppressWarnings("unchecked")
	public List<WizardPlaceModel> digest() throws IOException, SAXException  {
		return (List<WizardPlaceModel>)digester.parse(getClass().getResourceAsStream("starbucks.xml"));
	}
	
	private void setRules()  {
		digester.addObjectCreate("elements", ArrayList.class);
		digester.addObjectCreate("elements/Starbuck_Store", WizardPlaceModel.class);//TODO:talk to the implementation...create a PlaceModelImpl...and let these as an interface...
		digester.addBeanPropertySetter("elements/Starbuck_Store/Store_Name", "name");
		digester.addBeanPropertySetter("elements/Starbuck_Store/Store_Address", "street");
		digester.addBeanPropertySetter("elements/Starbuck_Store/Store_Phone_Number", "phoneNumber");
		digester.addSetNext("elements/Starbuck_Store", "add");
	}

}//END OF FILE