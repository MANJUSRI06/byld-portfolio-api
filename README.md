# BYLD Portfolio API

A Spring Boot backend application for managing investment portfolios, stock holdings, transactions, and webhook-based price alerts.

This project was built as part of the BYLD Backend Developer Internship Assignment.

---

# Features

## Core Portfolio Management

- Create investment portfolios
- Manage stock holdings
- Buy and sell stocks
- Weighted average cost basis calculation
- Portfolio summary APIs
- Profit/Loss calculation
- Exception handling for invalid operations

---

## Variant C — Price Alert System

- Create price alerts for stocks
- ABOVE / BELOW alert support
- Scheduled polling every 30 seconds
- Webhook integration using webhook.site
- Alerts automatically become INACTIVE after firing once

---

# Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Backend language |
| Spring Boot | REST API framework |
| PostgreSQL | Database |
| Flyway | Database migrations |
| Docker | PostgreSQL containerization |
| Maven | Dependency management |
| Postman | API testing |

---

# Project Structure

```text
src/main/java/com/manju/byldportfolioapi
│
├── controller
├── service
├── repository
├── entity
├── dto
├── exception
├── scheduler
├── config
└── util
```

---

# Database Setup

The project uses PostgreSQL running inside Docker.

## Start PostgreSQL Container

Run the following command from the project root:

```bash
docker compose up -d
```

Verify container is running:

```bash
docker ps
```

---

# Application Configuration

Located in:

```text
src/main/resources/application.properties
```

Example configuration:

```properties
server.port=9090

spring.datasource.url=jdbc:postgresql://localhost:5432/byld_db
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

---

# Flyway Migration

Migration file location:

```text
src/main/resources/db/migration
```

Example:

```text
V1__init.sql
```

Flyway automatically creates database tables during application startup.

---

# Running the Application

## Start Spring Boot Application

Run:

```bash
mvn spring-boot:run
```

Or run the main class directly from IntelliJ IDEA.

Application runs on:

```text
http://localhost:9090
```

---

# API Endpoints

## Portfolio APIs

### Create Portfolio

```http
POST /v1/portfolios
```

Request:

```json
{
  "clientName": "Manjusri",
  "riskProfile": "MODERATE"
}
```

---

### Get Portfolio Summary

```http
GET /v1/portfolios/{portfolioId}
```

Returns:

- holdings
- invested value
- market value
- profit/loss
- current prices

---

## Holdings APIs

### Buy Stock

```http
POST /v1/portfolios/{portfolioId}/holdings
```

Request:

```json
{
  "symbol": "INFY",
  "quantity": 10,
  "price": 1500
}
```

---

### Sell Stock

```http
POST /v1/portfolios/{portfolioId}/transactions/sell
```

Request:

```json
{
  "symbol": "INFY",
  "quantity": 5,
  "price": 2000
}
```

Validation:

- Prevents selling more quantity than available
- Returns HTTP 409 Conflict if insufficient holdings exist

---

## Price Alert APIs

### Create Alert

```http
POST /v1/portfolios/{portfolioId}/alerts
```

Request:

```json
{
  "symbol": "INFY",
  "kind": "ABOVE",
  "price": 1800,
  "webhookUrl": "https://webhook.site/your-url"
}
```

---

### Get Alerts

```http
GET /v1/portfolios/{portfolioId}/alerts
```

---

# Price Feed Simulation

The project uses a deterministic simulated price feed.

Example:

```text
INFY → 2460
TCS → 4100
HDFCBANK → 1650
```

This helps maintain reproducible behavior during testing.

---

# Webhook Testing

Webhook testing was performed using:

```text
https://webhook.site
```

Steps:

1. Open webhook.site
2. Copy generated webhook URL
3. Use the URL while creating alerts
4. Wait for scheduler execution
5. Verify incoming webhook POST requests

Alerts fire only once and then become INACTIVE.

---

# Profit/Loss Calculation

Portfolio summary API includes:

- currentPrice
- investedValue
- marketValue
- profitLoss

Example:

```json
{
  "symbol": "INFY",
  "quantity": 25,
  "averageCost": 1833,
  "currentPrice": 2460,
  "investedValue": 45837.5,
  "marketValue": 61500,
  "profitLoss": 15662.5
}
```

---

# Exception Handling

Custom exceptions implemented:

- ResourceNotFoundException
- InsufficientHoldingException

Global exception handling returns proper HTTP responses.

---

# Design Decisions

## Why BigDecimal?

Financial calculations require precision.

Using floating-point values can introduce rounding errors.

All money-related calculations therefore use BigDecimal.

---

## Why Flyway?

Flyway provides:

- reproducible schema management
- version-controlled migrations
- safer database evolution

This avoids issues caused by automatic schema generation.

---

# Future Improvements

Possible future enhancements:

- Authentication & Authorization
- Real stock market API integration
- Redis caching
- Swagger/OpenAPI documentation
- Unit and integration tests
- Deployment to cloud platforms
- Kafka-based event processing
- Portfolio analytics dashboard

---

GitHub Repository:

```text
https://github.com/MANJUSRI06/byld-portfolio-api
```

---

# Assignment Notes

- Variant Implemented: Variant C
- All APIs tested using Postman
- PostgreSQL runs via Docker
- Scheduler polling interval: 30 seconds
- Alerts become INACTIVE after firing once

