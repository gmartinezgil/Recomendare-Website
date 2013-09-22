/**
 * 
 */
package mx.com.recomendare.services.recommender;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mx.com.recomendare.db.entities.Item;
import mx.com.recomendare.db.entities.ItemComment;
import mx.com.recomendare.db.entities.ItemLocation;
import mx.com.recomendare.db.entities.ItemPicture;
import mx.com.recomendare.db.entities.Location;
import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.services.cache.CacheService;
import mx.com.recomendare.services.db.CategoriesDAO;
import mx.com.recomendare.services.db.CitiesDAO;
import mx.com.recomendare.services.db.CountriesDAO;
import mx.com.recomendare.services.db.DatabaseService;
import mx.com.recomendare.services.db.ItemsDAO;
import mx.com.recomendare.services.db.SubcategoriesDAO;
import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.commons.models.CategoryModelImpl;
import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.commons.models.ItemModelImpl;
import mx.com.recomendare.web.commons.models.LocationModelImpl;
import mx.com.recomendare.web.commons.models.PictureModel;
import mx.com.recomendare.web.commons.models.PictureModelImpl;
import mx.com.recomendare.web.commons.models.RecommendedItemModel;
import mx.com.recomendare.web.commons.models.RecommendedItemModelImpl;
import net.sf.ehcache.CacheException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.planetj.taste.common.TasteException;
import com.planetj.taste.correlation.UserCorrelation;
import com.planetj.taste.impl.correlation.AveragingPreferenceInferrer;
import com.planetj.taste.impl.correlation.PearsonCorrelation;
import com.planetj.taste.impl.neighborhood.NearestNUserNeighborhood;
import com.planetj.taste.impl.recommender.CachingRecommender;
import com.planetj.taste.impl.recommender.GenericUserBasedRecommender;
import com.planetj.taste.impl.recommender.slopeone.MemoryDiffStorage;
import com.planetj.taste.impl.recommender.slopeone.SlopeOneRecommender;
import com.planetj.taste.model.DataModel;
import com.planetj.taste.neighborhood.UserNeighborhood;
import com.planetj.taste.recommender.RecommendedItem;
import com.planetj.taste.recommender.Recommender;

/**
 * @author jerry
 *
 */
public final class RecommenderService extends AbstractService {
    //the recommender...
    private Recommender recommender;
    //the cache...
    private CacheService cacheService;
    //the database service...
    private DatabaseService databaseService;
    //the user's datamodel...
    private DataModel usersDataModel;
    
    //the log...
    private static final Log log = LogFactory.getLog(RecommenderService.class);

    /**
     * 
     */
    public RecommenderService(final DatabaseService databaseService) {
    	this.databaseService = databaseService;
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
			usersDataModel = new UserRecommendationDataModel(databaseService);
			try {
				recommender = new SlopeOneRecommender(usersDataModel, false, false, new MemoryDiffStorage(usersDataModel, false, false, 100));
				cacheService = new CacheService("recommender");
				started = true;
				log.info(getClass().getName()+"...started!");
			} catch (TasteException e) {
				log.error("Taste recommender engine error, reason - " + e.getMessage(), e);
			} catch (CacheException e) {
				log.error("Can't access the cache service, reason - " + e.getMessage(), e);
			}
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
			recommender = null;
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}

    /**
     * 
     * @param userId
     * @return
     * @throws TasteException 
     */
	@SuppressWarnings("unchecked")
	public List<RecommendedItemModel> getRecommendationsToUser(final String userScreenName) throws RecommenderServiceException {
        if (userScreenName != null && userScreenName.trim().length() > 0) {
            List<RecommendedItemModel> recommendationsToUser = null;
            try {
                recommendationsToUser = (List<RecommendedItemModel>) cacheService.getFromCache(userScreenName);
                if (recommendationsToUser != null) {
                    return recommendationsToUser;
                } else {
                    recommendationsToUser = new ArrayList<RecommendedItemModel>();
                    long start = System.currentTimeMillis();
                    final List<RecommendedItem> recommendations = recommender.recommend(userScreenName, Constants.MAX_RECOMMENDATIONS_PER_PAGE);
                    if (recommendations != null && recommendations.size() > 0) {
                        final Iterator<RecommendedItem> iterator = recommendations.iterator();
                        while (iterator.hasNext()) {
                            RecommendedItem recommendation = iterator.next();//TODO: have a RecommendedItemModel to describe the recommendations to the other area...
                            RecommendedItemModel item = new RecommendedItemModelImpl();
                            item.setId(Integer.parseInt(recommendation.getItem().getID().toString()));
                            item.setRecommendedValue(recommendation.getValue());
                            recommendationsToUser.add(item);
                        }
                        cacheService.addToCache(userScreenName, (Serializable) recommendationsToUser);
                    }
                    log.info("Recommendation took " + (System.currentTimeMillis() - start) + " ms");
                    return recommendationsToUser;
                }
            } catch (TasteException e) {
                log.error("Taste recommender engine error, reason - " + e.getMessage(), e);
                throw new RecommenderServiceException(e);
            } catch (IllegalStateException e) {
                log.error("Can't access the cache, reason - " + e.getMessage(), e);
                throw new RecommenderServiceException(e);
            } catch (CacheException e) {
                log.error("Can't retrieve the recommendations from the following user - " + userScreenName, e);
                throw new RecommenderServiceException(e);
            }
        }
        return null;
    }
	
