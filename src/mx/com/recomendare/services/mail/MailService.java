/**
 * 
 */
package mx.com.recomendare.services.mail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.util.Constants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jerry
 *
 */
public final class MailService extends AbstractService {
	//the mail session...
	private Session session;
	//the path to the properties file...
	private String pathToMailPropertiesFile;
	
	//the log...
	private static final Log log = LogFactory.getLog(MailService.class);
	
	/**
	 * 
	 */
	public MailService(final String pathToMailPropertiesFile) {
		this.pathToMailPropertiesFile = pathToMailPropertiesFile;
		doStart();
	}
	
	//********************************
	//*********SERVICE***************
	//********************************
	/**
	 * 
	 */
	public void doStart() {
		log.info("Starting - "+getClass().getName()+"...");
		if(!started)  {
			java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			final Properties properties = new Properties();
			try {
				properties.load(new FileInputStream(pathToMailPropertiesFile));
				session = Session.getInstance(properties, new Authenticator(){
					protected PasswordAuthentication getPasswordAuthentication() {
						String username = properties.getProperty("mail.smtp.user");
						String password = properties.getProperty("mail.smtp.password");
						return new PasswordAuthentication(username, password);
					}
				});
				session.setDebug(true);
				started = true;
				log.info(getClass().getName()+"...started!");
			} catch (FileNotFoundException e) {
				log.error("Can't find the file in the path - "+pathToMailPropertiesFile+", reason - "+e.getMessage(), e);
			} catch (IOException e) {
				log.error("Can't read the file in the path - "+pathToMailPropertiesFile+", reason - "+e.getMessage(), e);
			}
		}
		else  {
			log.error(getClass().getName()+"...already started!");
		}
	}
	
	/**
	 * 
	 */
	public void doStop() {
		if(started)  {
			log.info("Stoping - "+getClass().getName()+"...");
			session = null;
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}

	/**
	 * 
	 * @param destinationAddress
	 * @param content
	 * @throws MessagingException
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public void sendMailTo(final String destinationAddress, final String content) throws MessagingException, IOException  {//TODO:save the recipient address for next marketing... 
		if(destinationAddress != null && destinationAddress.trim().length() > 0)  {
			Transport transport = session.getTransport();
			MimeMessage message = new MimeMessage(session);
			InternetAddress from = new InternetAddress(Constants.SITE_EMAIL_ADDRESS);
			InternetAddress to = new InternetAddress(destinationAddress);
			message.setFrom(from);
			message.addRecipient(Message.RecipientType.TO, to);
			message.setSubject(Constants.SITE_EMAIL_SUBJECT);			
			message.setDataHandler(new DataHandler(new ByteArrayDataSource(content, "text/html")));
			message.setHeader("X-Mailer", Constants.SITE_NAME);
			message.setSentDate(new Date());
			// send the thing off
			transport.connect();
			transport.send(message);
			transport.close();
		}
	}

}//END OF FILE