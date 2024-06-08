package com.catalogger.services;
import com.catalogger.models.Customer;
import java.util.ArrayList;
import com.data.types.DataTable;
import com.data.types.DataRow;
import com.data.types.DataColumn;

public class CustomerService {
	private static CustomerService instance  = null;
	private CacheService cacheService        = null;
	private DataService dbService            = null;

	private CustomerService() {
		this.cacheService = CacheService.getInstance();
		this.dbService    = DataService.getInstance();
	}

	public static CustomerService getInstance() {
		if (CustomerService.instance == null) {
			CustomerService.instance = new CustomerService();
		}
		return CustomerService.instance;
	}

	public boolean addCustomer(Customer customer) {
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM CUSTOMERS WHERE CUSTOMER_FIRSTNAME = '%s' AND CUSTOMER_LASTNAME", customer.getFirstName(), customer.getLastName()));
		if (tab.getRows().size() > 0) {
			return true;
		}
		if (this.dbService.runServiceQuery(String.format("INSERT INTO CUSTOMERS (CUSTOMER_FIRSTNAME, CUSTOMER_LASTNAME, CUSTOMER_ADDRESS, CUSTOMER_CITY, CUSTOMER_STATE, CUSTOMER_COUNTRY," +
			 "CUSTOMER_ISBN, CREATED_DATE, LAST_UPDATED_DATE) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
			customer.getFirstName(), customer.getLastName(), customer.getAddress(), customer.getCity(), customer.getState(), customer.getCountry(), customer.getISBN()))) {
			return true;
		}
		return false;
	}

	public boolean modifyCustomer(Customer customer) {
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM CUSTOMERS WHERE CUSTOMER_ID = '%d'", customer.getId()));
		if (tab.getRows().size() < 1) {
			return true;
		}
		if (this.dbService.runServiceQuery(String.format("UPDATE CUSTOMER SET CUSTOMER_FIRSTNAME = '%s', CUSTOMER_LASTNAME = '%s', CUSTOMER_ADDRESS = '%s', CUSTOMER_CITY = '%s', CUSTOMER_STATE = '%s', CUSTOMER_COUNTRY = '%s'," +
			"CUSTOMER_ISBN = '%s', LAST_UPDATED_DATE = CURRENT_TIMESTAMP WHERE CUSTOMER_ID = '%d'",
			customer.getFirstName(), customer.getLastName(), customer.getAddress(), customer.getCity(), customer.getState(), customer.getCountry(), customer.getISBN(), customer.getId()))) {
			return true;
		}
		return false;
	}

	public boolean removeCustomer(Customer customer) {
		if (this.dbService.runServiceQuery(String.format("DELETE FROM TITLE_HISTORY WHERE TITLE_COPY_ID IN (SELECT TITLE_COPY_ID FROM TITLE_COPY WHERE TITLE_ID IN (SELECT TITLE_ID FROM TITLES WHERE CUSTOMER_ID = '%d'))",
			customer.getId()))) {
			if (this.dbService.runServiceQuery(String.format("DELETE FROM TITLE_COPY WHERE TITLE_ID IN (SELECT TITLE_ID FROM TITLES WHERE CUSTOMER_ID = '%d'", customer.getId()))) {
				if (this.dbService.runServiceQuery(String.format("DELETE FROM TITLES WHERE CUSTOMER_ID = '%d'", customer.getId()))) {
					return this.dbService.runServiceQuery(String.format("DELETE FROM CUSTOMERS WHERE CUSTOMER_ID = '%d'", customer.getId()));
				}
			}
		}
		return false;	
	}

	public Customer getCustomer(int id, boolean useCache) {
		Customer rst = null;
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM CUSTOMERS WHERE CUSTOMER_ID = '%d'", id));
		if (tab.getRows().size() > 0) {
			DataRow row = tab.getRows().get(0);
			rst = new Customer();
			rst.setId(id);
			rst.setFirstName(row.getColumn("customer_firstname").getColumnValue());
			rst.setLastName(row.getColumn("customer_lastname").getColumnValue());
			rst.setLabel(String.format("%s %s", rst.getFirstName(), rst.getLastName()));
			rst.setAddress(row.getColumn("customer_address").getColumnValue());
			rst.setCity(row.getColumn("customer_city").getColumnValue());
			rst.setState(row.getColumn("customer_state").getColumnValue());
			rst.setCountry(row.getColumn("customer_country").getColumnValue());
			rst.setISBN(row.getColumn("customer_isbn").getColumnValue());
			rst.setLastUpdatedDate(row.getColumn("last_updated_date").getColumnValue());
		}
		return rst;
	}

	public Customer[] getCustomers(boolean useCache) {
		ArrayList<Customer> rst = new ArrayList<Customer>();
		DataTable tab = this.dbService.serviceQuery("SELECT * FROM CUSTOMERS");
		for (DataRow row : tab.getRows()) {
			rst.add(this.getCustomer(Integer.parseInt(row.getColumn("customer_id").getColumnValue()), useCache));
		}
		return rst.toArray(new Customer[rst.size()]);
	}
}

