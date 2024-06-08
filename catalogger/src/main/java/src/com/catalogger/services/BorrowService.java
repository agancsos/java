package com.catalogger.services;
import com.catalogger.models.Borrow;
import com.catalogger.models.User;
import com.catalogger.models.Customer;
import com.catalogger.models.Title;
import java.util.ArrayList;
import java.util.HashMap;
import com.data.types.DataTable;
import com.data.types.DataRow;
import com.data.types.DataColumn;

public class BorrowService {
	private static BorrowService instance = null;
	private DataService dbService         = null;
	private TitleService titleService     = null;
	private CustomerService custService   = null;
	private ConfigurationService config   = null;
	private int lateDays                  = 30;

	private BorrowService() {
		this.dbService      = DataService.getInstance();
		this.titleService   = TitleService.getInstance();
		this.custService    = CustomerService.getInstance();
		this.config         = ConfigurationService.getInstance("");
		this.lateDays       = (int)this.config.getProperty("lateDays", 30);
	}

	public static BorrowService getInstance() {
		if (BorrowService.instance == null) {
			BorrowService.instance = new BorrowService();
		}
		return BorrowService.instance;
	}

	public Borrow getBorrow(int id) {
		Borrow rst    = null;
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT * FROM TITLE_HISTORY WHERE TITLE_HISTORY_ID = '%d'", id));
		if (tab.getRows().size() > 0) {
			DataRow row = tab.getRows().get(0);
			rst         = new Borrow();
			rst.setId(id);
			rst.setCustomer(this.custService.getCustomer(Integer.parseInt(row.getColumn("customer_id").getColumnValue()), false));
			rst.setTitleCopy(this.titleService.getTitleCopy(Integer.parseInt(row.getColumn("title_copy_id").getColumnValue())));
			rst.setCheckoutDate(row.getColumn("checkout_date").getColumnValue());
			rst.setCheckinDate(row.getColumn("checkin_date").getColumnValue());
			rst.setCreatedDate(row.getColumn("checkout_date").getColumnValue());
			rst.setLastUpdatedDate(row.getColumn("last_updated_date").getColumnValue());
			rst.setLate((Integer.parseInt(row.getColumn("title_history_islate").getColumnValue()) > 0 ? true : false));
			rst.setPaid((Integer.parseInt(row.getColumn("title_history_paid").getColumnValue()) > 0 ? true : false));
			rst.setState(Integer.parseInt(row.getColumn("title_history_state").getColumnValue()));
			rst.setLastUpdatedBy(new User());
			rst.getLastUpdatedBy().setId(Integer.parseInt(row.getColumn("user_id").getColumnValue()));
			rst.setLabel(String.format("%d-%s-%d", rst.getCustomer().getId(), rst.getCheckoutDate(), rst.getTitleCopy().getId()));
		}
		return rst; 
	}

	public Borrow[] getBorrows(Customer customer) {
		ArrayList<Borrow> rst = new ArrayList<Borrow>();
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT TITLE_HISTORY_ID FROM TITLE_HISTORY WHERE CUSTOMER_ID = '%d'", customer.getId()));
		for (DataRow row : tab.getRows()) {
			rst.add(this.getBorrow(Integer.parseInt(row.getColumn("title_history_id").getColumnValue())));
		}
		return rst.toArray(new Borrow[rst.size()]);
	}

	public Borrow[] getBorrowsByTitle(Title title) {
		ArrayList<Borrow> rst = new ArrayList<Borrow>();
        DataTable tab = this.dbService.serviceQuery(String.format("SELECT TITLE_HISTORY_ID FROM TITLE_HISTORY WHERE TITLE_ID = '%d'", title.getId()));
        for (DataRow row : tab.getRows()) {
            rst.add(this.getBorrow(Integer.parseInt(row.getColumn("title_history_id").getColumnValue())));
        }
        return rst.toArray(new Borrow[rst.size()]);
	}

