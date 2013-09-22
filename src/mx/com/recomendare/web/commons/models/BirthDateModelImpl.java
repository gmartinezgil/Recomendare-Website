/**
 * 
 */
package mx.com.recomendare.web.commons.models;

/**
 * @author jerry
 *
 */
public final class BirthDateModelImpl implements BirthDateModel {
	private static final long serialVersionUID = 1L;
	
	private short day;
	private short month;
	private short year;
	public short getDay() {
		return day;
	}
	public void setDay(short day) {
		this.day = day;
	}
	public short getMonth() {
		return month;
	}
	public void setMonth(short month) {
		this.month = month;
	}
	public short getYear() {
		return year;
	}
	public void setYear(short year) {
		this.year = year;
	}

}