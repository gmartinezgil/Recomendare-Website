/**
 * 
 */
package mx.com.recomendare.services.opencellid;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * @author jerry
 *
 */
public final class OpenCellIdDigester {

	private Digester digester;
	/**
	 * 
	 */
	public OpenCellIdDigester() {
		digester = new Digester();
		digester.setValidating(false);
		setRules();
	}
	
	private void setRules()  {
		digester.addObjectCreate("rsp/cell", OpenCellIdModel.class);
		digester.addSetProperties("rsp/cell", "mcc", "mcc");
		digester.addSetProperties("rsp/cell", "mnc", "mnc");
		digester.addSetProperties("rsp/cell", "cellId", "cellId");
		digester.addSetProperties("rsp/cell", "lac", "lac");
		digester.addSetProperties("rsp/cell", "lat", "lat");
		digester.addSetProperties("rsp/cell", "lon", "lon");
		digester.addSetProperties("rsp/cell", "range", "range");
	}
	
	public OpenCellIdModel digest(final String xmlContent) throws IOException, SAXException  {
		return (OpenCellIdModel)digester.parse(new StringReader(xmlContent));
	}
	
	public OpenCellIdModel digest() throws IOException, SAXException  {
		return (OpenCellIdModel)digester.parse(getClass().getResourceAsStream("opencellid.xml"));
	}

}//END OF FILE