# Worldpay offer-service
Spring boot Rest API with embedded Database for creating offers by merchants. Every OFFER contains information for Merchant who created it and list with products that are included in it. Every offer has an expire date.

### offer-service has 3 main Services and one Scheduled Task:
* Merchant Service -> Proxy layer between Controller and DB that operate all kind of "merchants" business logic 
* Offer service -> Same thing but for the OFFER. Operates business logic for create/cancel/get offers.
* Product Service -> Operates business logic for create/get of product items specific for each OFFER
* Cancel Expired offer schedule task -> Cancel every expired OFFER. Task is running based on a fixed rate which now is one minute, but best scenario is for every 12/24 hours.

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

### Assignment

You are required to create a simple RESTful software service that will
allow a merchant to create a new simple offer. Offers, once created, may be
queried. After the period of time defined on the offer it should expire and
further requests to query the offer should reflect that somehow. Before an offer
has expired users may cancel it.
