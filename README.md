# Personal Finance App

A personal finance management app built with a microservices architecture using **Spring Boot**, **Kafka**, **WebFlux**, **PostgreSQL**, **MongoDB**, and **Docker**.

## 📦 Architecture Overview

This project is composed of several microservices:

| Service            | Description                           |
| ------------------ | ------------------------------------- |
| `user-service`     | Manages user authentication and data  |
| `category-service` | Manages user-defined categories       |
| *(...)*            | *(Other services will be added soon)* |

>All services are registered with **Eureka** and communicate via **REST** and **Kafka events**.

## 🚧 Status

This project is currently in active development and evolving. Features, architecture, and services are being added and refined incrementally. Expect breaking changes and incomplete functionalities as development continues.  
Contributions and feedback are welcome!

## 🛠️ Tech Stack

* **Spring Boot**
* **Spring Cloud (Eureka, Feign)**
* **Spring Security + JWT**
* **Kafka (planned for event-driven communication)**
* **PostgreSQL & MongoDB**
* **Docker & Docker Compose (planned)**

## 📁 Folder Structure

```
/
├── user-service
├── category-service
├── ...
└── README.md (you are here)
```

## 🔜 Roadmap

* [x] Setup Eureka and service discovery
* [x] Create user-service with JWT authentication
* [x] Create category-service
* [ ] Add transaction-service
* [ ] Add analitics-service
* [ ] Add Kafka event publishing/subscribing
* [ ] Dockerize all services
> This roadmap is a work in progress and may evolve over time.
