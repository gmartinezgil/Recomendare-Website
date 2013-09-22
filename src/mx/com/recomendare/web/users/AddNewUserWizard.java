/**
 * 
 */
package mx.com.recomendare.web.users;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import mx.com.recomendare.util.Util;
import mx.com.recomendare.web.Index;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.components.MessageOkDialogPanel;
import mx.com.recomendare.web.commons.components.ShowLocationsDialogPanel;
import mx.com.recomendare.web.commons.components.UploadedThumbnailPicturePanel;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.commons.models.SelectOption;
import mx.com.recomendare.web.commons.models.detachables.CitiesSelectOptionListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.CountriesListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.DaysListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.GendersListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LanguagesSelectOptionListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.MonthsListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.YearsListDetachableModel;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.Resource;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.wizard.IWizardModel;
import org.apache.wicket.extensions.wizard.IWizardModelListener;
import org.apache.wicket.extensions.wizard.IWizardStep;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardModel;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author jerry
 *
 */
public final class AddNewUserWizard extends Wizard {
	private static final long serialVersionUID = 1L;

	//the log...
	private static final Log log = LogFactory.getLog(AddNewUserWizard.class);
	
	//the modal window...
	private ModalWindow window;
	
	/**
	 * @param id
	 * @param window 
	 */
	public AddNewUserWizard(final String id, final ModalWindow window) {
		super(id, false);
		this.window = window;
		//set the model to be used in the steps...
		IModel model = new CompoundPropertyModel(new UserWizardModel());
		setModel(model);
		//define the steps...
		UserCaptchaValidationStep captchaValidationStep = new UserCaptchaValidationStep(null, new LabelStringDetachableModel("TITLE"), new LabelStringDetachableModel("SUMMARY"), model);
		UserNameStep nameStep = new UserNameStep(captchaValidationStep, new LabelStringDetachableModel("TITLE"), new LabelStringDetachableModel("SUMMARY"), model);
		UserAddressStep addressStep = new UserAddressStep(nameStep, new LabelStringDetachableModel("TITLE"), new LabelStringDetachableModel("SUMMARY"), model);
		UserLoginStep loginStep = new UserLoginStep(addressStep, new LabelStringDetachableModel("TITLE"), new LabelStringDetachableModel("SUMMARY"), model);
		UserConfigurationStep configurationStep = new UserConfigurationStep(loginStep, new LabelStringDetachableModel("TITLE"), new LabelStringDetachableModel("SUMMARY"), model);
		captchaValidationStep.setNextStep(nameStep);
		nameStep.setNextStep(addressStep);
		addressStep.setNextStep(loginStep);
		loginStep.setNextStep(configurationStep);
		//next the model...
		DynamicWizardModel wizardModel = new DynamicWizardModel(captchaValidationStep);
		wizardModel.addListener(new IWizardModelListener(){
			private static final long serialVersionUID = 1L;
			public void onActiveStepChanged(IWizardStep step) {
				if(!step.isComplete())  {
					
				}
				log.info(step.getClass().getName());
			}
			public void onCancel() {
				setResponsePage(Index.class);
			}
			public void onFinish() {
				PageParameters parameters = new PageParameters();
				parameters.put("i", window.getModelObjectAsString());
				setResponsePage(ConfirmUserRegistrationPage.class, parameters);
			}
		});
		//finally set in the wizard...
		init(wizardModel);
	}
	
	
	/**
	 * 
	 */
	protected Component newButtonBar(String id) {
		return new AddNewUserWizardAjaxButtonBar(id, this, window);
	}

	/**
	 * 
	 */
	protected FeedbackPanel newFeedbackPanel(String id) {
		FeedbackPanel fp = super.newFeedbackPanel(id);
		fp.setVisible(false);
		return fp;
	}


	/**
	 * 
	 * AbstractDynamicWizardStep
	 *
	 */
	abstract class AbstractDynamicWizardStep extends DynamicWizardStep  {
		private static final long serialVersionUID = 1L;
		
		//the next step
		protected IDynamicWizardStep nextStep;
		//the alternative step
		protected IDynamicWizardStep alternateStep;

