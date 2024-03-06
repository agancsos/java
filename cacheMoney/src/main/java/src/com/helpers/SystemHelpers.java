package com.helpers;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.StringBuffer;

public class SystemHelpers {
	public static String readFile(String path) throws Exception {
    	String rst = "";
    	File f = new File(path);
    	FileReader reader = new FileReader(f);
    	BufferedReader br = new BufferedReader(reader);
    	StringBuffer sb   = new StringBuffer();
    	String buffer;
    	while((buffer = br.readLine()) != null){
        	sb.append(buffer);
        	sb.append("");
    	}
    	reader.close();
    	rst = sb.toString();
    	return rst;
	}

	public static void writeFile(String path, String data) throws Exception {
    	File f = new File(path);
    	OutputStream stream = new FileOutputStream(f);
    	stream.write(data.getBytes("UTF-8"),0,data.length());
	}
}

