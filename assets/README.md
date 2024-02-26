# Assets

## Synopsis
Lightweight solution for catalogging and documenting technical assets on a local network.

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
* The solution will not cross networks.

## Requirements
* The solution will be able to read a configuration file.
* The solution will be able to accept API requests.
* The solution will be able to add new assets.
* The solution will be able to list assets.

## Implementation Details
The server is a non-socketted server used purely for processing the transactions as well as initializing the RESTful API.  Upon starting of the service, it reads in the required configuration file to prepare the environment.  It then bootstraps the RESTful API.  Once the server is running, it periodically scans the network, extracts metadata on each device, and then updates a PostgreSQL database through the AssetService.

The AssetService is implemented as a singlet service and will remain in the JVM memory for the full lifetime of the server process.  The AssetService class also leverages the DataService singleton service in order to communicate with the PostgreSQL database instance.  It leverages JDBC along with the Postgres driver along with some custom mechanism to avoid connection exhaustion or rogue connections.


#### Asset
The solution is also driven by one common structure that's used in all the components and services.  The Asset model is made up of a small number of primitive fields and as such has manual marshaling to ensure consistent serialization and deserialization.

#### Flags
| Flag                    |                                                           |
| -- | -- |
| -f                      | Full path to configuration file.                          |

### AssetsAPI
The RESTful API leverages the Spring Boot framework and uses Model-View-Controller (MVC) components to expose an API through an embedded Tomcat server.  The API exposes the following endpoints.

#### Endpoints
| Endpoint                | Method    | Description                                   |
| -- | -- | -- |
| /api/list               | GET       | Lists all assets.                             |
| /api/get                | GET       | Gets a specific asset based on ID.            |
| /api/add                | POST      | Adds a new asset.                             |

## References

