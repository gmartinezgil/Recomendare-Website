/**
 * 
 */
package mx.com.recomendare.services.amazon;

import java.util.ArrayList;
import java.util.List;

import mx.com.recomendare.services.AbstractService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.a2s.AmazonA2S;
import com.amazonaws.a2s.AmazonA2SClient;
import com.amazonaws.a2s.AmazonA2SException;
import com.amazonaws.a2s.AmazonA2SLocale;
import com.amazonaws.a2s.model.Item;
import com.amazonaws.a2s.model.ItemSearchRequest;
import com.amazonaws.a2s.model.ItemSearchResponse;
import com.amazonaws.a2s.model.Items;

/**
 * @author jerry
 *
 */
public final class AmazonAffiliateService extends AbstractService {
	//the log...
    private static final Log log = LogFactory.getLog(AmazonAffiliateService.class);
    
    private static final String ACCESS_KEY = "AFFILIATE_ACCESS_KEY";
    private static final String ASSOCIATE_TAG = "AFFILIATE_TAG";
    
    public static final String BOOKS_CATEGORY = "Books";
    public static final String MUSIC_CATEGORY = "Music";
    public static final String DVD_CATEGORY = "DVD";
    
    private AmazonA2S service;

    /**
     * 
     */
	public AmazonAffiliateService() {
		doStart();
	}
	
	//********************************
	//*********SERVICE***************
	//********************************
	/**
	 * 
	 */
	public void doStart() {
		log.info("Starting - "+getClass().getName()+"...");
		if(!started)  {
			service = new AmazonA2SClient(ACCESS_KEY, ASSOCIATE_TAG, AmazonA2SLocale.US);
			started = true;
			log.info(getClass().getName()+"...started!");
		}
		else  {
			log.error(getClass().getName()+"...already started!");
		}
	}
	
	/**
	 * 
	 */
	public void doStop() {
		if(started)  {
			log.info("Stoping - "+getClass().getName()+"...");
			service = null;
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}
    
	/**
	 * 
	 * @param description
	 * @param category
	 * @return
	 */
	public List<AmazonItemModel> searchItemsLikeDescription(final String description, final String category) {
		List<AmazonItemModel> respondedItems = new ArrayList<AmazonItemModel>();
		ItemSearchRequest request = new ItemSearchRequest();
		request.setSearchIndex(category);
		request.setKeywords(description);
		List<String> responseGroup = request.getResponseGroup();
		responseGroup.add("Images");
		responseGroup.add("ItemAttributes"); 
		responseGroup.add("Large");
		try  {
			ItemSearchResponse response = service.itemSearch(request);
			log.info(response);
			List<Items> responseItems = response.getItems();
			if(responseItems != null && responseItems.size() > 0)  {
				Items info = response.getItems().get(0);
				List<Item> items = info.getItem();
				for (int i = 0; i < items.size(); i++) {
					Item item = items.get(i);
					AmazonItemModel itemModel = new AmazonItemModel();
					//item.getItemAttributes().get
					itemModel.setASIN(item.getASIN());
					itemModel.setDetailPageURL(item.getDetailPageURL());
					itemModel.setAuthor((String)item.getItemAttributes().getAuthor().get(0));
					itemModel.setManufacturer(item.getItemAttributes().getManufacturer());
					itemModel.setTitle(item.getItemAttributes().getTitle());
					itemModel.setImageURL(item.getSmallImage().getURL());
					respondedItems.add(itemModel);
				}
			}
		}
		catch(AmazonA2SException ex)  {
			log.error("Can't establish communication with AAS - reason = "+ex.getMessage(), ex);
		}
		return respondedItems;
	}

}//END OF FILE