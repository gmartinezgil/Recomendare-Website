/**
 * 
 */
package mx.com.recomendare.services.iata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import mx.com.recomendare.services.AbstractService;
import mx.com.recomendare.services.cache.CacheService;
import net.sf.ehcache.CacheException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.csvreader.CsvReader;

/**
 * @author jerry
 *
 */
public final class IATAService extends AbstractService {
	//the log...
	private static final Log log = LogFactory.getLog(IATAService.class);
	//the cache...
	private CacheService cacheService;
	//the path to the file...
	private String pathToIATACodesFile;
	//the CSV reader...
	private CsvReader reader;
	//
	private String fileContents;
	/**
	 * 
	 */
	public IATAService(final String pathToIATACodesFile) {
		this.pathToIATACodesFile = pathToIATACodesFile;
		doStart();
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.Service#doStart()
	 */
	public void doStart() {
		log.info("Starting - "+getClass().getName()+"...");
		if(!started)  {
			try {
				readFile();
				reader = new CsvReader(pathToIATACodesFile);
				reader.readHeaders();
				cacheService = new CacheService("iataservice");
				started = true;
				log.info(getClass().getName()+"...started!");
			}
			catch (FileNotFoundException e) {
				log.error("Can't find the IATA file in - "+pathToIATACodesFile+", reason - "+e.getMessage(), e);
			}
			catch (IOException e) {
				log.error("Can't read the IATA file at - "+pathToIATACodesFile+", reason - "+e.getMessage(), e);
			}
			catch (CacheException e) {
				log.error("Can't make use of the cache service - "+e.getMessage(), e);
			} 
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
			reader.close();
			reader = null;
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}
	
	/**
	 * 
	 * @param cityName
	 * @return
	 */
	public String getCodeFromCityName(final String cityName)  {
		String iataCode = (String)cacheService.getFromCache(cityName);
		if(iataCode != null)  {
			return iataCode;
		}
		else  {
			try {
				reader = CsvReader.parse(fileContents);
				reader.readHeaders();
				while(reader.readRecord())  {
					String iataCityName = reader.get("CITY");
					if(iataCityName.startsWith(cityName))  {
						iataCode = reader.get("CODE");
						log.info(cityName+" - "+"("+reader.getCurrentRecord()+") - "+iataCode);
						cacheService.addToCache(cityName, iataCode);
						return iataCode;
					}
				}
			} catch (IOException e) {
				log.error("Can't read the IATA file at - "+pathToIATACodesFile+", reason - "+e.getMessage(), e);
			}
			return null;
		}
	}
	
	//read the contents of the file and save in memory...
	private void readFile()  {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(pathToIATACodesFile)));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while((line = reader.readLine()) != null)  {
				sb.append(line);
				sb.append('\n');
			}
			fileContents = sb.toString();
		} catch (FileNotFoundException e) {
			log.error("Can't find the IATA file in - "+pathToIATACodesFile+", reason - "+e.getMessage(), e);
		} catch (IOException e) {
			log.error("Can't read the IATA file at - "+pathToIATACodesFile+", reason - "+e.getMessage(), e);
		}
	}

}//END OF FILE