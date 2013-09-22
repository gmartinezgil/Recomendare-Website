/**
 * 
 */
package mx.com.recomendare.web.items;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.util.Util;
import mx.com.recomendare.web.Index;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.components.ShowLocationsDialogPanel;
import mx.com.recomendare.web.commons.components.UploadedThumbnailPicturePanel;
import mx.com.recomendare.web.commons.models.AddressModelImpl;
import mx.com.recomendare.web.commons.models.CategoryModelImpl;
import mx.com.recomendare.web.commons.models.ItemModelImpl;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.commons.models.LocationModelImpl;
import mx.com.recomendare.web.commons.models.PictureModel;
import mx.com.recomendare.web.commons.models.PictureModelImpl;
import mx.com.recomendare.web.commons.models.PriceModelImpl;
import mx.com.recomendare.web.commons.models.SelectOption;
import mx.com.recomendare.web.commons.models.detachables.CategoriesSelectOptionListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.CitiesSelectOptionListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.CountriesListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.CurrenciesListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.SubcategoriesSelectOptionListDetachableModel;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.rating.RatingPanel;
import org.apache.wicket.extensions.wizard.IWizardModel;
import org.apache.wicket.extensions.wizard.IWizardModelListener;
import org.apache.wicket.extensions.wizard.IWizardStep;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardModel;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import wicket.contrib.gmap.GMap2;
import wicket.contrib.gmap.api.GControl;
import wicket.contrib.gmap.api.GIcon;
import wicket.contrib.gmap.api.GLatLng;
import wicket.contrib.gmap.api.GMapType;
import wicket.contrib.gmap.api.GMarker;
import wicket.contrib.gmap.api.GMarkerOptions;
import wicket.contrib.gmap.api.GOverlay;
import wicket.contrib.gmap.api.GPoint;
import wicket.contrib.gmap.api.GSize;
import wicket.contrib.tinymce.TinyMceBehavior;
import wicket.contrib.tinymce.settings.TinyMCESettings;

/**
 * @author Jerry
 *
 */
public final class AddNewItemWizard extends Wizard {
	private static final long serialVersionUID = 1L;

	//the log...
	private static final Log log = LogFactory.getLog(AddNewItemWizard.class);

