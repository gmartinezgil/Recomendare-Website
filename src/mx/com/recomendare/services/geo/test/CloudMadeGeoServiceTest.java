package mx.com.recomendare.services.geo.test;

import java.util.Iterator;

import junit.framework.TestCase;
import mx.com.recomendare.services.geo.CloudMadeGeoService;

import com.cloudmade.api.geometry.Point;
import com.cloudmade.api.routing.Route;
import com.cloudmade.api.routing.RouteInstruction;

public class CloudMadeGeoServiceTest extends TestCase {
	private CloudMadeGeoService geoService;
	protected void setUp() throws Exception {
		geoService = new CloudMadeGeoService();
	}

	/*
	public void testGetRouteFromStartEndPoints() {
		Route route = geoService.getRouteFromStartEndPoints(0.000000f, 0.000000f, 0.000000f, 0.000000f, "es", (short)0,(short)0);
		System.out.println(route.summary.totalDistance);
		System.out.println(route.summary.totalTime);
		System.out.println(route.summary.startPoint);
		System.out.println(route.summary.endPoint);
		
		Iterator<RouteInstruction> iterator = route.instructions.iterator();
		while (iterator.hasNext()) {
			RouteInstruction routeInstruction = (RouteInstruction) iterator.next();
			System.out.println(routeInstruction.instruction);
		}
		
		Iterator<Point> iterator1 = route.geometry.points.iterator();
		while (iterator1.hasNext()) {
			Point point = (Point) iterator1.next();
			System.out.println(point.lat);
			System.out.println(point.lon);
			System.out.println("----------------");
		}
		
		
		/*
		Iterator<TransitPoint> iterator = route.summary.transitPoints.iterator();
		while (iterator.hasNext()) {
			TransitPoint transitPoint = (TransitPoint) iterator.next();
			System.out.println(transitPoint.lat);
			System.out.println(transitPoint.lon);
			System.out.println("----------------------------");
		}
	}
*/
	
	public void testGetMultiplesLocationsFor()  {
		geoService.getMultipleLocationsFor("street number", "street name", "city name", "country name");
	}

}