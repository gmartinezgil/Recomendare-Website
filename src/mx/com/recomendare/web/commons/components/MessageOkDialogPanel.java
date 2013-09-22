/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author jerry
 *
 */
public final class MessageOkDialogPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	private ModalWindow modalWindow;

	/**
	 * @param id
	 * @param model
	 */
	public MessageOkDialogPanel(String id, IModel model, ModalWindow modalWindow) {
		super(id, model);
		this.modalWindow = modalWindow;
		init();
	}
	
	private void init()  {
		add(new Label("titleLabel", new LabelStringDetachableModel("MESSAGE")));
		add(new Label("messageLabel", getModelObjectAsString()));
		final AjaxButton okButton = new AjaxButton("okButton") {
			private static final long serialVersionUID = 1L;
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				modalWindow.close(target);
			}
		};
		okButton.setModel(new LabelStringDetachableModel("OK"));
		add(okButton);
	}

}//END OF FILE