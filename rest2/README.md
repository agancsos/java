# REST2

## Synopsis
The following project is a basic example of how the Spring Boot framework can be used to create a REST service WITHOUT Maven.  Why without Maven?  To prove that it's not actually needed and to prove that a REST service can be created with just the default tools in addition to the framework.

## Objectives
* Prove that a REST(ful) API can be built using Maven.
    * Service must come up
    * Must return a JSON response for GET requests (signleton and list)
    * Must accept JSON requests
* Dig deeper into Spring Boot.


## Assumptions
* Maven may or may not be used by the team.
* Tomcat may or may not be installed on the server.
* The service may or may not be ran from a local system.
* The service may or may not be developed using an IDE or Unix text editor.
* Spring is available as a framework in the infrastructure.
* Team has a firm foundation of general Java.

## Requirements
* The service will be built without Maven.
* The service will start without explicitly having a Tomcat server installed.
* The service will start up and will be accessible with at least 1 request.

## Implementation Details
As the project goes after creating a REST API without too many dependencies, I wanted to approach this from the notion that there could be a team that has Spring Boot built into the system infrastructure and has a solid foundation of Java in geenral, but might not be able to fully integrate the dependencies of Spring Boot.  In other words, use as much of the benefits that come with Spring Boot as possible, assuming that Maven cannot be used in the build process along with some other caveats considering it's an existing system.  In addition, we don't want to have a dependency on Tomcatm, if possible, so this is really where Spring Boot shines.

Now that we have a rough idea of how the infrastructure would look like, what do we need to build a REST or RESTful API?  Having had build REST services using MVC in the past, I didn't want to depend on MVC; however, as I would have learned later on, this was actually pretty important when generating the servlet mappings.  We could still lean on the Spring Boot framework for the heavy lifting, but MVC was still needed in our case so that we could reach the RestController.  

This then means that at the very least, we need the following classes:

### ServletConfigDispatcher class
Again, we implement this class as simply as possible just so that we can have the servlet mappings.

### SpringApplication class
This class should be a vanilla as possible so that we can take advantage of the Spring Boot framework.  The only thing we really implement here is the embedded Tomcat factory.  

### RestController class
This class is the main point of the project and simply implements the @RestController.  

The controller does depend on some sort of a "repository", which is implemented as a HashMap instead of a JPARepository.  Not for any specific reason other than to keep things simple, again, the purpose of this project is to be able to build the skeleton of the REST(ful) API without the non-Spring and Java dependencies (ie. Maven or Gradle).
The "repository" class depends on element items within the project, but aren't note worthy, just mentioning it to include them in the implementation details.  One thing I should mention about these models is that much like the REST API before, I had to implement my own JSON parsing, which I took issue with as I thought Spring had a more native way of serializing non-primitive types.  However, when I tried to look into any Java libraries, not just Spring, they all pointed to manually building out the JSON either manually buidling the string, building the JSONObject, or building out the JSONArray.  At the end of the day, these concerns really weren't in the scope of the project and I'm simply expressing concern for the future.

One more thing to mention about the RestController class is that I had to implement the PostMapping as a raw JSON string request because I kept getting a HTTP-415 status request and no matter what I did, I wasn't able to remove the ";charset=UTF-8" portion of the Content-Type header.  Some articles I found referenced:
1. Removing the Spring handler (Wasn't even explicitly adding this)
2. Adding the jackson-json dependency (Maven)
3. Using a raw ServletRequest (disgusting solution, but might be specific to MVC)

### Commands
|Command|Description|
|--|--|
|javac -cp ".:<path-to-spring-libs>/*" -d ./bin `find ./src -name *.java`  | Compile Java classes and drop them in the bin directory.  |
|jar -cfm dist/rest2.jar Manifest.txt -C ./bin .	                       | Create the jar binary.                                    |
|java -cp ".:./lib/*:./bin/" com.rest2.Rest2Application	                   | Run the REST service using the FQDN of the main class.    |
|java -cp ".:./lib/*:./bin/" com.rest2.Rest2Application --server.port=8083 | Run the REST service using a different port.              |


## Retrospective
* So, although it did take me 2 days to complete this project, it actually wasn't that bad.  The main issue I ran into was the tomcat servlet porting, which was fixed by re-adding the WebApp module as I had removed it during my debugging.  
* Another note is that although Maven may or may not make the packaging simpler, I believe using this approach offers much more control over what gets included and doesn't require that many dependencies.  I will admit that it did take more debugging, but not an unreasonable amount.
* The one thing I don't like is that JPA wasn't really used here as I created a "repository" based on a HashMap, but I don't believe such an implementation is so uncommon, at least in the C++ world.
* On the bright side, it did offer a chance to dig into Java, which I haven't done since picking up Go, and always brings me back to the early days of development for me.
* What was the biggest stumbling block?  Probably making the application work with Tomcat WITHOUT Tomcat.  Really wished Java web applications ran on standard apache2, but such is life.

## Final Thoughts
* Can it be done?  Absolutely, it's software and it's within the confines of the assembly.  BUT, would this be normally done in an enterprise environment?  Depends actually.  If the organization is more Java-driven, then ABSOLUTELY note, Maven/Gradle as well as JPA would be fully integrated in their build PROCESSES, but if being done in a more .Net or C++ organization, then probably not.  For the record, Ant would have been acceptable in this scenario as it only tells the compiler HOW to build the package, similar to MAKE or CMAKE.  The issue I had with Maven/Gradle for the purposes of this project is they download what they interpret as a dependency, which is cool in theory, but I wanted to make sure that the project is light even during runtime so the JVM doesn't get bloated with garbage.

* Where do we go from here?  I wish I could say a more realistic application using PostgreSQL and enterprise standards, but for now that's not in the pipeline.

## References
* https://spring.io/guides/tutorials/rest/
* https://spring.io/projects/spring-framework 
* https://repo.spring.io/ui/native/release/org/springframework/boot/spring-boot 
* https://github.com/spring-io/sagan 
* https://spring.io/guides/gs/spring-boot/ 
* https://spring.io/guides/gs/intellij-idea/ 
* https://spring.io/guides/gs/sts/ 
* https://www.youtube.com/watch?v=keP-eMsPOZA
* https://docs.spring.io/spring-boot/docs/current/reference/html/using.html 
* https://zetcode.com/springboot/webapplicationtype/
* https://www.youtube.com/watch?v=9SGDpanrc8U
* https://stackoverflow.com/questions/11492325/post-json-fails-with-415-unsupported-media-type-spring-3-mvc

