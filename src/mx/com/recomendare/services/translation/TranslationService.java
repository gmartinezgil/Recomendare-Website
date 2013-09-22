/**
 * 
 */
package mx.com.recomendare.services.translation;

import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.util.Constants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.api.translate.Language;
import com.google.api.translate.Translate;

/**
 * @author jerry
 *
 */
public final class TranslationService extends AbstractService {
    //the log...
    private static final Log log = LogFactory.getLog(TranslationService.class);
    
    /**
     * 
     */
    public TranslationService()  {
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
			Translate.setHttpReferrer(Constants.SITE_URL);
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
	 * 
	 * @param textToTranslate
	 * @param fromLanguage
	 * @param toLanguage
	 * @return
	 */
	public String translateTo(final String textToTranslate, final String fromLanguageCode, final String toLanguageCode)  {
		if(textToTranslate != null && textToTranslate.trim().length() > 0 && !fromLanguageCode.equals(toLanguageCode))  {
			Language fromLanguage = null;
			if(fromLanguageCode.equals("es")) fromLanguage = Language.SPANISH;
			if(fromLanguageCode.equals("en")) fromLanguage = Language.ENGLISH;
			if(fromLanguageCode.equals("fr")) fromLanguage = Language.FRENCH;
			if(fromLanguageCode.equals("pt")) fromLanguage = Language.PORTUGUESE;
			Language toLanguage = null;
			if(toLanguageCode.equals("es")) toLanguage = Language.SPANISH;
			if(toLanguageCode.equals("en")) toLanguage = Language.ENGLISH;
			if(toLanguageCode.equals("fr")) toLanguage = Language.FRENCH;
			if(toLanguageCode.equals("pt")) toLanguage = Language.PORTUGUESE;
			try {
				return Translate.execute(textToTranslate, fromLanguage, toLanguage);
			} catch (Exception e) {
				log.error("Can't translate the text '"+textToTranslate+"', reason - "+e.getMessage(), e);
				return textToTranslate;
			}
		}
		return textToTranslate;
	}

}//END OF FILE