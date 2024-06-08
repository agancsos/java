package com.catalogger.models;
import org.json.JSONObject;

public class Customer extends Item {
	private int id                 = -1;
	private String label           = "";
	private String firstName       = "";
	private String lastName        = "";
	private String address         = "";
	private String city            = "";
	private String state           = "";
	private String country         = "";
	private String isbn            = "";
	private String createdDate     = "";
	private String lastUpdatedDate = "";

	public Customer() {
	}

	public void reloadFromJson(String raw) {
		JSONObject dict = new JSONObject(raw);
		if (dict.has("id")) {
			this.id = dict.getInt("id");
		}
		if (dict.has("label")) {
			this.label = dict.getString("label");
		}
		if (dict.has("firstName")) {
			this.firstName = dict.getString("firstName");
		}
		if (dict.has("lastName")) {
			this.lastName = dict.getString("lastName");
		}
		if (dict.has("address")) {
			this.address = dict.getString("address");
		}
		if (dict.has("city")) {
            this.city = dict.getString("city");
        }
		if (dict.has("state")) {
            this.state = dict.getString("state");
        }
		if (dict.has("country")) {
            this.country = dict.getString("country");
        }
		if (dict.has("isbn")) {
            this.isbn = dict.getString("isbn");
        }
		if (dict.has("createdDate")) {
			this.createdDate = dict.getString("createdDate");
		}
		if (dict.has("lastUpdatedDate")) {
			this.lastUpdatedDate = dict.getString("lastUpdatedDate");
		}
		if (this.firstName.equals("") && this.lastName.equals("") && !this.label.equals("")) {
			String[] comps = this.label.split(" ");
			if (comps.length > 0) {
				this.firstName = comps[0];
			}
			if (comps.length > 1) {
				this.lastName = comps[1];
			}
		}	
	}

	public String toJsonString() {
		String rst = "{";
		rst += String.format("\"@type\": %d", ObjectType.getOrdinal(ObjectType.CUSTOMER));
		rst += String.format(",\"id\": %d", this.id);
		rst += String.format(",\"label\": \"%s %s\"", this.firstName, this.lastName);
		rst += String.format(",\"firstName\": \"%s\"", this.firstName);
		rst += String.format(",\"lastName\": \"%s\"", this.lastName);
		rst += String.format(",\"address\": \"%s\"", this.address);
		rst += String.format(",\"city\": \"%s\"", this.city);
		rst += String.format(",\"state\": \"%s\"", this.state);
		rst += String.format(",\"country\": \"%s\"", this.country);
		rst += String.format(",\"isbn\": \"%s\"", this.isbn);
		rst += String.format(",\"createdDate\": \"%s\"", this.createdDate);
		rst += String.format(",\"lastUpdatedDate\": \"%s\"", this.lastUpdatedDate);
		rst += "}";
		return rst;
	}

	public int getId() { return this.id; }
	public String getLabel() { return this.label; }
	public String getFirstName() { return this.firstName; }
	public String getLastName() { return this.lastName; }
	public String getAddress() { return this.address; }
	public String getCity() { return this.city; }
	public String getState() { return this.state; }
	public String getCountry() { return this.country; }
	public String getISBN() { return this.isbn; }
	public String getCreatedDate() { return this.createdDate; }
	public String getLastUpdatedDate() { return this.lastUpdatedDate; }

	public void setId(int a) { this.id = a; }
	public void setLabel(String a) { this.label = a; }
	public void setFirstName(String a) { this.firstName = a; }
	public void setLastName(String a) { this.lastName = a; }
	public void setAddress(String a) { this.address = a; }
	public void setCity(String a) { this.city = a; }
	public void setState(String a) { this.state = a; }
	public void setCountry(String a) { this.country = a; }
	public void setISBN(String a) { this.isbn = a; }
	public void setCreatedDate(String a) { this.createdDate = a; }
	public void setLastUpdatedDate(String a) { this.lastUpdatedDate = a; }
	public int getObjectType() { return ObjectType.getOrdinal(ObjectType.CUSTOMER); }
}
