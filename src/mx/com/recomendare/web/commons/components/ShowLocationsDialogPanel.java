/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.commons.models.SelectOption;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * @author Jerry
 *
 */
public final class ShowLocationsDialogPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	//the log...
	private static final Log log = LogFactory.getLog(ShowLocationsDialogPanel.class);
	
	//the reference to the window...
	private ModalWindow parentWindow;
	
	public ShowLocationsDialogPanel(String id, IModel model, ModalWindow parentWindow) {
		super(id, model);
		this.parentWindow = parentWindow;
		init();
	}
	
	private void init()  {
		add(new LocationSelectionForm("locationSelectionForm", new CompoundPropertyModel(new LocationSelectedModel())));
	}
	
	/**
	 * SelectLocationForm
	 *
	 */
	final class LocationSelectionForm extends Form {
		private static final long serialVersionUID = 1L;
		
		public LocationSelectionForm(String id, IModel model) {
			super(id, model);
			init();
		}

		@SuppressWarnings("unchecked")
		private void init()  {
			//fill
			final List<SelectOption> locationOptions = getSelectOptionsFromLocations((List<LocationModel>)ShowLocationsDialogPanel.this.getModelObject());
			final LocationSelectedModel firstLocation = (LocationSelectedModel)getModelObject();
			firstLocation.setLocation(locationOptions.get(0));
			//locations
			add(new Label("titleLabel", new LabelStringDetachableModel("SELECT_LOCATION")));
			final DropDownChoice locations = new DropDownChoice("locations", new PropertyModel(getModelObject(), "location"), locationOptions, WebUtil.KEYVALUE_CHOICE_RENDERER){
				private static final long serialVersionUID = 1L;
                protected CharSequence getDefaultChoice(final Object selected) {
                    return "<option value=\"\"></option>";
                }
			};
			add(locations);
			//button
			final AjaxButton okButton = new AjaxButton("okButton") {
				private static final long serialVersionUID = 1L;
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					if(target != null)  {
						LocationSelectedModel model = (LocationSelectedModel)form.getModelObject();
						LocationModel location = ((List<LocationModel>)ShowLocationsDialogPanel.this.getModelObject()).get(Integer.parseInt(model.getLocation().getKey()));
						log.info(location);
						parentWindow.setModel(new Model(location));
						parentWindow.close(target);
					}
				}
			};
			okButton.setModel(new LabelStringDetachableModel("OK"));
			add(okButton);
		}
	}
	
	/**
	 * LocationSelectedModel
	 *
	 */
	final class LocationSelectedModel implements Serializable {
		private static final long serialVersionUID = 1L;
		private SelectOption location;
		
		public void setLocation(SelectOption location) {
			this.location = location;
		}
		public SelectOption getLocation() {
			return location;
		}
		
	}
	
	//to convert to the model...
	private List<SelectOption> getSelectOptionsFromLocations(final List<LocationModel> locations)  {
		List<SelectOption> options = new ArrayList<SelectOption>(locations.size());
		Iterator<LocationModel> iterator = locations.iterator();
		while (iterator.hasNext()) {
			LocationModel model = (LocationModel) iterator.next();
			SelectOption option = new SelectOption(String.valueOf(model.getId()), model.getSummary());
			options.add(option);
		}
		return options;
	}

}//END OF FILE