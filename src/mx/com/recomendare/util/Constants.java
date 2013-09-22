package mx.com.recomendare.util;

/**
 * 
 * @author jerry
 *
 */
public interface Constants {
	public static final String VERSION = "0.8";
	public static final String SITE_NAME = "recomendare";
	public static final String SITE_SLOGAN = "site slogan here";
	public static final String COPYRIGHT = "All Rights Reserved.";
	
	//public static final String SITE_URL = "http://www."+SITE_NAME+".com";
	public static final String SITE_URL = "http://localhost:8080/"+SITE_NAME;
	
	public static final String SITE_EMAIL_ADDRESS = "info@"+SITE_NAME+".com";
	public static final String SITE_EMAIL_SUBJECT = "email subject";
	
	public static final short MAX_ITEMS_PER_PAGE = 10;
	public static final int MAX_COMMENTS_PER_USER = 30;
	public static final int MAX_RECOMMENDATIONS_PER_PAGE = 5;
	public static final int MAX_COMMENTS_FRONT_PAGE = 3;
	
	public static final String GMAP_LOCALHOST_8080_KEY = "GOOGLE_MAPS_KEY";
	//where everything was born...my home... :)
	public static final float DEFAULT_LATITUDE =0.00000000f;
	public static final float DEFAULT_LONGITUDE =0.00000000f;
	public static final int COUNTRIES_VIEW_ZOOM_LEVEL = 1;
	public static final int STATE_VIEW_ZOOM_LEVEL = 5;
	public static final int CITY_VIEW_ZOOM_LEVEL = 10;
	public static final int STREET_VIEW_ZOOM_LEVEL = 15;
	
	//the supported languages in the system...
	public static final String SPANISH_LANGUAGE_CODE = "es";
	public static final String ENGLISH_LANGUAGE_CODE = "en";
	public static final String FRENCH_LANGUAGE_CODE = "fr";
	public static final String PORTUGESE_LANGUAGE_CODE = "pt";
	
	public static final String DEFAULT_LANGUAGE_CODE = SPANISH_LANGUAGE_CODE;
	public static final String DEFAULT_INTERNATIONAL_LANGUAGE_CODE = ENGLISH_LANGUAGE_CODE;
	public static final String DEFAULT_CITY_NAME = "Mexico";
	public static final String DEFAULT_INTERNATIONAL_CITY_NAME = "California";
	public static final String DEFAULT_COUNTRY_CODE = "MX";
	public static final String DEFAULT_COUNTRY_NAME = "Mexico";
	public static final String DEFAULT_INTERNATIONAL_COUNTRY_CODE = "US";
	public static final String DEFAULT_INTERNATIONAL_COUNTRY_NAME = "USA";
	
	public static final long ONE_HOUR_OF_TIME = 3600000L;
	
	public static final short QUALIFIED_RATING = 3;//TODO:it should be at least 5...
	public static final int QUALIFIED_NUMBER_RATINGS = 3; //TODO:it should be at least 10...
	
	//for the Constants table...
	public static final String GENDER_TYPE = "GENDER_TYPE";
	
}//END OF FILE