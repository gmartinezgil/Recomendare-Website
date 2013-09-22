/**
 * 
 */
package mx.com.recomendare.services.yahoo.boss;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * @author jerry
 *
 */
public final class YahooBossDigester {

	private Digester digester;
	/**
	 * 
	 */
	public YahooBossDigester() {
		digester = new Digester();
		digester.setValidating(false);
		setRulesDigester();
	}
	
	void setRulesDigester()  {
		digester.addObjectCreate("ysearchresponse/resultset_web", ArrayList.class);
		digester.addObjectCreate("ysearchresponse/resultset_web/result", YahooBossSearchResultModel.class);
		digester.addBeanPropertySetter("ysearchresponse/resultset_web/result/abstract", "description");
		digester.addBeanPropertySetter("ysearchresponse/resultset_web/result/clickurl", "clickurl");
		digester.addBeanPropertySetter("ysearchresponse/resultset_web/result/date", "date");
		digester.addBeanPropertySetter("ysearchresponse/resultset_web/result/dispurl", "dispurl");
		digester.addBeanPropertySetter("ysearchresponse/resultset_web/result/title", "title");
		digester.addBeanPropertySetter("ysearchresponse/resultset_web/result/url", "url");
		digester.addSetNext("ysearchresponse/resultset_web/result", "add");
	}
	
	@SuppressWarnings("unchecked")
	public List<YahooBossSearchResultModel> digest(final String xmlContent) throws IOException, SAXException  {
		return (List<YahooBossSearchResultModel>)digester.parse(new StringReader(xmlContent));
	}
	
	@SuppressWarnings("unchecked")
	public List<YahooBossSearchResultModel> digest() throws IOException, SAXException  {
		return (List<YahooBossSearchResultModel>)digester.parse(getClass().getResourceAsStream("yahooboss.xml"));
	}

}//END OF FILE