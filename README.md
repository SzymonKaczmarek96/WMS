# WMS

This Warehouse Management System (WMS) is designed to streamline inventory management and stock tracking processes within warehouses. 
It provides functionalities for managing products, stocks, and storehouses efficiently.

Technologies Used
.Java
.Spring Boot
.Hibernate
.JUnit
.Docker

## Overview
The Warehouse Management System simplifies the handling of check stock tasks in warehouses.
It leverages Java, Spring Boot, Hibernate, JUnit, and Docker to ensure robustness, scalability, and ease of testing and deployment..

### Project Structure
.config: Contains configuration classes for Spring Boot application setup.
.controller: Handles HTTP requests and serves as the entry point for various operations.
.dto: Data Transfer Objects for transferring data between layers.
.entity: Entity classes representing database tables.
.exceptions: Custom exception classes for handling specific error scenarios.
.repository: Interfaces for database interaction using Spring Data JPA.
.service: Business logic implementations for handling warehouse operations.
.resources: Configuration files and static resources.
.test: Contains unit and integration tests for ensuring the reliability of the application.

##### Setup

1. Clone this repo
```
git clone https://github.com/SzymonKaczmarek96/WMS.git
```
2.Build the project using Maven
```
mvn clean install
```
```
mvn spring-boot:run
```

3.Docker Setup:
Create and start the Docker containers
```
docker-compose up
```
docker-compose configuartion
```
version: '3.8'

services:
  postgres:
    image: postgres:15
    ports:
      - "5432:5432"
    volumes:
      - db_data:/var/lib/postgresql/data
    environment:
      POSTGRES_PASSWORD: user
      POSTGRES_USER: postgres
      POSTGRES_DB: warehouse
volumes:
  db_data:
```
###### Endpoints

|HTTP Method|URL|Description|
|---|---|---|
|PRODUCT|
|`GET`|http://localhost:8000/products | Get list of products |
|`GET`|http://localhost:8000/products/{productName}| Get Product by product name |
|`POST`|http://localhost:8000/products/create | Create product by request body |
|`PUT`|http://localhost:8000/products/update/{productName} | Update product by request body |
|`DELETE`|http://localhost:8000/delete/{productName} | Delete product by productName |
|STOCK|
|`GET`|http://localhost:8000/stock | Get list of stocks |
|`GET`|http://localhost:8000/stock/{productName}| Get Stock by product name |
|`POST`|http://localhost:8000/stock/create/{productName}/{quantity} | Create stock by productName and quantity |
|`PUT`|http://localhost:8000/stock/update/{productName}/{quantity} | Update stock by productName and quantity |
|STOREHOUSE|
|`GET`|http://localhost:8000/storehouse | Get list of stocks |
|`POST`|http://localhost:8000/storehouse/{productName}| Create storehouse by request body |
|`DELETE`|http://localhost:8000/storehouse/{storehouseName} | Delete storehous by storehouseName |


