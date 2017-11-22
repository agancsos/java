import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

/**
 * This class helps manage SQLite databases
 */
public class AMGSQLite{
	private String databaseFile = "";
	private Connection databaseHandler = null;
	private Statement statementHandler = null;

	public AMGSQLite(){
	}

	public AMGSQLite(String path){
		databaseFile = path;
	}

	private boolean connect(){
		try{
			databaseHandler = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
			statementHandler = databaseHandler.createStatement();
		}
		catch(Exception e){
			return false;
		}
		return true;
	}

	private void disconnect() throws BadDataException{
		try{
			databaseHandler.close();
		}
		catch(Exception e){
			throw new BadDataException("Failed to disconnect from database...");
		}
	}

	public boolean runQuery(String sql){
		if(connect()){
			try{
				statementHandler.executeUpdate(sql);
				disconnect();
			}
            catch(Exception e){
                return false;
            }

		}
		return true;
	}

	public ArrayList<AMGSQLiteRow> query(String sql) throws BadDataException{
		ArrayList<AMGSQLiteRow> mFinal = new ArrayList<AMGSQLiteRow>();
		if(connect()){
			try{
				ResultSet localResultSet = this.statementHandler.executeQuery(sql);
      			while (localResultSet.next()){
        			String str = "";
        			ResultSetMetaData localResultSetMetaData = localResultSet.getMetaData();
        			for(int i = 0; i < localResultSetMetaData.getColumnCount(); i++){
          				if(i > 0){
            				str = str + ";";
          				}
          				str = str + localResultSet.getString(i + 1);
        			}
        			mFinal.add(new AMGSQLiteRow(null,str.split(";")));
      			}
				localResultSet.close();
				disconnect();
			}
			catch(Exception e){
				throw new BadDataException("Failed to read database...");
			}
		}
		return mFinal;
	}
}
