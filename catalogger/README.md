# Catalogger

## Synopsis
As libraries might have a large collection of media that are still managed via paper and the provided technology is not used, it's evident that a modernization is needed for software-provided cataloging.
 
## Assumptions
* The infrastructure may or may not be on a Linux, macOS, or Windows system.
* The software will need to use a server-API-client architecture.
* All components must either be open-source or built in-house.
* The catalog must be able to support multiple media types.
* The solution might be reused by other organizations.
 
## Requirements
* The solution will be able to add new users to the catalog.
* The solution will be able to update existing users in the catalog.
* The solution will be able to remove existing users from the catalog.
* The solution will be able to add new media to the catalog.
* The solution will be able to update existing media in the catalog.
* The solution will be able to remove existing media from the catalog. 
* The solution will be able to add media log entries to the catalog.
* The solution will be able to detect lateness on a return.
* The solution will be able to calculate late fees based on a simple formula.
* The solution will be able to detect available copies.
* The solution will be customizable.
* The solution will be able to extract data from the barcode.
* The solution will be able to detect physical location of media.

## Implementation Details

### Configuration
| Key                         | Description                                                        |
|--|--|
| traceLevel                  | Level of the tracing.  Possible values are 0-4.                    |
| organization                | Name of the organization using the solution.                       |
| database.serverName         | Host of the PostgreSQL server.                                     |
| database.database           | Name of the database for the platform.                             |
| database.username           | Username for the database.                                         |
| database.password           | Custom-encrypted password for the database.                        |
| database.driver             | unixODBC driver to use to connect to the platform database.        |

### Endpoints
| Path                      | Method  | Description                                                          |
|--|--|--|
| /api/version/                 | GET     | Retrieves the version of the server                              |
| /api/messages/                | GET     | Retrieves the message logs                                       |
| /api/heartbeat/               | GET     | Pings the authenticated user                                     |
| /api/create/                  | GET     | Retrieves the JSON structure for the specified object type       |
| /api/isbn/                    | GET     | Retrieves available information on an ISBN                       |
| /api/user/                    | GET     | Retrieves sharable information about the authenticated user      |
| /api/profile/getbyname/       | POST    | Retrieves sharable information about the specified user          |
| /api/profile/list/            | GET     | Lists all sharable user information                              |
| /api/profile/add/             | POST    | Add a user                                                       |
| /api/profile/update/          | POST    | Update the authenticated user                                    |
| /api/profile/delete/          | POST    | Remove a authenticated user                                      | 
| /api/profile/updatesettings/  | POST    | Update the settings for the authenticated user                   |
| /api/auth/                    | POST    | Authenticate                                                     |
| /api/group/add/               | POST    | Add a new user group                                             |
| /api/group/remove/            | POST    | Remove a user group                                              |
| /api/group/getall/            | POST    | Alias for list                                                   |
| /api/group/list/              | POST    | Get all user groups                                              |
| /api/group/ismember/          | POST    | Checks if a user is a member of the specified group              |
| /api/group/addmember/         | POST    | Adds a member to a user group                                    |
| /api/group/removemember/      | POST    | Remove a user from a user group                                  |
| /api/group/haspermissions/    | POST    | Checks if a user has the required permissions                    |
| /api/authors/list/            | GET     | Lists all authors                                                |
| /api/authors/get/             | GET     | Retrieves a specific author                                      |
| /api/authors/add/             | POST    | Adds a new author                                                |
| /api/authors/update/          | POST    | Updates an existing author                                       |
| /api/authors/remove/          | POST    | Removes an existing author                                       |
| /api/publishers/list/         | GET     | Lists all publishers                                             |
| /api/publishers/get/          | GET     | Retrieves a specific publisher                                   |
| /api/publishers/add/          | POST    | Adds a new publisher                                             |
| /api/publishers/update/       | POST    | Updates an existing publisher                                    |
| /api/publishers/remove/       | POST    | Removes an existing publisher                                    |
| /api/customers/list/          | GET     | Lists all customers                                              |
| /api/customers/get/           | GET     | Retrieves a specific customer                                    |
| /api/customers/add/           | POST    | Adds a new customer                                              |
| /api/customers/update/        | POST    | Updates an existing customer                                     |
| /api/customers/remove/        | POST    | Removes an existing customer                                     |
| /api/titles/list/             | GET     | Lists all titles                                                 |
| /api/titles/get/              | GET     | Retrieves a specific title                                       |
| /api/titles/add/              | POST    | Add a new title                                                  |
| /api/titles/update/           | POST    | Updates an existing title                                        |
| /api/titles/remove/           | POST    | Removes an existing title                                        |
| /api/titles/copies/           | GET     | Lists all title copies for a specific title                      |
| /api/titles/addcopy/          | POST    | Adds a copy for an existing title                                |
| /api/titles/removecopy/       | POST    |	Removes a copy for an existing title                             |
| /api/borrows/list/            | GET     | Lists all rentals for a specific customer                        |
| /api/borrows/get/             | GET     | Retrieves a specific rental                                      |
| /api/borrows/checkout/        | POST    | Marks a copy of a title as checked out by a customer             |
| /api/borrows/checkin/         | POST    | Marks a copy of a title as checked in by a customer              |
| /api/borrows/markpaid/        | POST    | Marks a late rental as paid by a customer                        |
| /api/borrows/late/            | GET     | Checks if a rental was checked in late and by how many days      |

## Configuration
```json
{
    "database": {
        "provider": "postgres",
        "server": "",
        "database": "",
        "dbaUsername": "",
        "dbaPassword": "",
        "username": "",
        "password": ""
    },
    "mq": {
        "url": "",
        "username": "",
        "password": "",
        "queue": "",
        "disabled": true
  }
}
```

## Examples
```sh
mvn package tomcat7:redeploy -Dtomcat.server=CHANGEME -Dtomcat.username=CHANGEME -Dtomcat.password=CHANGEME
```

## References
* https://www.baeldung.com/spring-boot-no-web-server