	/**
	 * @param id
	 */
	public AddNewItemWizard(String id) {
		super(id, false);
		//add(HeaderContributor.forCss("structure.css"));
		//add(HeaderContributor.forCss("form.css"));
		//add(HeaderContributor.forCss("theme.css"));
		//set the model to be used in the steps...
		IModel model = new CompoundPropertyModel(new ItemWizardModel());
		setModel(model);
		//define the steps...
		ItemNameStep nameStep = new ItemNameStep(null, new LabelStringDetachableModel("SET_CONTACT"), new LabelStringDetachableModel("CONTACT_INFORMATION"), model);
		ItemFacilitiesStep facilitiesStep = new ItemFacilitiesStep (nameStep, new LabelStringDetachableModel("SET_FACILITIES"), new LabelStringDetachableModel("FACILITIES_INFORMATION"), model);
		ItemPicturesStep picturesStep = new ItemPicturesStep(facilitiesStep, new LabelStringDetachableModel("SET_PICTURES"), new LabelStringDetachableModel("PICTURES_INFORMATION"), model);
		ItemLocationStep locationStep = new ItemLocationStep(picturesStep, new LabelStringDetachableModel("SET_LOCATION"), new LabelStringDetachableModel("LOCATION_INFORMATION"), model);
		nameStep.setNextStep(facilitiesStep);
		facilitiesStep.setNextStep(picturesStep);
		picturesStep.setAlternateStep(locationStep);
		//next the model...
		DynamicWizardModel wizardModel = new DynamicWizardModel(nameStep);
		wizardModel.addListener(new IWizardModelListener(){
			private static final long serialVersionUID = 1L;
			public void onActiveStepChanged(IWizardStep step) {
				log.info(step.getClass().getName());
			}
			public void onCancel() {
				setResponsePage(Index.class);
			}
			public void onFinish() {
				//finished submit model...
				ItemWizardModel model = (ItemWizardModel)getModelObject();
				log.info(model);
				//item to save...
				ItemModelImpl item = new ItemModelImpl();
				//general...
				item.setName(model.getName());
				item.setDescription(model.getDescription());
				//category...
				CategoryModelImpl category = new CategoryModelImpl();
				category.setCategoryId(Integer.parseInt(model.getCategory().getKey()));
				category.setSubcategoryId(Integer.parseInt(model.getSubCategory().getKey()));
				item.setCategory(category);
				//price
				PriceModelImpl price = new PriceModelImpl();
				price.setMinPrice(model.getMinimumPrice());
				price.setMaxPrice(model.getMaximumPrice());
				price.setOriginalCurrencyId(Integer.parseInt(model.getCurrency().getKey()));
				item.setPrice(price);
				//picture
				final String uploadedPictureName = System.getProperty("java.io.tmpdir")+File.separator+"uploads"+File.separator+((SignInSession)getSession()).getUser().getScreenName()+".img";
				log.info(uploadedPictureName);
				final File pictureFile = new File(uploadedPictureName);
				if(pictureFile.exists())  {
					long length = pictureFile.length();
					if(length < Integer.MAX_VALUE) {
						try {
							PictureModelImpl picture = new PictureModelImpl();
							picture.setContent(Util.getContentAsBytesFromFile(pictureFile));
					        List<PictureModel> pictures = new ArrayList<PictureModel>();
							pictures.add(picture);
							item.setPictures(pictures);
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
				//location
				if(model.getCountry() != null && model.getCity() != null)  {
					LocationModelImpl location = new LocationModelImpl();
					location.setCountryId(Integer.parseInt(model.getCountry().getKey()));
					location.setCityId(Integer.parseInt(model.getCity().getKey()));
					location.setLatitude(model.getLatitude());
					location.setLongitude(model.getLongitude());
					item.setLocation(location);
					AddressModelImpl address = new AddressModelImpl();
					address.setStreetName(model.getStreet());
					address.setStreetOutsideNumber(model.getStreetNumber());
					address.setTelephoneNumber(model.getPhoneNumber());
					address.setHomePageURL(model.getURL());
					item.setAddress(address);
				}
				int newItemModelId = ((Main)getApplication()).getServices().getMainService().addItemModel(item, ((SignInSession)getSession()).getUser().getScreenName());
				//TODO:add a dialog confirming the creation of the item and then redirect...
				PageParameters parameters = new PageParameters();
				parameters.put("i", newItemModelId);
				parameters.put("n", WebUtil.encodeURL(item.getName()));
				setResponsePage(ViewItemPage.class, parameters);
			}
		});
		//finally set in the wizard...
		init(wizardModel);
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
	 * ItemNameStep
	 *
	 */
	final class ItemNameStep extends AbstractDynamicWizardStep {
		private static final long serialVersionUID = 1L;

		//the form...
		//private Form form;

		/**
		 * @param previousStep
		 * @param title
		 * @param summary
		 * @param model
		 */
		public ItemNameStep(IDynamicWizardStep previousStep, IModel title, IModel summary, IModel model) {
			super(previousStep, title, summary, model);
			init();
		}

		/*
		//overwrite to set the form parameters... 
		protected void onInit(IWizardModel wizardModel) {
			form = AddNewItemWizard.this.getForm();
			super.onInit(wizardModel);
		}
		*/

		/**
		 * 
		 */
		private void init() {
			//SUBCATEGORY
			add(new Label("itemSubCategoryLabel", new LabelStringDetachableModel("SUBCATEGORY")));
			final DropDownChoice subCategory = new DropDownChoice("subCategory", new PropertyModel(getModelObject(), "subCategory"), Collections.EMPTY_LIST, WebUtil.KEYVALUE_CHOICE_RENDERER) {
				private static final long serialVersionUID = 1L;
				protected CharSequence getDefaultChoice(final Object selected) {
					return "<option value=\"\"></option>";
				}
			};
			subCategory.setOutputMarkupId(true);
			subCategory.setRequired(true);
			add(subCategory);
			//CATEGORY
			add(new Label("itemCategoryLabel", new LabelStringDetachableModel("CATEGORY")));
			final DropDownChoice category = new DropDownChoice("category", new PropertyModel(getModelObject(), "category"), new CategoriesSelectOptionListDetachableModel(), WebUtil.KEYVALUE_CHOICE_RENDERER) {
				private static final long serialVersionUID = 1L;
				protected CharSequence getDefaultChoice(final Object selected) {
					return "<option value=\"\"></option>";
				}
			};
			category.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;
				protected void onUpdate(AjaxRequestTarget target) {
					if(target != null)  {
						ItemWizardModel model = (ItemWizardModel)getModelObject();
						log.info("model ajax selected - "+model.getCategory());
						subCategory.setChoices(new SubcategoriesSelectOptionListDetachableModel(Integer.parseInt(model.getCategory().getKey())));
						target.addComponent(subCategory);
					}
				}
			});
			category.setRequired(true);
			add(category);
			//NAME
			add(new Label("itemNameLabel", new LabelStringDetachableModel("TITLE")));
			final RequiredTextField name = new RequiredTextField("name", new PropertyModel(getModelObject(), "name"));
			name.add(StringValidator.maximumLength(100));
			add(name);
			/*
			//PICTURE
			final NonCachingImage pictureItemFounded = new NonCachingImage("pictureItemFounded", WebUtil.QUESTIONMARK_IMAGE);
			pictureItemFounded.setOutputMarkupId(true);
			add(pictureItemFounded);
			//SEARCH BUTTON
			final AjaxFallbackButton searchItemButton = new AjaxFallbackButton("searchItemButton", form) {
				private static final long serialVersionUID = 1L;
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					String textToSearch = name.getModelObjectAsString();//model.getName();
					log.info(textToSearch);
					if(textToSearch != null && textToSearch.trim().length() > 0)  {
						AmazonAffiliateService affiliateService = ((Main)getApplication()).getServices().getAmazonAffiliateService(); 
						List<AmazonItemModel> results = affiliateService.searchItemsLikeDescription(textToSearch, AmazonAffiliateService.BOOKS_CATEGORY);//TODO:have to ask for the selected category...
						if(target != null)  {
							if(results.size() > 0)  {
								final AmazonItemModel item = results.get(0);
								if(item.getImageURL() != null && item.getImageURL().trim().length() > 0)  {
									pictureItemFounded.setImageResource(new DynamicImageResource(){
										private static final long serialVersionUID = 1L;
										protected byte[] getImageData() {
											return ((Main)getApplication()).getServices().getHttpService().getContenAsBytesFrom(item.getImageURL());
										}
									});
									target.addComponent(pictureItemFounded);
								}
							}
						}
					}
				}
			};
			searchItemButton.setDefaultFormProcessing(false);
			searchItemButton.setOutputMarkupId(true);
			searchItemButton.setModel(new LabelStringDetachableModel("SEARCH"));
			add(searchItemButton);
			*/
			//RATING
            add(new Label("ratingLabel", new LabelStringDetachableModel("RATING")));
            final RatingPanel ratingPanel = new RatingPanel("rating", new PropertyModel(getModelObject(), "rating"), ItemWizardModel.MAX_RATING, true) {
                private static final long serialVersionUID = 1L;
                protected boolean onIsStarActive(int star) {
                    return (star < ((Integer) getModelObject()).intValue()) ? true : false;
                }
                protected void onRated(int rating, AjaxRequestTarget target) {
                    if (target != null) {
                    	((ItemWizardModel)ItemNameStep.this.getModelObject()).setRating(rating);
                    }
                }
            };
            add(ratingPanel);
            //COMMENTS
            add(new Label("itemDescriptionLabel", new LabelStringDetachableModel("DESCRIPTION")));
            final TextArea description = new TextArea("description", new PropertyModel(getModelObject(), "description"));            
            TinyMCESettings settings = new TinyMCESettings(TinyMCESettings.Theme.advanced, TinyMCESettings.Language.es);//TODO:switch the language...
            /*
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
            description.add(new TinyMceBehavior(settings));
            description.add(StringValidator.maximumLength(200));
			description.setRequired(true);
            add(description);
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
	 * ItemDetailsStep
	 *
	 */
	final class ItemFacilitiesStep extends AbstractDynamicWizardStep  {
		private static final long serialVersionUID = 1L;

		/**
		 * @param previousStep
		 * @param title
		 * @param summary
		 * @param model
		 */
		public ItemFacilitiesStep(IDynamicWizardStep previousStep, IModel title, IModel summary, IModel model) {
			super(previousStep, title, summary, model);
			init();
		}
		
		private void init()  {
			//MIN PRICE
			add(new Label("itemMinPriceLabel", new LabelStringDetachableModel("MIN_PRICE")));
			final TextField minPrice = new TextField("minPrice", new PropertyModel(getModelObject(), "minimumPrice"), Double.class);
			minPrice.setRequired(true);
			add(minPrice);
			//MAX PRICE
			add(new Label("itemMaxPriceLabel", new LabelStringDetachableModel("MAX_PRICE")));
			final TextField maxPrice = new TextField("maxPrice", new PropertyModel(getModelObject(), "maximumPrice"), Double.class);
			maxPrice.setRequired(true);
			add(maxPrice);
			//CURRENCY
			final LocationModel location = WebUtil.getUserActualLocation((SignInSession)getSession());
			ItemWizardModel model = (ItemWizardModel)getModelObject(); 
            model.setCurrency(new SelectOption(String.valueOf(location.getCurrencyId()), location.getCurrencyName())); //yes, it works... :)
			add(new Label("itemCurrencyLabel", new LabelStringDetachableModel("CURRENCY")));
			final DropDownChoice currency = new DropDownChoice("currency", new PropertyModel(getModelObject(), "currency"), new CurrenciesListDetachableModel(), WebUtil.KEYVALUE_CHOICE_RENDERER) {
				private static final long serialVersionUID = 1L;
				protected CharSequence getDefaultChoice(final Object selected) {
					return "<option value=\"\"></option>";
				}
			};
			currency.setRequired(true);
			add(currency);
		}

		public boolean isLastStep() {
			return false;
		}

		public IDynamicWizardStep next() {
			return nextStep;
		}
	}
	
	/**
	 * ItemPictureStep
	 *
	 */
	final class ItemPicturesStep extends AbstractDynamicWizardStep {
		private static final long serialVersionUID = 1L;
		
		//upload local folder...
        private Folder uploadFolder;

		/**
		 * @param previousStep
		 * @param title
		 * @param summary
		 * @param model
		 */
		public ItemPicturesStep(IDynamicWizardStep previousStep, IModel title, IModel summary, IModel model) {
			super(previousStep, title, summary, model);
			uploadFolder = new Folder(System.getProperty("java.io.tmpdir"), "uploads");
            uploadFolder.mkdirs();
            log.info(uploadFolder.getAbsolutePath());
			init();
		}
		
		//overwrite to set the form parameters... 
        protected void onInit(IWizardModel wizardModel) {
            // form settings
            Form form = AddNewItemWizard.this.getForm();
            form.setMultiPart(true);
            form.setMaxSize(Bytes.megabytes(3));
            super.onInit(wizardModel);
        }

        private void init() {
            //THUMBNAIL IMAGE
            final UploadedThumbnailPicturePanel uploadedPicturePanel = new UploadedThumbnailPicturePanel("uploadedPicturePanel");
            add(uploadedPicturePanel);
            //FILE UPLOAD FIELD
            add(new Label("imagesLabel", new LabelStringDetachableModel("SELECT_IMAGE")));
            final FileUploadField pictureUpload = new FileUploadField("pictureUpload");
            add(pictureUpload);
            //UPLOAD BUTTON 
            final Button pictureUploadButton = new Button("pictureUploadButton", new LabelStringDetachableModel("UPLOAD")) {
                private static final long serialVersionUID = 1L;
                public void onSubmit() {
                    final FileUpload pictureToUpload = pictureUpload.getFileUpload();
                    if (pictureToUpload != null) {
                        log.info("Uploaded file '" + pictureToUpload.getClientFileName() + "', of type '" + pictureToUpload.getContentType() + "' and length '" + pictureToUpload.getSize() + "'");
                        if(Util.isPictureFileFormatSupported(pictureToUpload.getContentType())) {
                            File file = new File(uploadFolder.getAbsoluteFile(), ((SignInSession)getSession()).getUser().getScreenName()+".img");
                            log.info(file.getAbsolutePath());
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
        }

		/* (non-Javadoc)
		 * @see org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep#isLastStep()
		 */
		public boolean isLastStep() {
			int categoryId = Integer.parseInt(((ItemWizardModel)getModelObject()).getCategory().getKey());
			return (categoryId == 0 || categoryId == 1)?false:true;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep#next()
		 */
		public IDynamicWizardStep next() {
			int categoryId = Integer.parseInt(((ItemWizardModel)getModelObject()).getCategory().getKey());
			return (categoryId == 0 || categoryId == 1)?alternateStep:nextStep;
		}
	}

	/**
	 * ItemLocationStep
	 *
	 */
	final class ItemLocationStep extends AbstractDynamicWizardStep {
		private static final long serialVersionUID = 1L;
		
		//the map...
		private GMap2 gMap;
		//dialog...
		private ModalWindow locationsModalWindow;

		/**
		 * @param previousStep
		 * @param title
		 * @param summary
		 * @param model
		 */
		public ItemLocationStep(IDynamicWizardStep previousStep, IModel title, IModel summary, IModel model) {
			super(previousStep, title, summary, model);
			init();
		}
		
		private void init()  {
			//location model variables
			final LocationModel location = WebUtil.getUserActualLocation((SignInSession)getSession());
			ItemWizardModel model = (ItemWizardModel)getModelObject(); 
            model.setCountry(new SelectOption(String.valueOf(location.getCountryId()), location.getCountryName())); //yes, it works... :)
            model.setCity(new SelectOption(String.valueOf(location.getCityId()), location.getCityName()));
            //DIALOG
			locationsModalWindow = new ModalWindow("locationsModalWindow");
			locationsModalWindow.setCookieName("location-1");
			locationsModalWindow.setResizable(false);
			locationsModalWindow.setInitialWidth(60);
			locationsModalWindow.setInitialHeight(8);
			locationsModalWindow.setWidthUnit("em");
			locationsModalWindow.setHeightUnit("em");
			locationsModalWindow.setCssClassName(ModalWindow.CSS_CLASS_BLUE);
			/*
			locationsModalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback(){
				private static final long serialVersionUID = 1L;
				public boolean onCloseButtonClicked(AjaxRequestTarget target) {
					foundedLocation = (LocationModel)locationsModalWindow.getModelObject();
					final GMarkerOptions options = new GMarkerOptions().draggable(true);
					center = new GMarker(new GLatLng(foundedLocation.getLatitude(), foundedLocation.getLongitude()), options);
					center.setLatLng(new GLatLng(foundedLocation.getLatitude(), foundedLocation.getLongitude()));
					gMap.addOverlay(center);
					gMap.setCenter(new GLatLng(foundedLocation.getLatitude(), foundedLocation.getLongitude()));
					gMap.setZoom(Constants.STREET_VIEW_ZOOM_LEVEL);
					return true;
				}
			});
			*/
			locationsModalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback(){
				private static final long serialVersionUID = 1L;
				public void onClose(AjaxRequestTarget target) {
					final LocationModel foundedLocation = (LocationModel)locationsModalWindow.getModelObject();
					ItemWizardModel model = (ItemWizardModel)ItemLocationStep.this.getModelObject();
					model.setLatitude(foundedLocation.getLatitude());
					model.setLongitude(foundedLocation.getLongitude());
					GOverlay center = getCurrentPositionOverlay(foundedLocation.getLatitude(), foundedLocation.getLongitude(), model.getStreet());
					gMap.setOverlays(Collections.singletonList(center));
					gMap.setCenter(new GLatLng(foundedLocation.getLatitude(), foundedLocation.getLongitude()));
					gMap.setZoom(Constants.STREET_VIEW_ZOOM_LEVEL);
				}
			});
			add(locationsModalWindow);
			//CITY
            add(new Label("cityName", new LabelStringDetachableModel("CITY")));
            final DropDownChoice city = new DropDownChoice("city", new PropertyModel(getModelObject(), "city"), new CitiesSelectOptionListDetachableModel(model.getCountry().getKey())/*Collections.EMPTY_LIST*/, WebUtil.KEYVALUE_CHOICE_RENDERER) {
                private static final long serialVersionUID = 1L;
                protected CharSequence getDefaultChoice(final Object selected) {
                    return "<option value=\"\"></option>";
                }
            };
            city.setRequired(true);
            city.setOutputMarkupId(true);
            add(city);
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
                        ItemWizardModel model = (ItemWizardModel) getModelObject();
                        log.info("model ajax selected - " + model.getCountry());
                        city.setChoices(new CitiesSelectOptionListDetachableModel(model.getCountry().getKey()));
                        target.addComponent(city);
                    }
                }
            });
            //country.setNullValid(false);
            country.setRequired(true);
            add(country);
            //STREET
            add(new Label("streetName", new LabelStringDetachableModel("STREET")));
            final RequiredTextField street = new RequiredTextField("street", new PropertyModel(getModelObject(), "street"));
            street.add(new AjaxFormComponentUpdatingBehavior("onblur"){
				private static final long serialVersionUID = 1L;
				protected void onUpdate(AjaxRequestTarget target) {
					ItemWizardModel model = (ItemWizardModel)ItemLocationStep.this.getModelObject();
					final String streetName = model.getStreet();
					final String streetNumber = model.getStreetNumber();
					if(streetName != null && streetName.trim().length() > 0)  {
						final String cityName = model.getCity().getValue();
						final String countryName = model.getCountry().getValue();
						List<LocationModel> locations = ((Main)getApplication()).getServices().getGeoCoderService().getMultipleLocationsFor(streetNumber, streetName, cityName, countryName);
						if(locations != null && locations.size() > 0)  {
							if(locations.size() == 1)  {
								if(target != null)  {
									final LocationModel foundedLocation = locations.get(0);
									model.setLatitude(foundedLocation.getLatitude());
									model.setLongitude(foundedLocation.getLongitude());
									final GOverlay center = getCurrentPositionOverlay(foundedLocation.getLatitude(), foundedLocation.getLongitude(), model.getStreet());
									gMap.setOverlays(Collections.singletonList(center));
									gMap.setCenter(new GLatLng(foundedLocation.getLatitude(), foundedLocation.getLongitude()));
									gMap.setZoom(Constants.STREET_VIEW_ZOOM_LEVEL);
								}
							}
							else  {
								if(target != null)  {
									locationsModalWindow.setTitle(new LabelStringDetachableModel("LOCATIONS_FOUNDED"));
									locationsModalWindow.setContent(new ShowLocationsDialogPanel(locationsModalWindow.getContentId(), new CompoundPropertyModel(locations), locationsModalWindow));
									locationsModalWindow.show(target);
								}
							}
						}
					}
				}
            });
            /*
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
            */
            street.add(StringValidator.maximumLength(30));
            add(street);
            //STREET NUMBER
            add(new Label("streetNumberName", "#"));
            final RequiredTextField streetNumber = new RequiredTextField("streetNumber", new PropertyModel(getModelObject(), "streetNumber"));
            streetNumber.add(new AjaxFormComponentUpdatingBehavior("onblur"){
				private static final long serialVersionUID = 1L;
				protected void onUpdate(AjaxRequestTarget target) {
					ItemWizardModel model = (ItemWizardModel)ItemLocationStep.this.getModelObject();
					final String streetName = model.getStreet();
					final String streetNumber = model.getStreetNumber();
					if(streetName != null && streetName.trim().length() > 0)  {
						final String cityName = model.getCity().getValue();
						final String countryName = model.getCountry().getValue();
						List<LocationModel> locations = ((Main)getApplication()).getServices().getGeoCoderService().getMultipleLocationsFor(streetNumber, streetName, cityName, countryName);
						if(locations != null && locations.size() > 0)  {
							if(locations.size() == 1)  {
								if(target != null)  {
									final LocationModel foundedLocation = locations.get(0);
									model.setLatitude(foundedLocation.getLatitude());
									model.setLongitude(foundedLocation.getLongitude());
									final GOverlay center = getCurrentPositionOverlay(foundedLocation.getLatitude(), foundedLocation.getLongitude(), model.getStreet());
									gMap.setOverlays(Collections.singletonList(center));
									gMap.setCenter(new GLatLng(foundedLocation.getLatitude(), foundedLocation.getLongitude()));
									gMap.setZoom(Constants.STREET_VIEW_ZOOM_LEVEL);
								}
							}
							else  {
								if(target != null)  {
									locationsModalWindow.setTitle(new LabelStringDetachableModel("LOCATIONS_FOUNDED"));
									locationsModalWindow.setContent(new ShowLocationsDialogPanel(locationsModalWindow.getContentId(), new CompoundPropertyModel(locations), locationsModalWindow));
									locationsModalWindow.show(target);
								}
							}
						}
					}
				}
            });
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
            final RequiredTextField url = new RequiredTextField("url", new PropertyModel(getModelObject(), "URL"));
            add(url);
            //MAP
            gMap = new GMap2("gmap", Constants.GMAP_LOCALHOST_8080_KEY);
    		gMap.setZoom(Constants.CITY_VIEW_ZOOM_LEVEL);
    		gMap.addControl(GControl.GMapTypeControl);
    		gMap.addControl(GControl.GLargeMapControl);
    		gMap.setMapType(GMapType.G_NORMAL_MAP);
    		gMap.setCenter(new GLatLng(location.getLatitude(), location.getLongitude()));
    		gMap.setOutputMarkupId(true);
    		add(gMap);
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
		
		private GOverlay getCurrentPositionOverlay(final float latitude, final float longitude, final String title)  {
			final GIcon icon = new GIcon(urlFor(new ResourceReference(AddNewItemWizard.class, "image2.gif"))
					.toString(), urlFor(new ResourceReference(AddNewItemWizard.class, "shadow2.png"))
					.toString()).iconSize(new GSize(64, 64)).shadowSize(new GSize(64, 64)).iconAnchor(
					new GPoint(19, 40)).infoWindowAnchor(new GPoint(9, 2)).infoShadowAnchor(
					new GPoint(18, 25));
			final GMarkerOptions options = new GMarkerOptions(title, icon).draggable(true);
			final GLatLng latLng = new GLatLng(latitude, longitude); 
			return new GMarker(latLng, options);
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
			return nextStep;
		}
	}

}//END OF FILE