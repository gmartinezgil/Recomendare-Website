/**
 * 
 */
package mx.com.recomendare.services.geo;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import mx.com.recomendare.web.commons.models.LocationModelImpl;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * @author jerry
 *
 */
public final class GeoNamesDigester {
	//the digester...
	private Digester digester;
	/**
	 * 
	 */
	public GeoNamesDigester() {
		digester = new Digester();
		digester.setValidating(false);
	}
	
	private void setRulesGeoNamePlace()  {
		digester.clear();
		digester.addObjectCreate("geonames", ArrayList.class);
		digester.addObjectCreate("geonames/geoname", PlaceModel.class);
		digester.addBeanPropertySetter("geonames/geoname/name", "name");
		digester.addBeanPropertySetter("geonames/geoname/lat", "latitude");
		digester.addBeanPropertySetter("geonames/geoname/lon", "longitude");
		digester.addBeanPropertySetter("geonames/geoname/countryCode", "countryCode");
		digester.addBeanPropertySetter("geonames/geoname/countryName", "countryName");
		digester.addSetNext("geonames/geoname", "add");
	}
	
	private void setRulesWikipediaEntryPlace()  {
		digester.clear();
		digester.addObjectCreate("geonames", ArrayList.class);
		digester.addObjectCreate("geonames/entry", PlaceModel.class);
		digester.addBeanPropertySetter("geonames/entry/title", "name");
		digester.addBeanPropertySetter("geonames/entry/lat", "latitude");
		digester.addBeanPropertySetter("geonames/entry/lon", "longitude");
		digester.addBeanPropertySetter("geonames/entry/countryCode", "countryCode");
		digester.addBeanPropertySetter("geonames/entry/wikipediaUrl", "URL");
		digester.addBeanPropertySetter("geonames/entry/thumbnailImg", "imageURL");
		digester.addSetNext("geonames/entry", "add");
	}
	
	private void setRulesGeoCountrySubdivision() {
		digester.clear();
		digester.addObjectCreate("geonames/countrySubdivision", LocationModelImpl.class);
		digester.addBeanPropertySetter("geonames/countrySubdivision/countryCode", "countryCode");
		digester.addBeanPropertySetter("geonames/countrySubdivision/countryName", "countryName");
		digester.addBeanPropertySetter("geonames/countrySubdivision/adminName1", "cityName");
	}
	
	/**
	 * 
	 * @param xmlContent
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 */
	@SuppressWarnings("unchecked")
	public List<PlaceModel> digestGeoNamePlace(final String xmlContent) throws IOException, SAXException  {
		setRulesGeoNamePlace();
		return (List<PlaceModel>)digester.parse(new StringReader(xmlContent));
	}
	
	/**
	 * 
	 * @param xmlContent
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 */
	@SuppressWarnings("unchecked")
	public List<PlaceModel> digestWikipediaEntryPlace(final String xmlContent) throws IOException, SAXException  {
		setRulesWikipediaEntryPlace();
		return (List<PlaceModel>)digester.parse(new StringReader(xmlContent));
	}
	
	/**
	 * 
	 * @param xmlContent
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 */
	public LocationModelImpl digestGeoCountrySubdivision(final String xmlContent) throws IOException, SAXException  {
		setRulesGeoCountrySubdivision();
		return (LocationModelImpl)digester.parse(new StringReader(xmlContent));
	}
	
}//END OF FILE