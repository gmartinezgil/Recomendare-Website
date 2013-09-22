/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.search.SearchResultsPage;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Response;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteBehavior;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.IAutoCompleteRenderer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author jerry
 *
 */
public final class SearchPanel extends Panel {
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 */
	public SearchPanel(String id) {
		super(id);
		//SEARCH
        add(new SearchForm("searchForm", new CompoundPropertyModel(new SearchModel())));
	}
	
	/**
	 * 
	 * @param id
	 * @param model
	 */
	public SearchPanel(String id, IModel model) {
		super(id, model);
		//SEARCH
        add(new SearchForm("searchForm", model));
	}



	/**
     * SearchForm 
     */
    final class SearchForm extends Form {
        private static final long serialVersionUID = 1L;
        
        public SearchForm(String id, IModel model) {
            super(id, model);
            init();
        }

        private void init() {
        	//location...
        	final LocationModel location = WebUtil.getUserActualLocation((SignInSession)getSession());
            //the search field...
            final TextField search = new TextField("search");
            //set the custom renderer for this text field...
            final IAutoCompleteRenderer informativeRenderer = new AbstractAutoCompleteRenderer() {
                private static final long serialVersionUID = 1L;
                protected String getTextValue(Object object) {
                    return ((NameCategoryPairModel) object).getName();
                }
                protected void renderChoice(Object object, Response response, String arg2) {
                    String val = ((NameCategoryPairModel) object).getName();
                    response.write("<div style='float:left; color:orange;'>");
                    response.write(val);
                    response.write("</div><div style='text-align:right; width:100%;'>");
                    response.write(((NameCategoryPairModel) object).getCategory());
                    response.write("</div>");
                }
            };
            search.add(new AutoCompleteBehavior(informativeRenderer) {
                private static final long serialVersionUID = 1L;
                //the list of the items founded...
                private List<NameCategoryPairModel> itemsPairsFounded = new ArrayList<NameCategoryPairModel>();
                
                protected Iterator<NameCategoryPairModel> getChoices(final String input) {
                    if (input.trim().length() > 0) {
                        itemsPairsFounded.clear();
                        List<ItemModel> itemsFounded = ((Main)getApplication()).getServices().getMainService().getItemModelsByKeyword(input, location.getLanguageCode(), location.getCountryCode());
                        if(itemsFounded != null && itemsFounded.size() > 0)  {
                        	final Iterator<ItemModel> iterator = itemsFounded.iterator();
                        	while (iterator.hasNext()) {
								final ItemModel itemModel = (ItemModel) iterator.next();
								NameCategoryPairModel pairModel = new NameCategoryPairModel(itemModel.getName(), itemModel.getCategory().getCategoryName());
								itemsPairsFounded.add(pairModel);
							}
                        }
                    }
                    return itemsPairsFounded.iterator();
                }
            });
            search.setOutputMarkupId(true);
            add(search);
            //button...
            final Button searchButton = new Button("searchButton", new LabelStringDetachableModel("SEARCH")) {
                private static final long serialVersionUID = 1L;
                public void onSubmit() {
                    SearchModel model = (SearchModel) getForm().getModelObject();
                    String query = model.getSearch();
                    if(query != null && query.trim().length() > 0)  {
                    	if(model.getSearchOptions().intValue() == 0)  {
                    		PageParameters parameters = new PageParameters();//TODO:should be a statistic taken into account...
                            parameters.put("s", query);
                            setResponsePage(SearchResultsPage.class, parameters);
                    	}
                    	else  {
                    		//TODO: google adsense search...
                    	}
                    }
                }
            };
            add(searchButton);
            //search options...
            final RadioGroup radioGroup = new RadioGroup("searchOptions");
            final Radio localSearchRadio = new Radio("localSearchRadio", new Model(0));
            radioGroup.add(localSearchRadio);
            radioGroup.add(new Label("localSearchRadioName", Constants.SITE_NAME));
            final Radio googleAdSenseSearchRadio = new Radio("googleAdSenseSearchRadio", new Model(1));
            radioGroup.add(googleAdSenseSearchRadio);
            radioGroup.add(new Image("googleAdSenseSearchRadioImage", WebUtil.GOOGLE_ADSENSE_SEARCH_IMAGE));
            add(radioGroup);
        }
    }

    /**
     * SearchModel 
     */
    public static final class SearchModel implements Serializable {
        private static final long serialVersionUID = 1L;
        
        //the field of text...
        private String search;
        //the options
        private Integer searchOptions;
        
        public SearchModel() {}

		public SearchModel(String search) {
			this.search = search;
		}

        public String getSearch() {
            return search;
        }

        public void setSearch(String search) {
            this.search = search;
        }

        public Integer getSearchOptions() {
			return searchOptions;
		}

		public void setSearchOptions(Integer searchOptions) {
			this.searchOptions = searchOptions;
		}

		public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            sb.append(getClass().getName());
            sb.append("] - {search = '");
            sb.append(search);
            sb.append("'}");
            return sb.toString();
        }
    }
    
    /**
     * NameCategoryPairModel
     * represents a name and a category values to show...in the search text field... 
     *
     */
    final class NameCategoryPairModel implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String name;
        private String category;

        public NameCategoryPairModel(String name, String category) {
            this.name = name;
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            sb.append(getClass().getName());
            sb.append("] - {name = '");
            sb.append(name);
            sb.append("', category = '");
            sb.append(category);
            sb.append("'}");
            return sb.toString();
        }
    }

}//END OF FILE