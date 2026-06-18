# Multi-Vendor Delivery System

A microservices-based food delivery platform built with Spring Boot and React. It supports multiple restaurants (vendors), customers, delivery drivers, and real-time order tracking.

---

## Architecture Overview

```
React Frontend (Vite + TypeScript)
         │
         ▼
   API Gateway :8765  ──── JWT validation + routing
         │
         ├──► UserAuthenticationService :9898
         ├──► RestaurantService         :8084
         ├──► OrderManagementService    :8083
         ├──► PaymentService            :8085
         └──► DeliveryAndLogisticService:8087
                    │
         RestaurantEurekaServer :8761  (service registry)

Message Bus: Apache Kafka
Caching:     Redis
```

---

## Services

| Service | Port | DB | Description |
|---|---|---|---|
| RestaurantEurekaServer | 8761 | — | Netflix Eureka service registry |
| ApiGateway | 8765 | — | Spring Cloud Gateway, JWT filter, routing |
| UserAuthenticationService | 9898 | MySQL | Registration, login, JWT issuance, WebSocket |
| RestaurantService | 8084 | MySQL | Restaurant/menu management |
| OrderManagementService | 8083 | MySQL | Order lifecycle management |
| PaymentService | 8085 | MySQL | Payment processing |
| DeliveryAndLogisticService | 8087 | PostgreSQL | Driver assignment, delivery tracking |

---

## Tech Stack

**Backend**
- Java 17, Spring Boot 3.x / 4.x
- Spring Cloud (Eureka, Gateway, OpenFeign, Resilience4j)
- Spring Security + JWT (jjwt)
- Spring Kafka, Spring Data JPA, Spring Data Redis
- MySQL, PostgreSQL

**Frontend**
- React 19, TypeScript, Vite
- React Router v7, Axios
- SockJS + STOMP (WebSocket)
- Lucide React

---

## Prerequisites

- Java 17+
- Node.js 18+
- Maven 3.8+
- MySQL (for most services)
- PostgreSQL (for DeliveryAndLogisticService)
- Apache Kafka + Zookeeper
- Redis

---

## Getting Started

### 1. Environment Variables

Copy `.env.example` to `.env` and set the required values, or export them in your shell:

```powershell
$env:DB_PASSWORD="your_mysql_password"
$env:DB_PASSWORD_POSTGRES="your_postgres_password"
$env:JWT_SECRET="your_base64_secret"          # must match across all services
$env:CORS_ALLOWED_ORIGINS="http://localhost:5173"
```

> The JWT_SECRET **must** be identical for `UserAuthenticationService` and `ApiGateway`.

### 2. Start Infrastructure

Start Kafka, Zookeeper, and Redis before the services.

### 3. Start Backend Services (in order)

```bash
# 1. Eureka Server
cd RestaurantEurekaServer && mvnw spring-boot:run

# 2. Core services (any order after Eureka)
cd UserAuthenticationService && mvnw spring-boot:run
cd RestaurantService         && mvnw spring-boot:run
cd OrderManagementService    && mvnw spring-boot:run
cd PaymentService             && mvnw spring-boot:run
cd DeliveryAndLogisticService && mvnw spring-boot:run

# 3. API Gateway (last)
cd ApiGateway && mvnw spring-boot:run
```

### 4. Start Frontend

```bash
cd react-frontend
npm install
npm run dev        # runs on http://localhost:5173
```

---

## User Roles

- **Customer** — browse restaurants, place orders, track delivery
- **Restaurant Owner** — manage menus, view and process orders
- **Delivery Driver** — view assigned deliveries, update status

---

## Security

See [docs/SecurityConfiguration.md](docs/SecurityConfiguration.md) for full details on:
- JWT secret configuration and rotation
- CORS setup
- Environment-based configuration
- Production secret management recommendations

---

## Project Structure

```
Multi-VendorDeliverSystem/
├── RestaurantEurekaServer/       # Service registry
├── ApiGateway/                   # Gateway + JWT filter
├── UserAuthenticationService/    # Auth service
├── RestaurantService/            # Restaurant/menu service
├── OrderManagementService/       # Order service
├── PaymentService/               # Payment service
├── DeliveryAndLogisticService/   # Delivery service
├── react-frontend/               # React/TypeScript SPA
├── frontend/                     # Legacy HTML dashboards
├── docs/                         # Documentation
└── .env                          # Local env config (gitignored)
```
