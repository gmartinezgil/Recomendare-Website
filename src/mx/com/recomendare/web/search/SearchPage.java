/**
 * 
 */
package mx.com.recomendare.web.search;

import java.io.Serializable;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * @author jerry
 *
 */
public final class SearchPage extends BasePage {
    private static final long serialVersionUID = 1L;

    //the log...
    private static final Log log = LogFactory.getLog(SearchPage.class);
    
    /**
     * 
     */
    public SearchPage() {
        add(new SearchForm("searchForm", new CompoundPropertyModel(new SearchModel())));
    }

    /**
     * 
     * @author jerry
     *
     */
    final class SearchForm extends Form {
        private static final long serialVersionUID = 1L;

        public SearchForm(String id, IModel model) {
            super(id, model);
            init();
        }

        private void init() {
            //SEARCH FIELD
            add(new Label("searchTextLabel", new LabelStringDetachableModel("SEARCH_FOR")));
            final RequiredTextField searchTextField = new RequiredTextField("searchTextField", new PropertyModel(getModelObject(), "searchQuery"));
            add(searchTextField);
            //SEARCH BUTTON
            final Button searchButton = new Button("searchButton", new LabelStringDetachableModel("SEARCH")) {
                private static final long serialVersionUID = 1L;
                public void onSubmit() {
                    String query = ((SearchModel) getForm().getModelObject()).getSearchQuery();
                    if (query != null && query.trim().length() > 0) {//TODO:regexp for patterns of invalid characters...
                        log.info(query);
                        PageParameters parameters = new PageParameters();
                        parameters.put("s", query);
                        setResponsePage(SearchPage.class, parameters);
                    }
                }
            };
            add(searchButton);
        }
    }

    /**
     * @author jerry
     *
     */
    final class SearchModel implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String searchQuery;

        /**
         * @return the searchQuery
         */
        public String getSearchQuery() {
            return searchQuery;
        }

        /**
         * @param searchQuery the searchQuery to set
         */
        public void setSearchQuery(String searchQuery) {
            this.searchQuery = searchQuery;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            sb.append(getClass().getName());
            sb.append("] - {searchQuery = '");
            sb.append(searchQuery);
            sb.append("'}");
            return sb.toString();
        }
    }

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new Model(Constants.SITE_NAME + " - " +new LabelStringDetachableModel("SEARCH"));
	}

	protected String getMetaDescription() {
		return "";
	}

	protected String getMetaKeywords() {
		return "";
	}
    
}//END OF FILE