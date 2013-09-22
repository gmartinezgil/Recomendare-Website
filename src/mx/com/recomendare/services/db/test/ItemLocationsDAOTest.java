package mx.com.recomendare.services.db.test;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import mx.com.recomendare.db.entities.ItemLocation;
import mx.com.recomendare.services.db.DatabaseService;
import mx.com.recomendare.services.db.ItemLocationsDAO;
import mx.com.recomendare.web.commons.models.LocationModelImpl;

public class ItemLocationsDAOTest extends TestCase {
private DatabaseService databaseService;
	
	protected void setUp() throws Exception {
		databaseService = new DatabaseService();
	}
	
	@SuppressWarnings("static-access")
	public void testGetItemLocationsNearByLocation() {
		ItemLocationsDAO itemLocationsDAO = databaseService.getPlacesDAO();
		itemLocationsDAO.setSession(databaseService.getSessionFactory().openSession());
		LocationModelImpl location = new LocationModelImpl();
		location.setLatitude(0.0000000000f);
		location.setLongitude(0.0000000000f);
		List<ItemLocation> results = itemLocationsDAO.getItemLocationsNearByLocation(location);
		Iterator<ItemLocation> iterator = results.iterator();
		while (iterator.hasNext()) {
			ItemLocation itemLocation = iterator.next();
			System.out.println(itemLocation.getId() + " - " +itemLocation.getLocationName());
		}
	}

}
