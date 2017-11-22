/**
 * This is the main class used as the entry point from the command-line
 */
class JavaETL{

	public static void menu(){
		System.out.println(new AMGString("").padLeft(80,"="));
		System.out.println("* Name   : JavaETL");
		System.out.println("* Author : Abel Gancsos");
		System.out.println("* Version: v. 1.0.0");
		System.out.println("* Flags:");
		System.out.println("	* -f: Full path of the application file");
		System.out.println("	* -d: Type of data to extract");
		System.out.println("	* -b: Full path to the resources directory");
        System.out.println(new AMGString("").padLeft(80,"="));
	}
	
	public static void main(String[] args){
		boolean help = false;		
		AMGETL session = new AMGETL();

		if(args.length > 0){
			for(int i = 0; i < args.length; i++){
				if(args[i].equals("-h")){
					help = true;
				}
				else if(args[i].equals("-f")){
					session.setApplicationFile(args[i + 1]);
				}
                else if(args[i].equals("-d")){
                    session.setDataType(args[i + 1]);
                }
                else if(args[i].equals("-s")){
                    session.setSilent(true);
                }
				else if(args[i].equals("-b")){
					session.setBase(args[i + 1]);
				}
			}
		}

		if(help){
			menu();
		}
		else{
			session.run();
		}
	}
}
