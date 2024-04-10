package com.jspInfo.services;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;
import com.helpers.SystemHelpers;
import org.json.JSONObject;
import org.json.JSONArray;

public class JSPInfoService {
	private static JSPInfoService instance     = null;
	private HashMap<String, Object> properties = new HashMap<String, Object>();

	private JSPInfoService(String configurationFile) {
		String[] sysEnvs = {
        };
        HashMap<String, String> sysCmds = new HashMap<String, String>() {{
        }};

		if (!configurationFile.equals("")) {
			JSONObject json = null;
			try {
				String raw      = SystemHelpers.readFile(configurationFile);
				json            = new JSONObject(raw);
				if (json.has("environmentVariables")) {
					ArrayList<String> temp = new ArrayList<String>();
					for (int i = 0; i < ((JSONArray)json.get("environmentVariables")).length(); i++) {
						temp.add(((JSONArray)json.get("environmentVariables")).getString(i));
					}
					sysEnvs = temp.toArray(new String[temp.size()]);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (json.has("commands")) {
					Iterator<String> keys = json.getJSONObject("commands").keys();
					while (keys.hasNext()) {
						String key = keys.next();
						sysCmds.put(key, json.getJSONObject("commands").getString(key));
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		for (String env : sysEnvs) {
			this.properties.put(env, System.getenv(env));
		}
		for (Map.Entry<String, String> pair : sysCmds.entrySet()) {
			if (pair.getValue().contains("rm ") || pair.getValue().contains("del ")) {
				continue;
			}
			try {
				this.properties.put(pair.getKey(), SystemHelpers.runCmd(pair.getValue()).replace("\n", ""));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static JSPInfoService getInstance(String path) {
		if (JSPInfoService.instance == null) {
			JSPInfoService.instance = new JSPInfoService(path);
		}
		return JSPInfoService.instance;
	}

	public HashMap<String, Object> getProperties() {
		return this.properties;
	}
}	
