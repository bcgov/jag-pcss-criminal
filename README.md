# jag-pcss-criminal

[![Lifecycle:Experimental](https://img.shields.io/badge/Lifecycle-Experimental-339999) [![img](https://img.shields.io/badge/Chat-on%20RocketChat-%230f95d0.svg)](https://chat.developer.gov.bc.ca/group/efiling-hub-integration)

### Recommended Tools
* Intellij
* Docker
* Docker Compose
* Maven
* Java 11
* Lombok

## Summary

## Project Structure

### Required Environmental Variables

BASIC_AUTH_PASS: The password for the basic authentication. This can be any value for local.

BASIC_AUTH_USER: The username for the basic authentication. This can be any value for local.

ORDS_HOST: The url for ords rest package.

SPLUNK_HTTP_URL: The url for the spluck hec. For local splunk this value should be 127.0.0.1:8088 for
remote do not include /services/collector.

SPLUNK_TOKEN: The bearer token to authenticate the application.

SPLUNK_INDEX: The index that the application will push logs to. The index must be created in splunk

## Building the Project
1) Set intellij to use java 11 for the project modals and sdk
2) Run ``mvn compile``
3) Make sure ```target/generated-sources/xjc``` folder in included in module path for ```pcss-models ``` and ``` pcss-secure-modals```

## Running the Project
Option A) Intellij
1) Create intellij run configuration from PCSS Application
2) Set env variables. See the .env-template
3) Run the application

Option B) Jar
1) Run ```mvn package```
2) Run ```java -jar ./target/pcss-application.jar```

Option C) Docker
1) Run ```mvn package```
2) Run ```docker build -t pcss-civil-api .``` from root folder
3) Run ```docker run -p 8080:8080 pcss-civil-api```

Option D) Docker Compose
1) Run ```mvn package```
2) Run ```docker-compose up pcss-civil-api```

Option D) Eclipse
1) Clone the project into a local folder.
2) Import the Maven project using the Maven Project Import Wizard.
3) Set Variables either as Windows/Linux Environmental variables or POM goal Environment Variables:

BASIC_AUTH_PASS

BASIC_AUTH_USER

ORDS_HOST

SPLUNK_HTTP_URL

SPLUNK_TOKEN

SPLUNK_INDEX

4) Create POM goals: clean install, spring-boot:run  (when running locally).

### Pre Commit
1) Do not commit \CRLF use unix line enders
2) Run the linter ```mvn spotless:apply```

### JaCoCo Coverage Report
1) Run ```mvn clean verify```
3) Open ```pcss-code-coverage/target/site/jacoco-aggregate/index.html``` in a browser
