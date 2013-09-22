/**
 * 
 */
package mx.com.recomendare.web.comments;

import java.io.Serializable;

import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.ItemCommentsDAO;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.items.ViewItemPage;
import mx.com.recomendare.web.session.SignInSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Application;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.rating.RatingPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.hibernate.Session;

import wicket.contrib.tinymce.TinyMceBehavior;
import wicket.contrib.tinymce.settings.TinyMCESettings;

/**
 * @author jerry
 *
 */
//TODO:it should be an ajax form panel...all the time present...and submit immediately to the page...w/o refresh...
public final class CommentsTextPanel extends Panel {
    private static final long serialVersionUID = 1L;
    
    //the log...
    private static final Log log = LogFactory.getLog(CommentsTextPanel.class);

    /**
     * 
     * @param id
     * @param model
     */
    public CommentsTextPanel(String id, IModel model) {
        super(id, model);
        log.info(getModelObject());
        init();
    }

    private void init() {
        add(new CommentsForm("commentsForm", new CompoundPropertyModel(new CommentsFormModel())));
    }

    /**
     * The form
     */
    final class CommentsForm extends Form {
        private static final long serialVersionUID = 1L;

        public CommentsForm(String id, IModel model) {
            super(id, model);
            init();
        }

        private void init() {
            //RATING
            add(new Label("ratingLabel", new LabelStringDetachableModel("RATING")));
            final RatingPanel ratingPanel = new RatingPanel("rating", new PropertyModel(getModelObject(), "rating"), CommentsFormModel.MAX_RATING, true) {
                private static final long serialVersionUID = 1L;
                protected boolean onIsStarActive(int star) {
                    return (star < ((Integer) getModelObject()).intValue()) ? true : false;
                }
                protected void onRated(int rating, AjaxRequestTarget target) {
                    if (target != null) {
                        ((CommentsFormModel) CommentsForm.this.getModelObject()).setRating(rating);
                    }
                }
            };
            add(ratingPanel);
            //COMMENTS
            add(new Label("commentsLabel", new LabelStringDetachableModel("COMMENTS")));
            final TextArea comments = new TextArea("comments", new PropertyModel(getModelObject(), "comments"));            
            TinyMCESettings settings = new TinyMCESettings(TinyMCESettings.Theme.advanced, TinyMCESettings.Language.es);//TODO:switch the language...
            /*
            TinyMCESettings tinyMCESettings = new TinyMCESettings(TinyMCESettings.Mode.exact, TinyMCESettings.Theme.advanced);
            //language
            tinyMCESettings.setLanguage(TinyMCESettings.Language.ES);
            //spell
            SpellCheckPlugin spellCheckPlugin = new SpellCheckPlugin();
            tinyMCESettings.add(spellCheckPlugin.getSpellCheckButton(), TinyMCESettings.Toolbar.first, TinyMCESettings.Position.after);
            //emotions
            EmotionsPlugin emotionsPlugin = new EmotionsPlugin();
            tinyMCESettings.add(emotionsPlugin.getEmotionsButton(), TinyMCESettings.Toolbar.first, TinyMCESettings.Position.after);
            //context menu...
            ContextMenuPlugin contextMenuPlugin = new ContextMenuPlugin();
            tinyMCESettings.register(contextMenuPlugin);
            //remove the no needed...
			tinyMCESettings.disableButton(tinyMCESettings.anchor);
			tinyMCESettings.disableButton(tinyMCESettings.backcolor);
			tinyMCESettings.disableButton(tinyMCESettings.bold);
			tinyMCESettings.disableButton(tinyMCESettings.bullist);
			tinyMCESettings.disableButton(tinyMCESettings.charmap);
			tinyMCESettings.disableButton(tinyMCESettings.cleanup);
			tinyMCESettings.disableButton(tinyMCESettings.code);
			tinyMCESettings.disableButton(tinyMCESettings.fontselect);
			tinyMCESettings.disableButton(tinyMCESettings.fontsizeselect);
			tinyMCESettings.disableButton(tinyMCESettings.forecolor);
			tinyMCESettings.disableButton(tinyMCESettings.formatselect);
			tinyMCESettings.disableButton(tinyMCESettings.hr);
			tinyMCESettings.disableButton(tinyMCESettings.image);
			tinyMCESettings.disableButton(tinyMCESettings.indent);
			tinyMCESettings.disableButton(tinyMCESettings.italic);
			tinyMCESettings.disableButton(tinyMCESettings.justifycenter);
			tinyMCESettings.disableButton(tinyMCESettings.justifyfull);
			tinyMCESettings.disableButton(tinyMCESettings.justifyleft);
			tinyMCESettings.disableButton(tinyMCESettings.justifyright);
			tinyMCESettings.disableButton(tinyMCESettings.link);
			tinyMCESettings.disableButton(tinyMCESettings.newdocument);
			tinyMCESettings.disableButton(tinyMCESettings.numlist);
			tinyMCESettings.disableButton(tinyMCESettings.outdent);
			tinyMCESettings.disableButton(tinyMCESettings.removeformat);
			tinyMCESettings.disableButton(tinyMCESettings.separator);
			tinyMCESettings.disableButton(tinyMCESettings.strikethrough);
			tinyMCESettings.disableButton(tinyMCESettings.styleselect);
			tinyMCESettings.disableButton(tinyMCESettings.sub);
			tinyMCESettings.disableButton(tinyMCESettings.sup);
			tinyMCESettings.disableButton(tinyMCESettings.underline);
			tinyMCESettings.disableButton(tinyMCESettings.unlink);
			tinyMCESettings.disableButton(tinyMCESettings.visualaid);
            comments.add(new TinyMceBehavior(tinyMCESettings, false));
            */
            comments.add(new TinyMceBehavior(settings));
            add(comments);
            //BUTTONS
            final Button addCommentButton = new Button("add", new LabelStringDetachableModel("ADD")) {
                private static final long serialVersionUID = 1L;
				public void onSubmit() {
					final CommentsFormModel comments = (CommentsFormModel)getForm().getModelObject();
					final ItemModel model = (ItemModel)CommentsTextPanel.this.getModelObject();
					log.info("submited form - " + comments);
                    log.info("parent model object - "+model);
                    Session session = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
                    ItemCommentsDAO ratingsDAO =((Main)Application.get()).getServices().getDatabaseService().getRatingsDAO();
                    ratingsDAO.setSession(session);
                    ratingsDAO.saveRatingComment(model.getId(), ((SignInSession)getSession()).getUser(), comments.getRating(), comments.getComments());
                    PageParameters parameters = new PageParameters();
                    parameters.put("i", model.getId());
                    parameters.put("n", model.getName());
                    setResponsePage(ViewItemPage.class, parameters);
				}
            };
            add(addCommentButton);
            final Button cancelCommentButton = new Button("cancel", new LabelStringDetachableModel("CANCEL")) {
                private static final long serialVersionUID = 1L;
                public void onSubmit() {
                	final ItemModel model = (ItemModel)CommentsTextPanel.this.getModelObject();
                	PageParameters parameters = new PageParameters();
                    parameters.put("i", model.getId());
                    parameters.put("n", model.getName());
                    setResponsePage(ViewItemPage.class, parameters);
                }
            };
            cancelCommentButton.setDefaultFormProcessing(false);
            add(cancelCommentButton);
        }
    }

    /**
     * CommentsFormModel
     */
    final class CommentsFormModel implements Serializable {
        private static final long serialVersionUID = 1L;
        
        //the rating...
        public static final int MAX_RATING = 5;
        private int rating;
        //the comments...
        private String comments;

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            sb.append(getClass().getName());
            sb.append("] - {comments = '");
            sb.append(comments);
            sb.append("', rating = '");
            sb.append(rating);
            sb.append("'}");
            return sb.toString();
        }
    }
    
}//END OF FILE