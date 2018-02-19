#!/bin/sh

APP_ROOT=$(dirname %1)

## Compile Java code
javac -cp .:./*.jar:../lib/*.jar $APP_ROOT/*.java  

## Create Manifest file
echo "Main-Class: Main\nClass-Path: *.jar sqlite-jdbc-3.16.1.jar\n"  > $APP_ROOT/Manifest.txt

## Create Jar file
jar cvfm \
	$APP_ROOT/../bin/OraRefresh.jar \
	Manifest.txt \
	$APP_ROOT/*.class \
