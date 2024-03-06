# CacheMoney

## Synopsis

## Assumptions
* There is a need for the solution.
* The solution will use all open-source technologies.
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
* The solution will be able to add new transactions.
* The solution will be able to list transactions.

## Implementation Details
The server is a non-socketted server used purely for processing the transactions as well as initializing the RESTful API.  Upon starting of the service, it reads in the required configuration file to prepare the environment.  It then bootstraps the RESTful API.  Once the server is running, it periodically pops from an ActiveMQ queue manager using the MessageService singleton, processeses the transaction, and then updates a PostgreSQL database through the TransactionService.

The TransactionService is implemented as a singlet service and will remain in the JVM memory for the full lifetime of the server process.  The TransactionService class also leverages the DataService singleton service in order to communicate with the PostgreSQL database instance.  It leverages JDBC along with the Postgres driver along with some custom mechanism to avoid connection exhaustion or rogue connections.  To help with performance, we implement a CacheService singlet cache using the ehcache library using a strict heap size and a reasonable expiration to ensure that we don't use stale data.  Upon call certain methods in the service, we may also on-demand update the live cache to ensure that the cache is most up-to-date.


#### Transaction
The solution is also driven by one common structure that's used in all the components and services.  The Transaction model is made up of a small number of primitive fields and as such has manual marshaling to ensure consistent serialization and deserialization.

#### Flags
| Flag                    |                                                           |
| -- | -- |
| -f                      | Full path to configuration file.                          |


#### Examples
```sh
mvn exec:java -Dexec.mainClass=MainClass -Dexec.args="-f config.json"
```

### AssetsAPI
The RESTful API leverages the Spring Boot framework and uses Model-View-Controller (MVC) components to expose an API through an embedded Tomcat server.  The API exposes the following endpoints.

#### Endpoints
| Endpoint                | Method    | Description                                   |
| -- | -- | -- |
| /api/transactions/list  | GET       | Lists all transactions for a given account.   |
| /api/transactions/get   | GET       | Gets a specific transaction based on ID.      |
| /api/transactions/add   | POST      | Adds a new transaction.                       |

#### Examples
```sh
curl -XPOST http://localhost:8080/api/transactions/add -H "Content-Type: application/json" -d '{"accountId": 1, "amount": 1000.00, "direction": 1}' # Add a new deposit transaction
curl -XPOST http://localhost:8080/api/transactions/add -H "Content-Type: application/json" -d '{"accountId": 1, "amount": 50.50, "direction": 0}'   # Add a new charge transaction
```

### Console
The solution comes with a light-weight front-end portal to display the transactions in the system using JavaServer Pages (JSP) and the internal service classes.  Authentication was left out as a wide variety of methods could be implemented and the solution is meant for demonstration or training purposes.

## References
* https://stackoverflow.com/questions/5935892/if-else-within-jsp-or-jstl
* https://www.geeksforgeeks.org/jstl-fn-length-function/
* https://www.baeldung.com/ehcache
* https://documentation.softwareag.com/terracotta/terracotta_10-7/webhelp/index.html#page/terracotta-db-webhelp/co-expiry.html

