/**
 * 
 */
package mx.com.recomendare.web.comments;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.commons.models.detachables.ItemDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.session.AuthenticatedPage;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author jerry
 *
 */
public final class AddCommentPage extends AuthenticatedPage {
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @param parameters
     */
    public AddCommentPage(PageParameters parameters) {
        super(parameters);
        String itemKey = parameters.getString("i");
        if (itemKey != null && itemKey.trim().length() > 0) {
            int itemId = Integer.parseInt(itemKey);
            IModel itemModel = new CompoundPropertyModel(new ItemDetachableModel(itemId, ItemDetachableModel.DETAIL_MODE));
            setModel(itemModel);
        }
        init();
    }

    private void init() {
        //COMMENT
        add(new Label("yourCommentsLabel", new LabelStringDetachableModel("YOUR_COMMENT")));
        final CommentsTextPanel commentsPanel = new CommentsTextPanel("commentsPanel", getModel());
        add(commentsPanel);
    }

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new Model(Constants.SITE_NAME + " - " +new LabelStringDetachableModel("ADD_COMMENT"));
	}

	protected String getMetaDescription() {
		return "";
	}

	protected String getMetaKeywords() {
		return "";
	}
    
}//END OF FILE