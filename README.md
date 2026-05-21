# TaskFlow

A collaborative task management platform built with Java microservices.

TaskFlow was built to demonstrate practical microservices architecture using a real-world use case. It's not trying to be a perfect enterprise system — it's meant to show how an experienced backend engineer thinks about distributed systems, tradeoffs, and pragmatic design.

---

## What it does

- Users register and authenticate with JWT
- Tasks can be created, assigned, updated, and tracked
- When a task is assigned, a Kafka event triggers a notification for the assignee
- Notifications are stored in MongoDB and can be read from the frontend
- Everything runs with a single `docker-compose up`

---

## Screenshots

**Dashboard** — live stats, recent tasks, and overdue alerts in one view

![Dashboard](docs/screenshots/dashboard.png)

**Task list** — contextual quick-action buttons change based on current status (Start, Review, Done, Cancel, Reopen)

![Task list](docs/screenshots/task-list.png)

**Task detail** — full task view with comments, assignee, and status history

![Task detail](docs/screenshots/task-detail.png)

---

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                      Frontend (Vue.js)                  │
└──────────────────────────┬──────────────────────────────┘
                           │ HTTP
┌──────────────────────────▼──────────────────────────────┐
│                   Gateway Service :8080                 │
│           (Spring Cloud Gateway + JWT filter)           │
└──────┬──────────┬──────────────┬───────────────┬────────┘
       │          │              │               │
       ▼          ▼              ▼               ▼
  auth-service  user-service  task-service  notification-service
    :8081         :8082          :8083           :8084
  (PostgreSQL)  (PostgreSQL)  (PostgreSQL)    (MongoDB)
                                  │
                              Kafka events
                                  │
                                  ▼
                        notification-service
                          (async consumer)
```

**Each service owns its own database.** There are no shared tables or direct DB connections between services. They communicate via REST (synchronous) or Kafka (asynchronous).

---

## Services

### auth-service
Handles registration, login, token issuance and refresh. Nothing else.

JWT access tokens expire in 15 minutes. Refresh tokens last 7 days and are stored in the database so they can be revoked.

### user-service
Stores user profiles (name, email, avatar, etc). The auth-service handles passwords and tokens; user-service handles everything about the person.

Exposes a simple feign client that other services use to validate users without coupling to auth.

### task-service
The core of the application. Tasks can be created, assigned, have their status updated, and have comments added.

Uses a lightweight CQRS approach — commands (create, update, assign) go through separate handlers from queries (list, filter, search). This wasn't necessary at this scale, but it keeps write logic clean and makes it easier to optimize reads later if needed.

When a task is assigned, task-service emits a `task.assigned` Kafka event. It doesn't care what happens next — that's the point of event-driven.

### notification-service
Listens for Kafka events and creates notifications in MongoDB. Also handles a basic saga compensation: if the target user doesn't exist (maybe they were deleted), it emits a `notification.result` failure event that task-service uses to roll back the assignment.

MongoDB was chosen here because notifications are append-only, schema-flexible, and rarely need complex joins. It's a good fit.

### gateway-service
Single entry point. Validates JWT tokens before forwarding requests. Route configuration is in YAML — straightforward and readable.

No service discovery here. With 5 services in Docker Compose, hardcoded URLs in environment variables are fine. If this grew to 20+ services, something like Eureka or Kubernetes service mesh would make sense.

---

## Saga Pattern (Choreography)

The assignment flow is a simple choreography saga:

```
1. POST /tasks/{id}/assign
   → task-service validates task exists and is not already assigned
   → saves assignment (status: PENDING_NOTIFICATION)
   → emits: task.assigned

2. notification-service receives task.assigned
   → calls user-service to verify assignee still exists
   → if OK: creates notification, emits: notification.result { success: true }
   → if NOT OK: emits: notification.result { success: false } (compensation)

3a. task-service receives notification.result (success)
    → updates task assignment status to CONFIRMED

3b. task-service receives notification.result (failure)
    → rolls back assignment (assigneeId: null, status: UNASSIGNED)
    → could trigger a retry or alert (simplified here)
