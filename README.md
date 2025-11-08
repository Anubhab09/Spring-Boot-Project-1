🌱 Spring Boot Demo Project












🧩 Overview

A Spring Boot REST API that demonstrates a clean, layered architecture and containerized PostgreSQL integration using Docker Compose.
The project includes fully functional CRUD operations and supports bulk insertions via JSON payloads in Postman.

⚙️ Tech Stack

Backend: Spring Boot 3.x

Language: Java 17+

Database: PostgreSQL (via Docker container)

Containerization: Docker + Docker Compose

Build Tool: Maven

Testing Tool: Postman

IDE: IntelliJ IDEA

📂 Project Structure
demo-project-1/

├── src/

│   ├── main/

│   │   ├── java/com/anubhab09/demo_project1/

│   │   │   ├── controller/     # REST endpoints

│   │   │   ├── service/        # Business logic layer

│   │   │   └── repository/     # Data persistence

│   │   └── resources/

│   │       └── application.properties

│   └── test/

│

├── docker-compose.yml          # PostgreSQL container setup

├── pom.xml                     # Maven dependencies

├── .gitignore

└── README.md


🐳 Docker Setup
1️⃣ Start PostgreSQL container
docker compose up -d


This spins up a PostgreSQL container with your specified:

Database: mydb

User: anubhab09

Password: (as per your docker-compose.yml)

2️⃣ Connect to PostgreSQL

Once running, verify connection:

docker compose exec db psql -U anubhab09 -d mydb

3️⃣ Check created table
\dt
SELECT * FROM users;

🧠 Features

✅ CRUD operations on User entity
✅ Bulk insert via Postman
✅ Layered architecture (Controller → Service → Repository)
✅ PostgreSQL persistence with Docker Compose
✅ Simple dependency injection using @Autowired
✅ Application ready for production-level enhancements

🚀 Run the Application
Option 1 — IntelliJ

Just click Run ▶️ on the main Spring Boot class.

Option 2 — Terminal
mvn spring-boot:run


Then test APIs on:
👉 http://localhost:8080/Users

📬 Sample API Requests (Postman)

Create a single user

POST http://localhost:8080/Users
{
  "name": "Anubhab",
  "email": "anubhab09@gmail.com"
}


Bulk insert users

POST http://localhost:8080/Users/bulk
[
  { "name": "John", "email": "john@gmail.com" },
  { "name": "Emma", "email": "emma@gmail.com" }
]


Get all users

GET http://localhost:8080/Users

🔮 Upcoming Enhancements

Add global exception handling (@ControllerAdvice)

Add DTOs and mapping layer

Add Swagger/OpenAPI documentation

Containerize full app (Spring Boot + PostgreSQL in one network)

👨‍💻 Author

Anubhab Saha
📧 prolificworld4u@gmail.com

🌐 GitHub Anubhab09
