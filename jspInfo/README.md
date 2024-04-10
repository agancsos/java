# jspInfo

## Synopsis
A lightweight test Apache Tomcat web application that displays information of the current Tomcat host.

## Assumptions
* There is a need for the application.
* The application will be portable.
* The application should include full deployment features.
* The application will be hosted on a remote Apache Tomcat host.
* The remote Apache Tomcat host may be Linux, Windows, or macOS.
* The application may or may not need system command access.

## Requirements
* The application will be lightweight and easily cleanable.
* The application will be able to be deployed to a remote Tomcat server.
* The application will be able to retrieve system information.

## Implementation Details
The solution leverages the Spring Boot framework with a non-embedded Tomcat server so that it can be deployed to any supporting JSP server.  Upon loading the .war into the Tomcat server, it bootstraps the application with the needed singleton service classes along with the servlets needed to render the JSP pages and RESTful API.  The most important class is the JSPInfoService class, which is used to construct the properties that will be exposed to the public.  This class leverages the SystemHelpers class when needed to retrieve information from the system that are otherwise not available through standard means.  When we navigate to the right path, we then display the public properties using standard JSTL functions and HTML DOM objects.  The solution is implemented in a scalable way where additional features and services can later be added or new solutions can be built ontop of the base application.


### Examples
```sh
mvn package tomcat7:redeploy -Dtomcat.server=CHANGEME -Dtomcat.username=CHANGEME -Dtomcat.password=CHANGEME
```

### Configuration
```json
{
    "environmentVariables": [
    ],
    "commands": {
    }
}
```

## References
* https://www.baeldung.com/spring-boot-no-web-server

