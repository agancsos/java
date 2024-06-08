package com.catalogger.models;
import org.json.JSONObject;

public class Message {
	private int id                 = -1;
	private int level              = 0;
	private int category           = 0;
	private String text            = "";
	private String lastUpdatedDate = "";

	public Message() {
	}

	public void reloadFromJson(String raw) {
		JSONObject dict = new JSONObject(raw);
		if (dict.has("id")) {
			this.id = dict.getInt("id");
		}
		if (dict.has("level")) {
			this.level = dict.getInt("level");
		}
		if (dict.has("category")) {
			this.category = dict.getInt("category");
		}
		if (dict.has("text")) {
			this.text = dict.getString("text");
		}
		if (dict.has("lastUpdatedDate")) {
			this.lastUpdatedDate = dict.getString("lastUpdatedDate");
		}
	}

	public String toJsonString() {
		String rst = "{";
		rst += String.format("\"id\": %d", this.id);
		rst += String.format(",\"level\": %d", this.level);
		rst += String.format(",\"category\": %d", this.category);
		rst += String.format(",\"text\": \"%s\"", this.text.replace("\n", "\\n"));
		rst += String.format(",\"lastUpdatedDate\": \"%s\"", this.lastUpdatedDate);
		rst += "}";
		return rst;
	}
		
	public int getId() { return this.id; }
	public int getLevel() { return this.level; }
	public int getCategory() { return this.category; }
	public String getText() { return this.text; }
	public String getLastUpdatedDate() { return this.lastUpdatedDate; }

	public void setId(int a) { this.id = a; }
	public void setLevel(int a) { this.level = a; }
	public void setCategory(int a) { this.category = a; }
	public void setText(String a) { this.text = a; }
	public void setLastUpdatedDate(String a) { this.lastUpdatedDate = a; }

}
