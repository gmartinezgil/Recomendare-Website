/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.session.SignInPanel;
import mx.com.recomendare.web.session.SignInSession;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author jerry
 *
 */
public final class AjaxEditableTextPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	//the containers...
	private WebMarkupContainer readOnlyModeContainer;
	private WebMarkupContainer editModeContainer;
	//dialog...
	private ModalWindow signInModalWindow;

	/**
	 * @param id
	 * @param model
	 */
	public AjaxEditableTextPanel(String id, IModel model) {
		super(id, model);
		init();
	}
	
	private void init()  {
		//sign in session dialog...
		signInModalWindow = new ModalWindow("signInModalWindow");
		signInModalWindow.setCookieName("signindialog-1");
		signInModalWindow.setResizable(false);
		signInModalWindow.setInitialWidth(40);
		signInModalWindow.setInitialHeight(30);
		signInModalWindow.setWidthUnit("em");
		signInModalWindow.setHeightUnit("em");
		signInModalWindow.setCssClassName(ModalWindow.CSS_CLASS_BLUE);
		add(signInModalWindow);
		//read only container
		readOnlyModeContainer = new WebMarkupContainer("readOnlyModeContainer");
		final Label text = new Label("text", getModel());
		text.setEscapeModelStrings(false);
		readOnlyModeContainer.add(text);
		readOnlyModeContainer.setOutputMarkupId(true);
		readOnlyModeContainer.setOutputMarkupPlaceholderTag(true);
		add(readOnlyModeContainer);
		//edit model container
		editModeContainer = new WebMarkupContainer("editModeContainer");
		editModeContainer.add(new EditableTextForm("editableTextForm", getModel()));
		editModeContainer.setOutputMarkupId(true);
		editModeContainer.setOutputMarkupPlaceholderTag(true);
		editModeContainer.setVisible(false);
		add(editModeContainer);
		//edit text link
		final AjaxFallbackLink editTextLink = new AjaxFallbackLink("editTextLink"){
			private static final long serialVersionUID = 1L;
			public void onClick(AjaxRequestTarget target) {
				if(!((SignInSession)getSession()).isSignedIn())  {
					signInModalWindow.setTitle(new LabelStringDetachableModel("NOT_SIGNED_IN"));
					signInModalWindow.setContent(new SignInPanel(signInModalWindow.getContentId(), signInModalWindow, true) {
						private static final long serialVersionUID = 1L;
						public boolean signIn(final String username, final String password) {
							return ((SignInSession)getSession()).authenticate(username, password);
						}
					});
					signInModalWindow.show(target);
				}
				else  {
					//readOnlyModeContainer.setVisible((editModeContainer.isVisible())?false:true);
					readOnlyModeContainer.setVisible(false);
					target.addComponent(readOnlyModeContainer);
					//editModeContainer.setVisible((readOnlyModeContainer.isVisible())?false:true);
					editModeContainer.setVisible(true);
					target.addComponent(editModeContainer);
				}
			}
		};
		add(editTextLink);
	}
	
	/**
	 * EditableTextForm
	 *
	 */
	final class EditableTextForm extends Form {
		private static final long serialVersionUID = 1L;

		/**
		 * @param id
		 * @param model
		 */
		public EditableTextForm(String id, IModel model) {
			super(id, model);
			init();
		}
		
		private void init()  {
			final TextArea comments = new TextArea("editor", getModel());            
			//TinyMCESettings settings = new TinyMCESettings(TinyMCESettings.Theme.advanced, TinyMCESettings.Language.es);
            //comments.add(new TinyMceBehavior(settings));
            add(comments);
            //BUTTONS
            final AjaxFallbackButton saveButton = new AjaxFallbackButton("save", new LabelStringDetachableModel("SAVE"), this) {
                private static final long serialVersionUID = 1L;
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					AjaxEditableTextPanel.this.setModel(form.getModel());
					readOnlyModeContainer.setVisible(true);
					target.addComponent(readOnlyModeContainer);
					editModeContainer.setVisible(false);
					target.addComponent(editModeContainer);
				}
            };
            add(saveButton);
            final AjaxFallbackButton cancelButton = new AjaxFallbackButton("cancel", new LabelStringDetachableModel("CANCEL"), this) {
                private static final long serialVersionUID = 1L;
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					readOnlyModeContainer.setVisible(true);
					target.addComponent(readOnlyModeContainer);
					editModeContainer.setVisible(false);
					target.addComponent(editModeContainer);
				}
            };
            cancelButton.setDefaultFormProcessing(false);
            add(cancelButton);
		}

	}


}//END OF FILE