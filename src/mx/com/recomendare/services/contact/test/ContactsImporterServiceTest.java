package mx.com.recomendare.services.contact.test;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import mx.com.recomendare.services.contact.ContactsImporterService;
import mx.com.recomendare.web.commons.models.UserModel;

public class ContactsImporterServiceTest extends TestCase {
	private ContactsImporterService service;
	
	protected void setUp() throws Exception {
		service = new ContactsImporterService();
	}

	public void testGetContactsFromYahooMail() {
		//List<UserModel> contacts = service.getContactsFromMailService("EMAILADDRESS", "PASSWORD", ContactsImporterService.YAHOO_MAIL);
		List<UserModel> contacts = service.getContactsFromMailService("EMAILADDRESS", "PASSWORD", ContactsImporterService.GMAIL);
		//List<Contact> contacts = service.getContactsFromMailService("EMAILADDRESS", "PASSWORD", ContactsImporterService.HOTMAIL);
		if(contacts != null)  {
			Iterator<UserModel> iterator = contacts.iterator();
			while (iterator.hasNext()) {
				UserModel contact = (UserModel) iterator.next();
				System.out.println(contact.getName() + " - " + contact.getScreenName() + " - " + contact.getAddress().getEmail());
			}
		}
	}

}