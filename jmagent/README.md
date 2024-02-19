# Job Manager Agent

## Synopsis
This program is the Agent component of the Job Manager suite.  It's only purpose to to search through supported endpoints for relevant entries based on a user filter.

## Assumptions
* The user will have a valid configuration file.
* The user will start the Agent the the userData path.
* The data sources provide a list of entries in a parsable format.
* The data sources do not require a login.
* The user has internet access.
* The user copies or downloads the dependent libraries.
* The user has JRE 1.8.
* The user has a system that supports plist format.
* The Agent may or may not run in a Docker container.

## Requirements
* The Agent will run on macOS 10.14+.
* The Agent will use JRE 1.8 at runtime.
* The Agent will use the JSON Java library.
* THe Agent will use the JTidy Java library.
* The Agent will poll data sources on an interval.
* The Agent will update the jobs database with new companies.
* The Agent will update the jobs database with new results.
* The Agent will run until the user terminates it or the system goes down.

## Implementation Details
At the infrastructure level, the Agent simply uses a collection of services, reaching out to supported data sources, and then updates the SQLite database.

At the Objects level, the Agent uses a collection of models in the following services.

### DataService
This at the services level is considered to be the most critical as without this service, we're not able to update the jobs database.  It uses the internal data package for the models and the connection.

Updates to the database can be paused by setting the following plist value.
```plist
<key>safeMode</key>
<string>1</string>
```

### ProfileService
This service is considered to be the lightest seravice as all it does is retrieves the default profile details from the jobs database.

### CompanyService
This service is responsible for making sure the the companies are unique and for updating the database with new entries.

### ResultsService
This service is responsible for making sure the the results are unique and for updating the database with new entries.

### Flaga
|Flag|Description|
|--|--|
|-h,--help   | Print the help menu.                      |
|-p,--path   | Path to user data.                        |
|-l,--limit, | Limit of poll results.                    |

### Commands
|Command|Description|
|--|--|
|javac  -cp ".:./lib/*" -d ./bin `find ./src -name *.java`                     | Compile Java classes and drop them in the bin directory.  |
|jar -cfm dist/jmagent.jar Manifest.txt -C ./bin .                             | Create the jar binary.                                    |
|java -cp ".:./lib/*:./dist/*" com.jmagent.Main -p "<full-path-to-Job Monitor" | Run the CLI using the FQDN of the main class.             |

## References
