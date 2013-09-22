package mx.com.recomendare.db.entities;

// Generated May 13, 2009 4:44:37 PM by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

/**
 * Item generated by hbm2java
 */
public class Item implements java.io.Serializable {

	private int id;
	private ItemPrice itemPriceByMinPriceId;
	private ItemContact itemContact;
	private ItemLocation itemLocation;
	private ItemSubmittedBy itemSubmittedBy;
	private ItemPrice itemPriceByMaxPriceId;
	private Subcategory subcategory;
	private String name;
	private Set itemPictures = new HashSet(0);
	private Set userFavorites = new HashSet(0);
	private Set advertisedItems = new HashSet(0);
	private Set recommenderDiffsForItemIdA = new HashSet(0);
	private Set itemComments = new HashSet(0);
	private Set itemResumes = new HashSet(0);
	private Set itemHistorics = new HashSet(0);
	private Set recommenderDiffsForItemIdB = new HashSet(0);
	private Set itemAttributes = new HashSet(0);

	public Item() {
	}

	public Item(int id) {
		this.id = id;
	}

	public Item(int id, ItemPrice itemPriceByMinPriceId,
			ItemContact itemContact, ItemLocation itemLocation,
			ItemSubmittedBy itemSubmittedBy, ItemPrice itemPriceByMaxPriceId,
			Subcategory subcategory, String name, Set itemPictures,
			Set userFavorites, Set advertisedItems,
			Set recommenderDiffsForItemIdA, Set itemComments, Set itemResumes,
			Set itemHistorics, Set recommenderDiffsForItemIdB,
			Set itemAttributes) {
		this.id = id;
		this.itemPriceByMinPriceId = itemPriceByMinPriceId;
		this.itemContact = itemContact;
		this.itemLocation = itemLocation;
		this.itemSubmittedBy = itemSubmittedBy;
		this.itemPriceByMaxPriceId = itemPriceByMaxPriceId;
		this.subcategory = subcategory;
		this.name = name;
		this.itemPictures = itemPictures;
		this.userFavorites = userFavorites;
		this.advertisedItems = advertisedItems;
		this.recommenderDiffsForItemIdA = recommenderDiffsForItemIdA;
		this.itemComments = itemComments;
		this.itemResumes = itemResumes;
		this.itemHistorics = itemHistorics;
		this.recommenderDiffsForItemIdB = recommenderDiffsForItemIdB;
		this.itemAttributes = itemAttributes;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ItemPrice getItemPriceByMinPriceId() {
		return this.itemPriceByMinPriceId;
	}

	public void setItemPriceByMinPriceId(ItemPrice itemPriceByMinPriceId) {
		this.itemPriceByMinPriceId = itemPriceByMinPriceId;
	}

	public ItemContact getItemContact() {
		return this.itemContact;
	}

	public void setItemContact(ItemContact itemContact) {
		this.itemContact = itemContact;
	}

	public ItemLocation getItemLocation() {
		return this.itemLocation;
	}

	public void setItemLocation(ItemLocation itemLocation) {
		this.itemLocation = itemLocation;
	}

	public ItemSubmittedBy getItemSubmittedBy() {
		return this.itemSubmittedBy;
	}

	public void setItemSubmittedBy(ItemSubmittedBy itemSubmittedBy) {
		this.itemSubmittedBy = itemSubmittedBy;
	}

	public ItemPrice getItemPriceByMaxPriceId() {
		return this.itemPriceByMaxPriceId;
	}

	public void setItemPriceByMaxPriceId(ItemPrice itemPriceByMaxPriceId) {
		this.itemPriceByMaxPriceId = itemPriceByMaxPriceId;
	}

	public Subcategory getSubcategory() {
		return this.subcategory;
	}

	public void setSubcategory(Subcategory subcategory) {
		this.subcategory = subcategory;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set getItemPictures() {
		return this.itemPictures;
	}

	public void setItemPictures(Set itemPictures) {
		this.itemPictures = itemPictures;
	}

	public Set getUserFavorites() {
		return this.userFavorites;
	}

	public void setUserFavorites(Set userFavorites) {
		this.userFavorites = userFavorites;
	}

	public Set getAdvertisedItems() {
		return this.advertisedItems;
	}

	public void setAdvertisedItems(Set advertisedItems) {
		this.advertisedItems = advertisedItems;
	}

	public Set getRecommenderDiffsForItemIdA() {
		return this.recommenderDiffsForItemIdA;
	}

	public void setRecommenderDiffsForItemIdA(Set recommenderDiffsForItemIdA) {
		this.recommenderDiffsForItemIdA = recommenderDiffsForItemIdA;
	}

	public Set getItemComments() {
		return this.itemComments;
	}

	public void setItemComments(Set itemComments) {
		this.itemComments = itemComments;
	}

	public Set getItemResumes() {
		return this.itemResumes;
	}

	public void setItemResumes(Set itemResumes) {
		this.itemResumes = itemResumes;
	}

	public Set getItemHistorics() {
		return this.itemHistorics;
	}

	public void setItemHistorics(Set itemHistorics) {
		this.itemHistorics = itemHistorics;
	}

	public Set getRecommenderDiffsForItemIdB() {
		return this.recommenderDiffsForItemIdB;
	}

	public void setRecommenderDiffsForItemIdB(Set recommenderDiffsForItemIdB) {
		this.recommenderDiffsForItemIdB = recommenderDiffsForItemIdB;
	}

	public Set getItemAttributes() {
		return this.itemAttributes;
	}

	public void setItemAttributes(Set itemAttributes) {
		this.itemAttributes = itemAttributes;
	}

}
