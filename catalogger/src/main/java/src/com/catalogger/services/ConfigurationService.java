package com.catalogger.services;
import com.helpers.SystemHelpers;
import org.json.JSONObject;

public class ConfigurationService {
	private static ConfigurationService instance = null;
	private String configurationPath = "";
	private JSONObject container = null;

	private ConfigurationService() {
	}

	public static ConfigurationService getInstance(String path) {
		if (ConfigurationService.instance == null) {
			ConfigurationService.instance = new ConfigurationService();
			ConfigurationService.instance.configurationPath = path;
			try {
				String raw = SystemHelpers.readFile(path);
				ConfigurationService.instance.container = new JSONObject(raw);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ConfigurationService.instance;
	}

	public Object getProperty(String name, Object defaultValue) {
		JSONObject temp = null;
		String[] comps = name.split("\\.");
		int i = 0;
		while (i < comps.length - 1) {
			if (i == 0) {
				temp = this.container;
			} else {
				temp = temp.getJSONObject(comps[i]);
			}
			if (temp.isNull(comps[i])) {
				return defaultValue;
			}
			temp = temp.getJSONObject(comps[i]);
			i++;
		}
		if (temp == null || !temp.has(comps[i])) {
			return defaultValue;
		}
		return (Object)temp.get(comps[i]);
	}

	public String getConfigurationPath() { return this.configurationPath; }
}

