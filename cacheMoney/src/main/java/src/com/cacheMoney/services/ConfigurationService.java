package com.cacheMoney.services;
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
				String[] varNames = new String[] {
					"SERVER",
					"DATABASE",
					"DBA_USERNAME",
					"DBA_PASSWORD",
					"USERNAME",
					"PASSWORD"
				};
				String envConfig = "{\"database\": {";
				envConfig += "\"provider\": \"postgres\"";
				for (String varName : varNames) {
					if (System.getenv(String.format("ASSETS_%s", varName)) != null) {
						if (varName.equals("SERVER")) {
							envConfig += String.format(",\"server\": \"%s\"", System.getenv(String.format("ASSETS_%s", varName)));
						} else if (varName.equals("DATABASE")) {
							envConfig += String.format(",\"database\": \"%s\"", System.getenv(String.format("ASSETS_%s", varName)));
						} else if (varName.equals("DBA_USERNAME")) {
							envConfig += String.format(",\"dbaUsername\": \"%s\"", System.getenv(String.format("ASSETS_%s", varName)));
						} else if (varName.equals("DBA_PASSWORD")) {
							envConfig += String.format(",\"dbaPassword\": \"%s\"", System.getenv(String.format("ASSETS_%s", varName)));
						} else if (varName.equals("USERNAME")) {
							envConfig += String.format(",\"username\": \"%s\"", System.getenv(String.format("ASSETS_%s", varName)));
						} else if (varName.equals("PASSWORD")) {
							envConfig += String.format(",\"password\": \"%s\"", System.getenv(String.format("ASSETS_%s", varName)));
						}
					}
				}
				envConfig += "}}";
				ConfigurationService.instance.container = new JSONObject(envConfig);
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

