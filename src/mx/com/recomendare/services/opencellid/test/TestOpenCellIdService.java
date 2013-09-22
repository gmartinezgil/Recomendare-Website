package mx.com.recomendare.services.opencellid.test;

import java.io.IOException;

import junit.framework.TestCase;
import mx.com.recomendare.services.opencellid.OpenCellIdDigester;
import mx.com.recomendare.services.opencellid.OpenCellIdModel;

import org.xml.sax.SAXException;

public class TestOpenCellIdService extends TestCase {
	private OpenCellIdDigester digester;

	protected void setUp() throws Exception {
		digester = new OpenCellIdDigester();
	}

	public void testDigest() {
		try {
			OpenCellIdModel model = digester.digest();
			System.out.println(model.getLat());
			System.out.println(model.getLon());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
