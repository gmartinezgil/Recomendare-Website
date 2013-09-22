/**
 * 
 */
package mx.com.recomendare.web.commons.models.detachables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import mx.com.recomendare.web.commons.components.TagCloud;
import mx.com.recomendare.web.commons.models.CommentModel;
import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.search.SearchResultsPage;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author jerry
 *
 */
public final class ItemTagsLoadableDetachableModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
	//the model...
	private ItemModel model;
	//the keywords...
	private Map<String, Integer> keywords = new HashMap<String, Integer>();

	/**
	 * @param object
	 */
	public ItemTagsLoadableDetachableModel(ItemModel model) {
		this.model = model;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	protected Object load() {
		addKeywordsFromText(model.getName());
		addKeywordsFromText(model.getDescription());
		addKeywordsFromText(model.getCategory().getCategoryName());
		addKeywordsFromText(model.getCategory().getSubcategoryName());
		if(model.getComments() != null && model.getComments().size() > 0)  {
			Iterator<CommentModel> iterator = model.getComments().iterator();
			while (iterator.hasNext()) {
				CommentModel comment = (CommentModel) iterator.next();
				addKeywordsFromText(comment.getUserComment());
			}
		}
		List<TagCloud.Tag> tags = new ArrayList<TagCloud.Tag>(keywords.size());
		Iterator<String> iterator = keywords.keySet().iterator();
		while (iterator.hasNext()) {
			final String keyword = (String) iterator.next();
			final int weight = keywords.get(keyword);
			if(weight > 1)  {
				tags.add(new TagCloud.Tag(keyword, weight){
					private static final long serialVersionUID = 1L;
					public Link getLink(String id) {
						return new Link(id){
							private static final long serialVersionUID = 1L;
							public void onClick() {
								PageParameters parameters = new PageParameters();
			                    parameters.put("s", keyword);
			                    setResponsePage(SearchResultsPage.class, parameters);						}
						};
					}
				});
			}
		}
		return tags;
	}
	
	private void addKeywordsFromText(final String text)  {
		StringTokenizer tokenizer = new StringTokenizer(text);
		while (tokenizer.hasMoreElements()) {
			String token = tokenizer.nextToken();
			if(!keywords.containsKey(token))  {
				keywords.put(token, 1);
			}
			else  {
				keywords.put(token, (keywords.get(token) + 1));
			}
		}
	}

}//END OF FILE