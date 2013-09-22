/**
 * 
 */
package mx.com.recomendare.services.db;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import mx.com.recomendare.services.AbstractService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jmx.StatisticsService;

/**
 * @author jerry
 *
 */
public final class DatabaseService extends AbstractService {
	//the log...
	private static final Log log = LogFactory.getLog(DatabaseService.class);
	
	//the session factory...
	private static SessionFactory sessionFactory;
    //the categories DAO...
    private CategoriesDAO categories;
    //the sub categories DAO...
    private SubcategoriesDAO subcategories;
    //the countries DAO...
    private CountriesDAO countries;
    //the cities DAO...
    private CitiesDAO cities;
    //the languages DAO...
    private LanguagesDAO languages;
    //the places DAO...
    private ItemLocationsDAO places;
    //the ratings DAO...
    private ItemCommentsDAO ratings;
    //the items...
    private ItemsDAO items;
    //the users...
    private UsersDAO users;
    //the favorites...
    private FavoritesDAO favorites;
    //the streets...
    private StreetsDAO streets;
    //the requests...
    private RequestsDAO requests;
    //the statistics...
    private StatisticsDAO statistics;
    //the labels...
	private LabelsDAO labels;
	//the currencies...
	private CurrenciesDAO currencies;
	//the constants...
	private ConstantsDAO constants; 
	//the zones
	private ZonesDAO zones;
	/**
	 * 
	 */
    public DatabaseService() {
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
			getSessionFactory();
			categories = new CategoriesDAO();
	        subcategories = new SubcategoriesDAO();
	        countries = new CountriesDAO();
	        cities = new CitiesDAO();
	        languages = new LanguagesDAO();
	        places = new ItemLocationsDAO();
	        ratings = new ItemCommentsDAO();
	        items = new ItemsDAO();
	        users = new UsersDAO();
	        favorites = new FavoritesDAO();
	        streets = new StreetsDAO();
	        requests = new RequestsDAO();
	        statistics = new StatisticsDAO();
	        labels = new LabelsDAO();
	        currencies = new CurrenciesDAO();
	        constants = new ConstantsDAO();
	        zones = new ZonesDAO();
			started = true;
			log.info(getClass().getName()+"...started!");
		}
		else {
			log.error(getClass().getName()+"...already started!");
		}
	}

	/**
	 * 
	 */
	public void doStop() {
		if(started)  {
			log.info("Stoping - "+getClass().getName()+"...");
			getSessionFactory().close();
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}

    /**
     * 
     * @return
     */
    public CategoriesDAO getCategoriesDAO() {
        return categories;
    }

    /**
     * 
     * @return
     */
    public ItemsDAO getItemsDAO() {
        return items;
    }

    /**
     * @return the subcategories
     */
    public SubcategoriesDAO getSubcategoriesDAO() {
        return subcategories;
    }

    /**
     * @return the countries
     */
    public CountriesDAO getCountriesDAO() {
        return countries;
    }

    /**
     * 
     * @return
     */
    public CitiesDAO getCitiesDAO() {
        return cities;
    }

    /**
     * 
     * @return
     */
    public LanguagesDAO getLanguagesDAO() {
        return languages;
    }

    /**
     * @return the places
     */
    public ItemLocationsDAO getPlacesDAO() {
        return places;
    }

    /**
     * 
     * @return
     */
    public ItemCommentsDAO getRatingsDAO() {
        return ratings;
    }

    /**
     * 
     * @return
     */
    public UsersDAO getUsersDAO() {
        return users;
    }
    
    /**
     * 
     */
    public FavoritesDAO getFavoritesDAO() {
        return favorites;
    }
    
    /**
     * 
     * @return
     */
    public StreetsDAO getStreetsDAO() {
        return streets;
    }
    
    /**
     * 
     * @return
     */
    public RequestsDAO getRequestsDAO() {
        return requests;
    }
    
    /**
     * 
     * @return
     */
    public StatisticsDAO getStatisticsDAO() {
        return statistics;
    }

    /**
     * 
     * @return
     */
	public LabelsDAO getLabelsDAO() {
		return labels;
	}

	/**
	 * 
	 * @return
	 */
	public CurrenciesDAO getCurrenciesDAO() {
		return currencies;
	}

	/**
	 * 
	 * @return
	 */
	public ConstantsDAO getConstantsDAO() {
		return constants;
	}
	
	/**
	 * 
	 * @return
	 */
	public ZonesDAO getZonesDAO() {
		return zones;
	}
	
	/**
	 * 
	 * @return
	 */
	public ItemLocationsDAO getItemLocationsDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public synchronized static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				sessionFactory = new Configuration().configure().buildSessionFactory();
				MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
				final ObjectName objectName = new ObjectName("org.hibernate:type=statistics");
				final StatisticsService mBean = new StatisticsService();
				mBean.setStatisticsEnabled(true);
				mBean.setSessionFactory(sessionFactory);
				mbeanServer.registerMBean(mBean, objectName);
			} catch (MalformedObjectNameException e) {
				log.error("unable to register mbean", e);
				throw new RuntimeException(e);
			} catch (InstanceAlreadyExistsException e) {
				log.error("unable to register mbean", e);
				throw new RuntimeException(e);
			} catch (MBeanRegistrationException e) {
				log.error("unable to register mbean", e);
				throw new RuntimeException(e);
			} catch (NotCompliantMBeanException e) {
				log.error("unable to register mbean", e);
				throw new RuntimeException(e);
			}
		}
		return sessionFactory;
	}

}//END OF FILE