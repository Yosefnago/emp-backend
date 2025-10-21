# Employee Management System - Backend

Java & Spring Boot backend for a modular Employee Management System.

This service exposes secure RESTful APIs for managing employees, salary details, authentication, and user-related operations.  
Built using Spring Boot 3.x, JPA, PostgreSQL, and JWT-based authentication, following clean architecture and layered design.

---

## 📌 Features

- 🔐 User registration & login (JWT-based authentication)
- 👤 Employee CRUD operations
- 💰 Salary management (hourly rate, bonuses, overtime, pension, etc.)
- 📊 Aggregated salary reporting per user
- 🔄 Separation of concerns: Controller → Service → Repository layers
- 🛡️ Global exception handling with `@RestControllerAdvice`
- 📦 Modular DTOs for request/response encapsulation

---

## 🧱 Tech Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **JWT (JSON Web Tokens)**
- **Lombok**

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 14+
- IDE (e.g. IntelliJ)

### Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/ems-backend.git


2. Create a PostgreSQL database (e.g. ems_db) and configure credentials in src/main/resources/application.properties.

3. Run the application:
 ```bash
   mvn spring-boot:run
```

com.ms.sw
│
├── controller       // REST controllers
├── service          // Business logic
├── repository       // Spring Data JPA interfaces
├── entity           // JPA entities
├── dto              // Request/Response DTOs
├── exception        // Global & domain-specific exceptions
└── security         // JWT filters, config, and authentication

🔐 Security

JWT is used to secure endpoints.

Sensitive endpoints are protected via SecurityFilterChain.

@CurrentUser annotation extracts authenticated user from the security context.

📬 Contact

Developed by Yosef Nago
GitHub: github.com/Yosefnago

Email: yosinago5@gmail.com

