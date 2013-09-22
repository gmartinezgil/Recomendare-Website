package mx.com.recomendare.web.places;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.db.entities.Street;
import mx.com.recomendare.services.db.DatabaseRequestCycle;
import mx.com.recomendare.services.db.ItemLocationsDAO;
import mx.com.recomendare.services.db.StreetsDAO;
import mx.com.recomendare.web.Index;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.ImageModelImpl;
import mx.com.recomendare.web.commons.models.detachables.CategoriesSelectOptionListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.CitiesSelectOptionListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.CountriesListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.SubcategoriesSelectOptionListDetachableModel;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteBehavior;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.IAutoCompleteRenderer;
import org.apache.wicket.extensions.markup.html.image.resource.ThumbnailImageResource;
import org.apache.wicket.extensions.wizard.IWizardModel;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.extensions.wizard.WizardModel;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.hibernate.Session;

import com.aetrion.flickr.places.Place;

/**
 * 
 * @author jerry
 */
public final class NewPlaceWizard extends Wizard {
    private static final long serialVersionUID = 1L;
    
    //the place model to be added...
    private WizardPlaceModel placeModel;
    
    //the log...
    private static final Log log = LogFactory.getLog(NewPlaceWizard.class);

    public NewPlaceWizard(String id) {
        super(id, false);
        
        placeModel = new WizardPlaceModel();

        setModel(new CompoundPropertyModel(placeModel));
        WizardModel model = new WizardModel();
        model.add(new PlaceNameStep(placeModel));
        model.add(new PlaceContactStep(placeModel));
        //model.add(new PlaceLocation(placeModel));
        model.add(new PlaceImagesStep(placeModel));
        model.setLastVisible(false);
        init(model);
        //add(new AttributeModifier("class", ));
    }

    /**
     * 
     */
    public void onCancel() {
        log.info(placeModel);
        setResponsePage(Index.class);
    }

    /**
     * 
     */
    public void onFinish() {
        log.info(placeModel);
        Session dbSession = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
        ItemLocationsDAO places = ((Main) getApplication()).getServices().getDatabaseService().getPlacesDAO();
        places.setSession(dbSession);
        Place place = (Place) places.createPlace(((SignInSession)getSession()).getUser(),placeModel); //save the place... ;)
        PageParameters parameters = new PageParameters();
        parameters.put("i", place.getId());
        parameters.put("n", place.getItem().getName());
        setResponsePage(ViewPlacePage.class, parameters);
    }

    /*
     * The steps of the wizard...
     */
    /**
     * PlaceNameStep
     */
    final class PlaceNameStep extends WizardStep {       
        private static final long serialVersionUID = 1L;
        
        //the sub categories drop down choice...
        private DropDownChoice subCategories;

        public PlaceNameStep(WizardPlaceModel placeModel) {
            IModel newPlaceModel = new Model(placeModel);
            setModel(newPlaceModel);
            init();
        }

