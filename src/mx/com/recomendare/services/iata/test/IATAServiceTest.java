package mx.com.recomendare.services.iata.test;

import java.io.File;

import junit.framework.TestCase;
import mx.com.recomendare.services.iata.IATAService;

public class IATAServiceTest extends TestCase {
	private IATAService service;

	protected void setUp() throws Exception {
		File f = new File("build"+File.separator+"classes"+File.separator+"mx"+File.separator+"com"+File.separator+"recomendare"+File.separator+"services"+File.separator+"iata"+File.separator+"IATA.txt");
		service = new IATAService(f.getAbsolutePath());
	}

	public void testGetCodeFromCityName() {
		System.out.println(service.getCodeFromCityName("Ciudad de Mexico"));
		System.out.println(service.getCodeFromCityName("Playa del Carmen"));
		System.out.println(service.getCodeFromCityName("Cancun"));
	}

}
