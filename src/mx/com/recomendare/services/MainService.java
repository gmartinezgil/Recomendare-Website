/**
 * 
 */
package mx.com.recomendare.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mx.com.recomendare.db.entities.Category;
import mx.com.recomendare.db.entities.Contact;
import mx.com.recomendare.db.entities.Item;
import mx.com.recomendare.db.entities.ItemComment;
import mx.com.recomendare.db.entities.ItemLocation;
import mx.com.recomendare.db.entities.ItemPicture;
import mx.com.recomendare.db.entities.Location;
import mx.com.recomendare.db.entities.Picture;
import mx.com.recomendare.db.entities.User;
import mx.com.recomendare.services.cache.CacheService;
import mx.com.recomendare.services.currency.CurrencyConverterService;
import mx.com.recomendare.services.db.CategoriesDAO;
import mx.com.recomendare.services.db.CitiesDAO;
import mx.com.recomendare.services.db.CountriesDAO;
import mx.com.recomendare.services.db.CurrenciesDAO;
import mx.com.recomendare.services.db.DatabaseService;
import mx.com.recomendare.services.db.ItemCommentsDAO;
import mx.com.recomendare.services.db.ItemsDAO;
import mx.com.recomendare.services.db.SubcategoriesDAO;
import mx.com.recomendare.services.db.UsersDAO;
import mx.com.recomendare.services.security.EncryptService;
import mx.com.recomendare.web.commons.models.AddressModelImpl;
import mx.com.recomendare.web.commons.models.BirthDateModelImpl;
import mx.com.recomendare.web.commons.models.CategoryModel;
import mx.com.recomendare.web.commons.models.CategoryModelImpl;
import mx.com.recomendare.web.commons.models.CommentModel;
import mx.com.recomendare.web.commons.models.CommentModelImpl;
import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.commons.models.ItemModelImpl;
import mx.com.recomendare.web.commons.models.LocationModelImpl;
import mx.com.recomendare.web.commons.models.PictureModel;
import mx.com.recomendare.web.commons.models.PictureModelImpl;
import mx.com.recomendare.web.commons.models.PriceModelImpl;
import mx.com.recomendare.web.commons.models.UserModel;
import mx.com.recomendare.web.commons.models.UserModelImpl;
import net.sf.ehcache.CacheException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

/**
 * @author jerry
 *
 */
public final class MainService extends AbstractService {
	//the log...
    private static final Log log = LogFactory.getLog(MainService.class);
    
    //the types of detail of item...
	public static final short PREVIEW_MODE = 0;
	public static final short DETAIL_MODE = 1;
	
	//where we get the data...
	private DatabaseService database;
	//and transform it...
	private CurrencyConverterService currencyConverter;
	private EncryptService encryptService;
	
	//and the cache...
	private CacheService cacheService;

	public MainService(final DatabaseService database, final CurrencyConverterService currencyConverter, final EncryptService encryptService)  {
		this.database = database;
		this.currencyConverter = currencyConverter;
		this.encryptService = encryptService;
		doStart();
	}
	
	/* (non-Javadoc)
	 * @see mx.com.maryger.services.Service#doStart()
	 */
	public void doStart() {
		log.info("Starting - "+getClass().getName()+"...");
		if(!started)  {
			try {
				cacheService = new CacheService("mainservice");
				started = true;
				log.info(getClass().getName()+"...started!");
			} catch (CacheException e) {
				log.error("Can't make use of the cache service, reason - "+e.getMessage(), e);
			}
		}
		else  {
			log.error(getClass().getName()+"...already started!");
		}
	}

