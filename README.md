# TaskFlow

TaskFlow is a **full-stack software engineering reference project** showcasing modern architecture with microservices, authentication, messaging, batch processing, and containerization.

## 🚀 Tech Stack

### Backend
- Java 17
- Spring Boot 3.x
- Spring Security with JWT
- Spring Data JPA
- Kafka (async messaging)
- Spring Batch (batch jobs)

### Frontend
- Vue.js 3 + Vuetify *(can be replaced with React or Angular)*

### Infrastructure
- Docker & Docker Compose
- H2 (dev database)
- Kafka (via Docker)
- Postman Collections for testing

## 🧩 Microservices

| Service              | Description                                    |
|----------------------|------------------------------------------------|
| `auth-service`       | JWT-based authentication & user management     |
| `task-service`       | Task CRUD with status and user ownership       |
| `notification-service` | Kafka consumer for task events                |
| `batch-service`      | Batch generation of task reports               |
| `frontend-vue`       | Task dashboard with login and task management  |

## 📦 Project Structure

```
taskflow/
│
├── auth-service/
├── task-service/
├── notification-service/
├── batch-service/
├── frontend-vue/
└── docker/
    ├── docker-compose.yml
    └── kafka/
```

## 🛠️ Getting Started

> Ensure you have the following installed:

- Java 17
- Docker + Docker Compose
- Node.js & npm (for frontend)

### 1. Clone the repository

```bash
git clone https://github.com/coelhoevandro/taskflow.git
cd taskflow
```

### 2. Run all services with Docker

```bash
docker-compose up --build
```

This command will start:
- All backend microservices
- Kafka (via `bitnami/kafka`)
- Frontend Vue app

### 3. Access the system

| Component             | URL                                |
|-----------------------|-------------------------------------|
| Vue App               | http://localhost:8080               |
| Auth Service          | http://localhost:8081               |
| Task Service          | http://localhost:8082               |
| Notification Service  | http://localhost:8083               |
| Batch Service         | http://localhost:8084               |
| Kafka UI (optional)   | http://localhost:8085               |
| H2 Console (Auth DB)  | http://localhost:8081/h2-console    |

> **H2 credentials:**  
> JDBC URL: `jdbc:h2:mem:authdb`  
> Username: `sa`  
> Password: *(leave empty)*

## 🧪 Postman Collections

Use the included Postman collection to test authentication and task operations.

> ✅ Collection coming soon...

## 📌 Project Goals

This project serves as a **Proof of Concept (PoC)** to demonstrate:
- Full-stack development expertise
- Microservices architecture
- Secure authentication and authorization
- Async messaging with Kafka
- Batch processing with Spring Batch
- CI-ready Dockerized architecture
- Clean code, testing and modular design
