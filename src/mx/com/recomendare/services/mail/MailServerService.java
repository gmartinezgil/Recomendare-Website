/**
 * 
 */
package mx.com.recomendare.services.mail;

import mx.com.recomendare.services.AbstractService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ericdaugherty.mail.server.configuration.ConfigurationManager;
import com.ericdaugherty.mail.server.services.general.ServiceListener;
import com.ericdaugherty.mail.server.services.pop3.Pop3Processor;
import com.ericdaugherty.mail.server.services.smtp.SMTPProcessor;
import com.ericdaugherty.mail.server.services.smtp.SMTPSender;

/**
 * @author jerry
 *
 */
public final class MailServerService extends AbstractService {
	//***************************************************************
    // Variables
    //***************************************************************

    //Threads

    private static ServiceListener popListener;
    private static ServiceListener smtpListener;
    private static SMTPSender smtpSender;
    
    /** The SMTP sender thread */
    private static Thread smtpSenderThread;

    /** Logger Category for this class.  This variable is initialized once
     * the main logging system has been initialized */
    //the log...
	private static final Log log = LogFactory.getLog(MailServerService.class);
	
	//the directory where to find it's configuration files...
	private static String directory;
	/**
	 * 
	 */
	@SuppressWarnings("static-access")
	public MailServerService(final String directory) {
		this.directory = directory;
		doStart();
	}

	/* (non-Javadoc)
	 * @see mx.com.recomendare.services.Service#doStart()
	 */
	public void doStart() {
		log.info("Starting - "+getClass().getName()+"...");
		if(!started)  {
			startup();
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
			shutdown();
			started = false;
			log.info(getClass().getName()+"...stoped!");
		}
	}
	
	/**
     * This method is the entrypoint to the system and is responsible
     * for the initial configuration of the application and the creation
     * of all 'service' threads.
     */
	private static void startup()  {
		// Perform the basic application startup.  If anything goes wrong here,
        // we need to abort the application.
        try {

        	// Initialize the Configuration Manager.
            ConfigurationManager configurationManager = ConfigurationManager.initialize( directory );

            //Start the threads.
            int port;
            int executeThreads = configurationManager.getExecuteThreadCount();

            //Start the Pop3 Thread.
            port = configurationManager.getPop3Port();
            if( log.isDebugEnabled() ) log.debug( "Starting POP3 Service on port: " + port );
            popListener = new ServiceListener( port, Pop3Processor.class, executeThreads );
            new Thread( popListener, "POP3" ).start();

            //Start SMTP Threads.
            port = configurationManager.getSmtpPort();
            if( log.isDebugEnabled() ) log.debug( "Starting SMTP Service on port: " + port );
            smtpListener = new ServiceListener( port, SMTPProcessor.class, executeThreads );
            new Thread( smtpListener, "SMTP" ).start();

            //Start the SMTPSender thread (This thread actually delivers the mail recieved
            //by the SMTP threads.
            smtpSender = new SMTPSender();
            smtpSenderThread = new Thread( smtpSender, "SMTPSender" );
            smtpSenderThread.start();
            
        }
        catch( RuntimeException runtimeException ) {
            System.err.println( "The application failed to initialize." );
            System.err.println( runtimeException.getMessage() );
            runtimeException.printStackTrace();
        }
	}
	
	/**
     * Provides a 'safe' way for the application to shut down.  It will attempt
     * to stop the running threads.  If the threads to not stop within 60 seconds,
     * the application will force the threads to stop by calling System.exit();
     */
    private static void shutdown() {

        log.warn( "Shutting down Mail Server.  Server will shut down in 60 seconds." );

        popListener.shutdown();
        smtpListener.shutdown();
        smtpSender.shutdown();

        try{
            smtpSenderThread.join(10000);
        }
        catch (InterruptedException ie)
        {
            log.error("Was interrupted while waiting for thread to die");
        }

        log.info("Thread gracefully terminated");
        smtpSenderThread = null;
    }

}//EDN OF FILE