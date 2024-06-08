package com.catalogger.models;
import org.json.JSONObject;

public class ISBN {
	private String ean        = "";
	private String group      = "";
	private String publisher  = "";
	private String title      = "";
	private String checkDigit = "";

	public ISBN() {
	}

	public ISBN(String raw) {
		if (raw.length() == 10) {
			this.ean = "978";
			this.group = raw.substring(0, 1);
			this.publisher = raw.substring(2, 5);
			this.title = raw.substring(6, 8);
			this.checkDigit = raw.substring(9, 9);
		} else {
			this.ean = raw.substring(0, 2);
            this.group = raw.substring(3, 4);
            this.publisher = raw.substring(5, 8);
            this.title = raw.substring(9, 11);
            this.checkDigit = raw.substring(12, 12);
		}
	}

	public void reloadFromJson(String raw) {
		JSONObject dict = new JSONObject(raw);
		if (dict.has("ean")) {
			this.ean = dict.getString("ean");
		}
		if (dict.has("group")) {
			this.group = dict.getString("group");
		}
		if (dict.has("publisher")) {
			this.publisher = dict.getString("publisher");
		}
		if (dict.has("title")) {
			this.title = dict.getString("title");
		}
		if (dict.has("checkDigit")) {
			this.checkDigit = dict.getString("checkDigit");
		}
	}

	public String toJsonString() {
		String rst = "{";
        rst += String.format("\"ean\": \"%s\"", this.ean);
        rst += String.format(",\"group\": \"%s\"", this.group);
        rst += String.format(",\"publisher\": \"%s\"", this.publisher);
        rst += String.format(",\"title\": \"%s\"", this.title);
        rst += String.format(",\"checkDigit\": \"%s\"", this.checkDigit);
        rst += "}";
        return rst;
	}

	public String getEAN() { return this.ean; }
	public String getGroup() { return this.group; }
	public String getPublisher() { return this.publisher; }
	public String getTitle() { return this.title; }
	public String getCheckDigit() { return this.checkDigit; }
}	
