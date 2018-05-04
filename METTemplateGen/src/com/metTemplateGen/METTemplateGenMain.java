package com.metTemplateGen;

public class METTemplateGenMain{
    public static void helpMenu(){
        System.out.println(METTemplateGenHelper.padRight("", 80, "="));
        System.out.println("* Name      : MetTemplateGenerator");
        System.out.println("* Author    : Abel Gancsos");
        System.out.println("* Version   : v. 1.0.0");        
        System.out.println("* Flags      :");
        System.out.println("    * -p: Full path to generate tempate");
        System.out.println("    * -c: Remove the project");
        System.out.println(METTemplateGenHelper.padRight("", 80, "="));
    }

    public static void main(String[] args){
        boolean help = false;
        boolean clean = false;
        METTemplate session = new METTemplate();

        if(args.length > 0){
            for(int i = 0; i < args.length; i++){
                if(args[i].equals("-h")){
                    help = true;
                }
                else if(args[i].equals("-p")){
                    session.setTargetPath(args[i + 1]);
                }
                else if(args[i].equals("-c")){
                    clean = true;
                }
            }
        }
        if(help){
            helpMenu();
        }
        else if(clean){
            session.clean();
        }
        else{
            session.run();
        }
    }
}