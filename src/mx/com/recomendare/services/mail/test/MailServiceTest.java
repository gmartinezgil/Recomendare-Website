package mx.com.recomendare.services.mail.test;

import java.io.File;
import java.io.IOException;

import javax.mail.MessagingException;

import junit.framework.TestCase;
import mx.com.recomendare.services.mail.MailServerService;
import mx.com.recomendare.services.mail.MailService;

public class MailServiceTest extends TestCase {
	private MailService mailService;
	
	protected void setUp() throws Exception {
		File f = new File("."+File.separator+"build"+File.separator+"classes"+File.separator+"mx"+File.separator+"com"+File.separator+"recomendare"+File.separator+"services"+File.separator+"mail"+File.separator);
		System.out.println(f.getCanonicalPath());
		@SuppressWarnings("unused")
		MailServerService mailServerService = new MailServerService(f.getCanonicalPath());
		mailService = new MailService(f.getCanonicalPath() + File.separator + "mail.properties");
	}

	public void testSendMailTo() throws IOException, MessagingException {
		mailService.sendMailTo("emailaddress", "hola");
	}

}