package com.jmagent;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;

public class Helpers {
	public static String applicationName     = "Job Manager Agent";
	public static String authorName          = "Abel Gancsos";
	public static String versionString       = "1.0.0.0";
	public static String description         = "";
	static Map<String, String> flags  = new HashMap<String, String>() {{
		put("-h|--help", "Print the help menu.");
		put("-p|--path", "Path to user data.");
		put("-l|--limit", "Limit of poll results.");
	}};

	public static String padRight(String content, int len, String pad){
		if(content.length() > len){
			return content.substring(0,len);
		}
		else{
			String mfinal = "";
			for(int i = content.length(); i < len; i++){
				mfinal += pad;
			}
			return (content + mfinal);
		}
	}

	public static String padLeft(String content, int len, String pad){
		if(content.length() > len){
			return content.substring(0,len);
		}
		else{
			String mfinal = "";
			for(int i = content.length(); i < len; i++){
				mfinal += pad;
			}
			return (mfinal + content);
		}
	}

	public static void helpMenu() {
		System.out.println(Helpers.padRight("", 80, "#"));
		System.out.println(Helpers.padRight("# Name        : " + applicationName, 79, " ") + "#");
		System.out.println(Helpers.padRight("# Author      : " + authorName, 79, " ") + "#");				 
		System.out.println(Helpers.padRight("# Version     : v. " + versionString, 79, " ") + "#");
		System.out.println(Helpers.padRight("# Description : " + description, 79, " ") + "#");
		System.out.println(Helpers.padRight("# Flags       :", 79, " ") + "#");
		for (Map.Entry<String, String> entry : flags.entrySet()) {
			System.out.println(Helpers.padRight("# " + entry.getKey() + " : " + entry.getValue(), 79, " ") + "#");
		}
		System.out.println(Helpers.padRight("", 80, "#"));
	}

	public static String readFile(String path) {
		StringBuffer rsp = new StringBuffer();
		BufferedReader reader = null;
		File file = new File(path);	
		try {
			if (file.exists()) {
				reader = new BufferedReader(new FileReader(file)); 
				String buffer; 
				while ((buffer = reader.readLine()) != null) {
					rsp.append(String.format("%s\n", buffer));
				} 
			}	
		}
		catch(Exception e) {
			
		}
		finally {
			if (reader != null) {
				try {
					reader.close();
				}
				catch (Exception e2) { }
			}
		}
		return rsp.toString();
	}

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
}
