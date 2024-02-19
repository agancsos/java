package com.common;

public class StringHelpers {
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

	public static boolean isNullOrEmpty(String str) {
		return str.replaceAll("\\s+", "").equals("");
	}
}
