# Observability

A highly scalable, asynchronous architecture (like the one used in this flash sale project) requires a robust observability stack. When a request spans across a REST API, a Redis cache, a Kafka topic, and background workers, tracing errors and performance bottlenecks becomes complex without the right tools.

This project implements a complete monitoring and distributed tracing stack.

## Components

- **Micrometer & OpenTelemetry**: Instruments the Spring Boot application to generate metrics and distributed traces.
- **Prometheus**: A time-series database that scrapes and stores metrics from the application.
- **Grafana**: A visualization layer connected to Prometheus to create dashboards for the metrics.
- **Jaeger**: A distributed tracing backend that collects and visualizes the path of a single request across multiple microservices or asynchronous boundaries (like Kafka).

## Accessing the Dashboards

When running the project via `docker-compose up -d`, the following tools are available locally:

### 1. Grafana (Metrics)
- **URL**: `http://localhost:3000`
- **Credentials**: By default, Grafana might require a login (check `compose.yaml` for `GF_SECURITY_ADMIN_PASSWORD`, which is usually set to `admin`, with user `admin`).
- **Usage**: Grafana is pre-provisioned (via the `./grafana` directory) to connect to Prometheus. You can view metrics like CPU usage, HTTP request duration, and JVM memory.

### 2. Prometheus (Raw Metrics)
- **URL**: `http://localhost:9090`
- **Usage**: Useful for running custom PromQL queries if you want to inspect specific metrics directly.

### 3. Jaeger (Distributed Tracing)
- **URL**: `http://localhost:16686`
- **Usage**: Use Jaeger's UI to search for traces. When you place an order, you can see the trace start at the API Controller, span into the Redis check, the Kafka publish event, and finally the Kafka consumer processing the order. This is critical for understanding latency across the asynchronous boundary.

## How Tracing Works in this Codebase

The application uses `micrometer-tracing-bridge-otel` and `opentelemetry-exporter-otlp`. 
The `compose.yaml` sets the `MANAGEMENT_OTLP_TRACING_ENDPOINT` environment variable so that Spring Boot automatically pushes traces to Jaeger's OTLP receiver on port 4318. 
With `MANAGEMENT_TRACING_SAMPLING_PROBABILITY=1.0`, 100% of the requests are traced (ideal for development and study, but should be lowered in production).
