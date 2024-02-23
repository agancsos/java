# PlusONE

## Synopsis
A solution that offers a way to collect unique opinions on topics through a poll mechanism and to build high level statistics.

## Assumptions
* The solution is needed.
* The solution may or may not run on Linux, Windows, or macOS.
* The solution will need some sort of a data store.
* The solution will need to be able to handle large volumes.
* The solution will use all open-source components.
* The solution will need to process votes in the background.
* The solution will need a web API.
* The solution will need a web interface.
* The web interface will leverage the web API.

## Requirements
* The solution will allow users to create new poll questions.
* The solution will allow users to submit new votes.
* The solution will not allow users to update or remove polls.
* The solution will not allow users to update or remove votes.
* The solution will allow users to list polls.
* The solution will allow users to list votes.
* The solution will allow users to submit votes programmatically.
* The solution will allow users to create polls programmatically.

## Implementation Details
The solution is made up of three main components.

### PlusOneServer
The server is a non-socketted server used purely for processing the votes as well as initializing the RESTful API.  Upon starting of the service, it reads in the required configuration file to prepare the environment.  It then bootstraps the RESTful API.  Once the server is running, it periodically polls an ActiveMQ queue, if one is configured, processes the messages, and then stores the votes  into a PostgreSQL database through the VoteService.  Another service, PollService, handles incoming requests to either create a new poll along with the options or list the available polls in the solution.

The VoteService is implemented as a singleton service and will remain in the JVM memory for the full lifetime of the server process.  The VoteService class also leverages two other primary singleton services.  First, there's the DataService in order to communicate with the PostgreSQL database instance.  It leverages JDBC along with the Postgres driver along with some custom mechanism to avoid connection exhaustion or rogue connections.  The PollService is also implemented as an independent singlton service, but focusses on Polls for separation of duties.

Then there's the MessageService, which leverages JMS and the ActiveMQ provider to push or pop messages in order to scale with the large number of requests.  All four of these singleton services depend on a ConfigurationService singleton service in order to know the endpoints and credentials to use for communications.  They also might depend on a SecurityService helper class for decoding base64 data.

#### Flags
| Flag                       | Description                                        |
| -- | -- |
| -f                         | Full path to the configuration file.               |


#### Examples
```sh
mvn exec:java -Dexec.mainClass=MainClass -Dexec.args="-f config.json"
```

#### Vote
The solution is also driven by two common structures that're used in all the components and services.  The Vote model is made up of a small number of primitive fields and as such has manual marshaling to ensure consistent serialization and deserialization.

#### Poll
Another structure that is used in many components is the Poll model, which holds information about the questions and options that users have available.

### PlusOneAPI
The RESTful API leverages the Spring Boot framework and uses Model-View-Controller (MVC) components to expose an API through an embedded Tomcat server.  The API exposes the following main endpoints through two controllers.

#### Endpoint
| Endpoint                   | Method    | Description                            |
| -- | -- | -- |
| /api/polls/list            | GET       | Lists all polls.                       |
| /api/polls/get             | GET       | Get a specific poll.                   |
| /api/polls/add             | POST      | Creates a new poll.                    |
| /api/votes/list            | GET       | Lists all votes for a poll.            |
| /api/votes/add             | POST      | Submits a vote for a poll.             |

### Web Interface
The web interface is exposed using the embedded Tomcat server and is implemented using standard HTML/JavaScript.  Upon navigating to the web interface, a list of polls will be displayed and when a poll is selected, the user is able to submit their submission.  Upon casting a vote, the JavaScript helper functions will extract the needed properties, including the client's public IP addresses for uniqueness of the vote.  We then call the proper API endpoint, which either gets added onto the ActiveMQ queue or directly into the PostgreSQL database, depending if the queue is configured and enabled.  The web interface also offers a way to create a new poll, which gets added directly to the database via the proper API endpoing.

## References
 