	/* (non-Javadoc)
	 * @see mx.com.maryger.services.Service#doStop()
	 */
	public void doStop() {
		if(started)  {
			log.info("Stoping - "+getClass().getName()+"...");
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}
	
	/**
	 * 
	 * @param login
	 * @return
	 */
	@SuppressWarnings("static-access")
	public boolean alreadyExistsUserWithThisLogin(final String login) {
		final Session session = database.getSessionFactory().openSession();
		UsersDAO usersDAO = database.getUsersDAO();
		usersDAO.setSession(session);
		return usersDAO.existsUserWithThisLogin(login);
	}
	
	/**
	 * 
	 * @param screenNameToCompare
	 * @return
	 */
	@SuppressWarnings("static-access")
	public boolean alreadyExistsUserWithThisScreenName(final String screenNameToCompare)  {
		final Session session = database.getSessionFactory().openSession();
		UsersDAO usersDAO = database.getUsersDAO();
		usersDAO.setSession(session);
		return (usersDAO.getUserWithScreenName(screenNameToCompare) != null)?true:false;
	}
	
	/**
	 * 
	 * @param cityId
	 * @param countryCode
	 * @param languageCode
	 * @return
	 */
	@SuppressWarnings("static-access")
	public List<ItemModel> getAdvertisedItemModels(final int cityId, final String countryCode, final String languageCode) {
		List<ItemModel> results = null;
		final Session session = database.getSessionFactory().openSession();
		ItemsDAO itemsDAO = database.getItemsDAO();
		itemsDAO.setSession(session);
		List<Item> itemsFounded = itemsDAO.getAdvertisedItems(cityId, countryCode);
		if(itemsFounded != null && itemsFounded.size() > 0)  {
			results = new ArrayList<ItemModel>(itemsFounded.size());
			final Iterator <Item> iterator = itemsFounded.iterator();
			while(iterator.hasNext())  {
				final Item item = iterator.next();
				ItemModel itemModel = getItemModelFromItem(item, session, languageCode, countryCode, PREVIEW_MODE);
				results.add(itemModel);
			}
		}
		return results;
	}

	/**
	 * 
	 * @param itemId
	 * @param languageCode
	 * @param countryCode
	 * @param type
	 * @return
	 */
	@SuppressWarnings("static-access")
	public List<ItemModel> getRelatedItemsToThisItem(final int itemId, final String languageCode, final String countryCode, final short type)  {
		final Session session = database.getSessionFactory().openSession();
		ItemsDAO itemsDAO = database.getItemsDAO();
		itemsDAO.setSession(session);
		Item item = (Item)itemsDAO.load(itemId);
		final List<Item> items = itemsDAO.getItemsBySubcategory(String.valueOf(item.getSubcategory().getId()));
		List<ItemModel> results = new ArrayList<ItemModel>(items.size());
		final Iterator<Item> iterator = items.iterator();
		while (iterator.hasNext()) {//TODO:missing calculus including rating (better ratings) and nearby items by location (if apply)...
			Item itemRelated = (Item) iterator.next();
			ItemModel itemModel = getItemModelFromItem(itemRelated, session, languageCode, countryCode, type);
			results.add(itemModel);
		}
		return results;
	}
	
	/**
	 * 
	 * @param languageCode
	 * @return
	 */
	@SuppressWarnings("static-access")
	public List<CategoryModel> getAllCategories(final String languageCode)  {
		final Session session = database.getSessionFactory().openSession();
		final CategoriesDAO categoriesDAO = database.getCategoriesDAO();
		categoriesDAO.setSession(session);
		final List<Category> categories = categoriesDAO.getAllCategories(languageCode);
		List<CategoryModel> categoryModels = new ArrayList<CategoryModel>(categories.size());
		Iterator<Category> iterator = categories.iterator();
		while (iterator.hasNext()) {
			final Category category = (Category) iterator.next();
			CategoryModelImpl categoryModel = new CategoryModelImpl();
			categoryModel.setCategoryId(category.getId());
			categoryModel.setCategoryName(categoriesDAO.getCategoryName(category.getId(), languageCode));
			categoryModels.add(categoryModel);
		}
		return categoryModels;
	}
	
	/**
	 * 
	 * @param userId
	 * @param languageCode
	 * @param type
	 * @return
	 */
	@SuppressWarnings("static-access")
	public UserModel getUserModelById(final int userId, final String languageCode, final short type)  {
		UserModel userModel = (UserModel)cacheService.getFromCache(userId + languageCode + type);
		if(userModel != null)  {
			return userModel;
		}
		else {
			final Session session = database.getSessionFactory().openSession();
			final UsersDAO usersDAO = database.getUsersDAO();
			usersDAO.setSession(session);
			final User user = (User)usersDAO.load(userId);
			if(user != null)  {
				userModel = getUserModelFromUser(user, session, languageCode, type);
				cacheService.addToCache(userId + languageCode + type, userModel);
				return userModel;
			}
			return null;
		}
	}

	/**
	 * 
	 * @param cityId
	 * @param maxComments
	 * @param languageCode
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	public List<ItemModel> getLatestItemsComments(final int cityId, final int maxComments, final String languageCode)  {
		final Session session = database.getSessionFactory().openSession();
		final ItemCommentsDAO ratingsDAO = database.getRatingsDAO();
		ratingsDAO.setSession(session);
		final List<ItemComment> ratings = ratingsDAO.getLatestRatings(cityId, maxComments);
		List<ItemModel> itemsComments = new ArrayList<ItemModel>(ratings.size());
		final Iterator<ItemComment> iterator = ratings.iterator();
		while (iterator.hasNext()) {
			final ItemComment itemRating = iterator.next();
			final Item item = itemRating.getItem();
			ItemModelImpl itemModel = new ItemModelImpl();
			itemModel.setId(item.getId());
			itemModel.setName(item.getName());
			CommentModel commentModel = getCommentModelFromItemComment(itemRating, session, languageCode, PREVIEW_MODE);
			List<CommentModel> comments = new ArrayList<CommentModel>(1);
			comments.add(commentModel);
			itemModel.setComments(comments);
			itemsComments.add(itemModel);
		}
		return itemsComments;
	}
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("static-access")
	public int addUserModel(final UserModel model)  {
		final Session session = database.getSessionFactory().openSession();
		final UsersDAO usersDAO = database.getUsersDAO();
		usersDAO.setSession(session);
		UserModelImpl impl = (UserModelImpl) model;
		String encryptedPassword = encryptService.encrypt(model.getPassword());
		impl.setPassword((encryptedPassword != null)?encryptedPassword:model.getPassword());
		return usersDAO.createUser(impl);
	}
	
	/**
	 * 
	 * @param model
	 * @param userScreenName
	 * @return
	 */
	@SuppressWarnings("static-access")
	public int addItemModel(final ItemModel model, final String userScreenName)  {
		final Session session = database.getSessionFactory().openSession();
		final ItemsDAO itemsDAO = database.getItemsDAO();
		itemsDAO.setSession(session);
		return itemsDAO.createItem(model, userScreenName);
	}

	/**
	 * 
	 * @param cityId
	 * @param maxItems
	 * @param languageCode
	 * @param countryCode
	 * @return
	 */
	@SuppressWarnings("static-access")
	public List<ItemModel> getLatestItemModelsAdded(final int cityId, final short maxItems, final String languageCode, final String countryCode)  {
		List<ItemModel> results = null;
		final Session session = database.getSessionFactory().openSession();
		ItemsDAO itemsDAO = database.getItemsDAO();
		itemsDAO.setSession(session);
		List<Item> itemsFounded = itemsDAO.getLatestItemsAdded(cityId, countryCode, maxItems);
		if(itemsFounded != null && itemsFounded.size() > 0)  {
			results = new ArrayList<ItemModel>(itemsFounded.size());
			final Iterator <Item> iterator = itemsFounded.iterator();
			while(iterator.hasNext())  {
				final Item item = iterator.next();
				ItemModel itemModel = getItemModelFromItem(item, session, languageCode, countryCode, PREVIEW_MODE);
				results.add(itemModel);
			}
		}
		return results;
	}

	/**
	 * 
	 * @param keyword
	 * @param languageCode
	 * @param countryCode
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	public List<ItemModel> getItemModelsByKeyword(final String keyword, final String languageCode, final String countryCode)  {
		List<ItemModel> results = null;
		final Session session = database.getSessionFactory().openSession();
		final ItemsDAO itemsDAO = database.getItemsDAO();
		itemsDAO.setSession(session);
		List<Object> itemsFounded = itemsDAO.searchKeywordOnItems(keyword, languageCode);
		if(itemsFounded != null && itemsFounded.size() > 0)  {
			results = new ArrayList<ItemModel>(itemsFounded.size());
			final Iterator <Object> iterator = itemsFounded.iterator();
			while(iterator.hasNext())  {
				final Item item = (Item)iterator.next();
				ItemModel itemModel = getItemModelFromItem(item, session, languageCode, countryCode, PREVIEW_MODE);
				results.add(itemModel);
			}
		}
		return results;
	}

	/**
	 * 
	 * @param itemId
	 * @param languageCode
	 * @param countryCode
	 * @param type
	 * @return
	 */
	@SuppressWarnings("static-access")
	public ItemModel getItemModelById(final int itemId, final String languageCode, final String countryCode, final short type)  {
		ItemModel itemModel = (ItemModel)cacheService.getFromCache(itemId + countryCode + languageCode + type);
		if(itemModel != null)  {
			return itemModel;
		}
		else {
			final Session session = database.getSessionFactory().openSession();
			final ItemsDAO itemsDAO = database.getItemsDAO();
			itemsDAO.setSession(session);
			final Item item = itemsDAO.getItemById(itemId);
			if(item != null)  {
				itemModel = getItemModelFromItem(item, session, languageCode, countryCode, type);
				cacheService.addToCache(itemId + countryCode + languageCode + type, itemModel);
				return itemModel;
			}
			return null;
		}
	}
	
	//ITEM
	@SuppressWarnings("unchecked")
	private ItemModel getItemModelFromItem(final Item item, final Session session, final String languageCode, final String countryCode, final short type)  {
		ItemModelImpl itemModel = new ItemModelImpl();
		//id
		itemModel.setId(item.getId());
		//description
		itemModel.setName(item.getName());
		final ItemsDAO itemsDAO = database.getItemsDAO();
		itemsDAO.setSession(session);
		itemModel.setDescription(itemsDAO.getItemResume(item.getId(), languageCode));
		//pictures
		final Set<ItemPicture> pictures =  item.getItemPictures();
		if(pictures != null && pictures.size() > 0)  {
			List<PictureModel> modelPictures = new ArrayList<PictureModel>(pictures.size());
			//DETAIL
			if(type == DETAIL_MODE)  {
				Iterator<ItemPicture> iterator = pictures.iterator();
				while (iterator.hasNext()) {
					ItemPicture itemPicture = (ItemPicture) iterator.next();
					PictureModelImpl pictureModel = new PictureModelImpl();
					pictureModel.setContent(itemPicture.getPicture().getContent());
					modelPictures.add(pictureModel);
				}
			}
			else  {
				ItemPicture itemPicture = (ItemPicture)pictures.toArray()[0];
				PictureModelImpl pictureModel = new PictureModelImpl();
				pictureModel.setContent(itemPicture.getPicture().getContent());
				modelPictures.add(pictureModel);
			}
			itemModel.setPictures(modelPictures);
		}
		//ratings
		itemModel.setOverallRating(getOverallRatingFromItemComments(item.getItemComments()));
		itemModel.setNumberOfRatings(item.getItemComments().size());
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
			final CountriesDAO countriesDAO = database.getCountriesDAO();
			countriesDAO.setSession(session);
			locationModel.setCountryName(countriesDAO.getCountryName(itemLocation.getStreet().getZone().getCity().getCountry().getId(), languageCode));
			locationModel.setCountryCode(itemLocation.getStreet().getZone().getCity().getCountry().getCode());
			//city
			locationModel.setCityId(itemLocation.getStreet().getZone().getCity().getId());
			final CitiesDAO citiesDAO = database.getCitiesDAO();
			citiesDAO.setSession(session);
			locationModel.setCityName(citiesDAO.getCityName(itemLocation.getStreet().getZone().getCity().getId(), languageCode));
			//DETAIL
			if(type == DETAIL_MODE)  {
				//street
				AddressModelImpl addressModel = new AddressModelImpl();
				addressModel.setStreetName(itemLocation.getStreet().getName());
				addressModel.setStreetOutsideNumber(itemLocation.getStreetOutsideNumber());
				itemModel.setAddress(addressModel);
			}
			itemModel.setLocation(locationModel);
		}
		else  {//it's an item (CD, DVD, film, book, wine, etc.) with various locations where it's founded or sell them...or no location at all...
			//do nothing by the moment...
		}
		//categories
		CategoryModelImpl categoryModel = new CategoryModelImpl();
		//category
		categoryModel.setCategoryId(item.getSubcategory().getCategory().getId());
		final CategoriesDAO categoriesDAO = database.getCategoriesDAO();
		categoriesDAO.setSession(session);
		categoryModel.setCategoryName(categoriesDAO.getCategoryName(item.getSubcategory().getCategory().getId(), languageCode));
		//sub category
		categoryModel.setSubcategoryId(item.getSubcategory().getId());
		final SubcategoriesDAO subcategoriesDAO = database.getSubcategoriesDAO();
		subcategoriesDAO.setSession(session);
		categoryModel.setSubcategoryName(subcategoriesDAO.getSubcategoryName(item.getSubcategory().getId(), languageCode));
		itemModel.setCategory(categoryModel);
		//price
		final CurrenciesDAO currenciesDAO = database.getCurrenciesDAO();
		currenciesDAO.setSession(session);
		final String actualLocationCurrencyCode = currenciesDAO.getCurrencyCodeFromCountry(countryCode);
		final String originalCurrencyCode = item.getItemPriceByMinPriceId().getCurrency().getCode();
		PriceModelImpl priceModel = new PriceModelImpl();
		final double minPrice = item.getItemPriceByMinPriceId().getValue().doubleValue();
		priceModel.setMinPrice(currencyConverter.convertCurrencyFromCodeTo(minPrice, originalCurrencyCode, actualLocationCurrencyCode));
		final double maxPrice = item.getItemPriceByMaxPriceId().getValue().doubleValue();
		priceModel.setMaxPrice(currencyConverter.convertCurrencyFromCodeTo(maxPrice, originalCurrencyCode, actualLocationCurrencyCode));
		priceModel.setOriginalCurrencyCode(originalCurrencyCode);
		itemModel.setPrice(priceModel);
		//DETAIL
		if(type == DETAIL_MODE)  {
			if(itemModel.getLocation() != null)  {
				//address detail
				final Contact contact = (Contact)item.getItemContact().getContact();
				AddressModelImpl addressModel = (AddressModelImpl)itemModel.getAddress();
				addressModel.setTelephoneNumber(contact.getHomePhoneNumber());
				addressModel.setHomePageURL(contact.getHomepageAddress());
			}
			//comments
			List<CommentModel> comments = new ArrayList<CommentModel>();
			final Set<ItemComment> itemComments = item.getItemComments();
			if(itemComments != null && itemComments.size() > 0) {
				final Iterator <ItemComment> iterator = itemComments.iterator();
				while (iterator.hasNext()) {
					final ItemComment itemRating = iterator.next();
					CommentModel commentModel = getCommentModelFromItemComment(itemRating, session, languageCode, type);
					comments.add(commentModel);
				}
				itemModel.setComments(comments);
			}
		}
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
	
	//COMMENT
	private CommentModel getCommentModelFromItemComment(final ItemComment comment, final Session session, final String languageCode, final short type)  {
		CommentModelImpl commentModel = new CommentModelImpl();
		commentModel.setUserId(comment.getUser().getId());
		commentModel.setUserScreenName(comment.getUser().getUserConfiguration().getScreenName());
		commentModel.setRating(comment.getCalification());
		final ItemCommentsDAO ratingsDAO = database.getRatingsDAO();
		ratingsDAO.setSession(session);
		commentModel.setUserComments(ratingsDAO.getRatingComments(comment.getId(), languageCode));
		//picture
		final Picture picture = comment.getUser().getUserConfiguration().getPicture();
		if(picture != null)  {
			PictureModelImpl pictureModel = new PictureModelImpl();
			pictureModel.setContent(picture.getContent());
			commentModel.setUserAvatarPicture(pictureModel);
		}
		if(type == DETAIL_MODE)  {
			commentModel.setThumbsUp(comment.getRatedFavorably());
			commentModel.setThumbsDown(comment.getRatedNotFavorably());
		}
		return commentModel;
	}
	
	//USER
	private UserModel getUserModelFromUser(final User user, final Session session, final String languageCode, final short type) {
		UserModelImpl userModel = new UserModelImpl();
		userModel.setId(user.getId());
		userModel.setScreenName(user.getUserConfiguration().getScreenName());
		//picture
		Picture picture = user.getUserConfiguration().getPicture();
		if(picture != null)  {
			PictureModelImpl pictureModel = new PictureModelImpl();
			pictureModel.setContent(picture.getContent());
			userModel.setAvatarPicture(pictureModel);
		}
		//location
		LocationModelImpl locationModel = new LocationModelImpl();
		//latitude, longitude
		//locationModel.setLatitude(location.getLatitude());
		//locationModel.setLongitude(location.getLongitude());
		//country
		final CountriesDAO countriesDAO = database.getCountriesDAO();
		countriesDAO.setSession(session);
		locationModel.setCountryId(user.getUserConfiguration().getCity().getCountry().getId());
		locationModel.setCountryName(countriesDAO.getCountryName(user.getUserConfiguration().getCity().getCountry().getId(), languageCode));
		locationModel.setCountryCode(user.getUserConfiguration().getCity().getCountry().getCode());
		//city
		final CitiesDAO citiesDAO = database.getCitiesDAO();
		citiesDAO.setSession(session);
		locationModel.setCityId(user.getUserConfiguration().getCity().getId());
		locationModel.setCityName(citiesDAO.getCityName(user.getUserConfiguration().getCity().getId(), languageCode));
		//language
		locationModel.setLanguageId(user.getUserConfiguration().getLanguage().getId());
		locationModel.setLanguageName(user.getUserConfiguration().getLanguage().getName());
		locationModel.setLanguageCode(user.getUserConfiguration().getLanguage().getCode());
		userModel.setLocation(locationModel);
		//details
		if(type == DETAIL_MODE)  {
			userModel.setName(user.getPerson().getName());
			userModel.setFirstName(user.getPerson().getFirstname());
			userModel.setLastName(user.getPerson().getLastname());
			//gender
			userModel.setGenderId(user.getPerson().getConstant().getId());
			userModel.setGenderName(user.getPerson().getConstant().getKeyValue());
			//birth date
			BirthDateModelImpl birthDateModel = new BirthDateModelImpl();
			birthDateModel.setDay(user.getPerson().getBirthDay());
			birthDateModel.setMonth(user.getPerson().getBirthMonth());
			birthDateModel.setYear(user.getPerson().getBirthYear());
			userModel.setBirthDate(birthDateModel);
			//email
			AddressModelImpl addressModel = new AddressModelImpl();
			addressModel.setEmail(((Contact)user.getPerson().getContacts().toArray()[0]).getEmailAddress());//TODO:should be better from DAO query...
			userModel.setAddress(addressModel);
			//TODO:missing favorites, friends, requests, etc.
		}
		return userModel;
	}

}//END OF FILE