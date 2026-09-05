<div align="center">

<!-- LOGO -->
<!-- Replace the line below with your actual logo, e.g.: -->
<a href="https://ibb.co/9HB00Rdg"><img src="https://i.ibb.co/pjNTTCkw/hotel-white2.png" alt="hotel-white2" border="0"></a>

<br/>
<br/>
<br/>

# 🏨 AI Customer Support Microservice — Hotel Booking

**A Spring Boot backend for a hotel booking platform, with an AI agent layer that lets users manage bookings, refunds, and reservations through natural language.**

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/license-MIT-lightgrey)](#license)

</div>

---

## 📖 Overview

This project is a production-style backend for a hotel booking system, built in two deliberate phases:

1. **Phase 1 — Concurrent CRUD API**: a fully functional booking system (hotels, rooms, users, bookings, payments, refunds) with real concurrency handling — optimistic and pessimistic locking to prevent double-booking and lost updates under load.
2. **Phase 2 — AI Agent Layer**: a Spring AI–powered conversational layer on top of the same backend, using function/tool calling so an LLM can safely invoke real backend actions (check booking status, cancel + refund, book a room) without ever touching the database directly.

The goal of this project is to demonstrate both **core backend engineering** (concurrency, transactional integrity, REST API design) and **applied AI integration** (function calling, resilience around non-deterministic model calls) in one coherent system.

---

## 🧱 Architecture

```
User Query
    │
    ▼
Chat Endpoint (Spring Boot)
    │
    ▼
LLM (parses intent → picks function + args)
    │
    ▼
Java Service Layer (real business logic, DB access)
    │
    ▼
PostgreSQL
    │
    ▼
Result → LLM (summarizes) → User
```

The LLM never accesses the database or business logic directly — it can only request pre-approved backend actions, which are validated and executed entirely within the Java service layer.

---

## ✨ Features

- **Hotel & room management** — browse hotels, search available rooms by location/date/guests
- **Booking lifecycle** — create, view, cancel bookings with date-range overlap validation
- **Payments & refunds** — payment records tied to bookings, refund lifecycle tracking
- **Concurrency-safe** — optimistic locking (`@Version`) on rooms/bookings, pessimistic locking on the booking-creation hot path to prevent overselling
- **AI customer support agent** *(Phase 2)*:
  - Track booking / refund status
  - Cancel a booking and request a refund
  - Book a room via natural-language request
  - Guardrails requiring explicit confirmation before destructive actions

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot (Web, Data JPA, Validation) |
| Database | PostgreSQL |
| AI Layer | Spring AI + LLM function/tool calling |
| Concurrency | JPA optimistic locking, pessimistic row locks |
| Testing | JUnit 5, concurrent load tests (`ExecutorService`) |

---

## 🗄️ Database Schema

Entities: `Hotel`, `Room`, `User`, `Booking`, `Payment`, `Refund`

Full DDL available in [`Project Document`](https://docs.google.com/document/d/1nNh9MTvO5GtJBtDHhuKwDEAPXLVGITlkWMmDFsnwAQE/edit?usp=sharing).

---

## 📡 API Reference

### Hotels & Rooms
| Method | Endpoint | Description |
|---|---|---|
| GET | `/hotels` | List all hotels |
| GET | `/hotels/{hotelId}/rooms` | List rooms for a hotel |
| GET | `/rooms/{roomId}` | Get room details |
| GET | `/rooms/search` | Search available rooms by location/date/guests |

### Users
| Method | Endpoint | Description |
|---|---|---|
| POST | `/users` | Create a user |
| GET | `/users/{userId}` | Get user details |
| GET | `/users/{userId}/bookings` | List a user's bookings |
| GET | `/users/{userId}/refunds` | List a user's refunds |

### Bookings
| Method | Endpoint | Description |
|---|---|---|
| POST | `/bookings` | Create a booking |
| GET | `/bookings/{bookingId}` | Get booking details |
| POST | `/bookings/{bookingId}/cancel` | Cancel a booking (creates refund) |

### Payments & Refunds
| Method | Endpoint | Description |
|---|---|---|
| POST | `/payments` | Record a payment |
| GET | `/payments/{paymentId}` | Get payment details |
| GET | `/refunds/{refundId}` | Get refund details |

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven
- PostgreSQL 15+

### Setup

```bash
# Clone the repository
git clone https://github.com/prathamesh-kothalkar/hotel-booking-server.git
cd hotel-booking-server

# Configure the database
createdb hotel_booking

# Build and run
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## 🗺️ Roadmap

- [x] Phase 1 — Schema design + concurrency-safe CRUD API
- [x] Concurrent load tests proving overselling fix
- [ ] Phase 2 — Spring AI `@Tool` integration
- [ ] Streaming responses + latency optimization
- [ ] Guardrails & audit logging for AI-triggered actions
- [ ] Deployment

---

## 📄 License

This project is licensed under the MIT License.
