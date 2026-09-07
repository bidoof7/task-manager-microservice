# Task Manager Microservices

A personal **Java Spring Boot microservices project** built for learning, practicing backend architecture, and demonstrating production-oriented concepts during technical interviews.

> 🚧 **Work in Progress**
>
> This project is actively being developed. Some features and production-readiness improvements are still in progress. The architecture and implementation may evolve as I continue learning and refactoring the system.

---

## 📌 Overview

Task Manager is a backend-focused task management application designed around a **microservice architecture**.

The project started as a learning project and is gradually being extended with concepts commonly used in production backend systems, including:

* RESTful API
* Microservices architecture
* API Gateway
* JWT authentication
* Refresh token rotation
* Redis
* Apache Kafka
* Event-driven communication
* Transaction management
* Transactional Outbox Pattern
* Idempotent event processing
* Optimistic / pessimistic locking
* Rate limiting
* Docker & Docker Compose
* CI/CD
* Integration testing with Testcontainers
* Spring Boot Actuator

The primary goal is not to build a large-scale production system, but to **understand why these technologies and patterns are used and how they work together in a real backend system**.

---

# 🏗️ Architecture

```text
                         Client
                           │
                           ▼
                  ┌─────────────────┐
                  │   API Gateway   │
                  │   Port: 8080    │
                  └────────┬────────┘
                           │
             ┌─────────────┼─────────────┐
             │             │             │
             ▼             ▼             ▼
      ┌────────────┐ ┌────────────┐ ┌──────────────┐
      │    Auth    │ │    Task    │ │ Notification │
      │   Service  │ │   Service  │ │   Service    │
      │   :8081    │ │   :8082    │ │    :8083     │
      └─────┬──────┘ └─────┬──────┘ └──────┬───────┘
            │              │               │
            │              │               │
            ▼              ▼               │
         Redis          Database            │
            │                              │
            └──────────────┐               │
                           │               │
                           ▼               ▼
                      ┌────────────────────────┐
                      │      Apache Kafka       │
                      │   Event-driven flow     │
                      └────────────────────────┘
```

---

# 🧩 Services

## API Gateway

Responsible for:

* Single entry point for clients
* Routing requests to internal services
* Authentication / authorization integration
* Hiding internal service endpoints from clients

Default port:

```text
8080
```

---

## Auth Service

Responsible for:

* User registration
* Login
* JWT access token
* Refresh token
* Refresh token rotation
* Logout
* Authentication-related business logic

Default port:

```text
8081
```

Redis is used for refresh-token related functionality.

---

## Task Service

Responsible for:

* Task CRUD
* Task status management
* Task assignment
* Group/task relationships
* Task business rules
* Publishing task-related domain events

Default port:

```text
8082
```

This service is currently the main domain being used to practice:

* OOP
* Domain modeling
* Transaction management
* Transactional Outbox Pattern
* Concurrency control
* Event-driven architecture

---

## Notification Service

Responsible for:

* Consuming task-related events
* Processing asynchronous notifications
* Demonstrating Kafka consumer reliability
* Event-driven communication

Default port:

```text
8083
```

---

# 🔄 Event-Driven Communication

The project uses Apache Kafka for asynchronous communication between services.

For example:

```text
Task Service
     │
     │ TaskCreatedEvent
     ▼
   Kafka
     │
     ▼
Notification Service
     │
     ▼
Process notification
```

The project also explores the **Transactional Outbox Pattern** to solve the dual-write problem.

Instead of:

```text
Database Transaction
       +
Kafka Publish
```

the application can persist:

```text
Task
Outbox Event
```

within the same database transaction.

A separate process then publishes the event to Kafka.

```text
┌──────────────────────────┐
│      Task Service        │
│                          │
│  DB Transaction          │
│      │                   │
│      ├── Task            │
│      │                   │
│      └── Outbox Event    │
└────────────┬─────────────┘
             │
             ▼
       Outbox Processor
             │
             ▼
           Kafka
             │
             ▼
     Notification Service
```

---

# 🛠️ Tech Stack

## Backend

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* JWT
* Lombok

## Database & Storage

* Microsoft SQL Server
* Redis

## Messaging

* Apache Kafka

## Infrastructure

* Docker
* Docker Compose
* Nginx *(planned / deployment phase)*

## Testing

* JUnit
* Mockito
* Testcontainers *(in progress)*

## API Documentation

* OpenAPI
* Swagger UI

## CI/CD

* GitHub Actions

---

# 📁 Project Structure

```text
task-manager-microservice/
│
├── api-gateway/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── auth-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── task-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── notification-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── docker-compose.yml
├── .env.example
└── README.md
```

---

# ⚙️ Prerequisites

Make sure the following are installed:

* Git
* Java
* Maven
* Docker
* Docker Compose

Recommended:

```text
Java 8+
Docker
Docker Compose
Git
```

> The project currently targets Java 8 for compatibility with the project requirements and interview preparation.

---

# 🚀 Getting Started

## 1. Clone the repository

```bash
git clone https://github.com/happy-bmo/task-manager-microservice.git

cd task-manager-microservice
```

---

## 2. Create the environment file

Create a `.env` file from the example:

### Windows

```powershell
copy .env.example .env
```

### Linux / macOS

```bash
cp .env.example .env
```

Then update the values inside `.env`.

---

# 🔐 Environment Variables

