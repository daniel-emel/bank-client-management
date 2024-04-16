<h1 align="center"> Bank Client Management </h1> <br>

<p align="center">
  Spring Boot Demo Project For Storing Banking Clients
</p>


## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Requirements](#requirements)
- [Build and Deploy](#build-and-deploy)
- [Testing](#testing)
- [Documentation](#documentation)

## Features
* Storing client data in database, and populating it at startup
* Client entity has properties that are common in real life
* Client data validation
* CRUD methods provided by the REST API to manage clients
* REST API Endpoint that that returns the average age of the clients
* REST API Endpoint that returns all customers between 18 and 40 years old.
* Test for the controller and service layer to demonstrate that application works correctly.
* Several authentication methods (Basic, API-Key, mTLS) to secure the microservice
* TLS support for creating encrypted, secure communication channels.

## Requirements
* [Java 17](https://jdk.java.net/archive/)
* [Maven 3.8+](https://maven.apache.org)
* [Docker 25+](https://www.docker.com)
* [Windows (Optional)](https://www.microsoft.com/en-gb/windows/windows-11) Although the project is platform independent due to the technologies used, the .bat files I wrote for automating the build and deployment processes are Windows specific, so I recommend building and testing the application on Windows.

## Build and Deploy
### Requirements
* Make sure that your network (watch out for `company vpn`) allows access to public maven artifact repository.
* If you already have maven on your computer installed, then make sure that your `custom maven settings` (.m2) allow access to public maven artifact repository, and not only to private upstream repositories that do not have access to the public maven repository.
* The containerized microservice will be accessible via `port 8080` and the database via `port 3306` from localhost. So make sure that these ports are not in use on your computer. You can change them in `docker-compose.yml`
* Although the project platform is independent due to the technologies used, the .bat files I wrote are Windows specific, so I recommend building and testing the application on Windows. 

### Automated build and deployment
During development, I created three `.bat scripts to automate the build and deployment lifecycle`. These scripts are independent of each other. They can be executed in any order, but I prefixed them in the recommended order (start &rarr; stop &rarr; end) so they are easy to use.
* [start-clean-pull-build-deploy-start-project.bat](./devops/start-clean-pull-build-deploy-start-project.bat)
  * Use this script to `get a running build from the source code` with just executing one command.
* [stop-clean-docker-build.bat](./devops/stop-clean-docker-build.bat)
  * Use this script to `stop and clean up the currently running build`. This is sort of a sub-script (clean part) of the start-clean-pull-build-deploy-start-project.bat script.
* [end-clean-docker-project.bat](./devops/end-clean-docker-project.bat)
  * Use this script to `stop and clean up` not only the currently running `build` (containers), but all of the `Docker images` (even base images such as OpenJDK and MariaDB) `connected to this project`.

```shell
$ # Build and deploy the project in an automated way
$ cd [your-path-to-source-root]/devops
$ start-clean-pull-build-deploy-start-project.bat
```

```shell
$ # Stop and clean up the currently running build
$ cd [your-path-to-source-root]/devops
$ stop-clean-docker-build.bat.bat
```

```shell
$ # Stop and clean up the currently running build and all the images belonging to the project
$ cd [your-path-to-source-root]/devops
$ end-clean-docker-project.bat.bat
```

### Manual build and deployment
```shell
$ # Build and deploy your project manually
$ cd [your-path-to-source-root]/client-db
$ start-test-client-db.bat
$ cd ../client-backend
$ mvn clean package
$ ../client-db
$ stop-clean-test-client-db.bat
$ cd ..
$ docker-compose up -d
```

## Testing

### Postman
You can use [Postman](https://www.postman.com/) to test the microservice’s API and its different authentication methods.
I have created a postman collection, with the requests sorted into folders by authentication method. Use these precreated calls to thouroughly test the API.

#### Necessary configuration steps:
* Import the collection:
  * Collection: [bank_client_management.postman_collection.json](./test-the-app-in-action-with-an-http-client/bank_client_management.postman_collection.json)
  *	Find more information [here](https://learning.postman.com/docs/getting-started/importing-and-exporting/importing-data/#import-postman-data)
* Configure keystore used for calling the microservice:
  *	Preconfigured keystore: [test.http.client.p12](./test-the-app-in-action-with-an-http-client/test.http.client.p12)
  *	The keystore was generated by Keystore Explorer and contains the client key, client certificate and the (trusted) certificate of the client-backend microservice.
  *	During the certificate configuration, fill only the localhost, PFX and passphrase fields, leave the rest (CRT file, KEY file) empty.
  *	Find more information [here](https://learning.postman.com/docs/sending-requests/authorization/certificates/)

### Swagger and OpenAPI
I have added [OpenAPI](https://www.openapis.org/) and [Swagger](https://swagger.io/) support to the project.
After deployment, the standard Swagger page is automatically available at http://localhost:8080/swagger-ui/index.html
, which provides an interactive interface for testing API endpoints using the OpenAPI standard.
* I recommend using it when (m)TLS and all other authentication modes are disabled, because its authentication capabilities are limited.

## Documentation
For more information, please refer to the [documentation](./docs/Bank%20Client%20Management%20Documentation.docx) of the project.

