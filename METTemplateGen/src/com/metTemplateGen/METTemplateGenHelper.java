package com.metTemplateGen;

public class METTemplateGenHelper{
    public static String padRight(String a, int len, String b){
        if(a.length() >= len){
            return a.substring(0, len);
        }
        String temp = "";
        for(int i = a.length(); i < len; i++){
            temp += b;
        }
        return (temp + b);
    }
}