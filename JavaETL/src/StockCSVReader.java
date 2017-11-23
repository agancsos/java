import java.util.*;

/**
 * This class extracts stock stat records from a CSV file
 */
public class StockCSVReader extends AbstractCSVReader{
	public StockCSVReader(){
		super();
	}

	public StockCSVReader(String path){
		super(path);
	}

	private StockStatRecord rowToRecord(String row) throws BadDataException{
		String[] comps = row.split(",");
		if(comps.length == 7){
			return new StockStatRecord(comps[0],comps[2],comps[1],
						Float.parseFloat(comps[3]), Float.parseFloat(comps[4]),Float.parseFloat(comps[5]),
						Float.parseFloat(comps[6]));
		}
		else{
			throw new BadDataException("This is not a valid StockStatRecord...");
		}
	}

	public ArrayList<StockStatRecord> load() throws BadDataException{
		ArrayList<StockStatRecord> mFinal = new ArrayList<StockStatRecord>();
		String[] rawRecords;
		try{
			rawRecords = new AMGSystem(filePath).readFile().replace("\n\n","\n").split("\n");
			for(String row : rawRecords){
				if(!row.contains("exchange_country") && !row.contains("Record_ID#")){
					try{
						mFinal.add(rowToRecord(row));
					}
					catch(Exception e){
						continue;
					}
				}
			}		
		}
		catch(Exception e){
			throw new BadDataException("Failed to read Stock record file...");
		}
		return mFinal;
	}
}
