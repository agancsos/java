/*
	Name       : killswitchdata.java
	Author     : Abel Gancsos
	(c)        : 
	v.         : v. 1.0.0
	Description: This is the class that performs all the backend processes.
*/

/*              IMPORTS                   */
import java.util.*;
import java.text.*;
import java.io.*;
/******************************************/

public class KillswitchData{
	private AMGSQLite dataHandler = new AMGSQLite();
	public String dataFile   = "audit.db";
	public String errorTab   = "error_logs";
	public String[] stages  = {"Make Target","Copy Documents","Copy Images","Copy Desktop","Copy Exchange files"};
	public String targetPath = "stuff";
	private String platform = System.getProperty("os.name");
	public Boolean TEST = false;
	private String user = System.getProperty("user.name");
	private String windowsDrive = System.getProperty("user.home").split(":")[0];

	/*
		This is the constructor based on audit file and error table
		@param file Path to the audit database
		@param errors Name of the errors table
	*/	
	public KillswitchData(String file,String errors){
		dataFile = file;
		errorTab = errors;
		dataHandler.databaseFile = dataFile;
  		dataHandler.errorTable = errorTab;
	}

	/*
		This is the constructor based on the audit file
		@param file Path to the audit database
	*/
    public KillswitchData(String file){
        dataFile = file;
        dataHandler.databaseFile = dataFile;
        dataHandler.errorTable = errorTab;
		buildDB();
    }

	/*
		This is the default constructor
	*/
    public KillswitchData(){
        dataHandler.databaseFile = dataFile;
        dataHandler.errorTable = errorTab;
    }

	/*
		This method builds the tables for the audit table
	*/
	private void buildDB(){
		Vector<String> queries = new Vector<String>();
		queries.add("create table if not exists stages (stage_name character not null primary key default '',status character default 'Waiting')");
		queries.add("create table if not exists files  (file_name character default '',path character default '',copied integer default '0',last_updated_date timestamp default current_timestamp)");
		queries.add("delete from files");
		queries.add("delete from stages");
		for(String query : queries){
			dataHandler.runQuery(query);
		}
		addStages();
	}

	/*
		This method resets the stages for the backup process
	*/
	private void addStages(){
		if(dataHandler.query("select * from stages").length > 0){
			dataHandler.runQuery("delete from stages");
		}
		for(int i = 0; i < stages.length; i++){
			dataHandler.runQuery("insert into stages (stage_name) values ('" + stages[i] + "')");
		}
	}
	/*                                     STAGES                                             */

	/*
		This method creates the base directory
	*/
	private void makeTarget(){
		updateStageStatus("Make Target","Running");
		try{
			File tempDir = new File(targetPath);
			if(!TEST){
				tempDir.mkdir();
			}
		}
		catch(Exception e){
		}
	}

	/*
		This method inserts the specified file into the files table
		@param path Path of the file to insert into the database
	*/
	private void insertFile(String path){
		dataHandler.runQuery("insert into files (file_name,path,copied) values ('','" + path + "','1')");
	}

	/*
		This method loops through the files at the provided path
		@param path Path to the base directory to loop
		@param stage Current stage in order to keep track of the files
	*/ 
	private void enumerateFiles(String path,String stage){
		File[] files = new File(path).listFiles();
		AMGFile tempRoot = new AMGFile(new File(targetPath).getPath() +  new AMGFile(new File(path).getPath()).sPath);
		try{
			if(!TEST){
				new File(tempRoot.sPath.replace(windowsDrive + ":","")).mkdirs();
			}
		}
		catch(Exception e){
		}
		try{
			for(int i = 0; i < files.length; i++){
				File file = files[i];
				if(!file.getName().equals('.') && !file.getName().equals(".DS_Store") && !file.getName().equals("..")){
					if(file.isDirectory()){
						insertFile(file.getPath());
						AMGFile tempDir = new AMGFile(new File(targetPath).getPath() + new AMGFile(file.getPath().replace(windowsDrive + ":","")).sPath);
						if(!TEST){
							tempDir.mkdir();
						}
						enumerateFiles(new AMGFile(file.getPath()).sPath,stage);
					}
					else if(file.isFile()){
						insertFile(new AMGFile(file.getPath()).sPath);
						AMGFile tempDir = new AMGFile(new File(targetPath).getPath() + new AMGFile(file.getPath()).sPath);
						if(!TEST){
							new AMGFile(file.getPath(),tempDir.sPath.replace(windowsDrive + ":","")).copy();
						}
					}
				}
			}
		}
		catch(Exception e){
		}
	}

