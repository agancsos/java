import java.util.*;

/**
 * This class helps administer the rest of this utility
 */
public class AMGETL{
	private String applicationFile = "";
	private String dataType = "0";
	private boolean silent = false;
	private String base = "../resources/";

	public AMGETL(){
	}

	public void setApplicationFile(String path){
		applicationFile = path;
	}
	
	public String getApplicationFile(){
		return applicationFile;
	}

    public void setBase(String path){
        base = path;
    }

    public String getBase(){
        return base;
    }

	public void setDataType(String type){
		dataType = type;
	}
	
	public String getDataType(){
		return dataType;
	}

	public void setSilent(boolean s){
		silent = s;
	}

	public boolean getSilent(){
		return silent;
	}

	public void run(){
		AMGData dataSession = new AMGData(base);
		dataSession.createDB();

		if(!applicationFile.equals("")){
			if(!dataType.equals("")){
				switch(dataType){
					case "0":
						System.out.println("This is the default value, please specify an actual value...");
						break;
					case "1":
            			ArrayList<BaseballStatRecord> list1 = new ArrayList<BaseballStatRecord>();
            			try{
                			list1 = new BaseballCSVReader(applicationFile).load();
                			for(BaseballStatRecord cursor : list1){
                    			new AMGData(base).insertBaseball(cursor);
                    			System.out.println(cursor.toString2());
                			}
            			}
            			catch(Exception e){
            			}
						break;
					case "2":
		            	ArrayList<StockStatRecord> list2 = new ArrayList<StockStatRecord>();
            			try{
                			list2 = new StockCSVReader(applicationFile).load();
                			for(StockStatRecord cursor : list2){
                    			new AMGData(base).insertStock(cursor);
                    			System.out.println(cursor.toString2());
                			}
            			}
            			catch(Exception e){
            			}
						break;
					default:
						System.out.println("This data type has not been implemented yet...");
						break;
				}
			}
			else{
				if(!silent){
					System.out.println("You must specify a data type with the application file...");
				}
			}
		}
		else{
			// Extract baseball stats
			ArrayList<BaseballStatRecord> list1 = new ArrayList<BaseballStatRecord>();
			try{
				list1 = new BaseballCSVReader(base + "/csv/MLB2008.csv").load();
				for(BaseballStatRecord cursor : list1){
					new AMGData(base).insertBaseball(cursor);
					System.out.println(cursor.toString2());
				}
			}
			catch(Exception e){
			}

			// Extract stocks
            ArrayList<StockStatRecord> list2 = new ArrayList<StockStatRecord>();
            try{
                list2 = new StockCSVReader(base + "/csv/StockValuations.csv").load();
                for(StockStatRecord cursor : list2){
                    new AMGData(base).insertStock(cursor);
                    System.out.println(cursor.toString2());
                }
            }
            catch(Exception e){
            }
		}
	}
}
