/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.recomendare.web.commons.models.detachables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.db.entities.Country;
import mx.com.recomendare.db.entities.CountryName;
import mx.com.recomendare.web.commons.components.TagCloud;
import mx.com.recomendare.web.countries.ViewCountryPage;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.link.Link;

/**
 *
 * @author jerry
 */
public final class CountriesTagListDetachableModel extends AbstractCountriesListDetachableModel {
	private static final long serialVersionUID = 1L;
	
	/**
     * 
     * @param countries
     * @return
     */
    protected List<Object> getFormatToVisualize(final List<Country> countries) {
        List<Object> tags = new ArrayList<Object>(countries.size());
        final Iterator<Country> iterator = countries.iterator();
        while (iterator.hasNext()) {
            final Country country = iterator.next();
            final String countryName = getCountryName(country);
            tags.add(new TagCloud.Tag(countryName, country.getCities().size()) {
                private static final long serialVersionUID = 1L;
                public Link getLink(final String id) {
                    return new Link(id) {
                        private static final long serialVersionUID = 1L;
                        public void onClick() {
                            PageParameters parameters = new PageParameters();
                            parameters.put("i", country.getId());
                            parameters.put("n", countryName);
                            setResponsePage(ViewCountryPage.class, parameters);
                        }
                    };
                }
            });
        }
        return tags;
    }
    
    //to resolve the country name by it's language code...
    @SuppressWarnings("unchecked")
	private String getCountryName(final Country country)  {//TODO:instead of this, try to access directly the dao with the language...
    	Iterator<CountryName> iterator = country.getCountryNames().iterator();
    	while (iterator.hasNext()) {
			CountryName name = (CountryName) iterator.next();
			if(name.getLanguage().getCode().equals(WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession())).getLanguageCode())) {
				return name.getName();
			}
		}
    	return null;
    }
    
}//END OF FILE