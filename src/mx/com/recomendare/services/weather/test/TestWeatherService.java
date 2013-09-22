package mx.com.recomendare.services.weather.test;

import java.rmi.RemoteException;

import junit.framework.TestCase;
import mx.com.recomendare.services.weather.WeatherModel;
import mx.com.recomendare.services.weather.WeatherService;

public class TestWeatherService extends TestCase {
	private WeatherService service;

	protected void setUp() throws Exception {
		service = new WeatherService();
	}

	public void testGetWeatherConditionsByCity() throws RemoteException {
		WeatherModel model = service.getWeatherConditionsByCityCountry("Clisson", "France");//"Mexico City", "Mexico"
		System.out.println(model);
	}

}
