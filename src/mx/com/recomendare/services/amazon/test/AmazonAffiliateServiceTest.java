package mx.com.recomendare.services.amazon.test;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import mx.com.recomendare.services.amazon.AmazonAffiliateService;
import mx.com.recomendare.services.amazon.AmazonItemModel;

public class AmazonAffiliateServiceTest extends TestCase {
	private AmazonAffiliateService affiliate;
	
	protected void setUp() throws Exception {
		affiliate = new AmazonAffiliateService();
	}

	public void testSearchItemsLikeDescription() {
		List<AmazonItemModel> items = affiliate.searchItemsLikeDescription("java", AmazonAffiliateService.BOOKS_CATEGORY);
		Iterator<AmazonItemModel> iterator = items.iterator();
		while (iterator.hasNext()) {
			AmazonItemModel model = iterator.next();
			System.out.println(/*model.getDetailPageURL() + " - " + */model.getTitle());
		}
	}

}//END OF FILE