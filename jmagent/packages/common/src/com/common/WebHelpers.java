package com.common;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WebHelpers {
	public static String invokeGet(String endpoint, HashMap<String, String> headers) throws Exception {
		StringBuffer rsp = new StringBuffer();
		HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
		connection.setRequestMethod("GET");
		connection.setInstanceFollowRedirects(true);
		for (Map.Entry<String, String> header : headers.entrySet()) {
			connection.setRequestProperty(header.getKey(), header.getValue());
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
    		rsp.append(line);
		}
		reader.close();
		connection.disconnect();
		return rsp.toString();
	}

	public static String invokePost(String endpoint, HashMap<String, String> headers, HashMap<String, String> body) throws Exception {
		throw new UnsupportedOperationException();
	}
}

