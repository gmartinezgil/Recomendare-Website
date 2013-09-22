/**
 * 
 */
package mx.com.recomendare.services.flickr;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jerry
 *
 */
public final class PhotoModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String thumbnailUrl;
	private String mediumUrl;
	private String largeUrl;
	private float latitude;
	private float longitude;
	private Date datePosted;
	
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public String getMediumUrl() {
		return mediumUrl;
	}
	public void setMediumUrl(String mediumUrl) {
		this.mediumUrl = mediumUrl;
	}
	public String getLargeUrl() {
		return largeUrl;
	}
	public void setLargeUrl(String largeUrl) {
		this.largeUrl = largeUrl;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public Date getDatePosted() {
		return datePosted;
	}
	public void setDatePosted(Date datePosted) {
		this.datePosted = datePosted;
	} 
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(getClass().getName());
		sb.append("] - {title = '");
		sb.append(title);
		sb.append("', thumbnailUrl = '");
		sb.append(thumbnailUrl);
		sb.append("', mediumUrl = '");
		sb.append(mediumUrl);
		sb.append("', largeUrl = '");
		sb.append(largeUrl);
		sb.append("', latitude = '");
		sb.append(latitude);
		sb.append("', longitude = '");
		sb.append(longitude);
		sb.append("', datePosted = '");
		sb.append(datePosted);
		sb.append("'}");
		return sb.toString();
	}
	
}//END OF FILE