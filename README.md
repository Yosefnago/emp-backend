# Employee Management System - Backend

Java & Spring Boot backend for a modular Employee Management System.

This service exposes secure RESTful APIs for managing employees, notifications, salary details, authentication, and user-related operations.  
Built using Spring Boot 4, JPA, PostgreSQL, and JWT-based authentication, following clean architecture and layered design.


---
### See javadoc here : http://localhost:63342/emp-backend/EmployeeManeger/target/reports/apidocs/index.html
## 📌 Features

- 🔐 **User Authentication**
  
  - JWT-based login and secure endpoints
  - `@CurrentUser` annotation to inject authenticated user into controllers
    
- 👤 **Employee Management**

  - Add, update, delete (archive), and list employees
  - Validation for unique personal ID and email
  - Archived employees table for audit purposes

- 📊 **Dashboard**

  - Employee statistics per user
  - Last activity logs

- 📬 **Notifications**

  - Event reminders (upcoming and today)
  - Birthday reminders
  - Mark notifications as read, mark all read, delete single or all notifications

- 🧾 **Activity Logging**

  - Logs user actions (ADD, UPDATE, ARCHIVE) with timestamps
  - Fetch recent activity or full activity history

- 💰 **Salary Management** *(planned in code structure)*

  - Supports hourly rate, bonuses, overtime, pension, and aggregated reporting

- 🛡️ **Security**

  - Spring Security with stateless JWT authentication
  - WebSocket authentication for real-time notifications or server-status updates

- 🔄 **Layered Architecture**

  - Controller → Service → Repository separation
  - DTOs for request/response encapsulation
  - Global exception handling with `@RestControllerAdvice`

---

## 🧱 Tech Stack

- **Java 25**
- **Spring Boot 4.0.1**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **JWT (JSON Web Tokens)**
- **WebSocket + STOMP**
- **Lombok**

---

📁 Project Structure
```
com.ms.sw
│
├── attendance // Employee attendance management (entities, services)
├── config // Security configuration, JWT filters, WebSocket config/interceptor
├── employee // Employee management (entities, services, DTOs, repository)
├── exception // Global and domain-specific exception handling
├── notifications // Notification entities, services, controllers, repository
├── user // User management, Events, ActivityLogs, enums like ActionType
├── views // Dashboard, audit logs, and view-related services
└── YosefApplication // Main Spring Boot application class

```
🔐 Security
```
JWT secures all endpoints.

Sensitive endpoints are protected via SecurityFilterChain.

JwtAuthFilter validates tokens for REST endpoints.

JwtHandshakeInterceptor validates JWT for WebSocket connections.

@CurrentUser injects the authenticated User entity into controller methods.
```
⚡ WebSocket Support
```
Endpoint: /server-status

Uses STOMP over WebSocket

Authenticated via JWT token in the Authorization header

Can be extended for real-time notifications, server monitoring, or event updates
```
📬 Contact

Developed by Yosef Nago

GitHub: github.com/Yosefnago

Email: yosinago5@gmail.com

