package com.common.propertystore;
import java.util.Map;
import java.util.HashMap;
import com.common.FileHelpers;

public class BasicPropertyStore implements IPropertyStore {
	private String delimeter;
	private String storePath;
	private HashMap<String, Object> container;

	public BasicPropertyStore() {
		this.delimeter = "=";
		this.storePath = null;
		this.container = new HashMap<String, Object>();
		this.load();
	}

	public BasicPropertyStore(String path, String delimeter) {
		this.storePath = path;
		this.delimeter = delimeter;
		this.container = new HashMap<String, Object>();
		this.load();
	}

	public BasicPropertyStore(String path) {
		this.storePath = path;
        this.delimeter = "=";
        this.container = new HashMap<String, Object>();
        this.load();
	}

	private void load() {
		String raw = FileHelpers.readFile(this.storePath);
		String[] lines = raw.split("\\n");
		for (String line : lines) {
			if (line.equals("") || line.replaceAll("\\s+", "").charAt(0) == '#') { continue; }
			String[] comps = line.split(this.delimeter);
			this.container.put(comps[0], comps[1]);
		}
	}

	private void save() {
		String data = "";
		for (Map.Entry<String, Object> entry : this.container.entrySet()) {
			data += String.format("%s%s%v\n", entry.getKey(), this.delimeter, entry.getValue());
		}
		FileHelpers.writeData(this.storePath, data);
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

	public void setDelimeter(String delimeter) { this.delimeter = delimeter; }
	public void settStorePath(String path) {
		this.storePath = path; 
		this.load();
	}
	public String getDelimeter() { return this.delimeter; }
	public String getStorePath() { return this.storePath; }
}
