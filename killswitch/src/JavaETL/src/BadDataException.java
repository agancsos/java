/**
 * This is a custom exception class
 */
public class BadDataException extends Exception{

	public BadDataException(){
		super();
	}
	
	public BadDataException(String msg){
		super(msg);
	}
}
