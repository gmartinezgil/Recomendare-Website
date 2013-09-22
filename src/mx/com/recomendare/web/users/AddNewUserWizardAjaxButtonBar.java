/**
 * 
 */
package mx.com.recomendare.web.users;

import mx.com.recomendare.web.commons.components.AjaxWizardButton;
import mx.com.recomendare.web.commons.components.FormValidatorVisitor;
import mx.com.recomendare.web.commons.components.ShowProgressBarDialogPanel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.wizard.IWizardModel;
import org.apache.wicket.extensions.wizard.IWizardStep;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.extensions.wizard.WizardButtonBar;
import org.apache.wicket.markup.html.form.Form;

/**
 * @author jerry
 *
 */
public final class AddNewUserWizardAjaxButtonBar extends WizardButtonBar {
	private static final long serialVersionUID = 1L;
	//the wizard to control...
	private Wizard wizard;
	//the window to show...
	private ModalWindow window;

	public AddNewUserWizardAjaxButtonBar(final String id, final Wizard wizard, final ModalWindow window) {
		super(id, wizard);
		this.wizard = wizard;
		this.window = window;
		init();
	}

	private void init()  {
		//previous button
		addOrReplace(new AjaxWizardButton("previous", wizard, wizard.getForm(), "PREVIOUS") {
			private static final long serialVersionUID = 1L;
			protected void onClick(AjaxRequestTarget target, Form form) {
				getWizardModel().previous();
				target.addComponent(wizard);
			}

			public final boolean isEnabled() {
				return getWizardModel().isPreviousAvailable();
			}
		});
		//next button
		addOrReplace(new AjaxWizardButton("next", wizard, wizard.getForm(), "NEXT") {
			private static final long serialVersionUID = 1L;
			protected void onClick(AjaxRequestTarget target, Form form) {
				IWizardModel wizardModel = getWizardModel();
				IWizardStep step = wizardModel.getActiveStep();

				// let the step apply any state
				step.applyState();

				// if the step completed after applying the state, move the model onward
				if (step.isComplete()) {
					wizardModel.next();
				} else {
					form.visitChildren(new FormValidatorVisitor());
					error(new LabelStringDetachableModel("ERROR"));//getLocalizer().getString("org.apache.wicket.extensions.wizard.NextButton.step.did.not.complete", this)
				}
				target.addComponent(wizard);
			}

			public final boolean isEnabled() {
				return getWizardModel().isNextAvailable();
			}
		});
		//cancel button
		addOrReplace(new AjaxWizardButton("cancel", wizard, wizard.getForm(), "CANCEL"){
			private static final long serialVersionUID = 1L;
			protected void onClick(AjaxRequestTarget target, Form form) {
				getWizardModel().cancel();
				target.addComponent(wizard);
			}
			
			public final boolean isEnabled() {
				return true;
			}
		});
		//finish button
		addOrReplace(new AjaxWizardButton("finish", wizard, wizard.getForm(), "FINISH") {
			private static final long serialVersionUID = 1L;
			protected void onClick(AjaxRequestTarget target, Form form) {
				IWizardModel wizardModel = getWizardModel();
				IWizardStep step = wizardModel.getActiveStep();

				// let the step apply any state
				step.applyState();

				// if the step completed after applying the state, notify the wizard
				if (step.isComplete()) {
					window.setInitialWidth(80);
		            window.setInitialHeight(10);
		            window.setTitle(new LabelStringDetachableModel("SAVING_USER"));
		            window.setContent(new ShowProgressBarDialogPanel(window.getContentId(), wizard.getModel(), window));
					window.show(target);
					window.setWindowClosedCallback(new ModalWindow.WindowClosedCallback(){
						private static final long serialVersionUID = 1L;
						public void onClose(AjaxRequestTarget target) {
							getWizardModel().finish();
						}
					});
				}
				else {
					error(new LabelStringDetachableModel("ERROR"));//getLocalizer().getString("org.apache.wicket.extensions.wizard.FinishButton.step.did.not.complete", this)
				}
				target.addComponent(wizard);
			}

			public final boolean isEnabled() {
				IWizardStep activeStep = getWizardModel().getActiveStep();
				return (activeStep != null && getWizardModel().isLastStep(activeStep));
			}
		});

	}

}//END OF FILE