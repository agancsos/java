package com.catalogger.gui.components;

public class LinkController {
	private String label = "";
	private String name  = "";
	private String path  = "";

	public LinkController() {
	}

	public LinkController(String label, String name, String path) {
		this.label = label;
		this.name  = name;
		this.path  = path;
	}

	public String getName() { return this.name; }
	public String getLabel() { return this.label; }
	public String getPath() { return this.path; }

	public void setName(String a) { this.name = a; }
	public void setLabel(String a) { this.label = a; }
	public void setPath(String a) { this.path = a; }
}

