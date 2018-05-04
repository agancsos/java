@echo off
REM Name       : make.bat
REM Author     :
REM Version    : v. 1.0.0
REM Description: Helps build the project on a Windows system

REM Compile Java code
"%JAVA_HOME%\bin\javac.exe" com\amgCommon\*.java com\metTemplateGen\*.java

REM Create Manifest file
echo Main-Class: com.metTemplateGen.METTemplateGenMain  > Manifest.txt

REM Create Jar file
"%JAVA_HOME%\bin\jar.exe" cvfm ..\bin\METTemplateGen.jar Manifest.txt com\metTemplateGen\*.class com\amgCommon\*.class