```

No orchestration engine was used. For a system this size, choreography is simpler and easier to follow. An orchestrator (like Temporal or AWS Step Functions) would make sense if the saga had 6+ steps or complex branching.

---

## CQRS (Light)

CQRS is applied only in task-service, and only where it adds value:

- **Commands**: `CreateTaskCommand`, `AssignTaskCommand`, `UpdateTaskStatusCommand` — each handled by a dedicated handler class
- **Queries**: `TaskQueryService` — optimized projections, filtering, pagination

This avoids mixing write validation logic with read optimization. It's not a full event-sourced CQRS system, and it doesn't need to be.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| Gateway | Spring Cloud Gateway |
| Auth | Spring Security + JWT |
| ORM | Spring Data JPA (Hibernate) |
| HTTP Client | OpenFeign |
| Messaging | Apache Kafka |
| Relational DB | PostgreSQL 16 |
| Document DB | MongoDB 7 |
| Migrations | Flyway |
| Frontend | Vue.js 3 + Vuetify 3 + Pinia |
| Containers | Docker + Docker Compose |
| Build | Maven |

---

## Running locally

**Prerequisites:** Docker and Docker Compose installed.

```bash
git clone https://github.com/yourusername/taskflow.git
cd taskflow
docker-compose up --build
```

First startup takes a few minutes while images are pulled and services initialize.

Once running:
- Frontend: http://localhost:3000
- Gateway API: http://localhost:8080
- Auth Service: http://localhost:8081
- User Service: http://localhost:8082
- Task Service: http://localhost:8083
- Notification Service: http://localhost:8084

**Demo accounts (seeded automatically):**

| Email | Password | Role |
|---|---|---|
| admin@taskflow.dev | admin123 | ROLE_ADMIN |
| sarah.pm@taskflow.dev | user123 | ROLE_USER |
| carlos.dev@taskflow.dev | user123 | ROLE_USER |
| julia.qa@taskflow.dev | user123 | ROLE_USER |

---

## API

Import the Postman collection from `postman/TaskFlow.postman_collection.json`.

All requests go through the gateway at `http://localhost:8080`. Auth endpoints are public; everything else requires a `Bearer` token.

Quick test:
```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@taskflow.dev","password":"admin123"}'

# List tasks (use the token from above)
curl http://localhost:8080/api/tasks \
  -H "Authorization: Bearer <token>"
```

---

## Project Structure

```
taskflow/
├── auth-service/          # JWT auth, registration, token refresh
├── user-service/          # User profiles
├── task-service/          # Core task management + CQRS + Saga
├── notification-service/  # Kafka consumer + MongoDB storage
├── gateway-service/       # API gateway + JWT validation
├── frontend-app/          # Vue.js 3 SPA
├── infra/                 # Docker init scripts
├── postman/               # API collection
└── docker-compose.yml
```

---

## Observability

Every request gets a `X-Correlation-Id` UUID that is carried through the entire system. This makes it possible to trace a full flow across all services and Kafka events using a single grep.

**How it flows:**

```
Browser request
  → Gateway (CorrelationIdWebFilter)
      generates UUID if missing, adds X-Correlation-Id to response header

  → task-service (CorrelationIdFilter)
      extracts header into MDC → all logs tagged [cid=<uuid>]

  → Kafka: task.assigned
      AssignTaskHandler writes correlationId as a ProducerRecord header

  → notification-service (TaskEventConsumer)
      extracts header from ConsumerRecord, sets MDC → logs tagged with same cid
      propagates cid in notification.result reply event

  → task-service SagaEventConsumer
      extracts header from ConsumerRecord, sets MDC → saga logs tagged with same cid
```

All log lines across all services share the same format:
```
HH:mm:ss.SSS LEVEL [service-name] [cid=<uuid>] [uid=<uuid>] Logger - message
```

To trace a full assignment flow:
```bash
# replace with the correlation ID from the X-Correlation-Id response header
docker-compose logs | grep "cid=<your-uuid>"
```

**Gateway note:** Spring Cloud Gateway is reactive (WebFlux). MDC doesn't propagate automatically in reactive pipelines, so the gateway logs the correlation ID inline rather than via MDC. All other services are servlet-based and use standard MDC propagation.

---

## Tradeoffs & Decisions

**Why not Eureka/Consul for service discovery?**  
With 5 services in Docker Compose, it's overkill. Environment variables with hostnames are simpler and less infrastructure to maintain. When this moves to Kubernetes, service discovery comes built-in.

**Why synchronous auth validation in the gateway?**  
The gateway validates JWT locally (no network call) using the shared secret. This is fast and doesn't create a dependency on auth-service being up for every request.

**Why MongoDB for notifications?**  
Notifications are append-only documents that don't need complex relations. MongoDB makes the schema flexible (different notification types can have different payloads) and reads are fast since we just query by user ID.

**Why not Spring Cloud Config?**  
Environment variables in docker-compose.yml are sufficient for this setup. A config server adds complexity without much benefit until you have many more services or environments.

**Why choreography saga and not orchestration?**  
The assignment flow has 3 steps. Choreography is easier to understand and debug at this scale. An orchestrator would add infrastructure (another service, another DB) for something that doesn't need it yet.

---

## Roadmap

### Phase 1 (current)
- [x] Java microservices backend
- [x] Vue.js 3 frontend
- [x] JWT authentication with refresh tokens
- [x] Kafka event-driven communication
- [x] Choreography-based saga for task assignment
- [x] Lightweight CQRS in task-service
- [x] Docker Compose setup
- [x] Demo data seeding
- [x] Structured logging with correlation ID propagation across services and Kafka events

---

## License

MIT