        private void init() {
            //CATEGORY
            add(new Label("categoryName", new LabelStringDetachableModel("CATEGORY")));
            final DropDownChoice categories = new DropDownChoice("categories", new PropertyModel(getModelObject(), "categories"), new CategoriesSelectOptionListDetachableModel(), WebUtil.KEYVALUE_CHOICE_RENDERER) {
                private static final long serialVersionUID = 1L;
                protected CharSequence getDefaultChoice(final Object selected) {
                    return "<option value=\"\"></option>";
                }
            };
            categories.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                private static final long serialVersionUID = 1L;
                protected void onUpdate(AjaxRequestTarget target) {
                    if (target != null) {
                        WizardPlaceModel model = (WizardPlaceModel) getModelObject();
                        log.info("model ajax selected - " + model.getCategories());
                        subCategories.setChoices(new SubcategoriesSelectOptionListDetachableModel(Integer.parseInt(model.getCategories().getKey())));
                        target.addComponent(subCategories);
                    }
                }
            });
            categories.setRequired(true);
            add(categories);
            //SUBCATEGORY
            add(new Label("subCategoryName", new LabelStringDetachableModel("SUBCATEGORY")));
            subCategories = new DropDownChoice("subCategories", new PropertyModel(getModelObject(), "subCategories"), Collections.EMPTY_LIST, WebUtil.KEYVALUE_CHOICE_RENDERER) {
                private static final long serialVersionUID = 1L;
                protected CharSequence getDefaultChoice(final Object selected) {
                    return "<option value=\"\"></option>";
                }
            };
            subCategories.setOutputMarkupId(true);
            subCategories.setRequired(true);
            add(subCategories);
            //NAME
            add(new Label("placeName", new LabelStringDetachableModel("PLACE_NAME")));
            final RequiredTextField name = new RequiredTextField("name", new PropertyModel(getModelObject(), "name"));
            name.add(StringValidator.maximumLength(100));
            add(name);
            //DESCRIPTION
            add(new Label("descriptionName", new LabelStringDetachableModel("DESCRIPTION")));
            final TextArea description = new TextArea("description", new PropertyModel(getModelObject(), "description"));
            description.add(StringValidator.maximumLength(200));
            description.setRequired(true);
            add(description);
        }
    }

    /**
     * PlaceContactStep
     */
    final class PlaceContactStep extends WizardStep {
        private static final long serialVersionUID = 1L;

        //city field...
        private DropDownChoice city;	//i.e. San Francisco/Ciudad de Mexico

        public PlaceContactStep(WizardPlaceModel placeModel) {
            IModel newPlaceModel = new Model(placeModel);
            setModel(newPlaceModel);
            init();
        }

        private void init() {
            //TITLE
            //setTitleModel(new Model("Localidad"));
            //get from the user session...
            //((WizardPlaceModel)getModelObject()).setCountry(new SelectOption("1", "Ciudad de Mexico")); //yes, it works... :)
            //COUNTRY
            add(new Label("countryName", new LabelStringDetachableModel("COUNTRY")));
            final DropDownChoice country = new DropDownChoice("country", new PropertyModel(getModelObject(), "country"), new CountriesListDetachableModel(), WebUtil.KEYVALUE_CHOICE_RENDERER) {
                private static final long serialVersionUID = 1L;
                protected CharSequence getDefaultChoice(final Object selected) {
                    return "<option value=\"\"></option>";
                }
            };
            country.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                private static final long serialVersionUID = 1L;
                protected void onUpdate(AjaxRequestTarget target) {
                    if (target != null) {
                        WizardPlaceModel model = (WizardPlaceModel) getModelObject();
                        log.info("model ajax selected - " + model.getCountry());
                        city.setChoices(new CitiesSelectOptionListDetachableModel(model.getCountry().getKey()));
                        target.addComponent(city);
                    }
                }
            });
            //country.setNullValid(false);
            country.setRequired(true);
            add(country);
            //CITY
            add(new Label("cityName", new LabelStringDetachableModel("CITY")));
            city = new DropDownChoice("city", new PropertyModel(getModelObject(), "city"), Collections.EMPTY_LIST, WebUtil.KEYVALUE_CHOICE_RENDERER) {
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
            final RequiredTextField street = new RequiredTextField("street", new PropertyModel(getModelObject(), "street"));
            final IAutoCompleteRenderer informativeRenderer = new AbstractAutoCompleteRenderer() {
                private static final long serialVersionUID = 1L;
                protected String getTextValue(Object object) {
                    return ((Street) object).getName();
                }
                protected void renderChoice(Object object, Response response, String arg2) {
                    response.write("<div style='float:left; color:orange;'>");
                    response.write(((Street) object).getName());
                    response.write("</div>");
                }
            };
            street.add(new AutoCompleteBehavior(informativeRenderer) {
                private static final long serialVersionUID = 1L;
                protected Iterator getChoices(String input) {
                    List choicesToBeRendered = new ArrayList();
                    //TODO:should be only initialized once...if not there are many db sessions...
                    Session dbSession = ((DatabaseRequestCycle) RequestCycle.get()).getDatabaseSession();
                    StreetsDAO streetsDAO = ((Main) getApplication()).getServices().getDatabaseService().getStreetsDAO();
                    streetsDAO.setSession(dbSession);
                    List streets = streetsDAO.getStreetsByCityId(WebUtil.getUserActualLocation(((SignInSession)getSession())).getCityId());
                    if (streets != null) {
                        final Iterator iterator = streets.iterator();
                        while (iterator.hasNext()) {
                            Street street = (Street) iterator.next();
                            if (street.getName().startsWith(input.toLowerCase().trim())) {
                                choicesToBeRendered.add(street);
                            }
                        }
                    }
                    return choicesToBeRendered.iterator();
                }
            });
            street.add(StringValidator.maximumLength(30));
            add(street);
            //STREET NUMBER
            add(new Label("streetNumberName", "#"));
            final RequiredTextField streetNumber = new RequiredTextField("streetNumber", new PropertyModel(getModelObject(), "streetNumber"));
            add(streetNumber);
            //PHONE NUMBER
            add(new Label("phoneNumberName", new LabelStringDetachableModel("PHONE_NUMBER")));
            final RequiredTextField phoneNumber = new RequiredTextField("phoneNumber", new PropertyModel(getModelObject(), "phoneNumber"));
            //phoneNumber.add(NumberValidator.POSITIVE);
            add(phoneNumber);
            //EMAIL
            add(new Label("emailAddressName", new LabelStringDetachableModel("EMAIL_ADDRESS")));
            final TextField email = new TextField("email", new PropertyModel(getModelObject(), "email"));
            email.add(EmailAddressValidator.getInstance());
            add(email);
            //URL
            add(new Label("urlName", new LabelStringDetachableModel("WEBSITE_ADDRESS")));
            final RequiredTextField url = new RequiredTextField("url", new PropertyModel(getModelObject(), "url"));
            add(url);
        }
        /*
        //overwrite to apply the form before submit...
        public void applyState() {
        if(NewPlaceWizard.this.gMap.getOverlays().size() == 0)  {
        log.info("No location on map selected...");
        }
        super.applyState();
        }
         */
    }

    /**
     * PlaceImageStep
     */
    final class PlaceImagesStep extends WizardStep {

        private static final long serialVersionUID = 1L;

        /**
         * @param placeModel
         * @param summary
         */
        public PlaceImagesStep(WizardPlaceModel placeModel) {
            IModel newPlaceModel = new Model(placeModel);
            setModel(newPlaceModel);
            init();
        }

        //overwrite to set the form parameters... 
        protected void onInit(IWizardModel wizardModel) {
            // form settings
            Form form = NewPlaceWizard.this.getForm();
            form.setMultiPart(true);
            form.setMaxSize(Bytes.megabytes(1));
            super.onInit(wizardModel);
        }

        private void init() {
            //THUMBNAIL IMAGE
            final Image uploadedThumbnailImage = new Image("uploadedThumbnailImage", WebUtil.QUESTIONMARK_IMAGE);
            add(uploadedThumbnailImage);
            //the panel to upload the images...
            add(new Label("imagesLabel", new LabelStringDetachableModel("SELECT_IMAGE")));
            //FILE UPLOAD FIELD
            final FileUploadField pictureUpload = new FileUploadField("pictureUpload");
            add(pictureUpload);
            //UPLOAD BUTTON 
            final Button pictureUploadButton = new Button("pictureUploadButton", new LabelStringDetachableModel("UPLOAD")) {
                private static final long serialVersionUID = 1L;
                public void onSubmit() {
                    final FileUpload pictureToUpload = pictureUpload.getFileUpload();
                    if (pictureToUpload != null) {
                        log.info("Uploaded file '" + pictureToUpload.getClientFileName() + "', of type '" + pictureToUpload.getContentType() + "' and length '" + pictureToUpload.getSize() + "'");
                        final DynamicImageResource imageResource = new DynamicImageResource() {
                            private static final long serialVersionUID = 1L;
                            protected byte[] getImageData() {
                                return pictureToUpload.getBytes();
                            }
                        };
                        //TODO:this corrupts the stream...use the another method to save in a folder...
                        ThumbnailImageResource thumbnailImageResource = new ThumbnailImageResource(imageResource, 100);
                        thumbnailImageResource.setCacheable(false);
                        uploadedThumbnailImage.setImageResource(thumbnailImageResource);
                        //add to the model...
                        WizardPlaceModel model = NewPlaceWizard.this.placeModel;//TODO: it should be called with applyState of this WizardStep...instead of this hack...
                        ImageModelImpl picture = new ImageModelImpl(pictureToUpload.getClientFileName(), pictureToUpload.getContentType(), pictureToUpload.getBytes());
                        model.addPicture(picture);
                        log.info("Updated model - " + model);
                    }
                }
            }.setDefaultFormProcessing(false);
            add(pictureUploadButton);
        }
    }
    
}//END OF FILE