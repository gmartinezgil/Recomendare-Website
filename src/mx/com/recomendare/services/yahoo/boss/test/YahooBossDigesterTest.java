package mx.com.recomendare.services.yahoo.boss.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import mx.com.recomendare.services.yahoo.boss.YahooBossDigester;
import mx.com.recomendare.services.yahoo.boss.YahooBossSearchResultModel;

import org.xml.sax.SAXException;

public class YahooBossDigesterTest extends TestCase {
	private YahooBossDigester yahooBossDigester;
	
	protected void setUp() throws Exception {
		yahooBossDigester = new YahooBossDigester();
	}

	public void testDigest() throws IOException, SAXException {
		List<YahooBossSearchResultModel> results = yahooBossDigester.digest();
		Iterator<YahooBossSearchResultModel> iterator = results.iterator();
		while (iterator.hasNext()) {
			YahooBossSearchResultModel result = iterator.next();
			System.out.println("title - "+result.getTitle());
			System.out.println("description - "+result.getDescription());
			System.out.println("url - "+result.getUrl());
			System.out.println();
		}
	}

}
