/**
 * 
 */
package mx.com.recomendare.web.items;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import mx.com.recomendare.services.amazon.AmazonAffiliateService;
import mx.com.recomendare.services.amazon.AmazonItemModel;
import mx.com.recomendare.web.Index;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.SelectOption;
import mx.com.recomendare.web.commons.models.detachables.CategoriesSelectOptionListDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.SubcategoriesSelectOptionListDetachableModel;
import mx.com.recomendare.web.session.AuthenticatedPage;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * @author jerry
 *
 */
public final class AddNewItemPage extends AuthenticatedPage {

	//the log...
    private static final Log log = LogFactory.getLog(AddNewItemPage.class);
    
	/**
	 * 
	 */
	public AddNewItemPage() {
		init();
	}
	
	private void init()  {
		//TITLE
		add(new Label("title", new LabelStringDetachableModel("ADD_NEW_ITEM")));
		add(new AddNewItemForm("addItemForm", new CompoundPropertyModel(new ItemFormModel())));
	}
	
	/**
	 * AddNewItemForm
	 */
	final class AddNewItemForm extends Form {
		private static final long serialVersionUID = 1L;

		//the sub categories drop down choice...
		private DropDownChoice subCategory;
		/**
		 * @param id
		 * @param model
		 */
		public AddNewItemForm(String id, IModel model) {
			super(id, model);
			init();
		}
		
		private void init()  {
			//NAME
			add(new Label("itemNameTitle", new LabelStringDetachableModel("ADD_ITEM_NAME")));
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
						ItemFormModel model = (ItemFormModel)getModelObject();
						log.info("model ajax selected - "+model.getCategory());
						subCategory.setChoices(new SubcategoriesSelectOptionListDetachableModel(Integer.parseInt(model.getCategory().getKey())));
						target.addComponent(subCategory);
					}
	            }
			});
			category.setRequired(true);
			add(category);
			//SUBCATEGORY
			add(new Label("itemSubCategoryLabel", new LabelStringDetachableModel("SUBCATEGORY")));
			subCategory = new DropDownChoice("subCategory", new PropertyModel(getModelObject(), "subCategory"), Collections.EMPTY_LIST, WebUtil.KEYVALUE_CHOICE_RENDERER) {
				private static final long serialVersionUID = 1L;
				protected CharSequence getDefaultChoice(final Object selected) {
		                return "<option value=\"\"></option>";
		        }
			};
			subCategory.setOutputMarkupId(true);
			subCategory.setRequired(true);
			add(subCategory);
			//NAME
			add(new Label("itemNameLabel", new LabelStringDetachableModel("TITLE")));
			final RequiredTextField name = new RequiredTextField("name", new PropertyModel(getModelObject(), "name"));
			name.add(StringValidator.maximumLength(100));
			add(name);
			//PICTURE
			final NonCachingImage	pictureItemFounded = new NonCachingImage("pictureItemFounded", WebUtil.QUESTIONMARK_IMAGE);
			pictureItemFounded.setOutputMarkupId(true);
			add(pictureItemFounded);
			//SEARCH BUTTON
			final AjaxButton searchItemButton = new AjaxButton("searchItemButton", this) {
				private static final long serialVersionUID = 1L;
				protected void onSubmit(AjaxRequestTarget target, Form form) {
					final ItemFormModel model = (ItemFormModel)form.getModelObject();
					log.info("model - "+model);
					String textToSearch = model.getName();
					if(textToSearch != null && textToSearch.trim().length() > 0)  {
						AmazonAffiliateService affiliateService = ((Main)getApplication()).getServices().getAmazonAffiliateService(); 
						List<AmazonItemModel> results = affiliateService.searchItemsLikeDescription(textToSearch, AmazonAffiliateService.BOOKS_CATEGORY);//TODO:have to ask for the selected category...
						if(results.size() > 0)  {
							final AmazonItemModel item = results.get(0);
							if(item.getImageURL() != null && item.getImageURL().trim().length() > 0)  {
								pictureItemFounded.setImageResource(new DynamicImageResource(){
									private static final long serialVersionUID = 1L;
									protected byte[] getImageData() {
										return ((Main)getApplication()).getServices().getHttpService().getContenAsBytesFrom(item.getImageURL());
									}
								});
								if(target != null)  {
									target.addComponent(pictureItemFounded);
								}
							}
						}
					}
				}
			};
			searchItemButton.setModel(new LabelStringDetachableModel("SEARCH"));
			searchItemButton.setDefaultFormProcessing(false);
			searchItemButton.setOutputMarkupId(true);
			add(searchItemButton);
			//DESCRIPTION
			add(new Label("itemDescriptionLabel", new LabelStringDetachableModel("DESCRIPTION")));
			final TextArea description = new TextArea("description", new PropertyModel(getModelObject(), "description"));
			description.add(StringValidator.maximumLength(200));
			description.setRequired(true);
			add(description);
			//DETAILS
			add(new Label("itemDetailsTitle", new LabelStringDetachableModel("ADD_ITEM_DETAILS")));
			//SAVE
			final Button saveItemButton = new Button("saveItemButton"){
				private static final long serialVersionUID = 1L;
				public void onSubmit() {
					log.info(getForm().getModelObject());
					setResponsePage(Index.class);
					//setResponsePage(new ViewItemPage(getModelObject(), -1));
				}
			};
			saveItemButton.setModel(new LabelStringDetachableModel("SAVE"));
			add(saveItemButton);
			//CANCEL
			final Button cancelItemButton = new Button("cancelItemButton"){
				private static final long serialVersionUID = 1L;
				public void onSubmit() {
					log.info(getForm().getModelObject());
					setResponsePage(Index.class);
				}
			};
			cancelItemButton.setModel(new LabelStringDetachableModel("CANCEL"));
			cancelItemButton.setDefaultFormProcessing(false);
			add(cancelItemButton);
		}

	}
	
	/**
	 * ItemFormModel
	 */
	final class ItemFormModel implements Serializable {
		private static final long serialVersionUID = 1L;
		private SelectOption category;
		private SelectOption subCategory;
		private String name;
		private String description;
		/**
		 * @return the category
		 */
		public SelectOption getCategory() {
			return category;
		}
		/**
		 * @param category the category to set
		 */
		public void setCategory(SelectOption category) {
			this.category = category;
		}
		/**
		 * @return the subCategory
		 */
		public SelectOption getSubCategory() {
			return subCategory;
		}
		/**
		 * @param subCategory the subCategory to set
		 */
		public void setSubCategory(SelectOption subCategory) {
			this.subCategory = subCategory;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}
		/**
		 * @param desription the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			sb.append(getClass().getName());
			sb.append("] - {category = '");
			sb.append(category);
			sb.append("', subCategory = '");
			sb.append(subCategory);
			sb.append("', name = '");
			sb.append(name);
			sb.append("', description = '");
			sb.append(description);
			sb.append("'}");
			return sb.toString();
		}
		
	}

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new LabelStringDetachableModel("ADD_NEW_ITEM", LabelStringDetachableModel.TITLE_LABEL);
	}

	protected String getMetaDescription() {
		return "";
	}

	protected String getMetaKeywords() {
		return "";
	}

}//END OF FILE