/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables.sortables;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.util.Util;
import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author jerry
 *
 */
public final class SortableSearchResultsListDataProvider extends SortableDataProvider {
	private static final long serialVersionUID = 1L;
	
	//the type of sort to apply...
	public static final String SORT_BY_NAME = "name"; 
	public static final String SORT_BY_RATING = "rating";
	public static final String SORT_BY_PRICE = "price";
	public static final String SORT_BY_LOCATION = "location";
	public static final String SORT_BY_DISTANCE = "distance";
	
	private static final Log log = LogFactory.getLog(SortableSearchResultsListDataProvider.class);

	//the search results...
	private List<ItemModel> searchResultsItems;
	/**
	 * 
	 */
	public SortableSearchResultsListDataProvider(List<ItemModel> searchResultsItems) {
		this.searchResultsItems = searchResultsItems;
		setSort("relevance", true);//how they came from the search...
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(int, int)
	 */
	public Iterator<ItemModel> iterator(int first, int count) {
		final SortParam sortParam = getSort();
		log.info(sortParam.getProperty());
		log.info(sortParam.isAscending());
		if(sortParam.getProperty().equals(SORT_BY_NAME))  {
			Collections.sort(searchResultsItems, new Comparator<ItemModel>() {
				public int compare(final ItemModel o1, final ItemModel o2) {
					if(sortParam.isAscending()) {
						return o1.getName().compareTo(o2.getName());
					}
					else  {
						return -(o1.getName().compareTo(o2.getName()));
					}
				}
			});
		}
		else if(sortParam.getProperty().equals(SORT_BY_RATING))  {
			Collections.sort(searchResultsItems, new Comparator<ItemModel>() {
				public int compare(final ItemModel o1, final ItemModel o2) {
					//TODO:missing sortParam.isAscending() calculations...
					short o1OverralRating = o1.getOverallRating();
					short o2OverralRating = o2.getOverallRating();
					if(o1OverralRating > o2OverralRating) return 1;
					else if(o1OverralRating == o2OverralRating)  {
						if(o1.getNumberOfRatings() > o2.getNumberOfRatings()) return 1;
						else if(o1.getNumberOfRatings() == o2.getNumberOfRatings()) return 0;
						else return -1;
					}
					else if(o1OverralRating > o2OverralRating)  return -1;
					return 0;
				}
			});
		}
		else if(sortParam.getProperty().equals(SORT_BY_LOCATION))  {
			Collections.sort(searchResultsItems, new Comparator<ItemModel>() {
				public int compare(final ItemModel o1, final ItemModel o2) {
					if(o1.getLocation() != null && o2.getLocation() != null)  {
						if(sortParam.isAscending()) {
							return o1.getLocation().getCityName().compareTo(o2.getLocation().getCityName());
						}
						else  {
							return -(o1.getLocation().getCityName().compareTo(o2.getLocation().getCityName()));
						}
					}
					else {
						if(sortParam.isAscending()) {
							return o1.getCategory().getCategoryName().compareTo(o2.getCategory().getCategoryName());
						}
						else  {
							return -(o1.getCategory().getCategoryName().compareTo(o2.getCategory().getCategoryName()));
						}
					}
				}
			});
		}
		else if(sortParam.getProperty().equals(SORT_BY_DISTANCE))  {
			Collections.sort(searchResultsItems, new Comparator<ItemModel>() {
				public int compare(final ItemModel o1, ItemModel o2) {
					if(o1.getLocation() != null && o2.getLocation() != null)  {
						LocationModel location = WebUtil.getUserActualLocation(((SignInSession)RequestCycle.get().getSession()));
						LocationModel l1 = o1.getLocation();
						LocationModel l2 = o2.getLocation();
						double i1 = Util.calculateDistanceBetweenTwoPoints(l1.getLatitude(), l1.getLongitude(), location.getLatitude(), location.getLongitude());
						double i2 = Util.calculateDistanceBetweenTwoPoints(l2.getLatitude(), l2.getLongitude(), location.getLatitude(), location.getLongitude());
						if(sortParam.isAscending()) {
							if(i1 > i2) return 1;
							else if(i1 == i2) return 0;
							else return -1; 	
						}
						else  {
							if(i1 > i2) return -1;
							else if(i1 == i2) return 0;
							else return 1;
						}
					}
					else  {
						return 0;
					}
				}
			});
		}
		return searchResultsItems.subList(first, first+count).iterator();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
	 */
	public IModel model(Object o) {
		return new Model((ItemModel)searchResultsItems.get(searchResultsItems.indexOf(o)));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
	 */
	public int size() {
		return searchResultsItems.size();
	}

}//END O FILE