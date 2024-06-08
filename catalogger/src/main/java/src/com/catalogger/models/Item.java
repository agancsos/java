package com.catalogger.models;

public abstract class Item {
	protected int id = -1;
	protected String label = "";

	public Item() {
	}

	public String toJsonString() {
		return String.format("{\"id\": %d, \"label\": \"%s\", \"@type\": %d}", this.id, this.label, this.getObjectType());
	}

	public void setId(int a) { this.id = a; }
	public void setLabel(String a) { this.label = a; }
	public int getId() { return this.id; }
	public String getLabel() { return this.label; }
	public abstract int getObjectType();
}
