/**
 * 
 */
package mx.com.recomendare.services.db;

import java.util.List;

import mx.com.recomendare.db.entities.Item;
import mx.com.recomendare.db.entities.ItemLocation;
import mx.com.recomendare.web.commons.models.LocationModel;

/**
 * @author jerry
 *
 */
public final class ItemLocationsDAO extends AbstractDAO {

	/**
	 * 
	 * @param location
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemLocation> getItemLocationsNearByLocation(final LocationModel location)  {
		return session.createSQLQuery(
				"SELECT {itemlocation.*}, (((acos(sin((:lat*:pi/180)) * sin((location.latitude*:pi/180)) + cos((:lat*:pi/180)) * cos((location.latitude*:pi/180)) * cos(((:lon - location.longitude)*:pi/180))))*180/:pi)*60*1.423) AS distance " +
				"FROM ItemLocation AS itemlocation, Location AS location " +
				"WHERE itemlocation.location_id = location.id " +
				"HAVING distance <= :theDistance " +
				"ORDER BY distance ASC"
			).addEntity("itemlocation", ItemLocation.class).setDouble("lat", location.getLatitude()).setDouble("lon", location.getLongitude()).setFloat("pi", 3.141593F).setInteger("theDistance", 2).list();
	}
	
    /**
     * 
     * @param itemId
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<ItemLocation> getItemLocationsNearByItem(final int itemId) {
        ItemLocation itemLocation = getItemLocationFromItemById(itemId);
        if(itemLocation != null) {
        	return session.createSQLQuery(
											"SELECT {itemlocation.*}, (((acos(sin((:lat*:pi/180)) * sin((location.latitude*:pi/180)) + cos((:lat*:pi/180)) * cos((location.latitude*:pi/180)) * cos(((:lon - location.longitude)*:pi/180))))*180/:pi)*60*1.423) AS distance " +
											"FROM ItemLocation AS itemlocation, Location AS location " +
											"WHERE itemlocation.location_id = location.id " +
											"HAVING distance <= :theDistance " +
	    									"ORDER BY distance ASC"
										).addEntity("itemlocation", ItemLocation.class).setDouble("lat", itemLocation.getLocation().getLatitude()).setDouble("lon", itemLocation.getLocation().getLongitude()).setFloat("pi", 3.141593F).setInteger("theDistance", 2).list();
        }
        return null;
    }
    
    /**
     * 
     * @param itemId
     * @return
     */
    public ItemLocation getItemLocationFromItemById(final int itemId)  {
    	ItemsDAO itemsDAO = new ItemsDAO();
        itemsDAO.setSession(session);
        Item item = (Item)itemsDAO.load(itemId);
        return item.getItemLocation();
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
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see mx.com.recomendare.services.db.newschema.AbstractDAO#save(java.lang.Object)
     */
    public Object save(Object toSave) {
        // TODO Auto-generated method stub
        return null;
    }
    
}//END OF FILE