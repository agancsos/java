package com.jspInfo.services;
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
		String[] comps = name.split("\\.");
		JSONObject temp = null;
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
		if (temp.isNull(comps[i])) {
			return defaultValue;
		}
		return (Object)temp.get(comps[i]);
	}
}

