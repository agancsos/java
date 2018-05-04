package com.amgCommon;
/**
 * This is a custom exception class
 */
public class AMGSystemException extends Exception{

	public AMGSystemException(){
		super();
	}
	
	public AMGSystemException(String msg){
		super(msg);
	}
}