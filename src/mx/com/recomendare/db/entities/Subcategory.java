package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

/**
 * Subcategory generated by hbm2java
 */
public class Subcategory implements java.io.Serializable {

	private int id;
	private Category category;
	private Set subcategoryDescriptions = new HashSet(0);
	private Set items = new HashSet(0);

	public Subcategory() {
	}

	public Subcategory(int id) {
		this.id = id;
	}

	public Subcategory(int id, Category category, Set subcategoryDescriptions,
			Set items) {
		this.id = id;
		this.category = category;
		this.subcategoryDescriptions = subcategoryDescriptions;
		this.items = items;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Set getSubcategoryDescriptions() {
		return this.subcategoryDescriptions;
	}

	public void setSubcategoryDescriptions(Set subcategoryDescriptions) {
		this.subcategoryDescriptions = subcategoryDescriptions;
	}

	public Set getItems() {
		return this.items;
	}

	public void setItems(Set items) {
		this.items = items;
	}

}
