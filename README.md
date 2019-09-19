# Worldpay offer-service

Spring boot Rest API with embedded Database for creating offers by merchants. Every OFFER contains information for Merchant who created it and list with products that are included in it. Every offer has an expire date.

### Assignment

You are required to create a simple RESTful software service that will
allow a merchant to create a new simple offer. Offers, once created, may be
queried. After the period of time defined on the offer it should expire and
further requests to query the offer should reflect that somehow. Before an offer
has expired users may cancel it.

## Features
### offer-service has 3 main Services and one Scheduled Task:

* Merchant Service -> Proxy layer between Controller and DB that operate all kind of "merchants" business logic( create/get )
* Offer service -> Same thing but for the OFFER. Operates business logic for create/cancel/get offers. 
* Product Service -> Operates business logic for create/get of product items specific for each OFFER
* Cancel Expired offer schedule task -> Cancel every expired OFFER. Task is running based on a fixed rate which now is one minute, but best scenario is for every 12/24 hours.

### Springfox Swagger API documentation

* Swagger API documentation is available after start of the SERVICE on http://localhost:8025/swagger-ui.html
* There are example requests for every feature of the API. 
* Every endpoint inside of the API can be test from the Swagger

### 2 main CONTROLLERS

* Merchant Controller -> 3 endpoints ( create merchant/get all merchants/ get merchant by id
* Offer Controller -> 3 endpoints ( create merchant OFFER/get ACTIVE offers by merchant/cancel merchant offer )

### 3 main JPA entities

* Merchant
* Offer
* ProductItem

### OfferServiceException is thrown for negative scenarios

### There is Spring exception handler that handles all OfferServiceExceptions and build Error response with the specific HTTP status code for the scenario

### 3 Mappers that converts Request objects to JPA entities and JPA entities to Response objects

* Merchant Mapper
* Offer Mapper 
* Product Item Mapper

### 3 Main JPA Repositories that insert data into embedded Database 

* Merchant Repository
* Offer Repository
* Product Item Repository

### Unit and Integration Testing

* Mocking
* Every service has 100% code coverage for the business scenarios
* controller integration testing using MockMvc instance to setup a Spring MVC context with a web server


### Toolset

    Spring Boot
    Spring MVC
    Spring Data JPA
    Spring Data Rest
    Hibernate
    H2 Database
    Lombok
    Springfox Swagger UI
    Apache Common util library
    Maven
    GitHub for Mac OS
    Mockito
    MockMvc
    
## Prerequisites

* java version "11.0.4" 2019-07-16 LTS Java(TM) SE Runtime Environment 18.9
* IDEA or Eclipse IDE if you want to run build/run/debug source code easier
* Maven for build project

## Quickstart

Below all the commands to clone, build and run the project with Maven and Java 11 JDK:

    git clone https://github.com/IvanGTrendafilov/Worldpay-offer-service.git
    cd offer-service
    mvn clean install
    java -jar target/offer-service-0.0.1-SNAPSHOT.jar
    the embedded servlet container starts at http://localhost:8025
    Springfox Swagger documentation is available on http://localhost:8025/swagger-ui.html Every endpoint can be tested from there. There are examples in the UI


