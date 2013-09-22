/**
 * 
 */
package mx.com.recomendare.web.countries;

import java.util.List;

import mx.com.recomendare.db.entities.City;
import mx.com.recomendare.db.entities.CityName;
import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.commons.models.detachables.CitiesListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author jerry
 *
 */
//TODO: one optimization it should represent in the all countries page, the most viewed/searched/etc. countries in it's main body...
public final class ViewCountryPage extends BasePage {
    private static final long serialVersionUID = 1L;

    //the name of the country...
    private String countryName;

    /**
     * 
     * @param parameters
     */
    public ViewCountryPage(PageParameters parameters) {
        super(parameters);
        countryName = parameters.getString("n");
        String countryKey = parameters.getString("i");
        if(countryKey != null && countryKey.trim().length() > 0)  {
            IModel citiesModel = new CompoundPropertyModel(new CitiesListDetachableModel(countryKey));
            setModel(citiesModel);
            init();
        }
        else {/*throw new CityNotFoundException(cityKey);*/}
    }

    private void init() {
        //TITLE
        add(new Label("countryNameLabel", countryName));
        //CITIES
        final PageableListView citiesPageableListView = new PageableListView("cities", (List) getModelObject(), Constants.MAX_ITEMS_PER_PAGE) {
            private static final long serialVersionUID = 1L;
            protected void populateItem(final ListItem item) {
                final City city = (City) item.getModelObject();
                final String cityName = ((CityName)city.getCityNames().toArray()[0]).getName();
                final Link cityViewLink = new Link("cityViewLink") {
                    private static final long serialVersionUID = 1L;
                    public void onClick() {
                    	PageParameters parameters = new PageParameters();
                    	parameters.put("i", city.getId());
                    	parameters.put("n", cityName);
                        setResponsePage(ViewCityPage.class, parameters);
                    }
                };
                cityViewLink.add(new Label("cityNameLabel", cityName + " (" + city.getStreets().size() + ")"));
                item.add(cityViewLink);
            }
        };
        add(citiesPageableListView);
        add(new PagingNavigator("citiesNavigator", citiesPageableListView));
    }

    /**
     * 
     */
	protected IModel getPageTitle() {
		return new Model(Constants.SITE_NAME + " - " +new LabelStringDetachableModel("COUNTRY") + " - " +countryName);
	}

	protected String getMetaDescription() {
		return "";
	}

	protected String getMetaKeywords() {
		return "";
	}
    
}//END OF FILE