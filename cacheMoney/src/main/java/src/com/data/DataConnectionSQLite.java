package com.data;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.data.types.DataTable;
import com.data.types.DataRow;
import com.data.types.DataColumn;

/*
	This class is a custom wrapper class for the SQLite JDBC driver (.jar)
	Please ensure that the SQLite .jar is in the same path as the application .jar
	Otherwise, this application will need to compile using Java class path
*/
public class DataConnectionSQLite implements IDataConnection {
	private String databaseFile = "";
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
		This method checks if the error table exists then runs a query, auditing any errors
	*/
  	public boolean runQuery(String sql){
		connect();
		boolean result = false;
		try{
	  		this.statementHandler.executeUpdate(sql);
			result = true;
		}
		catch (Exception e){
		}
		disconnect();
		return result;
  	}
  
	/*
		This method retrieves data from the database
	*/
  	public DataTable query(String sql){
		connect();
		DataTable result = new DataTable();
		try{
	  		ResultSet localResultSet = this.statementHandler.executeQuery(sql);
	  		while (localResultSet.next()){
				DataRow row = new DataRow();
				ResultSetMetaData localResultSetMetaData = localResultSet.getMetaData();
				for(int i = 0; i < localResultSetMetaData.getColumnCount(); i++){
					DataColumn col = new DataColumn();
					col.setColumnName(localResultSetMetaData.getColumnName(i + 1));
					col.setColumnValue(localResultSet.getString(i + 1));
					row.addColumn(col);
				}
				result.addRow(row);
	  		}
	  		localResultSet.close();
		}
		catch(Exception localException){
		}
		disconnect();
		return result;
  	}
  
	/*
		This method retrieves the column names for the specified table
	*/
  	public String[] getColumnNames(String sql){
		connect();
		ArrayList<String> localArrayList = new ArrayList<String>();
		try{
	  		ResultSet localResultSet = this.statementHandler.executeQuery(sql);
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

	public void setDatabaseFile(String path) { this.databaseFile = path; }
	public String getDatabaseFile() { return this.databaseFile; }
}

