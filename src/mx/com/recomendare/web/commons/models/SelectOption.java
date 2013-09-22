package mx.com.recomendare.web.commons.models;

import java.io.Serializable;

public final class SelectOption implements Serializable {
	private static final long serialVersionUID = 1L;
	private String key;
	private String value;

	public SelectOption(String key, String value) {
	    this.key = key;
	    this.value = value;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(getClass().getName());
		sb.append("] - {key = '");
		sb.append(key);
		sb.append("', value = '");
		sb.append(value);
		sb.append("'}");
		return sb.toString();
	}
	
}//END OF FILE