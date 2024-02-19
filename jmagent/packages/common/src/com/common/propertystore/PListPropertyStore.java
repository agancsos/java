package com.common.propertystore;
import java.util.Map;
import java.util.HashMap;
import com.common.FileHelpers;

public class PListPropertyStore implements IPropertyStore {
	private String storePath;
	private HashMap<String, Object> container;

	public PListPropertyStore() {
		this.storePath = null;
		this.container = new HashMap<String, Object>();
		this.load();
	}

	public PListPropertyStore(String path) {
		this.storePath = path;
        this.container = new HashMap<String, Object>();
        this.load();
	}

	private void load() {
		String raw = FileHelpers.readFile(this.storePath);
		String[] lines = raw.split("\\n");
		String key = null;
		String value = null;
		for (String line : lines) {
			if (line.equals("") || line.replaceAll("\\s+", "").charAt(0) == '#') { continue; }
			if (key == null || value == null) {
				if (line.contains("<key>")) { key = line.split("<key>")[1].replace("</key>", ""); }
				else {
					String typeString = line.split(">")[0].replace("<", "").trim();
					try {
						value = line.split(String.format("<%s>", typeString))[1].replace(String.format("</%s>", typeString), "");
					}
					catch (Exception ex) {
						value = null;
						key = null;
					}
					this.container.put(key, value);
					key = value = null;
				}
			}
			
		}
	}

	private void save() {
	}

	public Object getPropertyValue(String name) {
		if (this.container.containsKey(name)) {
			return this.container.get(name);
		}
		return "";
	}

	public void setPropertyValue(String name, Object value) {
		this.container.put(name, value);
		this.save();
	}

	public void settStorePath(String path) {
		this.storePath = path; 
		this.load();
	}
	public String getStorePath() { return this.storePath; }
}
