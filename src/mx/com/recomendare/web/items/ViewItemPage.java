/**
 * 
 */
package mx.com.recomendare.web.items;

import mx.com.recomendare.util.Constants;
import mx.com.recomendare.util.Util;
import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.comments.CommentsLinkPanel;
import mx.com.recomendare.web.comments.CommentsTextPanel;
import mx.com.recomendare.web.commons.components.AjaxEditableTextPanel;
import mx.com.recomendare.web.commons.components.BlankPanel;
import mx.com.recomendare.web.commons.components.FavoritesLinkPanel;
import mx.com.recomendare.web.commons.components.ItemLocationPanel;
import mx.com.recomendare.web.commons.components.LinkedPanel;
import mx.com.recomendare.web.commons.components.NavigationPanel;
import mx.com.recomendare.web.commons.components.StarsRatingPanel;
import mx.com.recomendare.web.commons.components.TagCloud;
import mx.com.recomendare.web.commons.models.CommentModel;
import mx.com.recomendare.web.commons.models.ItemModel;
import mx.com.recomendare.web.commons.models.detachables.ItemDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.ItemTagsLoadableDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.places.AddPicturesToPlacePage;
import mx.com.recomendare.web.places.ViewAllPicturesOfPlacePage;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.users.ViewUserProfilePage;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.markup.html.image.resource.ThumbnailImageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.wicketstuff.lightbox.LightboxImage;
import org.wicketstuff.lightbox.LightboxImageData;

/**
 * @author jerry
 *
 */
public final class ViewItemPage extends BasePage {
	private static final long serialVersionUID = 1L;

	//the log...
	private static final Log log = LogFactory.getLog(ViewItemPage.class);
	
	public ViewItemPage(PageParameters parameters) {
		super(parameters);
		String itemKey = parameters.getString("i");
		log.info(itemKey);
		log.info(urlFor(ViewItemPage.class, parameters));
		if (itemKey != null && itemKey.trim().length() > 0) {
			int itemId = Integer.parseInt(itemKey);
			IModel itemModel = new CompoundPropertyModel(new ItemDetachableModel(itemId, ItemDetachableModel.DETAIL_MODE));
			setModel(itemModel);
			init();
		} else {//TODO:send to an 404 custom page...
		}
	}

