#!/bin/bash
###############################################################################
# Name        : build.sh                                                      #
# Author      : Abel Gancsos                                                  #
# Version     : v. 1.0.0.0                                                    #
# Description : Helps to build a jar binary.                                  #
###############################################################################
APP_ROOT=$(dirname $0)/../
APP_NAME=jmagent
rm -fr $APP_ROOT/dist/*
rm -fr $APP_ROOT/bin/*
javac -cp ".:$APP_ROOT/lib/*" -d "$APP_ROOT/bin" `find $APP_ROOT/src -name *.java`
jar -cfm "$APP_ROOT/dist/$APP_NAME.jar" "$APP_ROOT/Manifest.txt" -C "$APP_ROOT/bin" .
if [ -f "$APP_ROOT/dist/$APP_NAME.jar" ]; then
	echo "JAR '$APP_ROOT/dist/$APP_NAME.jar' fresh out the oven and ready to be distributed..."
else
	echo "Failed to build jar..."
fi
