#!/bin/bash
###############################################################################
# Name        : build_package.sh                                              #
# Author      : Abel Gancsos                                                  #
# Version     : v. 1.0.0.0                                                    #
# Description : Helps to build a jar packages.                                #
###############################################################################
PACKAGES_ROOT=
PACKAGE_NAME=
PACKAGES_LIB_PATH=
PACKAGES=
while getopts "p:r:l:" option; do
    case $option in
        p) PACKAGE_NAME=$OPTARG ;;
		r) PACKAGES_ROOT=$OPTARG ;;
		l) PACKAGES_LIB_PATH=$OPTARG ;;
        *) ;;
    esac
done

if [ "$PACKAGES_ROOT" = "" ]; then
	echo "Packages root cannot be empty..."
	exit -255
else
	PACKAGES=$(ls $PACKAGES_ROOT)
fi

for package in ${PACKAGES[@]}; do
	if [ "$PACKAGE_NAME" != "" ] && [ "$package" != "$PACKAGE_NAME" ]; then
		continue
	fi
	if [ -d "$PACKAGES_ROOT/$package/dist" ]; then
		rm -fr $PACKAGES_ROOT/$package/dist/*
	else
		mkdir $PACKAGES_ROOT/$package/dist
	fi
	if [ -d "$PACKAGES_ROOT/$package/bin" ]; then
		rm -fr $PACKAGES_ROOT/$package/bin/*
	else
		mkdir $PACKAGES_ROOT/$package/bin
	fi
	javac -cp ".:$PACKAGES_LIB_PATH:./lib" -d "$PACKAGES_ROOT/$package/bin" `find $PACKAGES_ROOT/$package/src -name *.java`
	jar -cf "$PACKAGES_ROOT/$package/dist/$package.jar" -C "$PACKAGES_ROOT/$package/bin" .
	if [ -f "$PACKAGES_ROOT/$package/dist/$package.jar" ]; then
		echo "JAR '$PACKAGES_ROOT/$package/dist/$package.jar' fresh out the oven and ready to be distributed..."
	else
		echo "Failed to build $package..."
	fi
done
