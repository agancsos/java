package com.common;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelpers {
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

	public static boolean writeData(String path, String data) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path));
			writer.write(data);
		}
		catch (IOException e) {
			return false;
		}
		finally {
			if (writer != null) {
		   		try {
					writer.close();
				}
				catch(Exception e) {
				}
			}
		}
		return true;
	}
}
