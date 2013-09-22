/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.recomendare.services.db;

import mx.com.recomendare.db.entities.ClickstreamStatistic;
import mx.com.recomendare.services.statistics.ItemStatisticModel;

import org.hibernate.Transaction;

/**
 *
 * @author jerry
 */
public final class StatisticsDAO extends AbstractDAO {
    
    /**
     * 
     * @param itemStatistic
     */
    public void createClickStreamStatistic(final ItemStatisticModel itemStatistic) {
        if (itemStatistic != null) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                //we create our object in the data base...
                ClickstreamStatistic clickStatistic = new ClickstreamStatistic();
                
                //clickStatistic.setItem(new Item(itemStatistic.getItemId()));
                
                if(itemStatistic.getUserSession() != null && itemStatistic.getUserSession().itIsAuthenticated())  {
                	UsersDAO users = new UsersDAO();
                    users.setSession(session);
                    clickStatistic.setUser(users.getUserWithScreenName(itemStatistic.getUserSession().getScreenName()));//TODO: it should save on the itemStatistic side only the screenName field...
                }
                
                clickStatistic.setSessionId(itemStatistic.getSessionId());
                clickStatistic.setOnPage(itemStatistic.getPagePath());
                //clickStatistic.setIpAddress(itemStatistic.getUserIpAddress());
                clickStatistic.setOnDate(itemStatistic.getDate());
                clickStatistic.setOnLink(itemStatistic.getLinkName());
                //clickStatistic.setBrowserName(itemStatistic.getBrowserName());
                //clickStatistic.setBrowserVersion(itemStatistic.getBrowserVersion());
                //clickStatistic.setBrowserPlatform(itemStatistic.getBrowserPlatform());
                //clickStatistic.setReferer(itemStatistic.getReferrer());
                
                session.save(clickStatistic);

                tx.commit();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                log.error("Can't save this Item statistic - " + itemStatistic + ", because - " + e.getMessage(), e);
            }
        }
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