package mx.com.recomendare.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.rmi.RemoteException;

import mx.com.recomendare.services.http.HttpService;
import mx.com.recomendare.services.weather.WeatherModel;
import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.session.UserSessionModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Resource;
import org.apache.wicket.markup.html.PackageResource;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.protocol.http.WebRequestCycle;

/**
 * 
 * @author jerry
 *
 */
public final class WebUtil {
    //images
    public static final Resource QUESTIONMARK_IMAGE = PackageResource.get(WebUtil.class, "questionmark.gif").setCacheable(true);
    public static final Resource ADD_IMAGE = PackageResource.get(WebUtil.class, "add.png").setCacheable(true);
    public static final Resource ADD_FAVORITE_IMAGE = PackageResource.get(WebUtil.class, "award_star_add.png").setCacheable(true);
    public static final Resource VIEW_FAVORITES_IMAGE = PackageResource.get(WebUtil.class, "award_star_gold_3.png").setCacheable(true);
    public static final Resource LOCATE_PLACE_ON_MAP_IMAGE = PackageResource.get(WebUtil.class, "map_go.png").setCacheable(true);
    public static final Resource NEARBY_PLACES_MAP_IMAGE = PackageResource.get(WebUtil.class, "map.png").setCacheable(true);
    public static final Resource SEND_EMAIL_IMAGE = PackageResource.get(WebUtil.class, "email_go.png").setCacheable(true);
    public static final Resource THUMB_UP_IMAGE = PackageResource.get(WebUtil.class, "thumb_up.png").setCacheable(true);
    public static final Resource THUMB_DOWN_IMAGE = PackageResource.get(WebUtil.class, "thumb_down.png").setCacheable(true);
    public static final Resource COMMENT_IMAGE = PackageResource.get(WebUtil.class, "comment.png").setCacheable(true);
    public static final Resource ADD_COMMENT_IMAGE = PackageResource.get(WebUtil.class, "comment_add.png").setCacheable(true);
    public static final Resource USER_STATUS_OFFLINE_IMAGE = PackageResource.get(WebUtil.class, "status_offline.png").setCacheable(true);
    public static final Resource PICTURE_EMPTY_IMAGE = PackageResource.get(WebUtil.class, "picture_empty.png").setCacheable(true);
    public static final Resource PICTURE_ADD_IMAGE = PackageResource.get(WebUtil.class, "picture_add.png").setCacheable(true);
    public static final Resource SEND_TO_PHONE_IMAGE = PackageResource.get(WebUtil.class, "phone_add.png").setCacheable(true);
    public static final Resource ADD_RSS_FEED_IMAGE = PackageResource.get(WebUtil.class, "feed_add.png").setCacheable(true);
    public static final Resource GO_TO_PAGE_IMAGE = PackageResource.get(WebUtil.class, "page_world.png").setCacheable(true);
    public static final Resource DELICIOUS_IMAGE = PackageResource.get(WebUtil.class, "delicious.png").setCacheable(true);
    public static final Resource DIGG_IMAGE = PackageResource.get(WebUtil.class, "digg.png").setCacheable(true);
    public static final Resource STUMBLE_IMAGE = PackageResource.get(WebUtil.class, "stumbleit.png").setCacheable(true);
    public static final Resource REDDIT_IMAGE = PackageResource.get(WebUtil.class, "reddit.png").setCacheable(true);
    public static final Resource TECHNORATI_IMAGE = PackageResource.get(WebUtil.class, "technorati.png").setCacheable(true);
    public static final Resource RIGHT_ARROW_IMAGE = PackageResource.get(WebUtil.class, "right-arrow.png").setCacheable(true);
    public static final Resource LEFT_ARROW_IMAGE = PackageResource.get(WebUtil.class, "left-arrow.png").setCacheable(true);
    
