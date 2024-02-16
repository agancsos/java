/*
	Name       : amgfile.java
	Author     : Abel Gancsos
	(c)        : 
	v.         : v. 1.0.0
	Description: This class is a custom File class that helps manage the file system
*/

/*              IMPORTS                   */
import java.util.*;
import java.text.*;
import java.nio.channels.FileChannel;
import java.io.*;
/******************************************/

public class AMGFile{
	public String sPath = "";
	public String tPath = "";
	public File sFile;
	public File tFile;

	/*
		This is the constructor based on the source path
	*/
	public AMGFile(String source){
		sPath = source.replace("\\","/");
		sFile = new File(sPath);
	}

	/*
		This is the constructor based on the source path and target path
	*/
    public AMGFile(String source,String target){
        sPath = source.replace("\\","/");
		tPath = target.replace("\\","/");
		sFile = new File(sPath);
		tFile = new File(tPath);
    }

	/*
		This method retrieves the directory of the source path
	*/
	public String getParentName(){
		String[] comps = sPath.split("/");
		return comps[comps.length - 1];
	}

	/*
		This method copies the file to the target path
	*/
	public void copy(){
		if(sFile.exists()){
			if(!tFile.exists()){
				try{
					tFile.createNewFile();
				}
				catch(Exception e){
				}
			}
			FileChannel sChannel = null;
			FileChannel tChannel = null;
			try{
				sChannel = new FileInputStream(sFile).getChannel();
				tChannel = new FileOutputStream(tFile).getChannel();
				tChannel.transferFrom(sChannel,0,sChannel.size());
				sChannel.close();
				tChannel.close();
			}
			catch(Exception e){
			}
		}
		else{
		}	
	}

	/*
		This method makes the source directory
	*/ 
	public void mkdir(){
		if(!sFile.exists()){
			try{
				sFile.mkdir();
			}
			catch(Exception e){	
				System.out.println(e);
			}
		}
	}
}
