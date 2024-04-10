package com.jspInfo.components;
import com.jspInfo.services.JSPInfoService;
import java.util.Map;

public class ApiHelpers {
	public static String getPropertiesJson() {
		String rst = "{";
		int i = 0;
		for (Map.Entry<String, Object> pair : JSPInfoService.getInstance("config.json").getProperties().entrySet()) {
			if (i > 0) {
				rst += ",";
			}
			rst += String.format("\"%s\": \"%s\"", pair.getKey(), pair.getValue());
			i++;
		}
		rst += "}";
		return rst;
	}
}


