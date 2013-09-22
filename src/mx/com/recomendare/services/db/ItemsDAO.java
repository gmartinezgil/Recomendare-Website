/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.recomendare.services.db;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mx.com.recomendare.db.entities.City;
import mx.com.recomendare.db.entities.Contact;
import mx.com.recomendare.db.entities.Currency;
import mx.com.recomendare.db.entities.Item;
import mx.com.recomendare.db.entities.ItemContact;
import mx.com.recomendare.db.entities.ItemLocation;
import mx.com.recomendare.db.entities.ItemPicture;
import mx.com.recomendare.db.entities.ItemPrice;
import mx.com.recomendare.db.entities.ItemResume;
import mx.com.recomendare.db.entities.Language;
import mx.com.recomendare.db.entities.Location;
import mx.com.recomendare.db.entities.Picture;
import mx.com.recomendare.db.entities.Street;
import mx.com.recomendare.db.entities.Subcategory;
import mx.com.recomendare.db.entities.User;
import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.commons.models.PictureModel;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import com.google.api.translate.Translate;

/**
 *
 * @author jerry
 */
public final class ItemsDAO extends AbstractDAO {
	
	/**
	 * 
	 * @param cityId
	 * @param countryCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Item> getAdvertisedItems(final int cityId, final String countryCode) {
		return session.createQuery(
									"SELECT i " +
									"FROM Item i, AdvertisedItem ai " +
									"WHERE ai.item = i"
									).list();
	}
	
	/**
	 * 
	 * @param model
	 * @param userScreenName
	 * @return
	 */
	public int createItem(final ItemModel model, final String userScreenName)  {
		//first get the user who add this item...
		UsersDAO usersDAO = new UsersDAO();
		usersDAO.setSession(session);
		User user = usersDAO.getUserWithScreenName(userScreenName);
		//now create the item...
		Transaction tx = null;
		try {
			//begin the transaction...
			tx = session.beginTransaction();
			
			//ITEM
			Item item = new Item();
			//NAME
			item.setName(model.getName());
			session.save(item);
			//RESUME
			LanguagesDAO languagesDAO = new LanguagesDAO();
			languagesDAO.setSession(session);
			Iterator<Language> iterator = languagesDAO.getAllLanguages().iterator();
			final String userLanguageCode = user.getUserConfiguration().getLanguage().getCode();
			while (iterator.hasNext()) {
				Language supportedLanguage = iterator.next();
				ItemResume itemResume = new ItemResume();
				itemResume.setLanguage(supportedLanguage);
				if(!userLanguageCode.equals(supportedLanguage.getCode()))  {
					//TODO:this is out of context...should be called in the services layer...and passed to this layer already translated...
					//itemResume.setResume(Translate.translate(model.getDescription(), userLanguageCode, supportedLanguage.getCode()));
				}
				else  {
					itemResume.setResume(model.getDescription());
				}
				itemResume.setItem(item);
				session.save(itemResume);
			}
			//SUBCATEGORY
			SubcategoriesDAO subcategoriesDAO = new SubcategoriesDAO();
			subcategoriesDAO.setSession(session);
			Subcategory subcategory = (Subcategory)subcategoriesDAO.load(model.getCategory().getSubcategoryId());
			item.setSubcategory(subcategory);
			session.update(item);
			//PRICES
			CurrenciesDAO currenciesDAO = new CurrenciesDAO();
			currenciesDAO.setSession(session);
			Currency currency = (Currency)currenciesDAO.load(model.getPrice().getOriginalCurrencyId());
			ItemPrice minPrice = new ItemPrice();
			minPrice.setValue(new BigDecimal(model.getPrice().getMinPrice()));
			minPrice.setCurrency(currency);
			session.save(minPrice);
			item.setItemPriceByMinPriceId(minPrice);
			session.update(item);
			ItemPrice maxPrice = new ItemPrice();
			maxPrice.setValue(new BigDecimal(model.getPrice().getMaxPrice()));
			maxPrice.setCurrency(currency);
			session.save(maxPrice);
			item.setItemPriceByMaxPriceId(maxPrice);
			session.update(item);
			//PICTURES
			if(model.getPictures() != null && model.getPictures().size() > 0)  {
				final Iterator<PictureModel> picturesIterator =  model.getPictures().iterator();
				while (picturesIterator.hasNext()) {
					PictureModel pictureModel = (PictureModel) picturesIterator.next();
					Picture picture = new Picture();
					picture.setContent(pictureModel.getContent());
					session.save(picture);
					ItemPicture itemPicture = new ItemPicture();
					itemPicture.setPicture(picture);
					itemPicture.setItem(item);
					session.save(itemPicture);
				}
			}
			//LOCATION
			if(model.getLocation() != null)  {
				//ZONE
				
				//STREET
				CitiesDAO citiesDAO = new CitiesDAO();
				citiesDAO.setSession(session);
				//City city = (City)citiesDAO.load(model.getLocation().getCityId());
				Street street = new Street();
				//street.setCity(city);
				street.setName(model.getAddress().getStreetName());
				session.save(street);
				//LOCATION
				Location location = new Location();
				location.setLatitude(new Double(model.getLocation().getLatitude()));
				location.setLongitude(new Double(model.getLocation().getLongitude()));
				session.save(location);
				//CONTACT
				Contact contact = new Contact();
				contact.setHomepageAddress(model.getAddress().getHomePageURL());
				contact.setHomePhoneNumber(model.getAddress().getTelephoneNumber());
				session.save(contact);
				ItemContact itemContact = new ItemContact();
				itemContact.setContact(contact);
				session.save(itemContact);
				item.setItemContact(itemContact);
				session.update(item);
				//PLACE
				ItemLocation itemLocation = new ItemLocation();
				itemLocation.setStreet(street);
				itemLocation.setLocation(location);
				itemLocation.setStreetOutsideNumber(model.getAddress().getStreetOutsideNumber());
				session.save(itemLocation);
				item.setItemLocation(itemLocation);
				session.update(item);
			}
			//and commit the result...
			tx.commit();
			
			return item.getId();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			log.error("Can't save this ItemModel - " + model + ", reason - " + e.getMessage(), e);
		}
		return -1;
	}
	
