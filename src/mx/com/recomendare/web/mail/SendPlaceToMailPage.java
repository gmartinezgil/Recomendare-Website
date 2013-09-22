/**
 * 
 */
package mx.com.recomendare.web.mail;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import mx.com.recomendare.db.entities.CategoryDescription;
import mx.com.recomendare.db.entities.CityName;
import mx.com.recomendare.db.entities.Contact;
import mx.com.recomendare.db.entities.CountryName;
import mx.com.recomendare.db.entities.SubcategoryDescription;
import mx.com.recomendare.util.Constants;
import mx.com.recomendare.web.Main;
import mx.com.recomendare.web.commons.models.detachables.ItemLocationDetachableModel;
import mx.com.recomendare.web.commons.models.detachables.LabelStringDetachableModel;
import mx.com.recomendare.web.places.ViewPlacePage;
import mx.com.recomendare.web.session.AuthenticatedPage;
import mx.com.recomendare.web.session.SignInSession;
import mx.com.recomendare.web.session.UserSessionModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.aetrion.flickr.places.Place;

/**
 * @author jerry
 *
 */
public final class SendPlaceToMailPage extends AuthenticatedPage {

	//the log...
    private static final Log log = LogFactory.getLog(SendPlaceToMailPage.class);

    //place id...
    private int placeId;
    //place name...
    private String placeName;
    
	/**
	 * @param parameters
	 */
	public SendPlaceToMailPage(PageParameters parameters) {
		super(parameters);
		placeName = parameters.getString("n");
		String placeKey = parameters.getString("i");
        if (placeKey != null && placeKey.trim().length() > 0) {
            placeId = Integer.parseInt(placeKey);
            init();
        } else {/*throws PlaceNotFoundException(placeKey);*//*setResponsePage(pageBack);*/
        }
	}
	
	private void init()  {
		//TITLE
		add(new Label("title", new LabelStringDetachableModel("SEND_PAGE_BY_MAIL")));
		//FORM
		add(new SendMailForm("sendMailForm", new CompoundPropertyModel(new MailModel())));
	}
	
	/**
	 * MailSendForm
	 *
	 */
	final class SendMailForm extends Form {
		private static final long serialVersionUID = 1L;

		/**
		 * @param id
		 * @param model
		 */
		public SendMailForm(String id, IModel model) {
			super(id, model);
			init();
		}
		
		private void init()  {
			// FORM
			add(new Label("emailDestinationAddressLabel", new LabelStringDetachableModel("DESTINATION_ADDRESS")));
			final RequiredTextField emailDestinationAddress = new RequiredTextField("emailDestinationAddress", new PropertyModel(getModel(), "emailDestinationAddress"));
			emailDestinationAddress.add(EmailAddressValidator.getInstance());
			add(emailDestinationAddress);
			final Button sendMailButton = new Button("sendMailButton", new LabelStringDetachableModel("SEND_MAIL")) {
				private static final long serialVersionUID = 1L;
				public void onSubmit() {
					MailModel mailModel = (MailModel)getForm().getModelObject();
					log.info(mailModel);
					try {
						IModel placeModel = new CompoundPropertyModel(new ItemLocationDetachableModel(placeId));
						((Main)getApplication()).getServices().getMailService().sendMailTo(mailModel.getEmailDestinationAddress(), getPageContent(placeModel));
						PageParameters parameters = new PageParameters();
						parameters.put("i", placeId);
						parameters.put("n", placeName);
						setResponsePage(ViewPlacePage.class, parameters);
					} catch (MessagingException e) {
						log.error("An error ocurred trying sending mail with the page with id = "+placeId+", reason = "+e.getMessage(), e);
					} catch (IOException e) {
						log.error("Can't connect with the mail server when trying sending the page with id = "+placeId+", reason = "+e.getMessage(), e);
					}
				}
			};
			add(sendMailButton);
			final Button cancelButton = new Button("cancelButton", new LabelStringDetachableModel("CANCEL")){
				private static final long serialVersionUID = 1L;
				public void onSubmit() {
					PageParameters parameters = new PageParameters();
					parameters.put("i", placeId);
					parameters.put("n", placeName);
					setResponsePage(ViewPlacePage.class, parameters);
				}
			};
			cancelButton.setDefaultFormProcessing(false);
			add(cancelButton);
		}

	}
	
	/**
	 * MailModel
	 *
	 */
	final class MailModel implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private String emailDestinationAddress;

		public String getEmailDestinationAddress() {
			return emailDestinationAddress;
		}

