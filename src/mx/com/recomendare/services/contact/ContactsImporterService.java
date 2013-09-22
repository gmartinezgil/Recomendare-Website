/**
 * 
 */
package mx.com.recomendare.services.contact;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.web.commons.models.AddressModelImpl;
import mx.com.recomendare.web.commons.models.UserModel;
import mx.com.recomendare.web.commons.models.UserModelImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xdatasystem.contactsimporter.ContactListImporter;
import com.xdatasystem.contactsimporter.ContactListImporterException;
import com.xdatasystem.contactsimporter.ContactListImporterFactory;
import com.xdatasystem.user.Contact;

/**
 * @author jerry
 *
 */
public final class ContactsImporterService extends AbstractService {
	//the log...
    private static final Log log = LogFactory.getLog(ContactsImporterService.class);
	
    //the importer
	private ContactListImporter importer;
	
	//the supported types of mail services...
	public static final short YAHOO_MAIL = 0;
	public static final short HOTMAIL = 1;
	public static final short GMAIL = 2;
	
	public ContactsImporterService()  {
		doStart();
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.Service#doStart()
	 */
	public void doStart() {
		log.info("Starting - "+getClass().getName()+"...");
		if(!started)  {
			started = true;
			log.info(getClass().getName()+"...started!");
		}
		else  {
			log.error(getClass().getName()+"...already started!");
		}
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.Service#doStop()
	 */
	public void doStop() {
		if(started)  {
			log.info("Stoping - "+getClass().getName()+"...");
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}

	/**
	 * 
	 * @param email
	 * @param password
	 * @param service
	 * @return
	 */
	public List<UserModel> getContactsFromMailService(final String email, final String password, final short service)  {
		switch (service) {
			case YAHOO_MAIL:
						importer = ContactListImporterFactory.yahoo(email, password);
						break;
			case HOTMAIL:
						importer = ContactListImporterFactory.hotmail(email, password);
						break;
			case GMAIL:
						importer = ContactListImporterFactory.gmail(email, password);
						break;
		}
		try {
			final List<Contact> contactsFounded = importer.getContactList();
			if(contactsFounded != null && contactsFounded.size() > 0)  {
				List<UserModel> contacts = new ArrayList<UserModel>(contactsFounded.size());
				final Iterator<Contact> iterator = contactsFounded.iterator();
				while (iterator.hasNext()) {
					final Contact contact = (Contact) iterator.next();
					UserModelImpl user = new UserModelImpl();
					user.setName(contact.getName());
					user.setScreenName(contact.getGeneratedName());
					AddressModelImpl address = new AddressModelImpl();
					address.setEmail(contact.getEmailAddress());
					user.setAddress(address);
					contacts.add(user);
				}
				return contacts;
			}
		} catch (ContactListImporterException e) {
			log.error("Error ocurred trying to import contacts from - "+email+", reason - "+e.getMessage(), e);
		}
		return null;
	}

}//END OF FILE