	private void init() {
		final ItemModel model = (ItemModel)getModelObject();
		/*
		//RSS
		add(FeedManager.createLink("itemFeed", ItemFeed.class));
		*/
		//NAME
		add(new Label("name", model.getName()));//new PropertyModel(getModel(), "name")
		//NAVIGATION
		add(new NavigationPanel("navigationPanel", getModel()));
		//LINKS
		add(new LinkedPanel("linksToPagePanel", new CompoundPropertyModel(model)));
		//FAVORITES
		add(new FavoritesLinkPanel("favoritesLinkPanel", new CompoundPropertyModel(model)));
		//PICTURES
		if(model.getPictures() != null && model.getPictures().get(0) != null && model.getPictures().get(0).getContent().length > 0) {
			final DynamicImageResource imageResource = new DynamicImageResource() {
				private static final long serialVersionUID = 1L;
				protected byte[] getImageData() {
					return model.getPictures().get(0).getContent();//the main picture...
				}
			};
			final ThumbnailImageResource thumbnailImageResource = new ThumbnailImageResource(imageResource, 255);
            //thumbnailImageResource.setCacheable(true);
			final Link viewAllPicturesLink = new Link("viewAllPicturesLink"){
				private static final long serialVersionUID = 1L;
				public void onClick() {
					PageParameters parameters = new PageParameters();
					parameters.put("i", model.getId());
					parameters.put("n", model.getName());
					setResponsePage(ViewAllPicturesOfPlacePage.class, parameters);
				}
			};
			viewAllPicturesLink.add(new Image("firstPicture", thumbnailImageResource));
			add(viewAllPicturesLink);
			//add the light box link...
			add(new Image("ligthBoxZoom", WebUtil.ZOOM_IMAGE));
			final String zoomLabel = /*new LabelStringDetachableModel("ZOOM").toString();*/"zoom";
			IModel lightBoxImageModel = new LightboxImageData.Builder(new ThumbnailImageResource(imageResource, 500)).linkText(zoomLabel).model();
			add(new LightboxImage("ligthBoxPicture", lightBoxImageModel));
		} else {
			final Link viewAllPicturesLink = new Link("viewAllPicturesLink"){
				private static final long serialVersionUID = 1L;
				public void onClick() {
					PageParameters parameters = new PageParameters();
					parameters.put("i", model.getId());
					parameters.put("n", model.getName());
					setResponsePage(AddPicturesToPlacePage.class, parameters);
				}
			};
			viewAllPicturesLink.add(new Image("firstPicture", WebUtil.QUESTIONMARK_IMAGE));
			add(viewAllPicturesLink);
		}
		//TODO:obviusly we have to ask for each picture and show it in a thumbnail...if there is none, show these...
		add(new Image("secondPicture", WebUtil.PICTURE_EMPTY_IMAGE));
		add(new Image("thirdPicture", WebUtil.PICTURE_EMPTY_IMAGE));
		final Link addMorePicturesLink = new Link("addMorePicturesLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				PageParameters parameters = new PageParameters();
				parameters.put("i", model.getId());
				parameters.put("n", model.getName());
				setResponsePage(AddPicturesToPlacePage.class, parameters);
			}
		};
		addMorePicturesLink.add(new Label("addMorePicturesLinkLabel", new LabelStringDetachableModel("ADD_PICTURES")));
		addMorePicturesLink.add(new Image("addMorePicturesLinkImage", WebUtil.PICTURE_ADD_IMAGE));
		add(addMorePicturesLink);
		//AD
		add(new Image("googleAdSenseImage", WebUtil.AD_200X200_IMAGE));
		//OVERALL RATING
		add(new StarsRatingPanel("overallRatingPanel", model.getOverallRating(), StarsRatingPanel.NORMAL_STYLE));
		add(new Label("numberOfRatingComments", String.valueOf(model.getNumberOfRatings())));//new PropertyModel(getModel(), "numberOfRatings")
		add(new Label("ratingCommentsLabel", new LabelStringDetachableModel("COMMENTS")));
		//DESCRIPTION
		add(new Label("descriptionLabel", new LabelStringDetachableModel("DESCRIPTION")));
		add(new AjaxEditableTextPanel("description", new PropertyModel(getModel(), "description")));
		//add(new Label("description", model.getDescription()));//new PropertyModel(getModel(), "description")
		//PRICE
		//TODO:have to refactor to a loadable detachable model with the constants calculation instead of calls to a webservice...this is costly... 
		add(new Label("priceRangeLabel", new LabelStringDetachableModel("PRICE_RANGE")));
		add(new Label("priceFromLabel", new LabelStringDetachableModel("FROM")));
		add(new Label("priceMinValue", Util.getCurrencyFormat(model.getPrice().getMinPrice(), getLocation().getLanguageCode(), getLocation().getCountryCode())));
		add(new Label("priceUntilLabel", new LabelStringDetachableModel("UNTIL")));
		add(new Label("priceMaxValue", Util.getCurrencyFormat(model.getPrice().getMaxPrice(), getLocation().getLanguageCode(), getLocation().getCountryCode())));
		//TAGS
		add(new Label("tagsLabel", new LabelStringDetachableModel("TAGS")));
		add(new TagCloud("tags", new ItemTagsLoadableDetachableModel(model)));
		//LOCATION
		Panel locationPanel = null;
		if(model.getLocation() != null)  {
			locationPanel = new  ItemLocationPanel("locationPanel", getModel());
		}
		else  {
			locationPanel = new BlankPanel("locationPanel");
		}
		add(locationPanel);
		//AD
		add(new Image("googleAdSenseBannerImage", WebUtil.AD_BANNER_IMAGE));
		//COMMENTS
		add(new Label("commentsLabel", new LabelStringDetachableModel("COMMENTS")));
		Panel commentsPanel = null;
		if( ((SignInSession)getSession()).isSignedIn() )  {//TODO: It have to show it all the time...redirect to the login only if it's not
			commentsPanel = new CommentsTextPanel("commentsEditorPanel", getModel());
		}
		else  {
			commentsPanel = new CommentsLinkPanel("commentsEditorPanel", getModel());
		}
		add(commentsPanel);
		final PageableListView commentsListView = new PageableListView("comments", model.getComments(), Constants.MAX_ITEMS_PER_PAGE) {
			private static final long serialVersionUID = 1L;
			protected void populateItem(final ListItem item) {
				final CommentModel comment = (CommentModel) item.getModelObject();
				//USER
				final Link commentUserDetailLink = new Link("commentUserDetailLink") {
					private static final long serialVersionUID = 1L;
					public void onClick() {
						PageParameters parameters = new PageParameters();
						parameters.put("i", comment.getUserId());
						parameters.put("n", comment.getUserScreenName());
						setResponsePage(ViewUserProfilePage.class, parameters);
					}
				};
				if(comment.getUserAvatarPicture() != null && comment.getUserAvatarPicture().getContent() != null) {
                	final DynamicImageResource imageResource = new DynamicImageResource() {
        				private static final long serialVersionUID = 1L;
        				protected byte[] getImageData() {
        					return comment.getUserAvatarPicture().getContent();
        				}
        			};
        			final ThumbnailImageResource thumbnailImageResource = new ThumbnailImageResource(imageResource, 50);
        			commentUserDetailLink.add(new Image("commentUserDetailLinkImage", thumbnailImageResource));
                }
                else  {
                	commentUserDetailLink.add(new Image("commentUserDetailLinkImage", WebUtil.USER_STATUS_OFFLINE_IMAGE));
                }
				commentUserDetailLink.add(new Label("commentUserDetailLinkScreenName", comment.getUserScreenName()));
				item.add(commentUserDetailLink);
				item.add(new StarsRatingPanel("commentUserRating", comment.getRating(), StarsRatingPanel.NORMAL_STYLE));
				final Label userComment = new Label("commentUserComment", comment.getUserComment());
				userComment.setEscapeModelStrings(false); 
				item.add(userComment);
				//THUMBS UP
				item.add(new Label("itServeYouThisCommentLabel", new LabelStringDetachableModel("IT_SERVE_YOU_COMMENT")));//TODO:change the label to RATE_COMMENT...
				final Label commentUserThumbUpValue = new Label("commentUserThumbUpValue", String.valueOf(comment.getThumbsUp()));//rating.getRatedFavorably()
				commentUserThumbUpValue.setOutputMarkupId(true);
				final AjaxFallbackLink commentUserThumbUpLink = new AjaxFallbackLink("commentUserThumbUpLink") {
					private static final long serialVersionUID = 1L;
					public void onClick(AjaxRequestTarget target) {
						//Short newRatingFavorablyToComment = ((Main)getApplication()).getDatabaseService().getRatingsDAO().addFavorableRatingToComment(rating.getId());
						int newRatingFavorablyToComment = comment.getThumbsUp();
						newRatingFavorablyToComment++;
						log.info(newRatingFavorablyToComment);
						if(target != null)  {
							//TODO:update in the database...
							commentUserThumbUpValue.setModel(new Model(newRatingFavorablyToComment));
							target.addComponent(commentUserThumbUpValue);
							setEnabled(false);
						}
					}
				};
				commentUserThumbUpLink.add(new Image("commentUserThumbUpLinkImage", WebUtil.THUMB_UP_IMAGE));
				commentUserThumbUpLink.add(commentUserThumbUpValue);
				commentUserThumbUpLink.add(new Label("commentUserThumbUpLinkVotesLabel", new LabelStringDetachableModel("VOTES")));
				item.add(commentUserThumbUpLink);
				//THUMBS DOWN
				final Label commentThumbDownValue = new Label("commentUserThumbDownValue", String.valueOf(comment.getThumbsDown()));//rating.getRatedNotFavorably()
				commentThumbDownValue.setOutputMarkupId(true);
				final AjaxFallbackLink commentThumbDownLink = new AjaxFallbackLink("commentUserThumbDownLink") {
					private static final long serialVersionUID = 1L;
					public void onClick(AjaxRequestTarget target) {
						//Short newRatingNotFavorablyToComment = ((Main)getApplication()).getDatabaseService().getRatingsDAO().addNotFavorableRatingToComment(rating.getId());
						int newRatingNotFavorablyToComment = comment.getThumbsDown();
						newRatingNotFavorablyToComment++;
						log.info(newRatingNotFavorablyToComment);
						if(target != null)  {
							//TODO:update in the database...
							commentThumbDownValue.setModel(new Model(newRatingNotFavorablyToComment));
							target.addComponent(commentThumbDownValue);
							setEnabled(false);
						}
					}
				};
				commentThumbDownLink.add(new Image("commentUserThumbDownLinkImage", WebUtil.THUMB_DOWN_IMAGE));
				commentThumbDownLink.add(commentThumbDownValue);
				commentThumbDownLink.add(new Label("commentUserThumbDownLinkVotesLabel", new LabelStringDetachableModel("VOTES")));
				item.add(commentThumbDownLink);
			}
		};
		add(commentsListView);
		//NAVIGATORS
		add(new PagingNavigator("topCommentsNavigator", commentsListView));
		add(new PagingNavigator("bottomCommentsNavigator", commentsListView));
		//RELATED
		/*
		add(new Label("relatedSitesToThis", new LabelStringDetachableModel("RELATED_SITES")));
		final List<YahooBossSearchResultModel> relatedSitesSearchResult = ((Main)getApplication()).getServices().getYahooBossSearchService().search(model.getName() + " +" + model.getCategory().getCategoryName(), getLocation().getLanguageCode(), getLocation().getCountryCode(), 0, 3);
		final ListView relatedSitesFromThisList = new ListView("relatedSitesFromThisList", (relatedSitesSearchResult != null && relatedSitesSearchResult.size() > 0)?relatedSitesSearchResult:Collections.EMPTY_LIST){
			private static final long serialVersionUID = 1L;
			protected void populateItem(ListItem item) {
				final YahooBossSearchResultModel result = (YahooBossSearchResultModel)item.getModelObject();
				final ExternalLink relatedSiteLink = new ExternalLink("relatedSiteLink", result.getUrl());
				final Label relatedSiteLinkLabel = new Label("relatedSiteLinkLabel", result.getTitle());
				relatedSiteLinkLabel.setEscapeModelStrings(false);
				relatedSiteLink.add(relatedSiteLinkLabel);
				relatedSiteLink.add(new AttributeModifier("title", true, new Model(result.getUrl())));
				item.add(relatedSiteLink);
				final Label relatedSiteDescriptionLabel = new Label("relatedSiteDescriptionLabel", result.getDescription());
				relatedSiteDescriptionLabel.setEscapeModelStrings(false); 
				item.add(relatedSiteDescriptionLabel);
				item.add(new Label("relatedSiteLinkURL", result.getUrl()));
			}
		};
		add(relatedSitesFromThisList);
		*/
		//AD
		add(new Image("googleAdSenseBannerImage1", WebUtil.AD_BANNER_IMAGE));
	}

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		final ItemModel model = (ItemModel)getModelObject();
		final String title = model.getName() + " - " + model.getCategory().getCategoryName() + " - " + model.getCategory().getSubcategoryName() + " - " + Constants.SITE_NAME; 
		return new Model(title);
	}

	/**
	 * 
	 */
	protected String getMetaDescription() {
		return "";
	}

	/**
	 * 
	 */
	protected String getMetaKeywords() {
		return "";
	}

}//END OF FILE