		/**
		 * @param previousStep
		 * @param title
		 * @param summary
		 * @param model
		 */
		public AbstractDynamicWizardStep(IDynamicWizardStep previousStep, IModel title, IModel summary, IModel model) {
			super(previousStep, title, summary, model);
		}

		public IDynamicWizardStep getNextStep() {
			return nextStep;
		}

		public void setNextStep(IDynamicWizardStep nextStep) {
			this.nextStep = nextStep;
		}

		public IDynamicWizardStep getAlternateStep() {
			return alternateStep;
		}

		public void setAlternateStep(IDynamicWizardStep alternateStep) {
			this.alternateStep = alternateStep;
		}
	}
	
	/**
	 *
	 * UserCaptchaValidationStep
	 *
	 */
	final class UserCaptchaValidationStep extends AbstractDynamicWizardStep  {
		private static final long serialVersionUID = 1L;

		//random password generated...
        private String imagePass = randomString(3, 5);
		
		//the captcha service...
	    private transient ImageCaptchaService captchaService = new DefaultManageableImageCaptchaService();

		/**
		 * @param previousStep
		 * @param title
		 * @param summary
		 * @param model
		 */
		public UserCaptchaValidationStep(IDynamicWizardStep previousStep, IModel title, IModel summary, IModel model) {
			super(previousStep, title, summary, model);
			init();
		}
		
		private void init() {
            //CAPTCHA
            add(new Label("captchaLabel", new LabelStringDetachableModel("ENTER_TEXT_FROM_IMAGE")));
            final NonCachingImage captchaImage = new NonCachingImage("captchaImage", getCaptchaImageResource());
            captchaImage.setOutputMarkupId(true);
            add(captchaImage);
            final AjaxFallbackLink reloadCaptchaImageLink = new AjaxFallbackLink("reloadCaptchaImageLink"){
				private static final long serialVersionUID = 1L;
				public void onClick(AjaxRequestTarget target) {
					imagePass = randomString(3, 5);
					captchaImage.setImageResource(getCaptchaImageResource());
					if(target != null)  {
						target.addComponent(captchaImage);
					}
				}
            };
            reloadCaptchaImageLink.add(new Label("reloadCaptchaImageLinkLabel", new LabelStringDetachableModel("RELOAD_CAPTCHA")));
            add(reloadCaptchaImageLink);
            final RequiredTextField captchaText = new RequiredTextField("captcha", new PropertyModel(getModelObject(), "captcha")) {
                private static final long serialVersionUID = 1L;
                protected final void onComponentTag(final ComponentTag tag) {
                    super.onComponentTag(tag);
                    // clear the field after each render
                    tag.put("value", "");
                }
            };
            captchaText.add(new AbstractValidator() {
				private static final long serialVersionUID = 1L;
				protected void onValidate(IValidatable validatable) {
            		if (!captchaService.validateResponseForID(imagePass, validatable.getValue())) {
            			error(validatable);
            		}
            	}
				protected String resourceKey() {
					return "captcha.validation.failed";
				}
            });
            add(captchaText);			
		}
		