	/*
		This method copies the specified file to the target folder
		@param path Path of the file to copy
		@param stage Current stage in order to keep track of status
	*/
    private void copyFiles(String path,String stage){
        File[] files = new File(path).listFiles();
        AMGFile tempRoot = new AMGFile(new File(targetPath).getPath() +  new AMGFile(new File(path).getPath()).sPath);
        try{
			if(!TEST){
            	new File(tempRoot.sPath.replace(windowsDrive + ":","")).mkdirs();
			}
        }
        catch(Exception e){
        }
        try{
            for(int i = 0; i < files.length; i++){
                File file = files[i];
                if(!file.getName().equals('.') && !file.getName().equals(".DS_Store") && !file.getName().equals("..")){
                    if(file.isFile()){
                        insertFile(new AMGFile(file.getPath()).sPath);
                        AMGFile tempDir = new AMGFile(new File(targetPath).getPath() + new AMGFile(file.getPath()).sPath);
						if(!TEST){
                        	new AMGFile(file.getPath(),tempDir.sPath.replace(windowsDrive + ":","")).copy();
						}
                    }
                }
            }
        }
        catch(Exception e){
        }
    }

	/*
		This method calls the process to copy the files under the user's documents folder
	*/
	private void copyDocuments(){
        updateStageStatus("Make Target","Complete");
        updateStageStatus("Copy Documents","Running");
		switch(platform.substring(0,3)){
			case "Mac":
				enumerateFiles("/Users/" + user + "/Documents","Documents");
				break;
			case "Win":
				enumerateFiles(windowsDrive + ":/Users/" + user + "/Documents","Documents");
				break;
			default:
				enumerateFiles("/home/" + user + "/Documents","Documents");
				break;
		}
	}

	/*
		This method calls the process to copy the files under the user's images folder
	*/
	private void copyImages(){
        updateStageStatus("Copy Documents","Complete");
        updateStageStatus("Copy Images","Running");
        switch(platform.substring(0,3)){
            case "Mac":
                enumerateFiles("/Users/" + user + "/Pictures","Pictures");
                break;
            case "Win":
                enumerateFiles(windowsDrive + ":/Users/" + user + "/Pictures","Pictures");
                break;
            default:
                enumerateFiles("/home/" + user + "/Documents","Pictures");
                break;
        }
	}

	/*
		This method calls the process to copy the files under the user's Desktop  folder
	*/
	private void copyDesktop(){
        updateStageStatus("Copy Images","Complete");
        updateStageStatus("Copy Desktop","Running");
        switch(platform.substring(0,3)){
            case "Mac":
                enumerateFiles("/Users/" + user + "/Desktop","Desktop");
                break;
            case "Win":
                enumerateFiles(windowsDrive + ":/Users/" + user + "/Desktop","Desktop");
                break;
            default:
                enumerateFiles("/home/" + user + "/Desktop","Desktop");
                break;
        }
	}

	/*
		This method checks if the system is Windows and then tries to copy Exchange files
	*/
	private void copyEWS(){
        updateStageStatus("Copy Desktop","Complete");
        updateStageStatus("Copy Exchange files","Running");
        switch(platform.substring(0,3)){
            case "Mac":
                break;
            case "Win":
                copyFiles(windowsDrive + ":/Users/" + user + "/AppData/Local/Microsoft/Outlook","Exchange");
                break;
            default:
                break;
        }
		updateStageStatus("Copy Exchange files","Complete");
	}

	/*
		This method retrieves the files from the database
	*/
	public String[] getFiles(){
		return dataHandler.query("select path||'/'||file_name from files where copied = '1' order by last_updated_date asc");
	}

	/*
		Gets the stages from the database
	*/
	public String getStageStatus(String stage){
		return dataHandler.query("select status from stages where stage_name = '" + stage + "'")[0];
	}

	/*
		This method sets the stage status
	*/
	public void updateStageStatus(String stage,String status){
		dataHandler.runQuery("update stages set status = '" + status + "' where stage_name = '" + stage + "'");
	}
	/******************************************************************************************/

	/*
		This method is the entry point to the class that calls all the stages in the process
	*/
	public void run(){
		buildDB();
		makeTarget();
		copyDocuments();
		copyImages();
		copyDesktop();
		copyEWS();
	}	
}
