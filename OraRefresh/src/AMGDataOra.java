import java.sql.*;

class AMGDataOra{
	private String dataSource;
	private String username;
	private String password;
	private boolean verbose;
	private Connection databaseHandler = null;

	public AMGDataOra(){
		verbose = false;
	}

	public void setDataSource(String a){
		dataSource = a;
	}
	
	public String getDataSource(){
		return dataSource;
	}

    public void setUsername(String a){
        username = a;
    }

    public String getUsername(){
        return username;
    }

    public void setPassword(String a){
        password = a;
    }

    public String getPassword(){
        return "";
    }

	public void setVerbose(boolean a){
		verbose = a;
	}

	public boolean getVerbose(){
		return verbose;
	}

	private boolean connect(){
		try{
			String source = "jdbc:oracle:thin:@" + dataSource + ":1521";
			databaseHandler = DriverManager.getConnection(source,username,password);
		}
		catch(Exception e){
			return false;
		}
		return true;
	}

	private boolean disconnect(){
		try{
			databaseHandler.close();
		}
		catch(Exception e){
			return false;
		}
		return true;
	}

	public boolean runQuery(String query){
		try{
			if(connect()){
				Statement statementHandler = databaseHandler.createStatement();
				statementHandler.execute(query);
				disconnect();
			}
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
}