The project uses environment variables for configuration instead of committing secrets directly into the repository.

Create:

```text
.env
```

Example:

```env
# ==============================
# SQL SERVER
# ==============================

MSSQL_SA_PASSWORD=ChangeThisPassword
MSSQL_DATABASE=task_manager


# ==============================
# REDIS
# ==============================

REDIS_HOST=redis
REDIS_PORT=6379


# ==============================
# KAFKA
# ==============================

KAFKA_BOOTSTRAP_SERVERS=kafka:9092


# ==============================
# JWT
# ==============================

JWT_SECRET=ChangeThisToALongRandomSecret
JWT_ACCESS_TOKEN_EXPIRATION=900000
JWT_REFRESH_TOKEN_EXPIRATION=604800000


# ==============================
# APPLICATION
# ==============================

SPRING_PROFILES_ACTIVE=docker
```

> ⚠️ **Never commit `.env` to Git.**

Make sure `.gitignore` contains:

```gitignore
.env
.env.*
!.env.example
```

---

# 🐳 Run with Docker Compose

Start the entire infrastructure:

```bash
docker compose up --build -d
```

Check running containers:

```bash
docker ps
```

Expected services:

```text
task-api-gateway
task-auth-service
task-task-service
task-notification-service
task-redis
task-kafka
task-sqlserver
```

---

# 🔍 API Access

When running locally:

```text
API Gateway:
http://localhost:8080
```

Services:

```text
Auth Service:
http://localhost:8081

Task Service:
http://localhost:8002

Notification Service:
http://localhost:8003
```

The recommended client entry point is the **API Gateway** rather than calling internal services directly.

---

# 📖 API Documentation

Swagger UI is available for the services when enabled.

Example:

```text
http://localhost:8080/swagger-ui/index.html
```

The exact Swagger endpoint may differ depending on the service configuration.

---

# 🧪 Testing

Run tests with Maven:

```bash
mvn test
```

For an individual service:

```bash
cd task-service
mvn test
```

Integration tests using Testcontainers are being added progressively.

---

# 🔒 Security

Authentication is based on:

```text
Login
  │
  ▼
Access Token + Refresh Token
  │
  ▼
Authenticated Request
  │
  ▼
API Gateway
  │
  ▼
Internal Service
```

JWT is used for access-token authentication.

Refresh tokens are handled separately and are intended to support token rotation and revocation.

Secrets such as:

* JWT secret
* Database credentials
* API credentials

are provided through environment variables.

---

# 📚 Concepts Being Practiced

This project is intentionally used as a learning environment for backend engineering concepts.

### Java / OOP

* Encapsulation
* Abstraction
* Inheritance
* Polymorphism
* Composition
* SOLID principles
* Collections
* Generics
* Exception handling
* Streams
* Concurrency

### Spring Boot

* Dependency Injection
* Bean lifecycle
* Spring Security
* Transaction management
* JPA/Hibernate
* Validation
* REST API design
* Configuration management
* Actuator

### Microservices

* Service boundaries
* API Gateway
* Inter-service communication
* Database-per-service considerations
* Fault tolerance
* Distributed transactions

### Event-Driven Architecture

* Kafka Producer
* Kafka Consumer
* Consumer groups
* Offset management
* Idempotent processing
* Consumer failure recovery
* Transactional Outbox
* Eventual consistency

### Database / Concurrency

* Transactions
* Isolation levels
* Optimistic locking
* Pessimistic locking
* Indexing
* Query optimization

### DevOps

* Docker
* Docker Compose
* Container networking
* Environment configuration
* GitHub Actions
* CI/CD
* VPS deployment *(planned)*

---

# 🗺️ Current Development Roadmap

The project is actively evolving.

Current focus:

```text
[x] Microservices architecture
[x] API Gateway
[x] JWT authentication
[x] Redis integration
[x] Kafka integration
[x] Basic event-driven communication
[x] Transactional Outbox foundation

[ ] Kafka consumer reliability
[ ] Complete Outbox reliability
[ ] Optimistic / pessimistic locking
[ ] Refresh token rotation
[ ] Rate limiting
[ ] Testcontainers integration tests
[ ] Spring Boot Actuator
[ ] Centralized logging
[ ] Monitoring
[ ] CI/CD deployment
[ ] VPS deployment
```

The checklist may change as the project evolves.

---

# 🎯 Project Goals

The main goals of this project are:

1. Strengthen Java and Spring Boot backend skills.
2. Understand microservice architecture through implementation rather than only theory.
3. Practice event-driven architecture with Kafka.
4. Understand distributed-system reliability problems.
5. Apply design patterns where they solve real problems.
6. Improve code quality through OOP, SOLID, and domain modeling.
7. Practice Docker and CI/CD.
8. Build a project that can be discussed during technical interviews.

---

# 🚧 Disclaimer

This is a **personal learning and interview demonstration project**, not a production system.

Some implementations are intentionally simplified, experimental, or still being improved.

The architecture is expected to evolve as new concepts are learned and applied.

The purpose of the project is to demonstrate:

> **understanding of backend engineering concepts and the ability to reason about architectural trade-offs**, rather than claiming that the system is production-ready.

---

# 👨‍💻 Author

**Dang Le Minh Duy**

Backend Developer

Interested in:

* Java
* Spring Boot
* Microservices
* Distributed Systems
* Event-Driven Architecture
* Docker
* Backend Engineering
