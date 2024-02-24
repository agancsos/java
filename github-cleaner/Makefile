###############################################################################
# Name        : Makefile                                                      #
# Author      : Abel Gancsos                                                  #
# Version     : v. 1.0.0.0                                                    #
# Description : Helps bootstrap this container.                               #
###############################################################################
.DEFAULT_GOAL := build
IMAGE_NAME    := $(shell basename "$$PWD" )

build:
	which javac
	javac -d bin -cp "lib/org.eclipse.jgit-6.8.0.202311291450-r.jar:src" $$(find . -name *.java)
	rm -fr bin/lib
	cp -r lib bin
	(cd bin/lib; jar -xf org.eclipse.jgit-6.8.0.202311291450-r.jar; jar -xf slf4j-api-2.0.9.jar)
	mv bin/lib/org bin
	rm -fr bin/lib
	jar -cfm dist/$(IMAGE_NAME).jar Manifest.txt -C ./bin .
	rm -fr bin/**

container:
	which docker
	docker build --platform linux/x86_64 -t $(IMAGE_NAME):latest .

run:
	which docker
	docker run -d --platform linux/x86_64 -e GITHUB_REPO=$(repo) -e DRY_RUN=$(dry_run) -e GIT_OUTPUT_PATH=$(output_path) -e SEARCH_PATTERN=$(pattern) --name $(name) --privileged  -t $(IMAGE_NAME):latest

test_container:
	which docker
	docker run --platform linux/x86_64 -e GITHUB_REPO=$(repo) -e DRY_RUN=$(dry_run) -e GIT_OUTPUT_PATH=$(output_path) -e SEARCH_PATTERN=$(pattern) --name $(name) --privileged  -t $(IMAGE_NAME):latest
	docker stop $(name)
	docker rm $(name)

test:
	make build
	java -jar dist/$(IMAGE_NAME).jar --repo test --dry 1 --pattern TEST -o /test

shutdown:
	which docker
	docker stop $(name); docker rm $(name)