	public boolean checkout(Title title, Customer customer, User user) {
		if (this.dbService.serviceQuery(String.format("SELECT 1 FROM TITLE_COPY WHERE TITLE_ID = '%d' AND TITLE_COPY_STATE = '0'", title.getId())).getRows().size() > 0) {
			DataTable nextTitleCopyRows = this.dbService.serviceQuery(String.format("SELECT * FROM TITLE_COPY WHERE TITLE_ID = '%d' AND TITLE_COPY_STATE = '0'", title.getId()));
			int copyId = Integer.parseInt(nextTitleCopyRows.getRows().get(0).getColumn("title_copy_id").getColumnValue());
			if (this.dbService.runServiceQuery(String.format("INSERT INTO TITLE_HISTORY (TITLE_COPY_ID, CUSTOMER_ID, CHECKOUT_DATE, TITLE_HISTORY_STATE, USER_ID) VALUES ('%d', '%d', CURRENT_TIMESTAMP, '1', '%d')",
            	copyId, customer.getId(), user.getId()))) {
				return this.dbService.runServiceQuery(String.format("UPDATE TITLE_COPY SET TITLE_COPY_STATE = '1' WHERE TITLE_COPY_ID = '%d'", copyId));
			}
		}
		return false;
	}

	public boolean checkin(Borrow borrow, User user) {
		if (this.dbService.runServiceQuery(String.format("UPDATE TITLE_HISTORY SET TITLE_HISTORY_STATE = '0', USER_ID = '%d', " +
			"CHECKIN_DATE = CURRENT_TIMESTAMP, LAST_UPDATED_DATE = CURRENT_TIMESTAMP WHERE TITLE_HISTORY_ID = '%d'", user.getId(), borrow.getId()))) {
			return this.dbService.runServiceQuery(String.format("UPDATE TITLE_COPY SET TITLE_COPY_STATE = '0' WHERE TITLE_COPY_ID = '%d'", borrow.getTitleCopy().getId()));
		}
		return false;
	}

	public boolean markPaid(Borrow borrow, User user) {
		return this.dbService.runServiceQuery(String.format("UPDATE TITLE_HISTORY SET TITLE_HISTORY_PAID = '1', USER_ID = '%d', " +
			"LAST_UPDATED_DATE = CURRENT_TIMESTAMP WHERE TITLE_HISTORY_ID = '%d'", user.getId(), borrow.getId()));
	}

	public Object[] getLateBorrows() {
		ArrayList<Object> rst = new ArrayList<Object>();
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT BORROW_ID, CUSTOMER_ID, CHECKOUT_DATE, CHECKIN_DATE, " +
			"TITLE_COPY_ID, DATE_PART('day', CHECKIN_DATE - CHECKOUT_DATE) AS DAYS_LATE FROM TITLE_HISTORY WHERE DAYS_LATE >= %d AND TITLE_HISTORY_PAID = '0'", this.lateDays));
		for (DataRow row : tab.getRows()) {
			int id = Integer.parseInt(row.getColumn("title_copy_id").getColumnValue());
			DataTable tab2 = this.dbService.serviceQuery(String.format("SELECT TITLE_ID FROM TITLE_COPY WHERE TITLE_COPY_ID = '%d'", id));
			id = Integer.parseInt(tab2.getRows().get(0).getColumn("title_id").getColumnValue());
			HashMap<String, Object> entry = new HashMap<String, Object>();
			entry.put("id", id);
			entry.put("days", Integer.parseInt(row.getColumn("days_late").getColumnValue()));
			entry.put("customerId", Integer.parseInt(row.getColumn("customer_id").getColumnValue()));
			entry.put("checkoutDate", row.getColumn("checkout_date").getColumnValue());
			entry.put("checkinDate", row.getColumn("checkin_date").getColumnValue());
			rst.add(entry);
		}
		return rst.toArray(new Object[rst.size()]);
	}
	
	public HashMap<String, Object> isLate(Borrow borrow) {
		DataTable tab = this.dbService.serviceQuery(String.format("SELECT COALESCE(DATE_PART('day', CHECKIN_DATE - CHECKOUT_DATE), '0') AS DAYS_LATE FROM TITLE_HISTORY WHERE TITLE_HISTORY_ID = '%d'", borrow.getId()));
		DataRow row   = tab.getRows().get(0);
		int temp      = Integer.parseInt(row.getColumn("days_late").getColumnValue());
		if (temp > this.lateDays) {
			return new HashMap<String, Object>() {{
				put("late", true);
				put("days", temp);
			}};
		}
		return new HashMap<String, Object>() {{
			put("late", false);
			put("days", temp);
		}};
	}
}

