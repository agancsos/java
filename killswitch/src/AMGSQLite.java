import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AMGSQLite
{
  public String databaseFile = "";
  public String errorTable = "";
  private Connection databaseHandler = null;
  private Statement statementHandler = null;
  
  private void connect(){
    try{
      this.databaseHandler = DriverManager.getConnection("jdbc:sqlite:" + this.databaseFile);
      this.statementHandler = this.databaseHandler.createStatement();
    }
    catch (Exception e) {}
  }
  
  private void disconnect(){
    try{
      this.databaseHandler.close();
    }
    catch (Exception e){
      System.out.println(e);
    }
  }
  
  private void runQuery2(String paramString){
    try{
      this.statementHandler.executeUpdate(paramString);
    }
    catch (Exception e){
      errorLog(paramString, e.toString());
    }
  }
  
  public void runQueryNoErrors(String paramString){
    connect();
    try{
      this.statementHandler.executeUpdate(paramString);
    }
    catch (Exception e) {}
    disconnect();
  }
  
  public void runQuery(String paramString){
    connect();
    if (!this.errorTable.equals("")) {
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
  
  public String[] query(String paramString){
    connect();
    if (!this.errorTable.equals("")) {
      runQuery2("create table if not exists " + this.errorTable + "(sql_text text,error_text text,last_updated_date timestamp default current_timestamp)");
    }
    ArrayList<String> localArrayList = new ArrayList<String>();
    try{
      ResultSet localResultSet = this.statementHandler.executeQuery(paramString);
      while (localResultSet.next()){
        String str = "";
        ResultSetMetaData localResultSetMetaData = localResultSet.getMetaData();
        for (int i = 0; i < localResultSetMetaData.getColumnCount(); i++){
          if (i > 0) {
            str = str + ";";
          }
          str = str + localResultSet.getString(i + 1);
        }
        localArrayList.add(str);
      }
      localResultSet.close();
    }
    catch (Exception localException){
      errorLog(paramString, localException.toString());
    }
    disconnect();
    return (String[])localArrayList.toArray(new String[0]);
  }
  
  public String[] columnNames(String paramString){
    connect();
    ArrayList<String> localArrayList = new ArrayList<String>();
    try{
      ResultSet localResultSet = this.statementHandler.executeQuery(paramString);
      ResultSetMetaData localResultSetMetaData = localResultSet.getMetaData();
      for (int i = 0; i < localResultSetMetaData.getColumnCount(); i++) {
        localArrayList.add(localResultSetMetaData.getColumnName(i + 1).toString().toUpperCase());
      }
      localResultSet.close();
    }
    catch (Exception localException) {}
    disconnect();
    return (String[])localArrayList.toArray(new String[0]);
  }
  
  private void errorLog(String paramString1, String paramString2){
    paramString1 = paramString1.replace("'", "''");
    paramString2 = paramString2.replace("'", "''");
    if (!this.errorTable.equals("")) {
      try{
        this.statementHandler.executeUpdate("insert into " + this.errorTable + " (sql_text,error_text) values ('" + paramString1 + "','" + paramString2 + "')");
      }
      catch (Exception localException) {}
    }
  }
}
