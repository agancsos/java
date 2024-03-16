# PrayerRequests

## Synopsis
A solution to share prayer requests as well as praises so that we can support one another in our burdens and share in our joy.

## Assumptions
* There is a need for the solution.
* The solution will use all open or custom technologies.
* The solution may or may not run on Linux, macOS, or Windows.
* The solution will need a data store.
* The solution may or may not need to handle a large volume.
* The solution will require authentication.
* The solution will require an open user registration process.
* The solution will require safe sharing of information.

## Requirements
* The solution will allow users to signup.
* The solution will allow users to authenticate.
* The solution will allow users to view a list of prayer requests.
* The solution will allow users to submit new prayer requests.
* The solution will allow users to edit existing prayer requests.
* The solution will allow users to delete existing prayer requests.
* The solution will allow users to view others prayer requests if shared.
* The solution will allow users to befriend each other.
* The solution will allow users to confirm a friend request.
* The solution will allow users to defriend each other.

## Implementation Details

### PrayerRequestServer
The server is a non-socketted server used purely for processing the prayer requests as well as initializing the RESTful API. Upon starting of the service, it reads in the required configuration file to prepare the environment. It then bootstraps the RESTful API. Once the server is running, it periodically pops from an ActiveMQ queue manager using the MessageService singleton, processeses the prayer requests, and then updates a PostgreSQL database through the PrayerRequestService.

The PrayerRequestService is implemented as a singlet service and will remain in the JVM memory for the full lifetime of the server process. The PrayerRequestService class also leverages the DataService singleton service in order to communicate with the PostgreSQL database instance. It leverages JDBC along with the Postgres driver along with some custom mechanism to avoid connection exhaustion or rogue connections. To help with performance, we implement a CacheService singlet cache using the ehcache library using a strict heap size and a reasonable expiration to ensure that we don't use stale data. Upon call certain methods in the service, we may also on-demand update the live cache to ensure that the cache is most up-to-date.  Friends are managed through the FriendRequestService singleton service, for which the service methods are invoked as needed.

### PrayerRequest
The solution is also driven by one common structure that's used in all the components and services. The PrayerRequest model is made up of a small number of primitive fields and as such has manual marshaling to ensure consistent serialization and deserialization.  The solution also leverages a User model to safely share user data when navigating the portal.

#### Flags
| Flag                    |                                                           |
| -- | -- |
| -f                      | Full path to configuration file.                          |


#### Examples
```sh
mvn exec:java -Dexec.mainClass=MainClass -Dexec.args="-f config.json"
```

### AssetsAPI
The RESTful API leverages the Spring Boot framework and uses Model-View-Controller (MVC) components to expose an API through an embedded Tomcat server.  The API exposes the following endpoints, which require authorization via a session token.

#### Endpoints
| Endpoint                | Method    | Description                                   |
| -- | -- | -- |
| /api/signup             | POST      | Register as a user.                           |
| /api/aauth              | POST      | Generate a token using username:password.     |
| /api/requests/list      | GET       | Lists all prayer requests.                    |
| /api/requests/get       | GET       | Gets a specific request based on ID.          |
| /api/requests/add       | POST      | Adds a new request.                           |
| /api/friends/list       | GET       | List all friends.                             |
| /api/friends/add        | POST      | Submit a friend request to a user.            |
| /api/friends/remove     | POST      | Remove a friend.                              |
| /api/friends/approve    | POST      | Approve a friend request using email.         |

#### Examples
```sh
curl -XPOST http://localhost:8080/api/auth -H "Content-Type: application/json" -d '{"credentials": ""}                                # Authenticate and receive a session token
curl -XPOST http://localhost:8080/api/requests/add -H "Content-Type: application/json" -d '{"request": "", "public": 0, "shared": 1}' # Add a new shared prayer request
```

### Console
The solution comes with a light-weight front-end portal to display the transactions in the system using JavaServer Pages (JSP) and the internal service classes.  Authentication as well as registration is done through a local database of credentials.  Upon authenticating, a randomly generated oken is provided for successive requests for both console navigation and REST requests. 

## References

