package mx.com.recomendare.services.eventful.test;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import mx.com.recomendare.services.eventful.EventfulModel;
import mx.com.recomendare.services.eventful.EventfulService;

public class EventfulServiceTest extends TestCase {
	private EventfulService eventfulService;
	
	protected void setUp() throws Exception {
		eventfulService = new EventfulService();
	}

	public void testGetEventsOnCity() {
		List<EventfulModel> events = eventfulService.getEventsFromPlace("Mexico City", "Mexico", null, null, null);
		Iterator<EventfulModel> iterator = events.iterator();
		while (iterator.hasNext()) {
			EventfulModel event = iterator.next();
			System.out.println("title - "+event.getTitle());
			System.out.println("description - "+event.getDescription());
			System.out.println();
		}
	}
	
}