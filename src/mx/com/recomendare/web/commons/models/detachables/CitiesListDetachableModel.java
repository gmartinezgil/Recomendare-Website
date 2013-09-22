/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.recomendare.web.commons.models.detachables;

import java.util.List;

/**
 *
 * @author jerry
 */
public class CitiesListDetachableModel extends AbstractCitiesListDetachableModel {
	private static final long serialVersionUID = 1L;

	/**
     * 
     * @param countryId
     */
    public CitiesListDetachableModel(final String countryId) {
        super(countryId);
    }

    /**
     * 
     * @param cities
     * @return
     */
    @SuppressWarnings("unchecked")
	protected List getFormatToVisualize(final List cities) {
        return cities;
    }

}//END OF FILE