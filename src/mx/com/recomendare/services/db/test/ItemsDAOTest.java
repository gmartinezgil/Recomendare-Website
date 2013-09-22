package mx.com.recomendare.services.db.test;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import mx.com.recomendare.db.entities.Item;
import mx.com.recomendare.services.db.DatabaseService;
import mx.com.recomendare.services.db.ItemsDAO;

import org.apache.lucene.queryParser.ParseException;

public class ItemsDAOTest extends TestCase {
	private DatabaseService databaseService;
	
	protected void setUp() throws Exception {
		databaseService = new DatabaseService();
	}

	/*
	public void testCreateNewIndexItemResumes() {
		ItemsDAO itemsDAO = databaseService.getItemsDAO();
		itemsDAO.setSession(databaseService.getSessionFactory().openSession());
		itemsDAO.createNewIndexItemResumes();
	}
	*/

	/*
	public void testSearchIndexItemResumes() throws ParseException  {
		ItemsDAO itemsDAO = databaseService.getItemsDAO();
		itemsDAO.setSession(databaseService.getSessionFactory().openSession());
		List results = itemsDAO.searchIndexItemResumes("hotel", "es");
		Iterator iterator = results.iterator();
		while (iterator.hasNext()) {
			ItemResume resume = (ItemResume) iterator.next();
			if(resume.getLanguage().getCode().equals("es"))  {
				System.out.println(resume.getResume());
			}
		}
	}
	*/

	/*
	public void testCreateNewIndexItemNames() {
		ItemsDAO itemsDAO = databaseService.getItemsDAO();
		itemsDAO.setSession(databaseService.getSessionFactory().openSession());
		itemsDAO.createNewIndexItemNames();
	}
	*/
	
	/*
	public void testSearchIndexItemNames() throws ParseException  {
		ItemsDAO itemsDAO = databaseService.getItemsDAO();
		itemsDAO.setSession(databaseService.getSessionFactory().openSession());
		List results = itemsDAO.searchIndexItemNames("Sacks");
		Iterator iterator = results.iterator();
		while (iterator.hasNext()) {
			Item name = (Item) iterator.next();
			System.out.println(name.getId() + " - " +name.getName());
		}
	}
	*/
	
	@SuppressWarnings("static-access")
	public void testSearchIndexOnItems() throws ParseException  {
		ItemsDAO itemsDAO = databaseService.getItemsDAO();
		itemsDAO.setSession(databaseService.getSessionFactory().openSession());
		List<Object> results = itemsDAO.searchKeywordOnItems("Sacks", "es");
		Iterator<Object> iterator = results.iterator();
		while (iterator.hasNext()) {
			Item name = (Item) iterator.next();
			System.out.println(name.getId() + " - " +name.getName());
		}
	}
	
}