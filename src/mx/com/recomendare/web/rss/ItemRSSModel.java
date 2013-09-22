/**
 * 
 */
package mx.com.recomendare.web.rss;

import java.io.Serializable;

/**
 * @author jerry
 *
 */
public final class ItemRSSModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String resume;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResume() {
		return resume;
	}
	public void setResume(String resume) {
		this.resume = resume;
	}

}//END OF FILE