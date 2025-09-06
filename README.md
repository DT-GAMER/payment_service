# Interswitch Payment Service (Demo)

A simplified payment processing microservice built with **Spring Boot (Java 17)**.

It demonstrates **transaction handling, idempotency, mock payment gateway integration, and API-level security**.

This project is designed to mimic core aspects of real-world payment systems such as:

* **Secure API access** with API keys
* **Idempotency keys** to prevent double-charging
* **Transaction lifecycle management** (authorization, settlement, failure)
* **Mock external gateway integration**
* **Basic fraud/risk hooks** (extensible)

---

## ⚙️ Features

* REST API to create and retrieve payments.
* Support for different transaction types (`card`, `wallet`, `bank`).
* Idempotency key enforcement (safe retries).
* In-memory storage (replaceable with DB).
* Mock gateway adapter for demo purposes.
* Lightweight security check with `X-API-KEY` header.
* Built with **Spring Boot 3.x** and Java 17.

---

## Architecture Overview

The project follows a clean layered structure:

```
src/main/java/com/payment_example/payments
│
├── controller   → REST controllers (PaymentController)
├── dto          → Request/response DTOs
├── model        → Transaction domain model
├── service      → Business logic (PaymentService)
└── gateway      → Mock external gateway adapter
```

---

## Getting Started

### Prerequisites

* Java **17**
* Maven **3.8+**
* cURL or Postman for API testing

### Clone the Repository

```bash
git clone https://github.com/<your-username>/payment_service.git
cd payment_service
```

### Build & Run

```bash
./mvnw clean package
./mvnw spring-boot:run
```

By default, the service runs at **[http://localhost:9090](http://localhost:9090)**

---

## API Usage

### 1. Create a Payment

```bash
curl -s -X POST "http://localhost:9090/api/payments" \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: valid-api-key" \
  -H "Idempotency-Key: idem-100" \
  -d '{"type":"card","amount":100,"currency":"NGN"}' | jq
```

 Example Response:

```json
{
  "id": "abb7b00f-2a2b-404a-8ef7-c6240cccb209",
  "type": "card",
  "amount": 100,
  "currency": "NGN",
  "status": "AUTHORIZED",
  "idempotencyKey": "idem-100"
}
```

---

### 2. Fetch a Payment by ID

```bash
curl -s -X GET "http://localhost:9090/api/payments/<PAYMENT_ID>" | jq
```

Example:

```bash
curl -s -X GET "http://localhost:9090/api/payments/abb7b00f-2a2b-404a-8ef7-c6240cccb209" | jq
```

---

### 3. Idempotency in Action

Re-sending the same request with the same `Idempotency-Key` will return the same transaction:

```bash
curl -s -X POST "http://localhost:9090/api/payments" \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: valid-api-key" \
  -H "Idempotency-Key: idem-100" \
  -d '{"type":"card","amount":100,"currency":"NGN"}' | jq
```

Response → same `id` as before.

---

## Security

* Every request must include a valid `X-API-KEY` header.
* Requests without it are **rejected with 401 Unauthorized**.

---

## Running Tests

```bash
./mvnw test
```

---

## Future Enhancements

* Persist transactions in **Postgres** with ACID guarantees.
* **Outbox pattern + Kafka** for event publishing.
* Integrate with **Ledger DB** for auditability.
* Add **Fraud/Risk Service** calls before authorization.
* Replace mock gateway with real network adapters.
