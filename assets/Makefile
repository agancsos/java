###############################################################################
# Name        : Makefile                                                      #
# Author      : Abel Gancsos                                                  #
# Version     : v. 1.0.0.0                                                    #
###############################################################################
.DEFAULT_GOAL := build
IMAGE_NAME    := $(shell basename "$$PWD" )

build:
	which javac
	if [! -d bin ]; then mkdir bin; fi
	if [! -d dist ]; then mkdir dist; fi
	javac -cp "lib/*" $$(find . -name *.java) -d bin
	cp -r resources/META-INF bin
	jar -cfm dist/$(IMAGE_NAME).jar Manifest.txt -C ./bin .
	cp -r lib dist
	rm -fr bin/**

clean:
	rm -fr bin/**
	rm -f dist/$(IMAGE_NAME).jar
	rm -fr dist/lib
