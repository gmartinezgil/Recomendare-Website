package mx.com.recomendare.services.amazon;

import java.io.Serializable;

public final class AmazonItemModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String imageURL;
	private String ASIN;
	private String detailPageURL;
	private String author;
	private String manufacturer;
	private String title;

	public void setImageURL(String url) {
		this.imageURL = url;
	}

	public String getImageURL() {
		return imageURL;
	}

	public String getASIN() {
		return ASIN;
	}

	public void setASIN(String asin) {
		ASIN = asin;
	}

	public String getDetailPageURL() {
		return detailPageURL;
	}

	public void setDetailPageURL(String detailPageURL) {
		this.detailPageURL = detailPageURL;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}//END OF FILE