#!/usr/bin/env python3
###############################################################################
# Name        : extract_maven_dependencies.py                                 #
# Author      : Abel Gancsos                                                  #
# Version     : v. 1.0.0.0                                                    #
# Description : Helps extract Maven dependencies from a lib directory.        #
###############################################################################
import os, sys;

if __name__ == "__main__":
	params               = {};
	for i in range(0, len(sys.argv) - 1):
		params[sys.argv[i]] = sys.argv[i + 1];
	lib_path             = params.get("-f", "");
	assert lib_path != "" and os.path.exists(lib_path), "\033[31mLibrary path must exist...\033[m";
	libraries            = os.listdir(lib_path);
	for lib in libraries:
		comps             = lib.replace(".jar", "").split("-");
		artifact_name    = "-".join(comps[:len(comps) - 1]);
		artifact_version = comps[len(comps) - 1];  
		print('''
        <dependency>
            <groupId></groupId>
            <artifactId>{0}</artifactId>
            <version>{1}</version>
        </dependency>'''.format(artifact_name, artifact_version));
