package mx.com.recomendare.services.geo.test;

import junit.framework.TestCase;
import mx.com.recomendare.services.geo.GeocoderService;
import mx.com.recomendare.services.http.HttpService;

public class GeocoderServiceTest extends TestCase {
	private HttpService httpService = new HttpService();
	private GeocoderService geocoderService = new GeocoderService(httpService);

	public void testGetMultipleLocationsFor() {
		geocoderService.getMultipleLocationsFor("strret number", "street name", "city name", "city");
	}

}
