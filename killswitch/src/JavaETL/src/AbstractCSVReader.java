/**
 * This class is the base CSVreader
 */
public class AbstractCSVReader{
	protected String filePath = "";

	public AbstractCSVReader(){
	}

	public AbstractCSVReader(String path){
		filePath = path;
	}
}