	/**
	 * 
	 * @param toUserScreenName
	 * @param languageCode
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	public List<ItemModel> getUserRecommendedItemsModelsToUser(final String toUserScreenName, final String languageCode)  {
		UserCorrelation userCorrelation = new PearsonCorrelation(usersDataModel);
		try {
			userCorrelation.setPreferenceInferrer(new AveragingPreferenceInferrer(usersDataModel));
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, userCorrelation, usersDataModel);
			recommender = new GenericUserBasedRecommender(usersDataModel, neighborhood, userCorrelation);
			Recommender cachingRecommender = new CachingRecommender(recommender);
			
			//TODO:use the cache to see if it's already recommended to this user by it's screen name... 
			final List<RecommendedItem> recommendations = cachingRecommender.recommend(toUserScreenName, Constants.MAX_RECOMMENDATIONS_PER_PAGE);
			if(recommendations != null && recommendations.size() > 0)  {
				final Iterator<RecommendedItem> iterator = recommendations.iterator();
				final Session session = databaseService.getSessionFactory().openSession();
				final ItemsDAO itemsDAO = databaseService.getItemsDAO();
				itemsDAO.setSession(session);
				while (iterator.hasNext()) {
					final RecommendedItem recommendedItem = iterator.next();
					final Item item = (Item)itemsDAO.load(Integer.parseInt(recommendedItem.getItem().getID().toString()));
					ItemModelImpl itemModel = getItemModelFromItem(item, session, languageCode);
					
				}
			}
		} catch (TasteException e) {
			log.error("Taste recommender engine error, reason - " + e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked" })
	private ItemModelImpl getItemModelFromItem(final Item item, final Session session, final String languageCode)  {
		ItemModelImpl itemModel = new ItemModelImpl();
		//general
		itemModel.setId(item.getId());
		itemModel.setName(item.getName());
		//rating
		itemModel.setOverallRating(getOverallRatingFromItemComments(item.getItemComments()));
		itemModel.setNumberOfRatings(item.getItemComments().size());
		//picture
		final Set<ItemPicture> pictures =  item.getItemPictures();
		if(pictures != null && pictures.size() > 0)  {
			List<PictureModel> modelPictures = new ArrayList<PictureModel>(pictures.size());
			ItemPicture itemPicture = (ItemPicture)pictures.toArray()[0];
			PictureModelImpl pictureModel = new PictureModelImpl();
			pictureModel.setContent(itemPicture.getPicture().getContent());
			modelPictures.add(pictureModel);
			itemModel.setPictures(modelPictures);
		}
		//location
		final ItemLocation itemLocation = item.getItemLocation();
		if(itemLocation != null)  {//it's a place where it's located a service (doctor, laundry, beauty salon, motel, etc.) or a restaurant...
			final Location location = itemLocation.getLocation();
			LocationModelImpl locationModel = new LocationModelImpl();
			//latitude, longitude
			locationModel.setLatitude(location.getLatitude().floatValue());
			locationModel.setLongitude(location.getLongitude().floatValue());
			//country
			locationModel.setCountryId(itemLocation.getStreet().getZone().getCity().getCountry().getId());
			final CountriesDAO countriesDAO = databaseService.getCountriesDAO();
			countriesDAO.setSession(session);
			locationModel.setCountryName(countriesDAO.getCountryName(itemLocation.getStreet().getZone().getCity().getCountry().getId(), languageCode));
			locationModel.setCountryCode(itemLocation.getStreet().getZone().getCity().getCountry().getCode());
			//city
			locationModel.setCityId(itemLocation.getStreet().getZone().getCity().getId());
			final CitiesDAO citiesDAO = databaseService.getCitiesDAO();
			citiesDAO.setSession(session);
			locationModel.setCityName(citiesDAO.getCityName(itemLocation.getStreet().getZone().getCity().getId(), languageCode));
			itemModel.setLocation(locationModel);
		}
		else  {//it's an item (CD, DVD, film, book, wine, etc.) with various locations where it's founded or sell them...or no location at all...
			//do nothing by the moment...
		}
		//categories
		CategoryModelImpl categoryModel = new CategoryModelImpl();
		//category
		categoryModel.setCategoryId(item.getSubcategory().getCategory().getId());
		final CategoriesDAO categoriesDAO = databaseService.getCategoriesDAO();
		categoriesDAO.setSession(session);
		categoryModel.setCategoryName(categoriesDAO.getCategoryName(item.getSubcategory().getCategory().getId(), languageCode));
		//sub category
		categoryModel.setSubcategoryId(item.getSubcategory().getId());
		final SubcategoriesDAO subcategoriesDAO = databaseService.getSubcategoriesDAO();
		subcategoriesDAO.setSession(session);
		categoryModel.setSubcategoryName(subcategoriesDAO.getSubcategoryName(item.getSubcategory().getId(), languageCode));
		itemModel.setCategory(categoryModel);
		return itemModel;
	}
	
	//RATING
	private short getOverallRatingFromItemComments(final Set<ItemComment> comments)  {
		if(comments != null && comments.size() > 0)  {
			//get the ratings...the average...
			short numberOfStars = 0;
			Iterator<ItemComment> iterator = comments.iterator();
			while (iterator.hasNext()) {
				ItemComment comment = iterator.next();
				numberOfStars += comment.getCalification().shortValue();
			}
			return ((short)(numberOfStars / comments.size()));
		}
		else {
			return ((short)0);
		}
	}
    
}//END OF FILE