	/**
     * 
     * @param subcategoryId
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Item> getItemsBySubcategory(final String subcategoryId)  {
        return session.createQuery(
        							"FROM Item i " +
        							"WHERE i.subcategory = ? " +
        							"ORDER BY i.name"
        							).setParameter(0, new Subcategory(Integer.parseInt(subcategoryId))).list();
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public Item getItemById(int id) {
        return (Item) session.get(Item.class, new Integer(id));
    }
    
   /**
    * 
    * @param itemId
    * @param languageCode
    * @return
    */
    public String getItemResume(final int itemId, final String languageCode)  {
    	return (String)session.createQuery(
    															"SELECT ir.resume "+
    															"FROM Item i, ItemResume ir, Language l "+
    															"WHERE ir.item = i "+
    															"AND ir.language = l "+
    															"AND i = ? "+
    															"AND l.code = ?"
    														).setParameter(0, new Item(itemId)).setParameter(1, languageCode).uniqueResult();
    }
    
    /**
     * 
     * @param keyword
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
	public List<Object> searchKeywordOnItems(final String keyword, final String languageCode) {
    	FullTextSession fullTextSession = Search.createFullTextSession(session);
    	/*
    	Analyzer analyzer = null;
    	if(languageCode.equalsIgnoreCase("es")) {
    		analyzer = new SpanishAnalyzer();
    	}
    	else if(languageCode.equalsIgnoreCase("pt")) {
    		analyzer = new PortugueseAnalyzer();
    	}
    	else if(languageCode.equalsIgnoreCase("en"))  {
    		analyzer = new StandardAnalyzer(); 
    	}
    	else {
    		analyzer = new StandardAnalyzer();
    	}
    	*/
    	MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"name", "resume"}, new StandardAnalyzer());
    	Query query = null;
		try {
			query = parser.parse(keyword);
		} catch (ParseException e) {
			log.error("Can't parse the query - "+keyword+", reason - "+e.getMessage(), e);
		}
    	org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(query, Item.class, ItemResume.class);
    	Iterator iterator = hibQuery.list().iterator();
    	Map<Integer, Item> items = new HashMap<Integer, Item>();
    	while (iterator.hasNext()) {
    		Object item	= iterator.next();
    		if(item instanceof Item)  {
    			if(!items.containsKey(((Item)item).getId()))  {
    				items.put(((Item)item).getId(), (Item)item);
    			}
    		}
    		else if(item instanceof ItemResume)  {
    			ItemResume itemResume = (ItemResume)item;
    			if(!items.containsKey(itemResume.getItem().getId()))  {
    				if(itemResume.getLanguage().getCode().equals(languageCode))  {//TODO:it should call in the query the language code...not here...
    					items.put(itemResume.getItem().getId(), itemResume.getItem());
        			}
    			}
    		}
		}
    	return Arrays.asList(items.values().toArray());
    }
    
    
    public List<Item> getItemsHighRanked(final int cityId, final short rating)  {
    	
    	return null;
    }

    /**
     * 
     * @param cityId
     * @param maxItems
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Item> getLatestItemsAdded(final int cityId, final String countryCode, final short maxItems)  {
    	CountriesDAO countriesDAO = new CountriesDAO();
    	countriesDAO.setSession(session);
    	if(!countriesDAO.isSupportedCountry(countryCode))  {
    		List<Item> latestItems = session.createQuery(
    				"FROM Item item " +
    				"ORDER BY item.id DESC"
    				).list();
    		return latestItems;
    	}
    	else  {
    		List<Item> latestItemsWithInCity = session.createQuery(
    				"SELECT i "+
    				"FROM Item i, ItemLocation il, City c, Zone z, Street s "+
    				"WHERE i.itemLocation = il " +
    				"AND il.street = s "+
    				"AND s.zone = z "+
    				"AND z.city = c "+
    				"AND c = ? "+ //TODO:should reference a name also...not only an id supplied in the first call to location...
    				"ORDER BY i.id DESC"
    		).setParameter(0, new City(cityId)).setCacheable(true).list();
    		List <Item> latestItemsWithNoPlace = session.createQuery(
    				"FROM Item item " +
    				"WHERE item.itemLocation = NULL "+
    				/*
    				"FROM Item item " +
    				"WHERE NOT EXISTS ELEMENTS(item.itemLocation) " +
    				*/
    				"ORDER BY item.id DESC"
    		).setCacheable(true).list();
    		if(latestItemsWithInCity != null && latestItemsWithInCity.size() > 0)  {
    			if(latestItemsWithNoPlace != null && latestItemsWithNoPlace.size() > 0)  {
    				latestItemsWithInCity.addAll(latestItemsWithNoPlace);
    				Collections.sort(latestItemsWithInCity, new Comparator<Item>(){
    					public int compare(final Item o1, final Item o2) {
    						if(o1.getId() > o2.getId()) return 1;
    						if(o1.getId() == o2.getId()) return 0;
    						else return -1;
    					}
    				});
    			}
    			return (latestItemsWithInCity.size() > maxItems)?latestItemsWithInCity.subList(0, maxItems):latestItemsWithInCity;
    		}
    		else if(latestItemsWithNoPlace != null && latestItemsWithNoPlace.size() > 0)  {
    			Collections.sort(latestItemsWithNoPlace, new Comparator<Item>(){
    				public int compare(final Item o1, final Item o2) {
    					if(o1.getId() > o2.getId()) return 1;
    					if(o1.getId() == o2.getId()) return 0;
    					else return -1;
    				}
    			});
    			return (latestItemsWithNoPlace.size() > maxItems)?latestItemsWithNoPlace.subList(0, maxItems):latestItemsWithNoPlace;
    		}
    	}
    	return null;
    }
    
    
    /* (non-Javadoc)
     * @see mx.com.recomendare.services.db.newschema.AbstractDAO#count()
     */
    public int count() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see mx.com.recomendare.services.db.newschema.AbstractDAO#load(long)
     */
    public Object load(long id) {
    	return session.load(Item.class, new Integer(String.valueOf(id)));
    }

    /* (non-Javadoc)
     * @see mx.com.recomendare.services.db.newschema.AbstractDAO#save(java.lang.Object)
     */
    public Object save(Object toSave) {
        // TODO Auto-generated method stub
        return null;
    }
    
    //UTIL...to create external lucene text indexes on every item on the database...
    @SuppressWarnings("unchecked")
	void createNewIndexItemResumes()  {
    	FullTextSession fullTextSession = Search.createFullTextSession(session);
    	Transaction tx = fullTextSession.beginTransaction();
    	List<ItemResume> itemResumes = session.createQuery("FROM ItemResume as itemResume").list();
    	Iterator<ItemResume> iterator = itemResumes.iterator();
    	while (iterator.hasNext()) {
			ItemResume resume = iterator.next();
			fullTextSession.index(resume);
		}//--.-ll...diego
    	tx.commit(); //index are written at commit time
    }
    
    @SuppressWarnings("unchecked")
	void createNewIndexItemNames()  {
    	FullTextSession fullTextSession = Search.createFullTextSession(session);
    	Transaction tx = fullTextSession.beginTransaction();
    	List<Item> itemNames = session.createQuery("FROM Item as item").list();
    	Iterator<Item> iterator = itemNames.iterator();
    	while (iterator.hasNext()) {
			Item name = (Item) iterator.next();
			fullTextSession.index(name);
		}
    	tx.commit(); //index are written at commit time
    }

}//END OF FILE