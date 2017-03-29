/*
	Name       : killswitch.java
	Author     : Abel Gancsos
	(c)        : Abel Gancsos Productions
	v.         : v. 1.0.0
	Description:
*/

/*              IMPORTS                   */
import javax.swing.*;
/******************************************/

class killswitch{
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
