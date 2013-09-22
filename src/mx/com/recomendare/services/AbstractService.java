/**
 * 
 */
package mx.com.recomendare.services;

/**
 * @author jerry
 *
 */
public abstract class AbstractService implements Service {
	//tells where the service started...
	protected boolean started;
	
	/**
	 * 
	 */
	public boolean isStarted() {
		return started;
	}

}//END OF FILE