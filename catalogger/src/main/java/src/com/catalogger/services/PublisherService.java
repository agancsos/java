package com.catalogger.services;
import com.catalogger.models.Publisher;
import java.util.ArrayList;
import com.data.types.DataTable;
import com.data.types.DataRow;
import com.data.types.DataColumn;

public class PublisherService {
	private static PublisherService instance = null;
	private CacheService cacheService        = null;
	private DataService dbService            = null;

	private PublisherService() {
		this.cacheService = CacheService.getInstance();
		this.dbService    = DataService.getInstance();
	}

	public static PublisherService getInstance() {
		if (PublisherService.instance == null) {
			PublisherService.instance = new PublisherService();
		}
		return PublisherService.instance;
	}

	public boolean addPublisher(Publisher publisher) {
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM PUBLISHER WHERE PUBLISHER_NAME = '%s'", publisher.getLabel()));
		if (tab.getRows().size() > 0) {
			return true;
		}
		if (this.dbService.runServiceQuery(String.format("INSERT INTO PUBLISHERS (PUBLISHER_NAME, PUBLISHER_ADDRESS, PUBLISHER_CITY, PUBLISHER_STATE, PUBLISHER_COUNTRY," +
			 "PUBLISHER_ISBN, CREATED_DATE, LAST_UPDATED_DATE) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
			publisher.getLabel(), publisher.getAddress(), publisher.getCity(), publisher.getState(), publisher.getCountry(), publisher.getISBN()))) {
			this.cacheService.updatePublisherCache(publisher.getId(), publisher);
			return true;
		}
		return false;
	}

	public boolean modifyPublisher(Publisher publisher) {
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM PUBLISHERS WHERE PUBLISHER_ID = '%d'", publisher.getId()));
		if (tab.getRows().size() < 1) {
			return true;
		}
		if (this.dbService.runServiceQuery(String.format("UPDATE PUBLISHER SET PUBLISHER_NAME = '%s', PUBLISHER_ADDRESS = '%s', PUBLISHER_CITY = '%s', PUBLISHER_STATE = '%s', PUBLISHER_COUNTRY = '%s'," +
			"PUBLISHER_ISBN = '%s', LAST_UPDATED_DATE = CURRENT_TIMESTAMP WHERE PUBLISHER_ID = '%d'",
			publisher.getLabel(), publisher.getAddress(), publisher.getCity(), publisher.getState(), publisher.getCountry(), publisher.getISBN(), publisher.getId()))) {
			this.cacheService.updatePublisherCache(publisher.getId(), publisher);
			return true;
		}
		return false;
	}

	public boolean removePublisher(Publisher publisher) {
		if (this.dbService.runServiceQuery(String.format("DELETE FROM TITLE_HISTORY WHERE TITLE_COPY_ID IN (SELECT TITLE_COPY_ID FROM TITLE_COPY WHERE TITLE_ID IN (SELECT TITLE_ID FROM TITLES WHERE PUBLISHER_ID = '%d'))",
			publisher.getId()))) {
			if (this.dbService.runServiceQuery(String.format("DELETE FROM TITLE_COPY WHERE TITLE_ID IN (SELECT TITLE_ID FROM TITLES WHERE PUBLISHER_ID = '%d'", publisher.getId()))) {
				if (this.dbService.runServiceQuery(String.format("DELETE FROM TITLES WHERE PUBLISHER_ID = '%d'", publisher.getId()))) {
					return this.dbService.runServiceQuery(String.format("DELETE FROM PUBLISHERS WHERE PUBLISHER_ID = '%d'", publisher.getId()));
				}
			}
		}
		return false;	
	}

	public Publisher getPublisher(int id, boolean useCache) {
		if (useCache && this.cacheService.containsPublisher(id)) {
			return this.cacheService.getPublisher(id);
		}
		Publisher rst = null;
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM PUBLISHERS WHERE PUBLISHER_ID = '%d'", id));
		if (tab.getRows().size() > 0) {
			DataRow row = tab.getRows().get(0);
			rst = new Publisher();
			rst.setId(id);
			rst.setLabel(String.format("%s", row.getColumn("publisher_name").getColumnValue()));
			rst.setAddress(row.getColumn("publisher_address").getColumnValue());
			rst.setCity(row.getColumn("publisher_city").getColumnValue());
			rst.setState(row.getColumn("publisher_state").getColumnValue());
			rst.setCountry(row.getColumn("publisher_country").getColumnValue());
			rst.setISBN(row.getColumn("publisher_isbn").getColumnValue());
			rst.setLastUpdatedDate(row.getColumn("last_updated_date").getColumnValue());
			this.cacheService.updatePublisherCache(id, rst);
		}
		return rst;
	}

	public Publisher[] getPublishers(boolean useCache) {
		ArrayList<Publisher> rst = new ArrayList<Publisher>();
		DataTable tab = this.dbService.serviceQuery("SELECT * FROM PUBLISHERS");
		for (DataRow row : tab.getRows()) {
			rst.add(this.getPublisher(Integer.parseInt(row.getColumn("publisher_id").getColumnValue()), useCache));
		}
		return rst.toArray(new Publisher[rst.size()]);
	}
}

