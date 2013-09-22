/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.recomendare.services.recommender;

/**
 *
 * @author Jerry
 */
public final class RecommenderServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public RecommenderServiceException(Throwable cause) {
        super(cause);
    }

    public RecommenderServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecommenderServiceException(String message) {
        super(message);
    }

    public RecommenderServiceException() {
        super();
    }
    
}//END OF FILE