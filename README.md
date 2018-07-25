
# Web Shop - Management APIs

> Set of RESTful APIs for handling the management of Web Shop.



## Development
(WIP)

### Project structure
- `src/` Source code, application resources and tests
  - `main/`
    - `docker/` files necessary to deploy the service as a Docker container
    - `java/dk/cristi/app/webshop/management`
      - `configs/` global configs for the project
      - `controllers/` classes specialized in handling requests; each endpoint/route is associated with a controller method, which normally would authenticate the user, validate the scopes, validate any input data, perform operation on resources (through service), and return the appropriate server status based on the outcome
      - `models/` representation of database tables as classes
      - `repositories/` data access classes
      - `security/` configuration classes for the security layer
      - `services/` classes specialized in handling data operations on models and more
    - `resources/` application properties & SQL specific per runtime profile
      - `META-INF/` internal Java meta directory (custom properties)
  - `tests/` test code for the project
    - `java/` test source code
      - `helpers/` utility code
      - `integration/` test integration of service with other services (auth, config, gateway, eureka)
      - `unit/` unit tests, no application context
        - `controllers/` controllers tests
      - `unit_context/` unit tests with application context
        - `controllers/` controllers tests
    - `resources/` resources specific for tests (usually application properties & SQL)


### Dependencies
The following tools and software are required before proceeding any further.   
These instructions assume Ubuntu 16.04 LTS as your OS.
If you're using a different system, you're on your own.
Make sure to run `sudo apt update` before installing any of the following.    
In case you stumble upon a dependency that you had to install but was not mentioned here, please update this section.

#### Java
(WIP)


#### Maven
(WIP)


#### MySQL
This is the database storing all the business logic data. It might come with your system. Alternatively, MariaDB can be installed instead, as they are fully interchangeable.
- Run: `sudo apt install mysql-server`
- Make sure to secure your DB instance, run: `mysql_secure_installation`

### Setup
(WIP)


## Deployment
(WIP)


## Usage

### Tools
It is recommended to use PostMan (a comprehensive HTTP request client) for testing and tinkering with the APIs.
It's quite intuitive to use and can be set up to automatically use authentication tokens and support multiple API locations using environment variables.   
Further options are available:
- deploy the front-end project [webshop-client-ui](https://github.com/Krismix1/webshop-client-ui) and run/inspect your request using a web browser debugging tool
- just use `curl`, the powerful HTTP client command line utility; it can easily be picked up by inspecting PostMan generated code (press the _Code_ orange button at the very right, under the big _Save_ button, then select _cURL_ from the dropdown)

### Authentication
(WIP)

### Responses
The API should return either JSON or nothing at all, in all of the cases.   
The following HTTP server states are used:
  - 200: OK (JSON data provided)
  - 204: OK (no data provided)
  - 400: Bad request (wrong JSON format, missing/incorrect data)
  - 401: Unauthorized (missing/expired token, wrong scopes, etc)
  - 422: Can't process entity (duplicate/missing data, wrong entity ID, grumpy database)
  - 5xx: Server error

> Please report any 500 server response, using the issue tracker. Remember to mention the requested endpoint, the body of the request (in case of PUT or POST), and any available log from the web server.



## System elements and concepts
(WIP)


## Milestones

- [ ] Unit tests
  - Controllers
    - [ ] CategoryResource
    - [ ] ProductResource
    - [x] ProductTypeResource
  - Services
  - Models
  - Repositories
  - Configs
- [ ] Unit with context
- [ ] Integration
- ...


### other tasks
- [ ] Add XML support, at least for returning content
- [ ] Swagger tests and configuration
- [ ] Create DB structure in SQL script (no auto DDL)
- [ ] ...
