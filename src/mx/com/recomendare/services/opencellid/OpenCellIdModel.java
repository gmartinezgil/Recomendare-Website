/**
 * 
 */
package mx.com.recomendare.services.opencellid;

/**
 * @author jerry
 *
 */
public class OpenCellIdModel {
	private String cellId;
	private String lac;
	private String mcc;
	private String mnc;
	private String lat;
	private String lon;
	private String range;
	
	public String getCellId() {
		return cellId;
	}
	public void setCellId(String cellId) {
		this.cellId = cellId;
	}
	public String getLac() {
		return lac;
	}
	public void setLac(String lac) {
		this.lac = lac;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public String getMnc() {
		return mnc;
	}
	public void setMnc(String mnc) {
		this.mnc = mnc;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	
	public String toString()  {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(getClass().getName());
		sb.append("] - {cellId = '");
		sb.append(cellId);
		sb.append("', lac = '");
		sb.append(lac);
		sb.append("', mcc = '");
		sb.append(mcc);
		sb.append("', mnc = '");
		sb.append(mnc);
		sb.append("', lat = '");
		sb.append(lat);
		sb.append("', lon = '");
		sb.append(lon);
		sb.append("', range = '");
		sb.append(range);
		sb.append("'}");
		return sb.toString();
	}
	
}