		public void setEmailDestinationAddress(String emailDestinationAddress) {
			this.emailDestinationAddress = emailDestinationAddress;
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			sb.append(getClass().getName());
			sb.append("] - {emailDestinationAddress = '");
			sb.append(emailDestinationAddress);
			sb.append("'}");
			return sb.toString();
		}
	}
	
	//
	private List getPageContentAsBodyPart(final IModel model)  {
		List bodiesPart = new ArrayList();
		BodyPart htmlBodyPart = new MimeBodyPart();
		final Place place = (Place) model.getObject();
		StringBuffer sb = new StringBuffer();
		//HTML
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		
		return bodiesPart;
	}
	
	private String getPageContent(final IModel model)  {
		Place place = (Place)model.getObject();
		StringBuffer sb = new StringBuffer();
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		//HEADER
		sb.append("<head>");
		sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />");
		sb.append("<title>");sb.append(Constants.SITE_NAME);sb.append(" - ");sb.append(Constants.SITE_SLOGAN);	sb.append("</title>");
		sb.append("<link href=\"");	sb.append(Constants.SITE_URL);sb.append("/default.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\"/>");
		sb.append("</head>");
		//BODY
		sb.append("<body>");
		
		//<!-- start header -->
		sb.append("<div id=\"header\">");
		sb.append("<div id=\"logo\">");
		sb.append("<h1><a href=\"");sb.append(Constants.SITE_URL);sb.append("/\">");sb.append(Constants.SITE_NAME);sb.append("</a></h1>");
		sb.append("<p>");sb.append(Constants.SITE_SLOGAN);sb.append("</p>");
		sb.append("</div>");
		sb.append("<div>");
		sb.append(new LabelStringDetachableModel("WELCOME"));sb.append(", ");
		sb.append("<a href=\"");sb.append(Constants.SITE_URL);sb.append("/login\">");sb.append(new LabelStringDetachableModel("LOGIN"));sb.append("</a>");
		sb.append("<a href=\"");sb.append(Constants.SITE_URL);sb.append("/register\">");sb.append(new LabelStringDetachableModel("REGISTER"));sb.append(" &gt;&gt;</a>");	
		sb.append("</div>");//<!-- <div id="rss"><a href="#" wicket:id="rssLink">Fuente RSS</a></div> -->
		sb.append("</div>");
		//<!-- end header -->
		//<!-- star menu -->
		sb.append("<div id=\"menu\">");
		sb.append("<ul>");
		sb.append("<li class=\"current_page_item\"><a href=\"");sb.append(Constants.SITE_URL);sb.append("/\">");sb.append(new LabelStringDetachableModel("HOME"));sb.append("</a></li>");
		sb.append("<li><a href=\"");sb.append(Constants.SITE_URL);sb.append("/add-place\">");sb.append(new LabelStringDetachableModel("ADD"));sb.append("</a></li>");
		sb.append("<li><a href=\"");sb.append(Constants.SITE_URL);sb.append("/login\">");sb.append(new LabelStringDetachableModel("LOGIN"));sb.append("</a></li>");
		sb.append("<li><a href=\"");sb.append(Constants.SITE_URL);sb.append("/search\">");sb.append(new LabelStringDetachableModel("SEARCH"));sb.append("</a></li>");
		sb.append("</ul>");
		sb.append("</div>");
		//	<!-- end menu -->
		
		//<!-- start page -->
		sb.append("<div id=\"page\">");
		
		//<!-- start ads -->
		sb.append("<div id=\"ads\">");
		sb.append("</div>");
		//<!-- end ads -->
		//<!-- start content -->
		sb.append("<div id=\"content\">");//TODO: apply i18n...in all the HTML...
		sb.append("<div class=\"post\">");
		sb.append("<div class=\"title\">");
		sb.append("<p align=\"left\">");
		sb.append("<a href=\"");sb.append(Constants.SITE_URL);sb.append("/country/i/");sb.append(place.getStreet().getCity().getCountry().getId());sb.append("/n/");sb.append(getCountryName(((SignInSession)getSession()).getUser(), place.getStreet().getCity().getCountry().getCountryNames()));sb.append("/\"><font size=\"-1\">");	sb.append(getCityName(((SignInSession)getSession()).getUser(), place.getStreet().getCity().getCityNames()));sb.append("</font></a> &gt; <a href=\"");sb.append(Constants.SITE_URL);sb.append("/city/i/");sb.append(place.getStreet().getCity().getId());sb.append("/n/");sb.append(getCityName(((SignInSession)getSession()).getUser(), place.getStreet().getCity().getCityNames()));sb.append("/\"><font size=\"-1\">");sb.append(getCityName(((SignInSession)getSession()).getUser(), place.getStreet().getCity().getCityNames()));sb.append("</font></a>");
		sb.append("</p>");
		sb.append("<p align=\"right\">");
		sb.append("<a href=\"");sb.append(Constants.SITE_URL);sb.append("/add-to-favorites/i/");sb.append(place.getItem().getId());sb.append("/\"><font size=\"-1\">Agregar a mis favoritos...</font><img src=\"");sb.append(Constants.SITE_URL);sb.append("/images/email_go.png\" /></a>");
		sb.append("</p>");
		sb.append("<h2>");sb.append(place.getItem().getName());sb.append("</h2>");
		sb.append("<p align=\"left\">");
		sb.append("<a href=\"");sb.append(Constants.SITE_URL);sb.append("/category/i/");sb.append(place.getItem().getSubcategory().getCategory().getId());sb.append("/n/");sb.append(getCategoryName(((SignInSession)getSession()).getUser(), place.getItem().getSubcategory().getCategory().getCategoryDescriptions()));sb.append("/\"><font size=\"-1\">");	sb.append(getCategoryName(((SignInSession)getSession()).getUser(), place.getItem().getSubcategory().getCategory().getCategoryDescriptions()));sb.append("</font></a> &gt; <a href=\"");sb.append(Constants.SITE_URL);sb.append("/subcategory/i/");sb.append(place.getItem().getSubcategory().getId());sb.append("/n/");sb.append(getSubcategoryName(((SignInSession)getSession()).getUser(), place.getItem().getSubcategory().getSubcategoryDescriptions()));sb.append("/\"><font size=\"-1\">");sb.append(getSubcategoryName(((SignInSession)getSession()).getUser(), place.getItem().getSubcategory().getSubcategoryDescriptions()));sb.append("</font></a>");
		sb.append("</p>");
		sb.append("</div>");
		sb.append("<div class=\"entry\">");
		sb.append("<h3>");
		short overallRating = 0;//DatabaseUtil.calculateOverallRating(place.getItem().getItemRatings());
		for(short i=0; i < 5; i++ )  {
			if(i < overallRating) {
				sb.append("<img src=\"");sb.append(Constants.SITE_URL);sb.append("/images/star1.gif\" />");
			}
			else {
				sb.append("<img src=\"");sb.append(Constants.SITE_URL);sb.append("/images/star0.gif\" />");
			}
		}
		sb.append(" - ");sb.append(place.getItem().getItemRatings().size());sb.append(" comentario(s).</h3>");
		//float minValue = (place.getItem().getItemPriceByMinPriceId() != null) ? place.getItem().getItemPriceByMinPriceId().getValue().floatValue() : 0F;
        //float maxValue = (place.getItem().getItemPriceByMaxPriceId() != null) ? place.getItem().getItemPriceByMaxPriceId().getValue().floatValue() : 0F;
		//sb.append("<h3>Precio - ");sb.append(Util.getCurrencyFormat(minValue, getLocation()));sb.append(" - ");sb.append(Util.getCurrencyFormat(maxValue, getLocation()));sb.append("</h3>");
		sb.append("<h3>Fotografias</h3>");
		sb.append("<img src=\"");sb.append(Constants.SITE_URL);sb.append("/images/questionmark.gif\" />");//TODO:have a method in the pictures DAO that gets the picture and set in the stream for these picture...like google map static image...
		sb.append("<a href=\"");sb.append(Constants.SITE_URL);sb.append("/place-images/i/");sb.append(place.getItem().getId());sb.append("/n/");sb.append(place.getItem().getName());sb.append("/\">mas &gt;&gt;</a>");
		sb.append("<p />");
		sb.append("<h3>Localizacion</h3>");
		sb.append("<center><table><tbody><tr>");
		sb.append("<td>");
		sb.append("<img src=\"http://maps.google.com/staticmap?center=");sb.append(place.getLocation().getLatitude().floatValue());sb.append(",");sb.append(place.getLocation().getLongitude().floatValue());sb.append("&zoom=");sb.append(Constants.STREET_VIEW_ZOOM_LEVEL);sb.append("&markers=");sb.append(place.getLocation().getLatitude().floatValue());sb.append(",");sb.append(place.getLocation().getLongitude().floatValue());sb.append(",red&size=");sb.append(255);sb.append("x");sb.append(300);sb.append("&key=");sb.append(Constants.GMAP_LOCALHOST_8080_KEY);sb.append("\" />");
		sb.append("</td>");
		sb.append("<td>");
		sb.append(place.getStreet().getName());sb.append(" # ");sb.append(place.getOutsideNumber());sb.append("<br />");
		sb.append(getCityName(((SignInSession)getSession()).getUser(), place.getStreet().getCity().getCityNames()));sb.append("<br />");
		sb.append(getCountryName(((SignInSession)getSession()).getUser(), place.getStreet().getCity().getCountry().getCountryNames()));sb.append("<br />");
		final String homePageAddress = (place.getItem().getContacts() != null && place.getItem().getContacts().size() > 0) ? ((Contact) place.getItem().getContacts().toArray()[0]).getHomepageAddress() : "";
		sb.append("<p><a href=\"");sb.append(homePageAddress);sb.append("\"><font size=\"-1\">Sitio Oficial</font></a></p>");
		sb.append("<p><a href=\"");sb.append(Constants.SITE_URL);sb.append("/nearby-places/i/");sb.append(place.getId());sb.append("/n/");sb.append(place.getItem().getName());sb.append("/\"><font size=\"-1\">Ver lugares cercanos...</font><img src=\"");sb.append(Constants.SITE_URL);sb.append("/images/map_go.png\" />\"</a></p>");
		sb.append("</td>");
		sb.append("</tr></tbody></table></center>");
		
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");
		//<!-- end content -->
		
		sb.append("</div>");
		
		sb.append("</body>");
		sb.append("</html>");
		
		/*
            <p>
            <h3><span wicket:id="placeDescriptionName">Descripcion</span></h3>
            <br />

            <span wicket:id="placeDescription">Buen restaurante ubicado en el nuevo centro comercial de parque Delta...</span>
            <p>
            <h3><span wicket:id="placeCommentsName">Comentarios</span></h3>
            <span wicket:id="comments"><wicket:panel>
    <p align="right">
        <a href="../../../../../?wicket:interface=:5:comments:addCommentLink::ILinkListener::" wicket:id="addCommentLink"><span wicket:id="addCommentLabel">Agregar comentario</span><img wicket:id="addPicture" src="../../../../../?wicket:interface=:5:comments:addCommentLink:addPicture::IResourceListener::"/></a>
    </p>
</wicket:panel></span>
            <table cellspacing="0" class="dataview">
                <tbody>
                    <tr wicket:id="placeComments">
                        <td>
                            <blockquote>
                                <a href="../../../../../?wicket:interface=:5:placeComments:1:userDetailLink::ILinkListener::" wicket:id="userDetailLink">
                                    <img wicket:id="placeCommentUserPicture" src="../../../../../?wicket:interface=:5:placeComments:1:userDetailLink:placeCommentUserPicture::IResourceListener::"/>
                                    <span wicket:id="placeCommentUserScreenName">Anton Ego</span>

                                </a> - <span wicket:id="placeCommentRating">***</span>
                                <br />
                                <span wicket:id="placeComment">Se veia bien por afuera, pero los alimentos no me gustaron mucho...</span>
                            </blockquote>
                        </td>
                    </tr>
                </tbody>

            </table>
            <p>			
            <div wicket:id="placeCommentsNavigator"><wicket:panel>
	<span wicket:id="first"><em>&lt;&lt;</em></span>&nbsp;<span wicket:id="prev"><em>&lt;</em></span>
    <span wicket:id="navigation">
		  <span wicket:id="pageLink"><em><span wicket:id="pageNumber">1</span></em></span>
    </span>
    <span wicket:id="next"><em>&gt;</em></span>&nbsp;<span wicket:id="last"><em>&gt;&gt;</em></span>
  </wicket:panel></div>

        </div>
    </div>
</wicket:extend></wicket:child>			
		</div>
		<!-- end content -->

			
		*/

		log.info(sb.toString());
		return sb.toString();
	}
	
	private String getCategoryName(final UserSessionModel user, final Set descriptions)  {
    	final Iterator iterator = descriptions.iterator();
    	while (iterator.hasNext()) {
			CategoryDescription description = (CategoryDescription) iterator.next();
			if(description.getLanguage().getCode().equals(user.getLocation().getLanguageCode()))  {
				return description.getName();
			}
		}
    	return null;
    }
	
	private String getSubcategoryName(final UserSessionModel user, final Set descriptions)  {
    	final Iterator iterator = descriptions.iterator();
    	while (iterator.hasNext()) {
			SubcategoryDescription description = (SubcategoryDescription) iterator.next();
			if(description.getLanguage().getCode().equals(user.getLocation().getLanguageCode()))  {
				return description.getName();
			}
		}
    	return null;
    }
	
	private String getCountryName(final UserSessionModel user, final Set names)  {
    	final Iterator iterator = names.iterator();
    	while (iterator.hasNext()) {
			CountryName name = (CountryName) iterator.next();
			if(name.getLanguage().getCode().equals(user.getLocation().getLanguageCode()))  {
				return name.getName();
			}
		}
    	return null;
    }
	
	private String getCityName(final UserSessionModel user, final Set names)  {
    	final Iterator iterator = names.iterator();
    	while (iterator.hasNext()) {
			CityName name = (CityName) iterator.next();
			if(name.getLanguage().getCode().equals(user.getLocation().getLanguageCode()))  {
				return name.getName();
			}
		}
    	return null;
    }

	/**
	 * 
	 */
	protected IModel getPageTitle() {
		return new Model(Constants.SITE_NAME + " - " +new LabelStringDetachableModel("SEND_BY_MAIL"));
	}

}//END OF FILE