# Flash Sale System

This project is a high-performance, scalable API designed to handle Flash Sale scenarios. It is built as a study in **Scalability** and **Clean Code** concepts, utilizing a modern Java stack and robust infrastructure components.

## 🚀 Key Features
- **High Concurrency Handling:** Designed to accept a massive spike in order requests without crashing.
- **Asynchronous Processing:** Decouples order ingestion from order fulfillment to guarantee low latency for users.
- **Clean Architecture:** Implements a Hexagonal (Ports and Adapters) architecture for maintainability and testability.
- **Observability:** Full tracing and monitoring stack to identify bottlenecks and track request flows.

## 🛠️ Technology Stack
- **Language:** Java 21
- **Framework:** Spring Boot 3
- **Database:** PostgreSQL (with Flyway for migrations)
- **Messaging/Event Streaming:** Apache Kafka
- **Caching & Locking:** Redis
- **Observability:** Prometheus, Grafana, Jaeger, OpenTelemetry, Micrometer
- **Load Testing:** K6

## 📚 Documentation
Detailed documentation on specific architectural and technical decisions can be found in the following files:

- [ARCHITECTURE.md](ARCHITECTURE.md): Deep dive into the Clean/Hexagonal Architecture implementation.
- [SCALABILITY.md](SCALABILITY.md): How the system handles high loads, including Kafka, Redis, and K6 stress testing.
- [OBSERVABILITY.md](OBSERVABILITY.md): Guide to the monitoring and distributed tracing stack.

## 🚦 Quick Start (Local Development)

The project includes a comprehensive `compose.yaml` file that spins up the entire infrastructure (Postgres, Kafka, Redis, Prometheus, Grafana, Jaeger).

### Prerequisites
- Docker & Docker Compose
- Java 21 (if running locally outside of Docker)
- Maven

### Running the Infrastructure

To start all required services:
```bash
docker-compose up -d
```

### Running the Application

You can start the Spring Boot application using the Maven wrapper:
```bash
./mvnw spring-boot:run
```
*(Note: Since `spring-boot-docker-compose` is in the dependencies, Spring Boot might automatically manage the compose lifecycle if configured to do so, but manual startup is always safe).*

The API will be available at `http://localhost:8080`.

### API Documentation (Swagger)

Once the application is running, you can access the OpenAPI/Swagger UI at:
- `http://localhost:8080/swagger-ui.html`

## 🧪 Testing

The project contains unit and integration tests. To run them:
```bash
./mvnw test
```

To run a stress test to see how the system handles load, you can use the provided K6 script. Make sure you have [K6 installed](https://k6.io/docs/get-started/installation/):
```bash
k6 run src/test/java/com/self/study/flashsale/k6/stressTestk6.js
```
