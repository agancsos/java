/**
 * This class is the base class for all ETL records
 */
public class AbstractRecord{
	protected String name = "";

	public void setName(String n){
		name = n;
	}

	public String getName(){
		return name;
	}

	public AbstractRecord(){
	}

	public AbstractRecord(String n){
		name = n;
	}

	public String toString2() throws BadDataException{
		throw new BadDataException("This feature is not supported...");
	}
}
