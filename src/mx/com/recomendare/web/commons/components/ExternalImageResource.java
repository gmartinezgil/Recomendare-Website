/**
 * 
 */
package mx.com.recomendare.web.commons.components;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.UrlResourceStream;

/**
 * @author jerry
 *
 */
public final class ExternalImageResource extends WebResource {
	private static final long serialVersionUID = 1L;

	private URL imageAdressURL;
	/**
	 * @throws MalformedURLException 
	 * 
	 */
	public ExternalImageResource(String url) throws MalformedURLException {
		imageAdressURL = new URL(url);
	}

	/* (non-Javadoc)
	 * @see wicket.Resource#getResourceStream()
	 */
	public IResourceStream getResourceStream() {
		return new UrlResourceStream(imageAdressURL);
	}

}
