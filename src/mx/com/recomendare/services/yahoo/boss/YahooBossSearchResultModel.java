/**
 * 
 */
package mx.com.recomendare.services.yahoo.boss;

import java.io.Serializable;

/**
 * @author jerry
 *
 */
public final class YahooBossSearchResultModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String description;
	private String clickurl;
	private String date;
	private String dispurl;
	private String title;
	private String url;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getClickurl() {
		return clickurl;
	}
	public void setClickurl(String clickurl) {
		this.clickurl = clickurl;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDispurl() {
		return dispurl;
	}
	public void setDispurl(String dispurl) {
		this.dispurl = dispurl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}//END OF FILE