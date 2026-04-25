# AI_LOG.md

## Tools Used

During this assignment, I used only ChatGPT as my AI assistant.

ChatGPT was used for:

- Spring Boot guidance
- debugging support
- Docker and PostgreSQL setup
- API design suggestions
- scheduler and webhook implementation help
- reviewing backend architecture ideas

All generated suggestions were manually reviewed, modified, tested, and debugged before being added to the project.

---

# Significant Prompts

## 1. Spring Boot Project Setup

### Prompt

"Help me create a Spring Boot backend project with PostgreSQL, Flyway, Docker, and REST APIs for a portfolio management system."

### What AI Produced

AI suggested:

- Spring Boot dependencies
- PostgreSQL configuration
- Docker Compose setup
- Initial package structure
- Flyway migration setup

### What I Kept

- Maven + Spring Boot setup
- PostgreSQL + Flyway configuration
- Docker Compose configuration
- Layered architecture (controller/service/repository)

### What I Rejected

Some generated configurations used `spring.jpa.hibernate.ddl-auto=update`. I removed that because the assignment specifically required Flyway migrations instead of schema auto-generation.

---

## 2. Weighted Average Cost Basis Logic

### Prompt

"How should weighted average cost basis be calculated correctly for stock holdings after multiple BUY transactions?"

### What AI Produced

AI suggested a weighted average formula using BigDecimal.

### What I Kept

The weighted average cost basis calculation approach:

(newQuantity × newPrice + oldQuantity × oldAverageCost) / totalQuantity

### What I Rejected

Initially, AI suggested using floating point values in one example. I replaced them with BigDecimal everywhere because money math with floating point values can create precision issues.

---

## 3. SELL Transaction Validation

### Prompt

"How to prevent selling more stock quantity than currently available in holdings?"

### What AI Produced

AI suggested checking available holding quantity before processing SELL transactions.

### What I Kept

- Quantity validation before SELL
- Returning HTTP 409 Conflict
- Custom exception handling

### What I Rejected

AI initially suggested returning a generic RuntimeException. I replaced it with a custom `InsufficientHoldingException` and added a proper global exception handler.

---

## 4. Docker and PostgreSQL Debugging

### Prompt

"Docker container is not starting correctly and Spring Boot cannot connect to PostgreSQL."

### What AI Produced

AI helped identify:

- Docker Desktop was not running
- PostgreSQL container status issues
- Port conflicts
- Docker Compose troubleshooting

### What I Kept

- Docker Compose based setup
- PostgreSQL container configuration
- Port mapping fixes

### What I Rejected

Some suggestions involved reinstalling Docker entirely. Instead, I fixed the issue by restarting Docker Desktop and validating container status.

---

## 5. Price Alert Variant (Variant C)

### Prompt

"Implement a scheduled price alert webhook system for portfolio holdings."

### What AI Produced

AI suggested:

- Alert entity design
- Scheduled polling logic
- Webhook triggering using RestTemplate
- ACTIVE/INACTIVE alert lifecycle

### What I Kept

- Scheduler running every 30 seconds
- Webhook.site integration for testing
- Alert status transition from ACTIVE to INACTIVE
- Deterministic price feed simulation

### What I Rejected

AI initially suggested repeatedly triggering alerts continuously. I corrected this because the assignment specifically required alerts to fire only once and then become INACTIVE.

---

## 6. Portfolio Profit/Loss Enhancement

### Prompt

"Enhance holdings response with current price, invested value, market value, and profit/loss calculation."

### What AI Produced

AI suggested calculating:

- investedValue
- marketValue
- profitLoss

### What I Kept

The complete enhancement because it made the portfolio system feel more realistic and aligned with real-world investment platforms.

### What I Modified

I adapted the response DTO structure and integrated calculations into the portfolio summary API instead of creating a separate endpoint.

---

# A Bug AI Introduced

One AI suggestion incorrectly caused alerts to repeatedly trigger every scheduler cycle. According to the assignment requirements, alerts should fire only once and then become INACTIVE.

I identified the issue during webhook testing using webhook.site because new requests kept appearing repeatedly.

I fixed this by:

- Updating alert status to INACTIVE immediately after successful webhook execution
- Adding a repository query to fetch only ACTIVE alerts during scheduler polling

This debugging process helped me better understand state transitions and scheduler-based workflows.

---

# A Design Choice I Made Against AI Suggestion

AI suggested implementing a very complex architecture early in the project, including:

- caching
- Redis
- authentication
- additional microservice-style abstractions

I intentionally avoided these additions because:

- the assignment emphasized correctness and reproducibility
- overengineering would increase debugging complexity
- keeping the scope focused improved stability and clarity

Instead, I focused on:

- clean REST APIs
- correct BigDecimal money math
- Docker reproducibility
- proper exception handling
- maintainable layered architecture

This trade-off helped me complete a stable and fully working solution within the expected time budget.

---

# Time Split

Approximate breakdown of time spent:

| Activity                            | Percentage |
| ----------------------------------- | ---------- |
| Writing and implementing code       | 40%        |
| Prompting and interacting with AI   | 20%        |
| Reviewing/modifying AI output       | 15%        |
| Debugging issues                    | 15%        |
| Testing APIs and scheduler/webhooks | 10%        |

---

# Reflection

This assignment significantly improved my understanding of backend engineering concepts including:

- Spring Boot layered architecture
- PostgreSQL integration
- Flyway migrations
- Docker-based reproducibility
- BigDecimal money calculations
- Scheduled jobs
- Webhook systems
- Exception handling
- REST API testing with Postman

The most important learning was that AI-generated code still requires careful review, debugging, and understanding before being used in production-style applications.

I also learned the importance of validating financial calculations carefully because small mathematical mistakes can cause major business issues in wealth-tech systems.
