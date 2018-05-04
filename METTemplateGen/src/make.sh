#!/bin/sh
## Name       : make.sh
## Author     : 
## Version    : v. 1.0.0
## Description: Helps build the project on a Unix system 

APP_ROOT=$(dirname %0)

## Compile Java code
javac \
	$APP_ROOT/com/metTemplateGen/*.java \
	$APP_ROOT/com/amgCommon/*.java \

## Create Manifest file
echo "Main-Class: com.metTemplateGen.METTemplateGenMain\n"  > $APP_ROOT/Manifest.txt

## Create Jar file
jar cvfm \
	$APP_ROOT/../bin/METTemplateGen.jar \
	$APP_ROOT/Manifest.txt \
	$APP_ROOT/com/metTemplateGen/*.class \
	$APP_ROOT/com/amgCommon/*.class \
