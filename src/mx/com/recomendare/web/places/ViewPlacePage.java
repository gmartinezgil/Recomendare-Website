package mx.com.recomendare.web.places;

import java.util.ArrayList;

import mx.com.recomendare.db.entities.Contact;
import mx.com.recomendare.services.currency.CurrencyConverterService;
import mx.com.recomendare.services.yahoo.boss.YahooBossSearchResultModel;
import mx.com.recomendare.util.Constants;
import mx.com.recomendare.util.Util;
import mx.com.recomendare.web.BasePage;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.categories.ViewCategoryPage;
import mx.com.recomendare.web.categories.ViewSubCategoryPage;
import mx.com.recomendare.web.comments.CommentsLinkPanel;
import mx.com.recomendare.web.comments.CommentsTextPanel;
import mx.com.recomendare.web.commons.components.FavoritesLinkPanel;
import mx.com.recomendare.web.commons.components.LinkedPanel;
import mx.com.recomendare.web.commons.components.StarsRatingPanel;
import mx.com.recomendare.web.commons.models.LocationModel;
import mx.com.recomendare.web.commons.models.detachables.CategoryNameStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.CityNameStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.CountryNameStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.ItemLocationDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.ItemResumeStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.RatingCommentStringDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.SubcategoryNameStringDetachableModel;
import mx.com.recomendare.web.countries.ViewCityPage;
import mx.com.recomendare.web.countries.ViewCountryPage;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.users.ViewUserProfilePage;
import mx.com.recomendare.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.markup.html.image.resource.ThumbnailImageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.wicketstuff.lightbox.LightboxImage;
import org.wicketstuff.lightbox.LightboxImageData;

import com.aetrion.flickr.places.Place;

/**
 * 
 * @author jerry
 *
 */
public final class ViewPlacePage extends BasePage {
	private static final long serialVersionUID = 1L;

	//the log...
	private static final Log log = LogFactory.getLog(ViewPlacePage.class);
	
	public ViewPlacePage(PageParameters parameters) {
		super(parameters);
		String placeKey = parameters.getString("i");
		log.info(placeKey);
		log.info(urlFor(ViewPlacePage.class, parameters));
		if (placeKey != null && placeKey.trim().length() > 0) {
			int placeId = Integer.parseInt(placeKey);
			IModel placeModel = new CompoundPropertyModel(new ItemLocationDetachableModel(placeId));
			setModel(placeModel);
			init();
		} else {/*throws PlaceNotFoundException(placeKey);*//*setResponsePage(pageBack);*/
		}
	}

