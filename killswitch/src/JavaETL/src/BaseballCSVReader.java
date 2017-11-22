import java.util.*;

/**
 * This class extracts baseball stat records from a CSV file
 */
public class BaseballCSVReader extends AbstractCSVReader{
	public BaseballCSVReader(){
		super();
	}

	public BaseballCSVReader(String path){
		super(path);
	}

	private BaseballStatRecord rowToRecord(String row) throws BadDataException{
		String[] comps = row.split(",");
		if(comps.length == 134){
			return new BaseballStatRecord(comps[0],Float.parseFloat(comps[2]),Integer.parseInt(comps[5]),
						Float.parseFloat(comps[68]));
		}
		else{
			throw new BadDataException("This is not a valid BaseballStatRecord...");
		}
	}

	public ArrayList<BaseballStatRecord> load() throws BadDataException{
		ArrayList<BaseballStatRecord> mFinal = new ArrayList<BaseballStatRecord>();
		String[] rawRecords;
		try{
			rawRecords = new AMGSystem(filePath).readFile().replace("\n\n","\n").split("\n");
			for(String row : rawRecords){
				if(!row.contains("exchange_country") && !row.contains("Record_ID#")){
					mFinal.add(rowToRecord(row));
				}
			}		
		}
		catch(Exception e){
			e.printStackTrace();
			throw new BadDataException("Failed to read Baseball record file...");
		}
		return mFinal;
	}
}
