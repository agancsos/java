package com.catalogger.models;
import org.json.JSONObject;

public class Borrow extends Item {
	private int id                 = -1;
	private String label           = "";
	private String checkoutDate    = "";
	private String checkinDate     = "";
	private TitleCopy titleCopy    = null;
	private Integer state          = 0;
	private Customer customer      = null;
	private boolean late           = false;
	private boolean paid           = false;
	private String createdDate     = "";
	private String lastUpdatedDate = "";
	private User lastUpdatedBy     = null;

	public Borrow() {
	}

	public void reloadFromJson(String raw) {
		JSONObject dict = new JSONObject(raw);
		if (dict.has("id")) {
			this.id = dict.getInt("id");
		}
		if (dict.has("label")) {
			this.label = dict.getString("label");
		}
		if (dict.has("checkoutDate")) {
			this.checkoutDate = dict.getString("checkoutDate");
		}
		if (dict.has("checkinDate")) {
			this.checkinDate = dict.getString("checkinDate");
		}
		if (dict.has("title")) {
			this.titleCopy = new TitleCopy();
			this.titleCopy.setId(dict.getInt("titleCopy"));
		}
		if (dict.has("createdDate")) {
			this.createdDate = dict.getString("createdDate");
		}
		if (dict.has("state")) {
			this.state = dict.getInt("state");
		}
		if (dict.has("customer")) {
			this.customer = new Customer();
			this.customer.setId(dict.getInt("customer"));
		}
		if (dict.has("late")) {
			this.late = (dict.getInt("late") == 0 ? false : true);
		}
		if (dict.has("paid")) {
			this.paid = (dict.getInt("paid") == 0 ? false : true);
		}
		if (dict.has("lastUpdatedDate")) {
			this.lastUpdatedDate = dict.getString("lastUpdatedDate");
		}
		if (dict.has("lastUpdatedBy")) {
			this.lastUpdatedBy = new User();
			this.lastUpdatedBy.setId(dict.getInt("lastUpdatedBy"));
		}
	}

	public String toJsonString() {
		String rst = "{";
		rst += String.format("\"@type\": %d", ObjectType.getOrdinal(ObjectType.BORROW));
		rst += String.format(",\"id\": %d", this.id);
		rst += String.format(",\"label\": \"%s\"", this.label);
		rst += String.format(",\"checkoutDate\": \"%s\"", this.checkoutDate);
		rst += String.format(",\"checkinDate\": \"%s\"", (this.checkinDate == null ? "" : this.checkinDate));
		rst += String.format(",\"title\": %d", this.titleCopy.getId());
		rst += String.format(",\"createdDate\": \"%s\"", this.createdDate);
		rst += String.format(",\"lastUpdatedDate\": \"%s\"", this.lastUpdatedDate);
		rst += String.format(",\"late\": %d", (this.late ? 1 : 0));
		rst += String.format(",\"paid\": %d", (this.paid ? 1 : 0));
		rst += String.format(",\"state\": %d", this.state);
		rst += String.format(",\"customer\": %d", this.customer.getId());
		rst += String.format(",\"lastUpdatedBy\": %d", this.lastUpdatedBy.getId());
		rst += "}";
		return rst;
	}

	public int getId() { return this.id; }
	public String getLabel() { return this.label; }
	public String getCheckoutDate() { return this.checkoutDate; }
	public String getCheckinDate() { return (this.checkinDate == null ? "" : this.checkinDate); }
	public TitleCopy getTitleCopy() { return this.titleCopy; }
	public String getCreatedDate() { return this.createdDate; }
	public String getLastUpdatedDate() { return this.lastUpdatedDate; }
	public int getState() { return this.state; }
	public Customer getCustomer() { return this.customer; }
	public boolean getLate() { return this.late; }
	public boolean getPaid() { return this.paid; }
	public User getLastUpdatedBy() { return this.lastUpdatedBy; }

	public void setId(int a) { this.id = a; }
	public void setLabel(String a) { this.label = a; }
	public void setCheckoutDate(String a) { this.checkoutDate = a; }
	public void setCheckinDate(String a) { this.checkinDate = a; }
	public void setTitleCopy(TitleCopy a) { this.titleCopy = a; }
	public void setCreatedDate(String a) { this.createdDate = a; }
	public void setLastUpdatedDate(String a) { this.lastUpdatedDate = a; }
	public void setCustomer(Customer a) { this.customer = a; }
	public void setLate(boolean a) { this.late = a; }
	public void setPaid(boolean a) { this.paid = a; }
	public void setState(int a) { this.state = a; }
	public void setLastUpdatedBy(User a) { this.lastUpdatedBy = a; }
	public int getObjectType() { return ObjectType.getOrdinal(ObjectType.BORROW); }
}
