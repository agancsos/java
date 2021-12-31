package com.rest2.models;
import org.json.*;

public abstract class Rest2Item {
	public Rest2Item() {}
	public Rest2Item(String json) {}
	public abstract void loadFromJson(String json);
	public abstract String getTypeName();
	public abstract String toJsonString();
}