		/**
		 * 
		 * @return
		 */
		private Resource getCaptchaImageResource()  {
			return new DynamicImageResource() {
				private static final long serialVersionUID = 1L;
				protected byte[] getImageData() {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					LocationModel location = WebUtil.getUserActualLocation((SignInSession)getSession());
					BufferedImage challenge = captchaService.getImageChallengeForID(imagePass, new Locale(location.getLanguageCode(), location.getCountryCode()));
					JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(baos);
					try {
						encoder.encode(challenge);
						return baos.toByteArray();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			};
		}
		
		//utility...
        private String randomString(int min, int max) {
            int num = randomInt(min, max);
            byte b[] = new byte[num];
            for (int i = 0; i < num; i++) {
                b[i] = (byte) randomInt('a', 'z');
            }
            return new String(b);
        }

        //utility...
        private int randomInt(int min, int max) {
            return (int) (Math.random() * (max - min) + min);
        }
		
		/* (non-Javadoc)
		 * @see org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep#isLastStep()
		 */
		public boolean isLastStep() {
			return false;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep#next()
		 */
		public IDynamicWizardStep next() {
			return nextStep;
		}

	}
	
	/**
	 * UserNameStep
	 *
	 */
	final class UserNameStep extends AbstractDynamicWizardStep {
		private static final long serialVersionUID = 1L;

		/**
		 * @param previousStep
		 * @param title
		 * @param summary
		 * @param model
		 */
		public UserNameStep(IDynamicWizardStep previousStep, IModel title, IModel summary, IModel model) {
			super(previousStep, title, summary, model);
			init();
		}
		
		private void init()  {
			//NAME
            add(new Label("nameLabel", new LabelStringDetachableModel("NAME")));
            final RequiredTextField name = new RequiredTextField("name", new PropertyModel(getModelObject(), "name"));
            //add(name);
            final FormComponentFeedbackBorder indicator = new FormComponentFeedbackBorder("indicator");
			indicator.add(name);
			indicator.setVisible(true);
			add(indicator);
            //FIRST NAME
            add(new Label("firstNameLabel", new LabelStringDetachableModel("FIRST_NAME")));
            final RequiredTextField firstName = new RequiredTextField("firstName", new PropertyModel(getModelObject(), "firstName"));
            add(firstName);
            //LAST NAME
            add(new Label("lastNameLabel", new LabelStringDetachableModel("LAST_NAME")));
            final RequiredTextField lastName = new RequiredTextField("lastName", new PropertyModel(getModelObject(), "lastName"));
            add(lastName);
            //GENDER
            add(new Label("genderLabel", new LabelStringDetachableModel("GENDER")));
            final DropDownChoice gender = new DropDownChoice("gender", new PropertyModel(getModelObject(), "gender"), new GendersListDetachableModel(), WebUtil.KEYVALUE_CHOICE_RENDERER){
            	private static final long serialVersionUID = 1L;
                protected CharSequence getDefaultChoice(final Object selected) {
                    return "<option value=\"\"></option>";
                }
            };
            gender.setRequired(true);
            add(gender);
            //BIRTH DATE
            add(new Label("birthdayLabel", new LabelStringDetachableModel("BIRTHDAY")));
            add(new Label("dayLabel", new LabelStringDetachableModel("DAY")));
            final DropDownChoice day = new DropDownChoice("day", new PropertyModel(getModelObject(), "day"), new DaysListDetachableModel(), WebUtil.KEYVALUE_CHOICE_RENDERER){
            	private static final long serialVersionUID = 1L;
                protected CharSequence getDefaultChoice(final Object selected) {
                    return "<option value=\"\"></option>";
                }
            };
            day.setRequired(true);
            add(day);
            add(new Label("monthLabel", new LabelStringDetachableModel("MONTH")));
            final DropDownChoice month = new DropDownChoice("month", new PropertyModel(getModelObject(), "month"), new MonthsListDetachableModel(), WebUtil.KEYVALUE_CHOICE_RENDERER){
            	private static final long serialVersionUID = 1L;
                protected CharSequence getDefaultChoice(final Object selected) {
                    return "<option value=\"\"></option>";
                }
            };
            month.setRequired(true);
            add(month);
            add(new Label("yearLabel", new LabelStringDetachableModel("YEAR")));
            final DropDownChoice year = new DropDownChoice("year", new PropertyModel(getModelObject(), "year"), new YearsListDetachableModel(), WebUtil.KEYVALUE_CHOICE_RENDERER){
            	private static final long serialVersionUID = 1L;
                protected CharSequence getDefaultChoice(final Object selected) {
                    return "<option value=\"\"></option>";
                }
            };
            year.setRequired(true);
            add(year);
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep#isLastStep()
		 */
		public boolean isLastStep() {
			return false;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep#next()
		 */
		public IDynamicWizardStep next() {
			return nextStep;
		}
	}
	
	/**
	 * UserAddressStep
	 *
	 */
	final class UserAddressStep extends AbstractDynamicWizardStep {
		private static final long serialVersionUID = 1L;
		
		//the fields needed to be updated...
		private DropDownChoice city;
		private RequiredTextField street;
		private RequiredTextField streetNumber;
		private RequiredTextField zone;
		private RequiredTextField postalCode;
		private RequiredTextField phoneNumber; 
		
		/**
		 * @param previousStep
		 * @param title
		 * @param summary
		 * @param model
		 */
		public UserAddressStep(IDynamicWizardStep previousStep, IModel title, IModel summary, IModel model) {
			super(previousStep, title, summary, model);
			init();
		}
		
		private void init()  {
			//location model variables
			final LocationModel location = WebUtil.getUserActualLocation((SignInSession)getSession());
			UserWizardModel model = (UserWizardModel)getModelObject(); 
            model.setCountry(new SelectOption(String.valueOf(location.getCountryId()), location.getCountryName())); //yes, it works... :)
            model.setCity(new SelectOption(String.valueOf(location.getCityId()), location.getCityName()));
            //DIALOG
            window.setInitialWidth(60);
            window.setInitialHeight(8);
            window.setWindowClosedCallback(new ModalWindow.WindowClosedCallback(){
				private static final long serialVersionUID = 1L;
				public void onClose(AjaxRequestTarget target) {
					updateLocationOnModelAndAjaxTarget((LocationModel)window.getModelObject(), target);
				}
			});
			//COUNTRY
            add(new Label("countryName", new LabelStringDetachableModel("COUNTRY")));
            final DropDownChoice country = new DropDownChoice("country", new PropertyModel(getModelObject(), "country"), new CountriesListDetachableModel(), WebUtil.KEYVALUE_CHOICE_RENDERER){
            	private static final long serialVersionUID = 1L;
                protected CharSequence getDefaultChoice(final Object selected) {
                    return "<option value=\"\"></option>";
                }
            };
            country.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                private static final long serialVersionUID = 1L;
                protected void onUpdate(AjaxRequestTarget target) {
                    if (target != null) {
                    	UserWizardModel model = (UserWizardModel)getModelObject();
                        city.setChoices(new CitiesSelectOptionListDetachableModel(model.getCountry().getKey()));
                        target.addComponent(city);
                    }
                }
            });
            country.setRequired(true);
            add(country);
            //CITY
            add(new Label("cityName", new LabelStringDetachableModel("CITY")));
            city = new DropDownChoice("city", new PropertyModel(getModelObject(), "city"), new CitiesSelectOptionListDetachableModel(model.getCountry().getKey())/*Collections.EMPTY_LIST*/, WebUtil.KEYVALUE_CHOICE_RENDERER) {
                private static final long serialVersionUID = 1L;
                protected CharSequence getDefaultChoice(final Object selected) {
                    return "<option value=\"\"></option>";
                }
            };
            city.setRequired(true);
            city.setOutputMarkupId(true);
            add(city);
            //STREET
            add(new Label("streetName", new LabelStringDetachableModel("STREET")));
            street = new RequiredTextField("street", new PropertyModel(getModelObject(), "street"));
            street.add(new AjaxFormComponentUpdatingBehavior("onblur"){
				private static final long serialVersionUID = 1L;
				protected void onUpdate(AjaxRequestTarget target) {
					UserWizardModel model = (UserWizardModel)UserAddressStep.this.getModelObject();
					final String streetName = model.getStreet();
					final String streetNumberValue = model.getStreetNumber();
					if(streetName != null && streetName.trim().length() > 0)  {
						final String cityName = model.getCity().getValue();
						final String countryName = model.getCountry().getValue();
						List<LocationModel> locations = ((Main)getApplication()).getServices().getGeoCoderService().getMultipleLocationsFor(streetNumberValue, streetName, cityName, countryName);
						if(locations != null && locations.size() > 0)  {
							if(locations.size() == 1)  {
								if(target != null)  {
									updateLocationOnModelAndAjaxTarget(locations.get(0), target);
								}
							}
							else  {
								if(target != null)  {
									window.setTitle(new LabelStringDetachableModel("MULTIPLE_LOCATIONS_FOUNDED"));
									window.setContent(new ShowLocationsDialogPanel(window.getContentId(), new CompoundPropertyModel(locations), window));
									window.show(target);
								}
							}
						}
					}
				}
            });
            street.setOutputMarkupId(true);
            add(street);
            //STREET NUMBER
            add(new Label("streetNumberName", new LabelStringDetachableModel("STREET_NUMBER")));
            streetNumber = new RequiredTextField("streetNumber", new PropertyModel(getModelObject(), "streetNumber"));
            streetNumber.add(new AjaxFormComponentUpdatingBehavior("onblur"){
				private static final long serialVersionUID = 1L;
				protected void onUpdate(AjaxRequestTarget target) {
					UserWizardModel model = (UserWizardModel)UserAddressStep.this.getModelObject();
					final String streetName = model.getStreet();
					final String streetNumberValue = model.getStreetNumber();
					if(streetName != null && streetName.trim().length() > 0)  {
						final String cityName = model.getCity().getValue();
						final String countryName = model.getCountry().getValue();
						List<LocationModel> locations = ((Main)getApplication()).getServices().getGeoCoderService().getMultipleLocationsFor(streetNumberValue, streetName, cityName, countryName);
						if(locations != null && locations.size() > 0)  {
							if(locations.size() == 1)  {
								if(target != null)  {
									updateLocationOnModelAndAjaxTarget(locations.get(0), target);
								}
							}
							else  {
								if(target != null)  {
									window.setTitle(new LabelStringDetachableModel("MULTIPLE_LOCATIONS_FOUNDED"));
									window.setContent(new ShowLocationsDialogPanel(window.getContentId(), new CompoundPropertyModel(locations), window));
									window.show(target);
								}
							}
						}
					}
				}
            });
            streetNumber.setOutputMarkupId(true);
            add(streetNumber);
            //ZONE
            add(new Label("zoneName", new LabelStringDetachableModel("ZONE")));
            zone = new RequiredTextField("zone", new PropertyModel(getModelObject(), "zone"));
            zone.setEnabled(false);
            zone.setOutputMarkupId(true);
            add(zone);
            //POSTAL CODE
            add(new Label("postalCodeName", new LabelStringDetachableModel("POSTAL_CODE")));
            postalCode = new RequiredTextField("postalCode", new PropertyModel(getModelObject(), "postalCode"));
            postalCode.setEnabled(false);
            postalCode.setOutputMarkupId(true);
            add(postalCode);
            //PHONE NUMBER
            add(new Label("phoneNumberName", new LabelStringDetachableModel("PHONE_NUMBER")));
            phoneNumber = new RequiredTextField("phoneNumber", new PropertyModel(getModelObject(), "phoneNumber"));
            phoneNumber.setOutputMarkupId(true);
            add(phoneNumber);
            //EMAIL
            add(new Label("emailAddressName", new LabelStringDetachableModel("EMAIL_ADDRESS")));
            final RequiredTextField email = new RequiredTextField("email", new PropertyModel(getModelObject(), "email"));
            email.add(new AjaxFormComponentUpdatingBehavior("onblur"){
				private static final long serialVersionUID = 1L;
				protected void onUpdate(AjaxRequestTarget target) {
					UserWizardModel model = (UserWizardModel)UserAddressStep.this.getModelObject();
					String login = model.getEmail();
					if(login != null && login.trim().length() > 0)  {
						if(((Main)getApplication()).getServices().getMainService().alreadyExistsUserWithThisLogin(login)) {
							model.setEmail("");
							target.addComponent(email);
							window.setTitle(new LabelStringDetachableModel("USER_WITH_SAME_EMAIL_FOUNDED"));
							window.setContent(new MessageOkDialogPanel(window.getContentId(), new Model("USE_ANOTHER_EMAIL"), window));
							window.show(target);
						}
					}
				}
            });
            email.add(EmailAddressValidator.getInstance());
            email.setOutputMarkupId(true);
            add(email);
		}
		
		/**
		 * 
		 * @param target
		 */
		private void updateLocationOnModelAndAjaxTarget(final LocationModel foundedLocation, AjaxRequestTarget target)  {
			UserWizardModel model = (UserWizardModel)UserAddressStep.this.getModelObject();
			model.setLatitude(foundedLocation.getLatitude());
			model.setLongitude(foundedLocation.getLongitude());
			model.setStreet(foundedLocation.getStreetName());
			target.addComponent(street);
			model.setZone(foundedLocation.getZoneName());
			target.addComponent(zone);
			model.setPostalCode(foundedLocation.getPostalCodeNumber());
			target.addComponent(postalCode);
			//focus
			//target.appendJavascript("document.getElementById('"+target.getLastFocusedElementId()+"').focus()");
			if(model.getStreetNumber() != null && model.getStreetNumber().trim().length() > 0)  {
				target.focusComponent(phoneNumber);
			}
			else  {
				target.focusComponent(streetNumber);
			}
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep#isLastStep()
		 */
		public boolean isLastStep() {
			return false;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep#next()
		 */
		public IDynamicWizardStep next() {
			return nextStep;
		}
	}
	
	/**
	 * 
	 * UserLoginStep
	 *
	 */
	final class UserLoginStep extends AbstractDynamicWizardStep  {
		private static final long serialVersionUID = 1L;
		
		/**
		 * @param previousStep
		 * @param title
		 * @param summary
		 * @param model
		 */
		public UserLoginStep(IDynamicWizardStep previousStep, IModel title, IModel summary, IModel model) {
			super(previousStep, title, summary, model);
			init();
		}
		
		private void init()  {
			//SCREEN_NAME
			add(new Label("screenNameLabel", new LabelStringDetachableModel("SCREEN_NAME")));
			final RequiredTextField screenName = new RequiredTextField("screenName", new PropertyModel(getModelObject(), "screenName"));
			screenName.add(new AjaxFormComponentUpdatingBehavior("onblur"){
				private static final long serialVersionUID = 1L;
				protected void onUpdate(AjaxRequestTarget target) {
					UserWizardModel model = (UserWizardModel)UserLoginStep.this.getModelObject();
					String login = model.getScreenName();
					if(login != null && login.trim().length() > 0)  {
						if(((Main)getApplication()).getServices().getMainService().alreadyExistsUserWithThisScreenName(login)) {
							model.setScreenName("");
							target.addComponent(screenName);
							window.setTitle(new LabelStringDetachableModel("USER_WITH_SAME_SCREEN_NAME_FOUNDED"));
							window.setContent(new MessageOkDialogPanel(window.getContentId(), new Model("USE_ANOTHER_SCREEN_NAME"), window));
							window.show(target);
						}
					}
				}
            });
			screenName.setOutputMarkupId(true);
			add(screenName);
			//LOGIN
            add(new Label("loginLabel", new LabelStringDetachableModel("LOGIN")));
            final RequiredTextField login = new RequiredTextField("login", new PropertyModel(getModelObject(), "email"));
            login.setEnabled(false);
            add(login);
            //PASSWORD
            add(new Label("passwordLabel", new LabelStringDetachableModel("PASSWORD")));
            final PasswordTextField password = new PasswordTextField("password", new PropertyModel(getModelObject(), "password"));
            password.setResetPassword(false);
            password.setRequired(true);
            add(password);
            //CONFIRM PASSWORD
            add(new Label("passwordConfirmationLabel", new LabelStringDetachableModel("CONFIRM_PASSWORD")));
            final PasswordTextField passwordConfirmation = new PasswordTextField("passwordConfirmation", new PropertyModel(getModelObject(), "passwordConfirmation"));
            passwordConfirmation.setResetPassword(false);
            passwordConfirmation.setRequired(true);
            add(passwordConfirmation);
            //VALIDATION
            add(new EqualPasswordInputValidator(password, passwordConfirmation));
		}
		
		/* (non-Javadoc)
		 * @see org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep#isLastStep()
		 */
		public boolean isLastStep() {
			return false;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep#next()
		 */
		public IDynamicWizardStep next() {
			return nextStep;
		}
		
	}
	
	/**
	 * UserConfigurationStep
	 *
	 */
	final class UserConfigurationStep extends AbstractDynamicWizardStep {
		private static final long serialVersionUID = 1L;
		//upload local folder...
        private Folder uploadFolder;
        
		/**
		 * @param previousStep
		 * @param title
		 * @param summary
		 * @param model
		 */
		public UserConfigurationStep(IDynamicWizardStep previousStep, IModel title, IModel summary, IModel model) {
			super(previousStep, title, summary, model);
			uploadFolder = new Folder(System.getProperty("java.io.tmpdir"), "uploads");
            uploadFolder.mkdirs();
			init();
		}
		
		//overwrite to set the form parameters... 
        protected void onInit(IWizardModel wizardModel) {
            // form settings
            Form form = AddNewUserWizard.this.getForm();
            form.setMultiPart(true);
            form.setMaxSize(Bytes.megabytes(3));
            super.onInit(wizardModel);
        }
		
		private void init()  {
            //AVATAR IMAGE
            add(new Label("avatarPictureLabel", new LabelStringDetachableModel("YOUR_AVATAR_IMAGE")));
            //THUMBNAIL IMAGE
            final UploadedThumbnailPicturePanel uploadedPicturePanel = new UploadedThumbnailPicturePanel("uploadedPicturePanel");
            add(uploadedPicturePanel);
            //FILE UPLOAD FIELD
            add(new Label("imagesLabel", new LabelStringDetachableModel("SELECT_IMAGE")));
            final FileUploadField pictureUpload = new FileUploadField("pictureUpload");
            pictureUpload.setRequired(true);
            add(pictureUpload);
            //UPLOAD BUTTON 
            final Button pictureUploadButton = new Button("pictureUploadButton", new LabelStringDetachableModel("UPLOAD")) {
                private static final long serialVersionUID = 1L;
                public void onSubmit() {
                    final FileUpload pictureToUpload = pictureUpload.getFileUpload();
                    if (pictureToUpload != null) {
                        log.info("Uploaded file '" + pictureToUpload.getClientFileName() + "', of type '" + pictureToUpload.getContentType() + "' and length '" + pictureToUpload.getSize() + "'");
                        if(Util.isPictureFileFormatSupported(pictureToUpload.getContentType())) {
                            File file = new File(uploadFolder.getAbsoluteFile(), pictureToUpload.getClientFileName());
                            UserWizardModel model = (UserWizardModel)UserConfigurationStep.this.getModelObject(); 
                            model.setFileNameOfAvatarPictureToUpload(uploadFolder.getAbsoluteFile() + File.separator +pictureToUpload.getClientFileName());
                            log.info(model.getFileNameOfAvatarPictureToUpload());
                            try {
                            	if(file.exists()) {
                            		file.delete();
                            	}
                                if (file.createNewFile()) {
                                    pictureToUpload.writeTo(file);
                                    pictureUpload.detachModels();
                                    uploadedPicturePanel.updatePicture(file.getAbsolutePath());
                                }
                            } catch (IOException e) {
                                log.error("Error when creating file - " + file, e);
                            }
                        }
                    } 
                }
            }.setDefaultFormProcessing(false);
            add(pictureUploadButton);
            //LANGUAGE
            final LocationModel location = WebUtil.getUserActualLocation((SignInSession)getSession());
            UserWizardModel model = (UserWizardModel)getModelObject();
            model.setLanguage(new SelectOption(String.valueOf(location.getLanguageId()), location.getLanguageName()));
            add(new Label("languageLabel", new LabelStringDetachableModel("PREFERED_LANGUAGE")));
            final DropDownChoice language = new DropDownChoice("language", new PropertyModel(getModelObject(), "language"), new LanguagesSelectOptionListDetachableModel(), WebUtil.KEYVALUE_CHOICE_RENDERER) {
                private static final long serialVersionUID = 1L;
                protected CharSequence getDefaultChoice(final Object selected) {
                    return "<option value=\"\"></option>";
                }
            };
            language.setRequired(true);
            add(language);
		}
		
		/* (non-Javadoc)
		 * @see org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep#isLastStep()
		 */
		public boolean isLastStep() {
			return true;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep#next()
		 */
		public IDynamicWizardStep next() {
			return null;
		}
	}

}//END OF FILE