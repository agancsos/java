import java.util.*;

/**
 * This class helps control the taks for the Oracle refresh
 */
class AMGOraRefresh{
    private boolean verbose;
    private boolean drop;
    private String dataSource;
    private String schemaName;
    private String password;
    private String sysPassword;
    private String dumpFilePath;
    private String importVariable;
    private AMGDataOra dataHandler = new AMGDataOra();

    private boolean dropSchema(){
    	try{
        	ArrayList<String> queries = new ArrayList<String>();
        	queries.add("drop user " + schemaName + " cascade");
        	for(String query : queries){
            	dataHandler.runQuery(query);
        	}
    	}
    	catch(Exception e){
        	return false;
    	}
    	return true;
	}

    private boolean createSchema(){
    	try{
        	ArrayList<String> queries = new ArrayList<String>();
        	ArrayList<String> queries2 = new ArrayList<String>();
        	queries.add("alter session set \"_oracle_script\"=true");
        	queries.add("create user " + schemaName + " identified by " + password);
        	queries2.add("create tablespace " + schemaName + "_ts datafile '" + schemaName + "_ts.tdf' size 50m autoextend on next 50m");
        	queries2.add("alter user " + schemaName + " default tablespace " + schemaName + "_ts");

       	 	for(String query : queries){
            	dataHandler.runQuery(query);
        	}

        	grantPermissions();

        	for(String query2 : queries2){
            	dataHandler.runQuery(query2);
        	}
    	}
    	catch(Exception e){
        	return false;
    	}
   		return true;
	}

    private boolean grantPermissions(){
    	try{
        	ArrayList<String> queries = new ArrayList<String>();
        	queries.add("grant all privileges on directory " + schemaName + "_ts to " + schemaName);
        	queries.add("grant all privileges to " + schemaName);
        	queries.add("grant create session to " + schemaName);
        	queries.add("grant alter session to " + schemaName);
        	queries.add("grant create table to " + schemaName);
        	queries.add("grant create view to " + schemaName);
        	queries.add("grant create procedure to " + schemaName);
        	queries.add("grant create sequence to " + schemaName);
        	queries.add("grant create trigger to " + schemaName);
        	queries.add("grant create type to " + schemaName);
        	queries.add("grant connect to " + schemaName);
        	queries.add("grant execute on sys.dbms_session to " + schemaName);
        	queries.add("create or replace context abat_" + schemaName + "  using " + schemaName + ".abat_setcontext");

        	for(String query : queries){
            	dataHandler.runQuery(query);
        	}
    	}
    	catch(Exception e){
        	return false;
    	}
    	return true;
	}

    public AMGOraRefresh(){
		drop = false;
		verbose = false;
		dataHandler.setVerbose(false);
	}

    public void setVerbose(boolean a){
		verbose = a;
	}

    public void setDrop(boolean a){
		drop = a;
	}

    public void setDataSource(String a){
		dataSource = a;
	}

    public void setSchemaName(String a){
		schemaName = a;
		dataHandler.setUsername(a);
	}

    public void setPassword(String a){
		password = a;
		dataHandler.setPassword(a);
	}

    public void setSysPassword(String a){
		sysPassword = a;
	}

    public void setDumpFilePath(String a){
		dumpFilePath = a;
	}

    public void setImportVariable(String a){
		importVariable = a;
	}

    public boolean getVerbose(){
		return verbose;
	}

    public boolean getDrop(){
		return drop;
	}

    public String getDataSource(){
		return dataSource;
	}

    public String getSchemaName(){
		return schemaName;
	}

    public String getPassword(){
		return "";
	}

    public String getSysPassword(){
		return "";
	}

    public String getDumpFilePath(){
		return dumpFilePath;
	}

    public String getImportVariable(){
		return importVariable;
	}

    public void refresh(){
    	if(verbose){
        	System.out.println("Running refresh process...");
    	}

    	if(drop){
        	if(verbose){
            	System.out.println("Running re-create schema process...");
        	}

        	dropSchema(); // Drop schema
        	createSchema(); // Create schema
    	}

    	if(verbose){
        	System.out.println("Running import process...");
    	}

    	// impdp
    	String cmd = "impdp system/" + sysPassword + "@" + dataSource + " -dumpfile=" + importVariable + ":" + dumpFilePath;
    	cmd = cmd + " -logfile=" + importVariable + ":import.log";
    	cmd = cmd + " -remap_schema=" + schemaName + ":" + schemaName + " table_exists_action=REPLACE";

		new AMGSystem().runCMD(cmd);

    	if(verbose){
        	System.out.println("Running command:  " + cmd);
    	}

    	if(verbose){
        	System.out.println("Completed refresh process...");
    	}
	}
}
