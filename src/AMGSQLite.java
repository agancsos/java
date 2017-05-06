import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
	This class is a custom wrapper class for the SQLite JDBC driver (.jar)
	Please ensure that the SQLite .jar is in the same path as the application .jar
	Otherwise, this application will need to compile using Java class path
*/
public class AMGSQLite{
	public String databaseFile = "";
	public String errorTable = "";
	private Connection databaseHandler = null;
	private Statement statementHandler = null;
  
	/*
		This method instantiates the connection to the file
	*/
	private void connect(){
		try{
			this.databaseHandler = DriverManager.getConnection("jdbc:sqlite:" + this.databaseFile);
 			this.statementHandler = this.databaseHandler.createStatement();
    	}
    	catch (Exception e) {}
	}
  
	/*
		This method disconnects from the database file
	*/
  	private void disconnect(){
    	try{
      	this.databaseHandler.close();
    	}
    	catch (Exception e){
      		System.out.println(e);
    	}
  	}
  
	/*
		This method runs the query and audits errors
	*/
  	private void runQuery2(String paramString){
    	try{
      	this.statementHandler.executeUpdate(paramString);
    	}
    	catch (Exception e){
      		errorLog(paramString, e.toString());
    	}
  	}
  
	/*
		This method runs a query without auditing errors
	*/
  	public void runQueryNoErrors(String paramString){
    	connect();
    	try{
      		this.statementHandler.executeUpdate(paramString);
    	}
    	catch (Exception e) {}
    	disconnect();
  	}
  
	/*
		This method checks if the error table exists then runs a query, auditing any errors
	*/
  	public void runQuery(String paramString){
    	connect();
    	if(!this.errorTable.equals("")){
      		runQuery2("create table if not exists " + this.errorTable + "(sql_text text,error_text text,last_updated_date timestamp default current_timestamp)");
    	}
    	try{
      		this.statementHandler.executeUpdate(paramString);
    	}
    	catch (Exception e){
      		errorLog(paramString, e.toString());
    	}
    	disconnect();
  	}
  
	/*
		This method retrieves data from the database
	*/
  	public String[] query(String paramString){
    	connect();
    	if(!this.errorTable.equals("")){
      		runQuery2("create table if not exists " + this.errorTable + "(sql_text text,error_text text,last_updated_date timestamp default current_timestamp)");
    	}
    	ArrayList<String> localArrayList = new ArrayList<String>();
    	try{
      		ResultSet localResultSet = this.statementHandler.executeQuery(paramString);
      		while (localResultSet.next()){
        		String str = "";
        		ResultSetMetaData localResultSetMetaData = localResultSet.getMetaData();
        		for(int i = 0; i < localResultSetMetaData.getColumnCount(); i++){
          			if(i > 0){
            			str = str + ";";
          			}
          			str = str + localResultSet.getString(i + 1);
        		}
        		localArrayList.add(str);
      		}
      		localResultSet.close();
    	}
    	catch(Exception localException){
      		errorLog(paramString, localException.toString());
    	}
    	disconnect();
    	return (String[])localArrayList.toArray(new String[0]);
  	}
  
	/*
		This method retrieves the column names for the specified table
	*/
  	public String[] columnNames(String paramString){
    	connect();
    	ArrayList<String> localArrayList = new ArrayList<String>();
    	try{
      		ResultSet localResultSet = this.statementHandler.executeQuery(paramString);
      		ResultSetMetaData localResultSetMetaData = localResultSet.getMetaData();
      		for(int i = 0; i < localResultSetMetaData.getColumnCount(); i++){
        		localArrayList.add(localResultSetMetaData.getColumnName(i + 1).toString().toUpperCase());
      		}
      		localResultSet.close();
    	}
    	catch (Exception localException) {}
    	disconnect();
    	return (String[])localArrayList.toArray(new String[0]);
  	}
  
	/*
		This method adds an error audit into the database
	*/
  	private void errorLog(String paramString1, String paramString2){
    	paramString1 = paramString1.replace("'", "''");
    	paramString2 = paramString2.replace("'", "''");
    	if(!this.errorTable.equals("")){
      		try{
        		this.statementHandler.executeUpdate("insert into " + this.errorTable + " (sql_text,error_text) values ('" + paramString1 + "','" + paramString2 + "')");
      		}
      		catch (Exception localException) {}
    	}
  	}
}
