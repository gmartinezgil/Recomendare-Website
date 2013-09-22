/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.mail.MessagingException;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.util.Util;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.AddressModelImpl;
import mx.com.recomendare.web.commons.models.BirthDateModelImpl;
import mx.com.recomendare.web.commons.models.LocationModelImpl;
import mx.com.recomendare.web.commons.models.PictureModelImpl;
import mx.com.recomendare.web.commons.models.UserModelImpl;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.users.UserWizardModel;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wicketstuff.progressbar.ProgressBar;
import org.wicketstuff.progressbar.Progression;
import org.wicketstuff.progressbar.ProgressionModel;

/**
 * @author jerry
 *
 */
public final class ShowProgressBarDialogPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	//the log...
	private static final Log log = LogFactory.getLog(ShowProgressBarDialogPanel.class);
	
	//the progress of the task...
	private int progress = 0;
	//the bar..
	private ProgressBar percentageCompletedBar; 
	//the modal window..
	private ModalWindow parentWindow;
	//the new id...
	private int newId = -1;

	/**
	 * @param id
	 * @param model
	 * @param window 
	 */
	public ShowProgressBarDialogPanel(final String id, final IModel model, final ModalWindow parentWindow) {
		super(id, model);
		this.parentWindow = parentWindow;
		init();
	}
	
	private void init()  {
		add(new Label("titleLabel", new LabelStringDetachableModel("MESSAGE")));
		add(new Label("messageLabel", new LabelStringDetachableModel("SUBMIT_YOUR_USER")));
		final Form form = new Form("form");
		//completed bar...
		percentageCompletedBar = new ProgressBar("percentageCompletedBar", new ProgressionModel() {
			private static final long serialVersionUID = 1L;
			protected Progression getProgression() {
	            return new Progression(progress);
	        }
	    });
		//percentageCompletedBar.setVisible(false);
		form.add(percentageCompletedBar);
		//buttons...
		final AjaxButton cancelButton = new AjaxButton("cancelButton") {
			private static final long serialVersionUID = 1L;
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				parentWindow.setModel(new Model(newId));
				parentWindow.close(target);
			}
		};
		cancelButton.setModel(new LabelStringDetachableModel("CANCEL"));
		form.add(cancelButton);
		final AjaxButton proceedButton = new AjaxButton("proceedButton"){
			private static final long serialVersionUID = 1L;
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				//percentageCompletedBar.setVisible(true);
				// disable buttons
				cancelButton.setEnabled(false);
				setEnabled(false);
				progress = 10;
				percentageCompletedBar.start(target);
				//MODEL
				UserWizardModel model = (UserWizardModel)ShowProgressBarDialogPanel.this.getModelObject();
				log.info(model);
				UserModelImpl user = new UserModelImpl();
				user.setName(model.getName());
				user.setFirstName(model.getFirstName());
				user.setLastName(model.getLastName());
				user.setScreenName(model.getScreenName());
				user.setLogin(model.getLogin());
				user.setPassword(model.getPassword());
				user.setGenderId(Integer.parseInt(model.getGender().getKey()));
				BirthDateModelImpl birthDate = new BirthDateModelImpl();
				birthDate.setDay(Short.parseShort(model.getDay().getKey()));
				birthDate.setMonth(Short.parseShort(model.getMonth().getKey()));
				birthDate.setYear(Short.parseShort(model.getYear().getKey()));
				user.setBirthDate(birthDate);
				user.setLastIPAddress(WebUtil.getUserRemoteAddress());
				progress = 20;
				//the address...
				AddressModelImpl address = new AddressModelImpl();
				address.setEmail(model.getEmail());
				address.setStreetName(model.getStreet());
				address.setStreetOutsideNumber(model.getStreetNumber());
				address.setTelephoneNumber(model.getPhoneNumber());
				user.setAddress(address);
				progress = 30;
				//location...
				LocationModelImpl location = new LocationModelImpl();
				location.setCityId(Integer.parseInt(model.getCity().getKey()));
				location.setLanguageId(Integer.parseInt(model.getLanguage().getKey()));
				location.setLatitude(model.getLatitude());
				location.setLongitude(model.getLongitude());
				user.setLocation(location);
				progress = 40;
				//TODO: could also include it's favorite location...or last location...
				//now the avatar picture...
				log.info("reading avatar in - "+model.getFileNameOfAvatarPictureToUpload());
				final File pictureFile = new File(model.getFileNameOfAvatarPictureToUpload());
				if(pictureFile.exists())  {
					long length = pictureFile.length();
					if(length < Integer.MAX_VALUE) {
						try {
							PictureModelImpl picture = new PictureModelImpl();
							picture.setContent(Util.getContentAsBytesFromFile(pictureFile));
							user.setAvatarPicture(picture);
							pictureFile.delete();
						} 
						catch (FileNotFoundException e) {
							log.error("Can't find the picture '"+pictureFile.getName()+"' in path '"+pictureFile.getAbsolutePath()+"'", e);
						} 
						catch (IOException e) {
							log.error("Can't read the picture '"+pictureFile.getName()+"' in path '"+pictureFile.getAbsolutePath()+"'", e);
						}
					}
				}
				progress = 50;
				//and submit it...
				newId = ((Main)getApplication()).getServices().getMainService().addUserModel(user);//TODO:have to throw the exception to acknowledge there was a problem...
				progress = 75;
				final String urlToConfirm =  Constants.SITE_URL + "/confirm/u/" + WebUtil.encodeURL(model.getScreenName()) + "/k/" + Util.getRandomUserKey();   
				StringBuffer content = new StringBuffer();
				content.append("<html><body><h3>");
				content.append(new LabelStringDetachableModel("CONFIRM_YOUR_SUBSCRIPTION").toString());
				content.append("</h3><p>");
				content.append(new LabelStringDetachableModel("CLICK_ON_THE_NEXT_ADDRESS").toString());
				content.append("</p><p><a href=\"");
				content.append(urlToConfirm);
				content.append("\">");
				content.append(urlToConfirm);
				content.append("</a>");
				content.append("</body></html>");
				try {
					((Main)getApplication()).getServices().getMailService().sendMailTo(model.getEmail(), content.toString());
				} 
				catch (MessagingException e) {
					log.error("Can't send email of confirmation, reason - "+e.getMessage(), e);
				} 
				catch (IOException e) {
					log.error("Can't connect to email server, reason - "+e.getMessage(), e);
				}
				progress = 100;
				//finish it..
				target.appendJavascript("alert('FINISHED!')");
	            parentWindow.setModel(new Model(newId));
	            parentWindow.close(target);
			}
		};
		proceedButton.setModel(new LabelStringDetachableModel("PROCEED"));
		form.add(proceedButton);
		add(form);
	}
	
}//END OF FILE