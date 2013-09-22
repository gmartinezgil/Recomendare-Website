package mx.com.recomendare.services.flickr.test;

import junit.framework.TestCase;
import mx.com.recomendare.services.flickr.FlickrService;

public class TestFlickrService extends TestCase {
	private FlickrService service;

	protected void setUp() throws Exception {
		service = new FlickrService();
	}

	public void testGetPhotosFromLocation() {
		service.getPhotosFromLocation(0.000000f, 0.000000f, 10);
	}

}