    //TEST
    public static final Resource AD_160X600_IMAGE = PackageResource.get(WebUtil.class, "160x600.gif").setCacheable(true);
    public static final Resource AD_120X90_IMAGE = PackageResource.get(WebUtil.class, "120x90_4.gif").setCacheable(true);
    public static final Resource AD_200X200_IMAGE = PackageResource.get(WebUtil.class, "200x200_text.gif").setCacheable(true);
    public static final Resource AD_468X15_IMAGE = PackageResource.get(WebUtil.class, "468x15.gif").setCacheable(true);
    public static final Resource AD_LEADERBOARD_IMAGE = PackageResource.get(WebUtil.class, "leaderboard.gif").setCacheable(true);
    public static final Resource AD_BANNER_IMAGE = PackageResource.get(WebUtil.class, "banner.gif").setCacheable(true);
    public static final Resource AD_234X60_IMAGE = PackageResource.get(WebUtil.class, "234x60.gif").setCacheable(true);
    public static final Resource AD_336X280_IMAGE = PackageResource.get(WebUtil.class, "336x280.gif").setCacheable(true);
    public static final Resource AD_170X15_IMAGE = PackageResource.get(WebUtil.class, "170x15.GIF").setCacheable(true);;
    public static final Resource HOTEL_IMAGE = PackageResource.get(WebUtil.class, "building.png").setCacheable(true);
    public static final Resource EVENT_IMAGE = PackageResource.get(WebUtil.class, "date.png").setCacheable(true);
    public static final Resource WHAT_TO_SEE_IMAGE = PackageResource.get(WebUtil.class, "camera.png").setCacheable(true);
    public static final Resource ZOOM_IMAGE = PackageResource.get(WebUtil.class, "magnifier.png").setCacheable(true);
    public static final Resource NO_PICTURE_IMAGE = PackageResource.get(WebUtil.class, "no_picture.gif").setCacheable(true);
    public static final Resource GOOGLE_ADSENSE_SEARCH_IMAGE = PackageResource.get(WebUtil.class, "google-adsense-search.jpg").setCacheable(true);
    
    //the countries flags...
    public static final Resource UNKWON_FLAG_IMAGE = PackageResource.get(WebUtil.class, "xx.png").setCacheable(false);
    //key value choice renderer...
    public static final ChoiceRenderer KEYVALUE_CHOICE_RENDERER = new ChoiceRenderer("value", "key");

    //the bots agents...
    private static final String[] botAgents = {	
    	"googlebot", "appie", "architext", "jeeves", "bjaaland", "ferret", "gulliver",
    	 "harvest", "htdig", "linkwalker", "lycos_", "moget", "muscatferret",
    	 "myweb", "nomad", "scooter", "yahoo!\\sslurp\\schina", "slurp",
    	 "weblayers", "antibot", "bruinbot", "digout4u", "echo!", "ia_archiver",
    	 "jennybot", "mercator", "netcraft", "msnbot", "petersnews",
    	 "unlost_web_crawler", "voila", "webbase", "webcollage", "cfetch",
    	 "zyborg", "wisenutbot", "robot", "crawl", "spider", "Lynx"
    	 };	

    
    //the log...
    private static final Log log = LogFactory.getLog(WebUtil.class);

    /**
     * 
     * @param session
     * @return
     */
    public static LocationModel getUserActualLocation(SignInSession session) {
        if (session != null) {
        	UserSessionModel user = session.getUser();
            if (session.isSignedIn()) {
                return user.getLocation();
            } else {
                if (user != null) {
                    return user.getLocation();
                } else {
                    final String userRemoteAddress = getUserRemoteAddress();
                    LocationModel location = ((Main)session.getApplication()).getServices().getGeoIpService().getLocation(userRemoteAddress);
                    if (location != null) {
                        user = new UserSessionModel();
                        user.setLocation(location);
                        session.setUser(user);
                    }
                    return location;
                }
            }
        }
        return null;
    }

    /**
     * 
     * @param session
     * @return
     */
    public static Resource getCountryFlagImageFromUserLocation(final String countryCode) {
        if (countryCode != null) {
        	return PackageResource.get(WebUtil.class, countryCode.toLowerCase() + ".png").setCacheable(true);
        }
        return UNKWON_FLAG_IMAGE;
    }


    /**
     * 
     * @param session
     * @return
     */
    public static WeatherModel getWeatherFromUserLocation(final LocationModel location) {
        if (location != null) {
        	//TODO:all the names are in english...
        	final String cityName = (location.getCityName().startsWith("Me") || location.getCityName().startsWith("MX")) ? location.getCityName() + " City" : location.getCityName();
        	final String countryName = location.getCountryName();
            try {
                return ((Main)((WebRequestCycle) RequestCycle.get()).getApplication()).getServices().getWeatherService().getWeatherConditionsByCityCountry(cityName, countryName);
            } catch (RemoteException e) {
            	log.error("Error while retrieving the weather conditions for location - "+location,e);
            	try  {
            		return ((Main)((WebRequestCycle) RequestCycle.get()).getApplication()).getServices().getWeatherService().getWeatherConditionsByCountry(countryName);
            	}
            	catch(RemoteException re)  {
            		log.error("Error while retrieving the weather conditions for location - "+location,e);
            	}
            }
        }
        return null;
    }
    
