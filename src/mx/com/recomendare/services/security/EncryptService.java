/**
 * 
 */
package mx.com.recomendare.services.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import mx.com.recomendare.services.AbstractService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Encoder;

/**
 * @author jerry
 *
 */
public final class EncryptService extends AbstractService {
	//the log...
	private static final Log log = LogFactory.getLog(EncryptService.class);
	/**
	 * 
	 */
	public EncryptService() {
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
			started = true;
			log.info(getClass().getName()+"...started!");
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
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}

	/**
	 * Encrypt the text given and returns the text encrypted...
	 * @param plaintext
	 * @return
	 * @throws EncryptException
	 */
	public String encrypt(final String plaintext) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(plaintext.getBytes("UTF-8"));
			byte raw[] = md.digest();
			String hash = (new BASE64Encoder()).encode(raw);
			return hash;
		}
		catch(NoSuchAlgorithmException e) {
			log.error("Encription Algorithm not founded, reason - "+e.getMessage(), e);
		}
		catch(UnsupportedEncodingException e) {
			log.error("Encoding not supported, reason - "+e.getMessage(), e);
		}
		return null;
	}

}//END OF FILE