	private void init() {
		final Place place = (Place) getModelObject();
		/*
		//RSS
		add(FeedManager.createLink("placeFeed", PlaceFeed.class));
		*/
		//NAME
		add(new Label("placeName", new PropertyModel(getModel(), "item.name")));
		//COUNTRY
		final Link viewCountryLink = new Link("viewCountryLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				PageParameters parameters = new PageParameters();
				parameters.put("i", place.getStreet().getCity().getCountry().getId());
				parameters.put("n", new CountryNameStringDetachableModel(place.getStreet().getCity().getCountry().getId()));
				setResponsePage(ViewCountryPage.class, parameters);
			}
		};
		viewCountryLink.add(new Image("countryLinkImage", WebUtil.getCountryFlagImageFromUserLocation(place.getStreet().getCity().getCountry().getCode())));
		viewCountryLink.add(new Label("countryLinkName", new CountryNameStringDetachableModel(place.getStreet().getCity().getCountry().getId())));
		add(viewCountryLink);
		//CITY
		final Link viewCityLink = new Link("viewCityLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				PageParameters parameters = new PageParameters();
				parameters.put("i", place.getStreet().getCity().getId());
				parameters.put("n", new CityNameStringDetachableModel(place.getStreet().getCity().getId()));
				setResponsePage(ViewCityPage.class, parameters);
			}
		};
		viewCityLink.add(new Label("cityOfLabel", new LabelStringDetachableModel("CITY_OF"))); 
		viewCityLink.add(new Label("cityLinkName", new CityNameStringDetachableModel(place.getStreet().getCity().getId())));
		add(viewCityLink);
		//CATEGORY
		final Link categoriesLink = new Link("placeCategoryLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				PageParameters parameters = new PageParameters();
				parameters.put("i", place.getItem().getSubcategory().getCategory().getId());
				parameters.put("n", new CategoryNameStringDetachableModel(place.getItem().getSubcategory().getCategory().getId()));
				setResponsePage(ViewCategoryPage.class, parameters);
			}
		};
		categoriesLink.add(new Label("placeCategoryLabel", new CategoryNameStringDetachableModel(place.getItem().getSubcategory().getCategory().getId())));//new PropertyModel(getModel(), "item.subcategory.category.name")
		add(categoriesLink);
		//SUBCATEGORY
		final Link subCategoriesLink = new Link("placeSubCategoryLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				PageParameters parameters = new PageParameters();
				parameters.put("i", place.getItem().getSubcategory().getId());
				parameters.put("n", new SubcategoryNameStringDetachableModel(place.getItem().getSubcategory().getId()));
				parameters.put("ci", place.getItem().getSubcategory().getCategory().getId());
				parameters.put("cn", new CategoryNameStringDetachableModel(place.getItem().getSubcategory().getCategory().getId()));
				setResponsePage(ViewSubCategoryPage.class, parameters);
			}
		};
		subCategoriesLink.add(new Label("placeSubCategoryLabel", new SubcategoryNameStringDetachableModel(place.getItem().getSubcategory().getId())));//new PropertyModel(getModel(), "item.subcategory.name")
		add(subCategoriesLink);
		//FAVORITES
		add(new FavoritesLinkPanel("favoritesLinkPanel", new CompoundPropertyModel(place.getItem())));
		//LINKS
		add(new LinkedPanel("linksToPage", new CompoundPropertyModel(place.getItem())));
		//PICTURE
		if (place.getItem().getPictureByPictureOneId() != null && place.getItem().getPictureByPictureOneId().getContent().length > 0) {
			final DynamicImageResource imageResource = new DynamicImageResource() {
				private static final long serialVersionUID = 1L;
				protected byte[] getImageData() {
					return place.getItem().getPictureByPictureOneId().getContent();//the main picture...
				}
			};
			final ThumbnailImageResource thumbnailImageResource = new ThumbnailImageResource(imageResource, 255);
            //thumbnailImageResource.setCacheable(true);
			final Link viewAllPicturesFromPlaceLink = new Link("viewAllPicturesFromPlaceLink"){
				private static final long serialVersionUID = 1L;
				public void onClick() {
					PageParameters parameters = new PageParameters();
					parameters.put("i", place.getId());
					parameters.put("n", place.getItem().getName());
					setResponsePage(ViewAllPicturesOfPlacePage.class, parameters);
				}
			};
			viewAllPicturesFromPlaceLink.add(new Image("firstPlacePicture", thumbnailImageResource));
			add(viewAllPicturesFromPlaceLink);

			//add the light box link...
			add(new Image("ligthBoxZoom", WebUtil.ZOOM_IMAGE));
			final String zoomLabel = /*new LabelStringDetachableModel("ZOOM").toString();*/"zoom";
			IModel lightBoxImageModel = new LightboxImageData.Builder(new ThumbnailImageResource(imageResource, 500)).linkText(zoomLabel).model();
			add(new LightboxImage("ligthBoxPicture", lightBoxImageModel));
		} else {
			final Link viewAllPicturesFromPlaceLink = new Link("viewAllPicturesFromPlaceLink"){
				private static final long serialVersionUID = 1L;
				public void onClick() {
					PageParameters parameters = new PageParameters();
					parameters.put("i", place.getId());
					parameters.put("n", place.getItem().getName());
					setResponsePage(AddPicturesToPlacePage.class, parameters);
				}
			};
			viewAllPicturesFromPlaceLink.add(new Image("firstPlacePicture", WebUtil.QUESTIONMARK_IMAGE));
			add(viewAllPicturesFromPlaceLink);
		}
		//TODO:obviusly we have to ask for each picture and show it in a thumbnail...if there is none, show these...
		add(new Image("secondPlacePicture", WebUtil.PICTURE_EMPTY_IMAGE));
		add(new Image("thirdPlacePicture", WebUtil.PICTURE_EMPTY_IMAGE));
		final Link addMorePicturesLink = new Link("addMorePicturesLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				PageParameters parameters = new PageParameters();
				parameters.put("i", place.getId());
				parameters.put("n", place.getItem().getName());
				setResponsePage(AddPicturesToPlacePage.class, parameters);
			}
		};
		addMorePicturesLink.add(new Label("addMorePicturesLinkLabel", new LabelStringDetachableModel("ADD_PICTURES")));
		addMorePicturesLink.add(new Image("addMorePicturesLinkImage", WebUtil.PICTURE_ADD_IMAGE));
		add(addMorePicturesLink);
		//AD
		add(new Image("googleAdSenseImage", WebUtil.AD_200X200_IMAGE));
		//OVERALL RATING
		add(new StarsRatingPanel("placeOverallRatingPanel", DatabaseUtil.calculateOverallRating(place.getItem().getItemRatings()), StarsRatingPanel.NORMAL_STYLE));
		add(new Label("placeOverallRatingComments", new PropertyModel(getModel(), "item.itemRatings.size")));
		add(new Label("placeCommentsLabel", new LabelStringDetachableModel("COMMENTS")));
		//PRICE
		//TODO:have to refactor to a loadable detachable model with the constants calculation instead of calls to a webservice...this is costly... 
		final CurrencyConverterService currencyService =	((Main)getApplication()).getServices().getCurrencyConverterService();
		float minValue = (place.getItem().getItemPriceByMinPriceId() != null) ? place.getItem().getItemPriceByMinPriceId().getValue().floatValue() : 0F;
		float maxValue = (place.getItem().getItemPriceByMaxPriceId() != null) ? place.getItem().getItemPriceByMaxPriceId().getValue().floatValue() : 0F;
		//convert them...
		final LocationModel location = WebUtil.getUserActualLocation((SignInSession)getSession());
		minValue = (float)currencyService.convertCurrencyFromCountryTo(minValue, place.getStreet().getCity().getCountry().getCode(), location.getCountryCode());
		maxValue = (float)currencyService.convertCurrencyFromCountryTo(maxValue, place.getStreet().getCity().getCountry().getCode(), location.getCountryCode());        
		add(new Label("placePriceRangeLabel", new LabelStringDetachableModel("PRICE_RANGE")));
		add(new Label("placePriceFromLabel", new LabelStringDetachableModel("FROM")));
		add(new Label("placePriceMinValue", Util.getCurrencyFormat(minValue, location.getLanguageCode(), location.getCountryCode())));
		add(new Label("placePriceUntilLabel", new LabelStringDetachableModel("UNTIL")));
		add(new Label("placePriceMaxValue", Util.getCurrencyFormat(maxValue, location.getLanguageCode(), location.getCountryCode())));
		//LOCATION
		add(new Label("placeLocation", new LabelStringDetachableModel("LOCATION")));//TODO: it should be called an image from the data base...
		final Image googleStaticPlaceMapImage = new Image("pictureMapLocation", WebUtil.getStaticMapImageResource(
																						place.getLocation().getLatitude().floatValue(), 
																						place.getLocation().getLongitude().floatValue(), 
																						Constants.STREET_VIEW_ZOOM_LEVEL, 
																						255, 
																						300
																						));
		final Link viewPlaceOnInteractiveMap = new Link("viewPlaceOnInteractiveMap"){
			private static final long serialVersionUID = 1L;
			public void onClick() {
				PageParameters parameters = new PageParameters();
				parameters.put("i", place.getId());
				parameters.put("n", place.getItem().getName());
				setResponsePage(ViewAnotherPlacesNearOfPage.class, parameters);
			}
		};
		viewPlaceOnInteractiveMap.add(googleStaticPlaceMapImage);
		add(viewPlaceOnInteractiveMap);		
		final Link viewAnotherPlacesNearOfLink = new Link("viewAnotherPlacesNearOfLink") {
			private static final long serialVersionUID = 1L;
			public void onClick() {
				PageParameters parameters = new PageParameters();
				parameters.put("i", place.getId());
				parameters.put("n", place.getItem().getName());
				setResponsePage(ViewAnotherPlacesNearOfPage.class, parameters);
			}
		};
		viewAnotherPlacesNearOfLink.add(new Image("nearByPlacesMapPicture", WebUtil.NEARBY_PLACES_MAP_IMAGE));
		viewAnotherPlacesNearOfLink.add(new Label("viewAnotherPlacesNearOfLabel", new LabelStringDetachableModel("VIEW_NEARBY_PLACES")));
		add(viewAnotherPlacesNearOfLink);
		//ADRESS...
		add(new Label("placeAddress", new LabelStringDetachableModel("ADDRESS")));
		add(new Label("streetOfLabel", new LabelStringDetachableModel("STREET_OF")));
		add(new Label("placeStreetName",place.getStreet().getName()));
		add(new Label("numberLabel", new LabelStringDetachableModel("NUMBER")));
		add(new Label("placeStreetNumber", place.getOutsideNumber()));
		add(new Label("phoneNumberLabel", new LabelStringDetachableModel("PHONE_NUMBER")));
		add(new Label("placePhoneNumber", "(52)56399534" /*((Contact)place.getItem().getContacts().toArray()[0]).getPerson().getWorkPhoneNumber()*/));
		add(new Label("cityOfLabel", new LabelStringDetachableModel("CITY_OF")));
		add(new Label("placeCityName", new CityNameStringDetachableModel(place.getStreet().getCity().getId())));//new PropertyModel(getModel(), "street.city.name")
		add(new Label("placeCountryName", new CountryNameStringDetachableModel(place.getStreet().getCity().getCountry().getId())));//new PropertyModel(getModel(), "street.city.country.name")
		//homepage
		final String homePageAddress = (place.getItem().getContacts() != null && place.getItem().getContacts().size() > 0) ? ((Contact) place.getItem().getContacts().toArray()[0]).getHomepageAddress() : "";
		//TODO:still you got to have statistics from here...also...
		final ExternalLink internetAddressLink = new ExternalLink("placeInternetAddressLink", homePageAddress);
		internetAddressLink.add(new Label("officialSiteLabel", new LabelStringDetachableModel("OFFICIAL_SITE")));
		internetAddressLink.add(new Image("officialSiteImage", WebUtil.GO_TO_PAGE_IMAGE));
		internetAddressLink.add(new AttributeModifier("title", true, new Model(homePageAddress)));
		add(internetAddressLink);
		//AD
		add(new Image("googleAdSenseBannerImage", WebUtil.AD_BANNER_IMAGE));
		//DESCRIPTION
		add(new Label("placeDescriptionName", new LabelStringDetachableModel("DESCRIPTION")));
		add(new Label("placeDescription", new ItemResumeStringDetachableModel(place.getItem().getId())));//new PropertyModel(getModel(), "item.resume")
		//COMMENTS
		add(new Label("placeCommentsName", new LabelStringDetachableModel("COMMENTS")));
		Panel commentsPanel = null;
		if( ((SignInSession)getSession()).isSignedIn() )  {//TODO: It have to show it all the time...redirect to the login only if it's not
			commentsPanel = new CommentsTextPanel("comments", new PropertyModel(getModel(), "item"));
		}
		else  {
			commentsPanel = new CommentsLinkPanel("comments", new PropertyModel(getModel(), "item"));
		}
		add(commentsPanel);
		final PageableListView commentsListView = new PageableListView("placeComments", new ArrayList(place.getItem().getItemRatings()), Constants.MAX_ITEMS_PER_PAGE) {
			private static final long serialVersionUID = 1L;
			protected void populateItem(final ListItem item) {
				final ItemRating rating = (ItemRating) item.getModelObject();
				//USER
				final Link userDetailLink = new Link("userDetailLink") {
					private static final long serialVersionUID = 1L;
					public void onClick() {
						PageParameters parameters = new PageParameters();
						parameters.put("i", rating.getUser().getId());
						parameters.put("n", rating.getUser().getUserConfiguration().getScreenName());
						setResponsePage(ViewUserProfilePage.class, parameters);
					}
				};
				userDetailLink.add(new Image("placeCommentUserPicture", WebUtil.USER_STATUS_OFFLINE_IMAGE));//TODO: by the moment...it should retrieve the blob image of the configuration of the user...
				userDetailLink.add(new Label("placeCommentUserScreenName", rating.getUser().getUserConfiguration().getScreenName()));
				item.add(userDetailLink);
				item.add(new StarsRatingPanel("placeCommentRating", rating.getCalification().intValue(), StarsRatingPanel.NORMAL_STYLE));
				final Label placeRatingCommentLabel = new Label("placeComment", new RatingCommentStringDetachableModel(rating.getId()));
				placeRatingCommentLabel.setEscapeModelStrings(false); 
				item.add(placeRatingCommentLabel);
				//THUMBS UP
				item.add(new Label("itServeYouThisCommentLabel", new LabelStringDetachableModel("IT_SERVE_YOU_COMMENT")));//TODO:change the label to RATE_COMMENT...
				final Label commentsThumbUpLabel = new Label("commentsThumbUpLabel", new Model(rating.getCalification()));//rating.getRatedFavorably()
				commentsThumbUpLabel.setOutputMarkupId(true);
				final AjaxFallbackLink commentThumbUpLink = new AjaxFallbackLink("commentThumbUp") {
					private static final long serialVersionUID = 1L;
					public void onClick(AjaxRequestTarget target) {
						//Short newRatingFavorablyToComment = ((Main)getApplication()).getDatabaseService().getRatingsDAO().addFavorableRatingToComment(rating.getId());
						short newRatingFavorablyToComment = rating.getCalification().shortValue();
						newRatingFavorablyToComment++;
						log.info(newRatingFavorablyToComment);
						if(target != null)  {
							commentsThumbUpLabel.setModel(new Model(newRatingFavorablyToComment));
							target.addComponent(commentsThumbUpLabel);
							setEnabled(false);
						}
					}
				};
				commentThumbUpLink.add(new Image("thumbUpImage", WebUtil.THUMB_UP_IMAGE));
				commentThumbUpLink.add(commentsThumbUpLabel);
				commentThumbUpLink.add(new Label("commentVotes", new LabelStringDetachableModel("VOTES")));
				item.add(commentThumbUpLink);
				//THUMBS DOWN
				final Label commentsThumbDownLabel = new Label("commentsThumbDownLabel", new Model(rating.getCalification()));//rating.getRatedNotFavorably()
				commentsThumbDownLabel.setOutputMarkupId(true);
				final AjaxFallbackLink commentThumbDownLink = new AjaxFallbackLink("commentThumbDown") {
					private static final long serialVersionUID = 1L;
					public void onClick(AjaxRequestTarget target) {
						//Short newRatingNotFavorablyToComment = ((Main)getApplication()).getDatabaseService().getRatingsDAO().addNotFavorableRatingToComment(rating.getId());
						short newRatingNotFavorablyToComment = rating.getCalification().shortValue();
						newRatingNotFavorablyToComment++;
						log.info(newRatingNotFavorablyToComment);
						if(target != null)  {
							commentsThumbDownLabel.setModel(new Model(newRatingNotFavorablyToComment));
							target.addComponent(commentsThumbDownLabel);
							setEnabled(false);
						}
					}
				};
				commentThumbDownLink.add(new Image("thumbDownImage", WebUtil.THUMB_DOWN_IMAGE));
				commentThumbDownLink.add(commentsThumbDownLabel);
				commentThumbDownLink.add(new Label("commentVotes", new LabelStringDetachableModel("VOTES")));
				item.add(commentThumbDownLink);
			}
		};
		add(commentsListView);
		add(new PagingNavigator("placeCommentsNavigator", commentsListView));
		//RELATED
		final ListView relatedSitesFromThisList = new ListView("relatedSitesFromThisList", ((Main)getApplication()).getServices().getYahooBossSearchService().search(place.getItem().getName(), location.getLanguageCode(), location.getCountryCode().toLowerCase(), 0, 10)){
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
	}

	/**
	 * 
	 */
	public String getVariation() {
		PageParameters parameters = getPageParameters();
		String formatParameter = parameters.getString("f");
		//log.info(formatParameter);
		return (formatParameter != null && formatParameter.trim().length() > 0 && formatParameter.equals("x"))?"xml":"";
	}

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		Model subcategoryName = new Model(new SubcategoryNameStringDetachableModel(((Place)getModelObject()).getItem().getSubcategory().getId()));
		final String title = ((Place)getModelObject()).getItem().getName() + " - " +subcategoryName.getObject().toString()+ " - " + Constants.SITE_NAME; 
		return new Model(title);
	}

}//END OF FILE