    /**
     * 
     * @return
     */
    public static String getUserRemoteAddress() {
        String userRemoteAddress = ((WebRequestCycle) RequestCycle.get()).getWebRequest().getHttpServletRequest().getRemoteAddr();
        log.info("remote address - "+userRemoteAddress);
        if (userRemoteAddress == null || userRemoteAddress.trim().length() == 0) {
        	//behind a proxy or firewall... 
            userRemoteAddress = ((WebRequestCycle) RequestCycle.get()).getWebRequest().getHttpServletRequest().getHeader("X-Forwarded-For");
            log.info("remote address (behind proxy) - "+userRemoteAddress);
        }
        //IPV4
        else if (userRemoteAddress.equals("127.0.0.1") || userRemoteAddress.startsWith("192.168.")) { //local host...only for testing pourposes...
        	//userRemoteAddress = "77.193.101.135";//clisson, france 
        	//userRemoteAddress = "201.89.145.85";//gramado,brazil
        	//userRemoteAddress = "128.100.57.19";//toronto canada
        	//userRemoteAddress = "74.6.28.231";//usa, sunnyvale
        	//userRemoteAddress = "86.136.176.201";//birmingham, england
        	//userRemoteAddress = "61.135.168.39";//beijing, china
        	//userRemoteAddress = "213.144.15.38";//karlsrune, germany
        	//userRemoteAddress = "190.189.89.173";//buenos aires, argentina
            userRemoteAddress = "189.146.182.29"; //near HOME, local ISP address provider...for testing..only...//mexico, mexico
        }
        //IPV6
        else if(userRemoteAddress.equals("0:0:0:0:0:0:0:1"))  {
        	//userRemoteAddress = "77.193.101.135";//clisson, france 
        	//userRemoteAddress = "201.89.145.85";//gramado,brazil
        	//userRemoteAddress = "128.100.57.19";//toronto canada
        	//userRemoteAddress = "74.6.28.231";//usa, sunnyvale
        	//userRemoteAddress = "86.136.176.201";//birmingham, england
        	//userRemoteAddress = "61.135.168.39";//beijing, china
        	//userRemoteAddress = "213.144.15.38";//karlsrune, germany
        	//userRemoteAddress = "190.189.89.173";//buenos aires, argentina
            userRemoteAddress = "189.146.182.29"; //near HOME, local ISP address provider...for testing..only...//mexico, mexico
        }
        return userRemoteAddress;
    }
    
    /**
     * 
     * @param agent
     * @return
     */
    public static boolean isAgent(final String agent) {
    	if (agent != null) {
    		final String lowerAgent = agent.toLowerCase();
    		for (final String bot : botAgents) {
    			if (lowerAgent.indexOf(bot) != -1) {
    				return true;
    			}
    		}
    	}
    	return false;
    }

   /**
    * 
    * @param latitude
    * @param longitude
    * @param zoomLevel
    * @param width
    * @param heigth
    * @return
    */
    public static BufferedDynamicImageResource getStaticMapImageResource(final float latitude, final float longitude, final int zoomLevel, final int width, final int heigth)  {
		return new BufferedDynamicImageResource() {
			private static final long serialVersionUID = 1L;
			protected byte[] getImageData() {
				final String googleMapsURL = "http://maps.google.com/staticmap?center="+latitude+","+longitude+"&zoom="+zoomLevel+"&markers="+latitude+","+longitude+",red&size="+width+"x"+heigth+"&key="+Constants.GMAP_LOCALHOST_8080_KEY;
				HttpService httpService = ((Main)RequestCycle.get().getApplication()).getServices().getHttpService(); 
				return httpService.getContenAsBytesFrom(googleMapsURL);
			}
		};
	}
    
    /**
     * 
     * @param url
     * @return
     */
    public static String encodeURL(final String url)  {
    	try {
			return URLEncoder.encode(url,"UTF-8");
		} 
    	catch (UnsupportedEncodingException e) {
    		log.error("Can't transform url - "+url+", reason - "+e.getMessage(), e);
		}
		return null;
    }
    
}//END OF FILE