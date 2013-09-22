/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.wizard.IWizard;
import org.apache.wicket.extensions.wizard.IWizardModel;
import org.apache.wicket.markup.html.form.Form;

/**
 * @author jerry
 *
 */

public abstract class AjaxWizardButton extends AjaxButton {
	private static final long serialVersionUID = 1L;
	private final IWizard wizard;

	public AjaxWizardButton(final String id, final IWizard wizard, final Form form, final String labelResourceKey){
		super(id, form);
		this.setModel(new LabelStringDetachableModel(labelResourceKey));
		this.wizard = wizard;
	}

	public AjaxWizardButton(final String id, final IWizard wizard, final String labelResourceKey) {
		this(id, wizard, null, labelResourceKey);
	}

	protected final IWizard getWizard() {
		return wizard;
	}

	protected final IWizardModel getWizardModel() {
		return getWizard().getWizardModel();
	}

	protected final void onSubmit(AjaxRequestTarget target, Form form) { 
		onClick(target, form);
	}

	protected abstract void onClick(AjaxRequestTarget target, Form form);
	
}//END OF FILE