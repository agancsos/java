# Transactions

## Synopsis
Solution to test payment transactions and to demonstrate knowledge of Java concepts.

## Assumptions
* There is a need for the solution.
* The solution will use all open-source technologies.
* ActiveMQ will already be configured and accessible.
* PostgreSQL will already be installed.
* All configurations required to run the solution will be provided by the user.
* The solution will use some sort of data source.
* The solution may or may not run on Linux, Windows, or macOS.
* The solution will provide a REST API.
* The solution will read a configuration file.
* The solution will leverage JSON data structures.

## Requirements
* The solution will be able to read a configuration file.
* The solution will be able to accept API requests.
* The solution will be able to read messages from MQ.
* The solution will be able to add new transactions.
* The solution will be able to list transactions.

## Implementation Details
The solution is made up of two main components.

### TransactionServer
The server is a non-socketted server used purely for processing the transactions as well as initializing the RESTful API.  Upon starting of the service, it reads in the required configuration file to prepare the environment.  It then bootstraps the RESTful API.  Once the server is running, it periodically polls an ActiveMQ queue, if one is configured, processes the messages, and then stores the transactions into a PostgreSQL database through the TransactionService.  

The TransactionService is implemented as a singlet service and will remain in the JVM memory for the full lifetime of the server process.  The TransactionService class also leverages two other primary singlet services.  First, there's the DataService in order to communicate with the PostgreSQL database instance.  It leverages JDBC along with the Postgres driver along with some custom mechanism to avoid connection exhaustion or rogue connections.

Then there's the MessageService, which leverages JMS and the ActiveMQ provider to push or pop messages in order to scale with the large number of requests.  All three of these singlet services depend on a ConfigurationService signlet service in order to know the endpoints and credentials to use for communications.  They also might depend on a SecurityService helper class for decoding base64 data.

### Transaction
The solution is also driven by one common structure that's used in all the components and services.  The Transaction model is made up of a small number of primitive fields and as such has manual marshaling to ensure consistent serialization and deserialization.

### TransactionAPI
The RESTful API leverages the Spring Boot framework and uses Model-View-Controller (MVC) components to expose an API through an embedded Tomcat server.  The API exposes two main endpoints.

#### Endpoints
| Endpoint                | Method    | Description                                   |
| -- | -- | -- |
| /api/get                | GET       | Lists all transactions for a specific symbol. |
| /api/add                | POST      | Adds a new transaction to the queue.          |

### Flags
| Flag                    | Description                      |
| -- | -- |
| -f                      | Full path to configuration file. |

### Examples
```sh
java -cp "dist/lib/*:dist/transactions.jar" MainClass -f ./config.json                                              # Starts the server and RESTful API
curl -XPOST http://localhost:8080/api/add -d '{}' -H "Content-Type: application/json" -H "Accept: application/json" # Add a new transaction for symbol TKX
curl -XGET http://localhost:8080/api/get?symbol=TRX -H "Accept: application/json"                                   # List all transactions for symbol TKQ
```

## References
* https://activemq.apache.org/components/classic/download/
* https://activemq.apache.org/components/classic/documentation/hello-world

