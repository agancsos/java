package com.rest2.models;
import org.json.*;

public class BasicItem extends Rest2Item {
	private String fullName = "";

	public BasicItem(String json) {
		JSONObject jsonObject = new JSONObject(json);
		fullName = (String)jsonObject.get("fullName");
	}

	public void loadFromJson(String json) {
		JSONObject jsonObject = new JSONObject(json);
		this.fullName = (String)jsonObject.get("fullName");
	}

	public String getFullName() { return fullName; }
	public void setFullName(String name) { fullName = name; }
	public String getTypeName() { return "Basic"; }
	public String toJsonString() {
		return String.format("{\"fullName\":\"%s\"}", this.getFullName());
	}
}
	
