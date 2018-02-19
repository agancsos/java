
class Main{
	public static void menu(){
    	System.out.println(new AMGString().padRight(80,"="));
    	System.out.println("* Name         : OraRefresh");
    	System.out.println("* Author       : Abel Gancsos");
    	System.out.println("* Version      : v. 1.0.0");
    	System.out.println("* Description  : This utility helps refresh an Oracle schema");
    	System.out.println("* Flags:");
    	System.out.println("   * -v: Verbose mode");
    	System.out.println("   * -d: Name of the data source (must be in TNSNAMES.ORA)");
    	System.out.println("   * -s: Name of the user schema (database)");
    	System.out.println("   * -p: Password to use for the schema");
    	System.out.println("   * -P: Password for the Oracle system user");
    	System.out.println("   * -f: Path to the dump file to import");
    	System.out.println("   * -i: Oracle directory name");
    	System.out.println("   * -D: Drop and re-recreate the schema");
    	System.out.println(new AMGString().padRight(80,"="));
	}
	
	public static void main(String[] args){
    	boolean help = false;
    	AMGOraRefresh session = new AMGOraRefresh();

    	// Read command-line parameters to configure envrionment
    	if((args.length - 1) > 0){
        	for(int i = 0; i < args.length; i++){
            	if(args[i].equals("-h")){
                	help = true;
            	}
            	else if(args[i].equals("-v")){
                	session.setVerbose(true);
            	}
            	else if(args[i].equals("-D")){
                	session.setDrop(true);
            	}
            	else if(args[i].equals("-d")){
                	session.setDataSource(args[i + 1]);
            	}
            	else if(args[i].equals("-s")){
                	session.setSchemaName(args[i + 1]);
            	}
            	else if(args[i].equals("-p")){
                	session.setPassword(args[i + 1]);
            	}
            	else if(args[i].equals("-P")){
                	session.setSysPassword(args[i + 1]);
            	}
            	else if(args[i].equals("-f")){
                	session.setDumpFilePath(args[i + 1]);
            	}
            	else if(args[i].equals("-i")){
                	session.setImportVariable(args[i + 1]);
            	}
        	}

        	if(help){
            	menu();
        	}
        	else{
            	session.refresh();
        	}
    	}
    	else{
        	menu();
    	}
	}
}
