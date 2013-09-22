package mx.com.recomendare.web.commons.models;

import java.io.Serializable;

/**
 * 
 * @author jerry
 *
 */
public final class ImageModelImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	//the file name...
	private String clientFileName;
	//the content type of the image...
	private String contentType;
	//the actual content of the image...
	private byte[] imageBytes;
	/**
	 * 
	 * @param clientFileName
	 * @param contentType
	 * @param bytes
	 */
	public ImageModelImpl(String clientFileName, String contentType, byte[] imageBytes) {
		this.clientFileName = clientFileName;
		this.contentType = contentType;
		this.imageBytes = imageBytes;
	}
	public String getClientFileName() {
		return clientFileName;
	}
	public void setClientFileName(String clientFileName) {
		this.clientFileName = clientFileName;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public byte[] getImageBytes() {
		return imageBytes;
	}
	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(getClass().getName());
		sb.append("] - {clientFileName = '");
		sb.append(clientFileName);
		sb.append("', contentType = '");
		sb.append(contentType);
		sb.append("', imageBytes = '");
		sb.append(imageBytes);
		sb.append("'}");
		return sb.toString();
	}

}//END OF FILE