package com.amgCommon;

/**
 * This class provides enhancemed features to the base String class
 * 
 * @author (Abel Gancsos) 
 * @version (10/23/2017)
 */
public class AMGString
{
    private String content = "";
    
    /**
     * Constructor for objects of class AMGString
     */
    public AMGString(){
        
    }

    public AMGString(String text){
        content = text;
    }
    
    public String padRight(int len, String pad){
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
   
    public String padLeft(int len, String pad){
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
    
    public String getContent(){
        return content;
    }
}
