# Eureka Server

## Description

This microservice acts as a Eureka server, responsible for service registration and discovery within the microservice ecosystem. It provides a central point for microservices to register themselves and discover other services dynamically.

## Basic Configuration

* Application name: `eureka-server`
* Runs on port: `8761`
* Configured *not* to register itself or fetch the registry from other instances (`register-with-eureka=false`, `fetch-registry=false`).

> Full configuration can be found in [`application.yaml`](src/main/resources/application.yaml).

## Main Endpoints

* Eureka Dashboard (UI): `http://localhost:8761/`
  Allows you to view registered services and their statuses.

## Technologies Used

* Spring Boot
* Spring Cloud Netflix Eureka Server

## Additional Notes

* This service does not register itself nor fetches the registry (`register-with-eureka: false`, `fetch-registry: false`).
* It is essential to have this service running for other microservices to register properly.
