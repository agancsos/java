import java.io.*;
import java.io.FileOutputStream.*;

/**
 * This class offers custom system operations
 */
public class AMGSystem{
	private String sourcePath = "";
	private String targetPath = "";
	private boolean silent = false;
	private boolean verbose = false;

	public void setSilent(boolean a){
		silent = a;
	}

	public void setVerbose(boolean a){
		verbose = a;
	}

	public boolean getSilent(){
		return silent;
	}

	public boolean getVerbose(){
		return verbose;
	} 

	public AMGSystem(){
	}

	public AMGSystem(String s){
		sourcePath = s;
	}

	/**
	 * This method will run a system command.
	 * @param cmd (Command to run on the system)
	 * @returns (True if successful, False if not)
	 */
	public Boolean runCMD(String cmd){
		if(verbose && !silent){
			System.out.println("Running: " + cmd);
		}
		try{
			StringBuffer stdout = new StringBuffer();
			Runtime workflowHost = Runtime.getRuntime();
			Process thread = workflowHost.exec(cmd);
			thread.waitFor();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(thread.getInputStream()));
			String line = "";
			while((line = buffer.readLine()) != null){
				if(verbose && !silent){
					System.out.println(line);
				}
			}
			return true;
		}
		catch(Exception e){
			if(!silent){
				e.printStackTrace(System.out);
			}
			return false;
		}
	}

	public AMGSystem(String s, String t){
		sourcePath = s;
		targetPath = t;
	}

	public void setTargetpath(String t){
		targetPath = t;
	}

	public String getTargetPath(){
		return targetPath;
	}

	public void setSourcePath(String s){
		sourcePath = s;
	}

	public String getSourcePath(){
		return sourcePath;
	}

	public boolean fileExists(String path){
		try{
			File f = new File(path);
			if(f.exists()){
				return true;
			}
			else{
				return false;
			}
		}
		catch(Exception e){
			return false;
		}
	}

	public boolean writeFile(String data) throws AMGSystemException{
		try{
			File f = new File(targetPath);
			OutputStream stream = new FileOutputStream(f);
			stream.write(data.getBytes("UTF-8"),0,data.length());
		}
		catch(Exception e){
			throw new AMGSystemException("Failed to write data file...");
		}
		return true;
	}

	public String readFile() throws AMGSystemException{
		String mFinal = "";
		if(fileExists(sourcePath)){
			try{
				File f = new File(sourcePath);
				FileReader reader = new FileReader(f);
				BufferedReader br = new BufferedReader(reader);
				StringBuffer sb = new StringBuffer();
				String buffer;
				while((buffer = br.readLine()) != null){
					sb.append(buffer);
					sb.append("\n");
				}
				reader.close();
				mFinal = sb.toString();
			}
			catch(Exception e){
				throw new AMGSystemException("Faild to read data file...");
			}
		}
		return mFinal;
	}	
}
