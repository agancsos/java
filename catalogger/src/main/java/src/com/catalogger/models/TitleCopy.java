package com.catalogger.models;
import org.json.JSONObject;

public class TitleCopy extends Item {
	private int id                 = -1;
	private String label           = "";
	private int state              = 0;
	private User lastUpdatedBy     = null;
	private Title title            = null;
	private String createdDate     = "";
	private String lastUpdatedDate = "";

	public TitleCopy() {
	}

	public void reloadFromJson(String raw) {
		JSONObject dict = new JSONObject(raw);
		if (dict.has("id")) {
			this.id = dict.getInt("id");
		}
		if (dict.has("label")) {
			this.label = dict.getString("label");
		}
		if (dict.has("state")) {
            this.state = dict.getInt("state");
        }
		if (dict.has("createdDate")) {
			this.createdDate = dict.getString("createdDate");
		}
		if (dict.has("lastUpdatedDate")) {
			this.lastUpdatedDate = dict.getString("lastUpdatedDate");
		}
		if (dict.has("lastUpdatedBy")) {
            this.lastUpdatedBy = new User();
            this.lastUpdatedBy.setId(dict.getInt("lastUpdatedBy"));
        }
		if (dict.has("title")) {
            this.title = new Title();
            this.title.setId(dict.getInt("title"));
        }
	}

	public String toJsonString() {
		String rst = "{";
		rst += String.format("\"@type\": %d", ObjectType.getOrdinal(ObjectType.TITLE_COPY));
		rst += String.format(",\"id\": %d", this.id);
		rst += String.format(",\"label\": \"%s\"", this.label);
		rst += String.format(",\"lastUpdatedBy\": %d", this.lastUpdatedBy.getId());
		rst += String.format(",\"state\": %d", this.state);
		rst += String.format(",\"title\": %d", this.title.getId());
		rst += String.format(",\"createdDate\": \"%s\"", this.createdDate);
		rst += String.format(",\"lastUpdatedDate\": \"%s\"", this.lastUpdatedDate);
		rst += "}";
		return rst;
	}

	public int getId() { return this.id; }
	public String getLabel() { return this.label; }
	public User getLastUpdatedBy() { return this.lastUpdatedBy; }
	public Title getTitle() { return this.title; }
	public int getState() { return this.state; }
	public String getCreatedDate() { return this.createdDate; }
	public String getLastUpdatedDate() { return this.lastUpdatedDate; }

	public void setId(int a) { this.id = a; }
	public void setLabel(String a) { this.label = a; }
	public void setLastUpdatedBy(User a) { this.lastUpdatedBy = a; }
	public void setTitle(Title a) { this.title = a; }
	public void setState(int a) { this.state = a; }
	public void setCreatedDate(String a) { this.createdDate = a; }
	public void setLastUpdatedDate(String a) { this.lastUpdatedDate = a; }
	public int getObjectType() { return ObjectType.getOrdinal(ObjectType.TITLE_COPY); }
}
