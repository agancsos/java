package com.metTemplateGen;

import java.util.*;

import com.amgCommon.AMGSystem;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.io.*;

public class METTemplate {
    private String targetPath = "./";
    private String projectName = "TEMPLATE";

    public METTemplate(){

    }

    private String getBashText(){
        String script = "";
        script += "#!/bin/sh";
        script += "\n## Name       : make.sh";
        script += "\n## Author     :"; 
        script += "\n## Version    : v. 1.0.0";
        script += "\n## Description: Helps build the project on a Unix system";
        script += "\n";
        script += "\nAPP_ROOT=$(dirname %0)";
        script += "\n";
        script += "\n## Compile Java code";
        script += "\njavac \\";
	    script += "\n\t$APP_ROOT/*.java";
        script += "\n";
        script += "\n## Create Manifest file";
        script += ("\necho \"Main-Class: " + projectName + "Main\"  > $APP_ROOT/Manifest.txt");
        script += "\n";
        script += "\n## Create Jar file";
        script += "\njar cvfm \\";
	    script += ("\n\t$APP_ROOT/../bin/" + projectName + ".jar \\");
	    script += "\n\t$APP_ROOT/Manifest.txt \\";
	    script += "\n\t$APP_ROOT/*.class";
        return script;
    }

    private String getBatchText(){
        String script = "";
        script += "@echo off";
        script += "\nREM Name       : make.bat";
        script += "\nREM Author     :";
        script += "\nREM Version    : v. 1.0.0";
        script += "\nREM Description: Helps build the project on a Windows system";
        script += "\n";
        script += "\nREM Compile Java code";
        script += "\n\"%JAVA_HOME%\\bin\\javac.exe\" .\\*.java";
        script += "\n";
        script += "\nREM Create Manifest file";
        script += ("\necho Main-Class: " + projectName + "Main  > Manifest.txt");
        script += "\n";
        script += "\nREM Create Jar file";
        script += ("\n\"%JAVA_HOME%\\bin\\jar.exe\" cvfm ..\\bin\\" + projectName + ".jar Manifest.txt .\\*.class");
        return script;
    }

    public void setTargetPath(String a){
        targetPath = a;
    }

    public String getTargetPath(){
        return targetPath;
    }

    public void setProjectName(String a){
        projectName = a;
    }

    public String getProjectName(){
        return projectName;
    }

    private void deletePath(String path){
        File pathObject = new File(path);
        if(Files.isDirectory(pathObject.toPath())){
            File[] children = pathObject.listFiles();
            for(File child : children){
                deletePath(child.toPath().toString());
            }
        }
        try{
            Files.delete(pathObject.toPath());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void clean(){
        if(Files.exists(new File(String.format("%s/%s", targetPath, projectName)).toPath())){
                deletePath(String.format("%s/%s", targetPath, projectName));
        }
    }

    private void createDirectories(){
        String[] paths = {"bin", "lib", "doc", "src", "src/com"};

        // Create project folder
        if(! Files.exists(new File(String.format("%s/%s", targetPath, projectName)).toPath())){
            try{
                Files.createDirectories(new File(String.format("%s/%s", targetPath, projectName)).toPath());
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        if(Files.exists(new File(String.format("%s/%s", targetPath, projectName)).toPath())){
            if(Files.isDirectory(new File(String.format("%s/%s", targetPath, projectName)).toPath())){
                for(String path : paths){
                    if(! Files.exists(new File(String.format("%s/%s/%s", targetPath, projectName, path)).toPath())){
                        try{
                            Files.createDirectories(new File(String.format("%s/%s/%s", targetPath, projectName, path)).toPath());
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }        
            }
        }
    }

    private void createMakeScripts(){
        // Create bash
        AMGSystem file = new AMGSystem("", String.format("%s/%s/src/make.sh", targetPath, projectName));
        try{
            file.writeFile(getBashText());
        }
        catch(Exception e){

        }

        // Create batch
        file = new AMGSystem("", String.format("%s/%s/src/make.bat", targetPath, projectName));
        try{
            file.writeFile(getBatchText());
        }
        catch(Exception e){
            
        }
    }

    public void run(){
        // Create directories
        createDirectories();

        // Create make scripts
        createMakeScripts();
    }
}