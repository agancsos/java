/*
	Name       : killswitch.java
	Author     : Abel Gancsos
	(c)        : Abel Gancsos Productions
	v.         : v. 1.0.0
	Description: This application makes a backup of core files and keeps track
                 of what files were found in each stage.  Folders include 
				 Downloads, Documents, Images, and Exchange files (if Windows).

				Note: It is recommended not to run this application from one of
				the listed directories.
*/

/*              IMPORTS                   */
import javax.swing.*;
/******************************************/

/*
	This is the main wrapper class for the application that sets up the 
	session object then runs the backup process.
*/
class killswitch{

	/*
		This is the command-line entry point that sets up the session object
		based on the command-line arguments.
	*/
	public static void main(String[] args){
		Boolean silent = false;
		AMGKillswitchGUI app = new AMGKillswitchGUI();

		if(args.length > 0){
			for(int i = 0; i < args.length; i++){
				if(args[i].equals("-s")){
					silent = true;
					app.silent = true;
				}
				else if(args[i].equals("-t")){
					app.target = args[i + 1];
				}
                else if(args[i].equals("-d")){
                    app.dbPath = args[i + 1];
                }
                else if(args[i].equals("-test")){
                    app.session.TEST = true;
                }
			}
		}
        try{
        	SwingUtilities.invokeLater(app);
		}
        catch(Exception e){
        }
		app.session.run();
		app.manualRefresh();
		app.exitApplication();
	}
}
