/**
 * 
 */
package mx.com.recomendare.web.search;

import java.util.Collections;
import java.util.List;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.util.Util;
import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.commons.components.ImageButtonPagingNavigator;
import mx.com.recomendare.web.commons.components.NavigationPanel;
import mx.com.recomendare.web.commons.components.SearchPanel;
import mx.com.recomendare.web.commons.components.StarsRatingPanel;
import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.commons.models.detachables.HighlightingModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.SearchResultsListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.sortables.SortableSearchResultsListDataProvider;
import mx.com.recomendare.web.items.ViewItemPage;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.image.resource.ThumbnailImageResource;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * @author jerry
 *
 */
public final class SearchResultsPage extends BasePage {
	//the log...
	private static final Log log = LogFactory.getLog(SearchResultsPage.class);
	
	//the keyword...
	private String searchKeywords;
	/**
	 * 
	 * @param parameters
	 */
	public SearchResultsPage(PageParameters parameters) {
		super(parameters);
		searchKeywords = parameters.getString("s");
		log.info(searchKeywords);
		if(searchKeywords != null && searchKeywords.trim().length() > 0)  {
			IModel resultsModel = new CompoundPropertyModel(new SearchResultsListDetachableModel(searchKeywords));
			setModel(resultsModel);
			init();
		}
		else  {
			IModel resultsModel = new CompoundPropertyModel(Collections.EMPTY_LIST);
			setModel(resultsModel);
			init();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void init()  {
		//TITLE
		add(new Label("title", new LabelStringDetachableModel("SEARCH_RESULTS")));
		add(new Label("searchKeywords", searchKeywords));
		//SEARCH FORM
		add(new SearchPanel("searchPanel", new CompoundPropertyModel(new SearchPanel.SearchModel(searchKeywords))));
		//SEARCH RESULTS
		add(new Label("totalSearchResultsLabel", new LabelStringDetachableModel("TOTAL_RESULTS")));
		add(new Label("totalSearchResultsValue", String.valueOf(((List<ItemModel>)getModelObject()).size())));
		//the list
		final SortableSearchResultsListDataProvider searchResultsListDataProvider = new SortableSearchResultsListDataProvider((List<ItemModel>)getModelObject());
		final DataView searchResultsListDataView = new DataView("searchResultsListDataView", searchResultsListDataProvider, Constants.MAX_ITEMS_PER_PAGE){
			private static final long serialVersionUID = 1L;
			protected void populateItem(final Item item) {
				final ItemModel model = (ItemModel)item.getModelObject();
				//id
				item.add(new Label("itemResultId", item.getId()));
				//location
				item.add(new NavigationPanel("navigation", item.getModel()));
				//distance
				if(model.getLocation() != null)  {
					item.add(new Label("itemResultDistance", String.valueOf(Util.calculateDistanceBetweenTwoPoints(getLocation().getLatitude(), getLocation().getLongitude(), model.getLocation().getLatitude(), model.getLocation().getLongitude())).substring(0, 4) + " km."));
				}
				else  {
					item.add(new Label("itemResultDistance", new LabelStringDetachableModel("NOT_APPLY")));
				}
				//link name
				final Link viewSearchResultItemByTextLink = new Link("viewSearchResultItemByTextLink"){
					private static final long serialVersionUID = 1L;
					public void onClick() {
						PageParameters parameters = new PageParameters();
                        parameters.put("i", model.getId());
                        parameters.put("n", model.getName());
                        setResponsePage(ViewItemPage.class, parameters);
					}
				};
				//picture
				if(model.getPictures() != null && model.getPictures().get(0) != null && model.getPictures().get(0).getContent().length > 0) {
                    final DynamicImageResource imageResource = new DynamicImageResource() {
                        private static final long serialVersionUID = 1L;
                        protected byte[] getImageData() {
                            return model.getPictures().get(0).getContent();
                        }
                    };
                    final ThumbnailImageResource thumbnailImageResource = new ThumbnailImageResource(imageResource, 80);
                    thumbnailImageResource.setCacheable(true);
                    viewSearchResultItemByTextLink.add(new Image("searchResultItemLinkPicture", thumbnailImageResource));
                } else {
                	viewSearchResultItemByTextLink.add(new Image("searchResultItemLinkPicture", WebUtil.PICTURE_EMPTY_IMAGE));
                }
				//name
				final Label itemResultName = new Label("itemResultName", new HighlightingModel(new PropertyModel(model, "name"), new Model(searchKeywords)));
				itemResultName.setEscapeModelStrings(false);
				viewSearchResultItemByTextLink.add(itemResultName);
				item.add(viewSearchResultItemByTextLink);
				//description
				final IModel previewDescription = (model.getDescription().length() > 50)?new Model(model.getDescription().substring(0, 50) +" ..."):new Model(model.getDescription()+" ...");
				final Label itemResultDescriptionLabel = new Label("itemResultDescription", new HighlightingModel(previewDescription, new Model(searchKeywords)));
				itemResultDescriptionLabel.setEscapeModelStrings(false);
				item.add(itemResultDescriptionLabel);
				//rating
				item.add(new StarsRatingPanel("itemResultRating", model.getOverallRating(), StarsRatingPanel.NORMAL_STYLE));
				//price
				item.add(new Label("itemResultPriceMinValue", Util.getCurrencyFormat(model.getPrice().getMinPrice(), getLocation().getLanguageCode(), getLocation().getCountryCode())));
				item.add(new Label("itemResultPriceMaxValue", Util.getCurrencyFormat(model.getPrice().getMaxPrice(), getLocation().getLanguageCode(), getLocation().getCountryCode())));
				//for the rows...
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					private static final long serialVersionUID = 1L;
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "row-a" : "row-b";
					}
				}));
			}
		};
		//HEADERS
		//id
		add(new Label("itemResultIdLabel", new LabelStringDetachableModel("ID")));
		//location
		final OrderByBorder itemResultOrderByLocation = new OrderByBorder("orderByLocation", SortableSearchResultsListDataProvider.SORT_BY_LOCATION, searchResultsListDataProvider) {
			private static final long serialVersionUID = 1L;
			protected void onSortChanged() {
            	searchResultsListDataView.setCurrentPage(0);
            }
        };
        itemResultOrderByLocation.add(new Label("itemResultOrderByLocationLabel", new LabelStringDetachableModel("LOCATION")));
        add(itemResultOrderByLocation);
        //distance
        final OrderByBorder itemResultOrderByDistance = new OrderByBorder("orderByDistance", SortableSearchResultsListDataProvider.SORT_BY_DISTANCE, searchResultsListDataProvider) {
			private static final long serialVersionUID = 1L;
			protected void onSortChanged() {
            	searchResultsListDataView.setCurrentPage(0);
            }
        };
        itemResultOrderByDistance.add(new Label("itemResultOrderByDistanceLabel", new LabelStringDetachableModel("DISTANCE")));
        add(itemResultOrderByDistance);
		//name
		final OrderByBorder itemResultOrderByName = new OrderByBorder("orderByName", SortableSearchResultsListDataProvider.SORT_BY_NAME, searchResultsListDataProvider) {
			private static final long serialVersionUID = 1L;
			protected void onSortChanged() {
            	searchResultsListDataView.setCurrentPage(0);
            }
        };
        itemResultOrderByName.add(new Label("itemResultOrderByNameLabel", new LabelStringDetachableModel("NAME")));
		add(itemResultOrderByName);
		//ratings
		final OrderByBorder itemResultOrderByRatings = new OrderByBorder("orderByRatings", SortableSearchResultsListDataProvider.SORT_BY_RATING, searchResultsListDataProvider) {
			private static final long serialVersionUID = 1L;
			protected void onSortChanged() {
            	searchResultsListDataView.setCurrentPage(0);
            }
        };
        itemResultOrderByRatings.add(new Label("itemResultOrderByRatingsLabel", new LabelStringDetachableModel("RATING")));
        add(itemResultOrderByRatings);
        //price
        final OrderByBorder itemResultOrderByPrice = new OrderByBorder("orderByPrice", SortableSearchResultsListDataProvider.SORT_BY_PRICE, searchResultsListDataProvider) {
			private static final long serialVersionUID = 1L;
			protected void onSortChanged() {
            	searchResultsListDataView.setCurrentPage(0);
            }
        };
        itemResultOrderByPrice.add(new Label("itemResultOrderByPriceLabel", new LabelStringDetachableModel("PRICE")));
        add(itemResultOrderByPrice);
        //NAVIGATORS
        add(searchResultsListDataView);
        add(new ImageButtonPagingNavigator("topNavigator", searchResultsListDataView));
        add(new ImageButtonPagingNavigator("bottomNavigator", searchResultsListDataView));
        //AD
        add(new Image("adSenseImage", WebUtil.AD_BANNER_IMAGE));
	}

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new Model(Constants.SITE_NAME + " - "+ new LabelStringDetachableModel("SEARCH_RESULTS")+" - "+searchKeywords);
	}

	protected String getMetaDescription() {
		return "";
	}

	protected String getMetaKeywords() {
		return "";
	}

}//END OF FILE