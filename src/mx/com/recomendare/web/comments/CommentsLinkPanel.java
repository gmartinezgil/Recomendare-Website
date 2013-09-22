/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.recomendare.web.comments;

import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 *
 * @author jerry
 */
public final class CommentsLinkPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
    /**
     * 
     * @param id
     * @param model
     */
    public CommentsLinkPanel(String id, IModel model) {
        super(id, model);
        init();
    }

    private void init() {
        final ItemModel model = (ItemModel)getModelObject();
        final Link addCommentLink = new Link("addCommentLink") {
            private static final long serialVersionUID = 1L;
            public void onClick() {
                PageParameters parameters = new PageParameters();
                parameters.put("i", model.getId());
                parameters.put("n", model.getName());
                setResponsePage(AddCommentPage.class, parameters);
            }
        };
        addCommentLink.add(new Image("addPicture", WebUtil.ADD_COMMENT_IMAGE));
        addCommentLink.add(new Label("addCommentLabel", new LabelStringDetachableModel("ADD_COMMENT")));
        add(addCommentLink);
    }

}//END OF FILE