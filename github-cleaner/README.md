# GitHub Cleaner

## Synopsis
A utility to sanatize  GitHub repositories.

## Assumptions
* There is a need for the utility.
* The utility may or may not run on Linux, Windows, or macOS.
* The utility will have access to the public network.
* The utility may or may not run the payload.
* The utility may depend on certain system calls.
* The utility will depend on certain command-line programs.

## Requirements
* The utility will be able to access GitHub repositories.
* The utility will be able to clone GitHub repositories.
* The utility will be able to scan through directories.
* The utility will be able to replace text in source code files.
* The utility will be able to push changes to GitHub repositories.
* The utility will clean up after itself.

## Implementation Details
Implementation of the utility was done with an AWS Lambda Function or containerized approach in that the whole process can be done in a sandboxed environment.  Upon invoking the CLI, the utility will prepare the environment based on environment variables or command-line arguments and then invoke a single service class.  The service class will then reach out to GitHub to clone the repository using the JGit library and then remove text based on the provided search pattern via REGEX.  It will then commit the changes and push the changes back to GitHub.

### Flags
| Flag            | Description
| -- | -- |
| -o              | Base directory for Git clone.      |
| --repo          | Full URL to the GitHub repository. |
| --dry           | No system changes.                 |
| --pattern       | Pattern to sanatize.               |
| --pat           | PAT for GitHub.                    |

### Examples
```sh
make container     # Builds the Docker container image
make test          # Performs a smoke test
make run name=test # Start a container with the name test
make run name-test repo=https://github.com/jdoe/java.git dry_run=0 output_path=./output pattern=test123
```

```sh
java -jar dist/debian-cleaner.jar --repo https://github.com/jdoe/java.git --dry 0 123123123123123
```

## References
* https://www.eclipse.org/jgit/download/
* https://onecompiler.com/posts/3sqk5x3td/how-to-clone-a-git-repository-programmatically-using-java
* https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/
