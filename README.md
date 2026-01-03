🌱 Spring Boot Microservices Project

🧩 Overview

This project started as a monolithic Spring Boot application and was later refactored into a microservices-based architecture to reflect real-world backend system design.

It is a User–Order–Inventory Management System, built entirely on the backend using Spring Boot, with service-to-service communication via REST and Kafka, PostgreSQL persistence, Redis caching, and Docker-based containerization.

This is not a demo CRUD app — it is an evolving, production-style backend system.

🧩 Microservices Breakdown

Service	Responsibility
User Service	User management, persistence, caching
Order Service	Order creation, user validation, event publishing
Inventory Service	Inventory tracking, order consumption via Kafka

⚙️ Tech Stack

Backend:

Spring Boot 3.x

Java 17+

Databases:

PostgreSQL (separate DB per service)

Caching:

Redis (used in User, Order, and Inventory services)

Inter-Service Communication:

REST APIs (User ↔ Order)

Kafka (Order → Inventory, Pub/Sub model)

Infrastructure:

Docker & Docker Compose

Kubernetes (used experimentally; Docker is primary runtime)

CI:

GitHub Actions (builds & pushes Docker images automatically)

Build Tool:

Maven


👨‍💻 Author

Anubhab Saha
System Engineer – TCS PRIME
📧 prolificworld4u@gmail.com

🌐 GitHub: Anubhab09
