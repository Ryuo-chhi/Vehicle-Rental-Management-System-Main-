# 🚀 Project Evolution Plan: Vehicle Rental Management System

This document outlines a detailed, step-by-step roadmap to upgrade the current console-based Java application into a professional, industry-standard enterprise system. The plan is organized by core engineering disciplines: **Software Engineering (SE)**, **Backend**, **Database**, and **Human-Computer Interaction (HCI)**.

---

## 🛠 Phase 1: Foundation & Build System (SE)
**Goal:** Transition from a manual, error-prone setup to a professional development workflow.

1.  **Initialize Maven/Gradle Build System:**
    *   **Action:** Create a `pom.xml` file to replace manual JAR management (like the current `/jdbc/` folder).
    *   **Details:** Define core dependencies: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `mysql-connector-j`, `lombok` (to reduce boilerplate), and `junit-jupiter` (for testing).
    *   **Refactor Structure:** Migrate source code to the standard Maven layout: `src/main/java/com/rental/system` and `src/test/java`. This ensures compatibility with modern IDEs and CI/CD tools.
    *   **Test Structure:** ✅ **COMPLETED** - Added Maven-standard test folders `src/test/java/com/rental/system` and `src/test/resources` with basic test setup and H2 database configuration.
2.  **Decompose the "Garage" Monolith (UI vs. Business Logic):**
    *   **The Problem:** `Garage.java` is currently a "God Object." it acts as the Waiter (taking orders), the Chef (cooking/logic), and the Pantry (holding data). If one part breaks, everything breaks.
    *   **The Architecture (The "Waiter & Chef" Model):**
        *   **The Face (Garage.java - Controller/UI):** Stays as the "Waiter." Its ONLY job is to talk to the user via the console.
        *   **The Brain (Service Layer):** Becomes the "Chef." Its ONLY job is to process data and enforce rules. It never uses a `Scanner` or `System.out`.
    *   **Structural Breakdown:**
        | What STAYS in `Garage.java` (UI) | What MOVES to `Service` Classes (Logic) |
        | :--- | :--- |
        | **User Input:** All `scanner.nextLine()` calls. | **Data Storage:** All `ArrayList` and `HashSet` lists. |
        | **Navigation:** Menus, `switch-cases`, and loops. | **Search/Filter:** Finding items by ID, Code, or Name. |
        | **Display:** All `System.out.println()` messages. | **Business Rules:** Checking license requirements or availability. |
        | **Formatting:** Validating that input is a valid number. | **Calculations:** Pricing, damage fees, and date math. |
        | **Feedback:** Telling the user if an action worked. | **DB Sync:** Direct calls to `DatabaseMapper`. |
    *   **Target Services:**
        *   `VehicleService`: Fleet management and availability.
        *   `CustomerService`: Profile management and validation.
        *   `RentalService`: Transaction logic and pricing.
        *   `StaffService`: Login and permission checks.
3.  **Implement Design Patterns for Scalability:**
    *   **Factory Pattern:** Create a `VehicleFactory` to handle the instantiation of `Car` and `Moto` objects. This replaces the `switch(type)` logic in `addVehicle`, making it easy to add new vehicle types (e.g., "Truck") later.
    *   **DTO (Data Transfer Object) Pattern:** Use DTOs to pass data between the backend and frontend. This prevents sensitive fields (like staff passwords) from accidentally being exposed in API responses.

---

## 🗄 Phase 2: Professional Data Persistence (Database)
**Goal:** Replace manual JDBC mapping with a robust Object-Relational Mapper (ORM).

1.  **Integrate Spring Data JPA:**
    *   **Action:** Annotate models (`Vehicle`, `Customer`, `Staff`, `Rent`) with `@Entity`, `@Id`, and `@Table`.
    *   **Details:** Implement inheritance strategies using `@Inheritance(strategy = InheritanceType.JOINED)` for the `Vehicle` hierarchy, allowing `Car` and `Moto` to have their own specific tables while sharing a common ID.
2.  **Define Repository Interfaces:**
    *   **Action:** Create interfaces like `VehicleRepository extends JpaRepository<Vehicle, Integer>`.
    *   **Benefit:** This provides built-in methods like `.findAll()`, `.save()`, and `.deleteById()`, eliminating the ~1000 lines of manual SQL and result-set mapping in `DatabaseMapper.java`.
3.  **Database Versioning (Flyway/Liquibase):**
    *   **Action:** Integrate a migration tool to manage schema changes programmatically.
    *   **Details:** Store `CREATE TABLE` scripts in `src/main/resources/db/migration`. This ensures that every developer and server has the exact same database structure.
4.  **Audit Logging:**
    *   **Action:** Use JPA Auditing (`@CreatedDate`, `@LastModifiedDate`) to automatically track when records are created or updated without writing manual timestamps.

---

## 🌐 Phase 3: Backend & REST API (Backend)
**Goal:** Transition from a local console menu to a scalable, web-accessible API.

1.  **Bootstrap Spring Boot:**
    *   **Action:** Create a `SpringBootApplication` entry point to replace `Main.java` and start an embedded Tomcat server.
    *   **Details:** Configure `application.properties` for database connections and environment-specific settings.
2.  **Build REST Controllers:**
    *   **Action:** Convert the current menu-driven console logic into RESTful endpoints.
    *   **Example Endpoints:**
        *   `GET /api/vehicles`: Returns a JSON list of the entire fleet instead of printing to a console.
        *   `POST /api/rents`: Initiates a new rental transaction.
        *   `PATCH /api/vehicles/{id}/return`: Processes a vehicle return and updates availability.
3.  **Global Error Handling:**
    *   **Action:** Implement a `@ControllerAdvice` class.
    *   **Details:** Map internal exceptions (e.g., `VehicleNotFoundException`) to standard HTTP status codes (like `404 Not Found`) and return clean, helpful JSON error messages.

---

## 🔐 Phase 4: Security & Professionalism (SE/Backend)
**Goal:** Implement enterprise-grade security and data integrity.

1.  **Secure Password Hashing:**
    *   **Action:** Integrate `BCryptPasswordEncoder`.
    *   **Details:** Replace the current plain-text password storage with cryptographic hashes. Even if the database is compromised, the actual passwords remain unreadable.
2.  **Authentication (Spring Security + JWT):**
    *   **Action:** Secure endpoints using Stateless Session management with JSON Web Tokens (JWT).
    *   **Details:** Implement a `/login` endpoint that returns a token, which the user must send with every future request to prove their identity.
3.  **Role-Based Access Control (RBAC):**
    *   **Action:** Enforce permissions at the controller level using `@PreAuthorize("hasRole('MANAGER')")`.
    *   **Details:** Ensure `RegularStaff` cannot access sensitive areas like staff management or financial reports.
4.  **Logging Strategy:**
    *   **Action:** Replace `System.out` with a professional logging framework like `SLF4J/Logback`.
    *   **Details:** Log critical events (errors, audits, transaction starts) with appropriate severity levels (INFO, WARN, ERROR).

---

## 🎨 Phase 5: Modern Interface (HCI)
**Goal:** Replace the CLI with a modern, responsive React web application.

1.  **Frontend Framework Setup:**
    *   **Action:** Initialize a React project using Vite for high-performance development.
2.  **Component-Based UI Design:**
    *   **Details:** Build reusable UI components:
        *   `VehicleGrid`: A visual gallery of available cars/motos using interactive cards.
        *   `RentalDashboard`: Real-time stats on active rents and revenue.
        *   `BookingForm`: A multi-step form with interactive date-pickers and real-time client-side validation.
3.  **HCI Design Principles:**
    *   **Visual Feedback:** Use "Toasts" or "Snackbars" to provide immediate confirmation of actions (e.g., "Vehicle successfully returned!").
    *   **Data Visualization:** Use libraries like `Recharts` or `Chart.js` to display the "Full Report" data as interactive graphs and revenue trends.

---

## 🧪 Phase 6: Testing & Quality Assurance (SE)
**Goal:** Ensure system reliability through automated testing and documentation.

1.  **Unit Testing:**
    *   **Action:** Write tests for the `Service` layer using **Mockito**.
    *   **Scope:** Test pricing logic, availability checks, and validation rules in isolation without needing a database.
2.  **Integration Testing:**
    *   **Action:** Use `@SpringBootTest` to verify that the Controller, Service, and Repository layers work correctly together using an H2 in-memory database.
3.  **Interactive API Documentation:**
    *   **Action:** Integrate `SpringDoc/Swagger`.
    *   **Benefit:** Provides a live UI at `/swagger-ui.html` where you can see all endpoints and test them directly by clicking buttons.

---

## 🏁 Summary Checklist
- [x] Initialize Maven project structure and `pom.xml`.
- [x] Migrate entities to JPA and define Repositories.
- [x] Refactor `Garage.java` logic into Spring Services.
- [x] Implement REST Controllers for all core modules.
- [x] Secure the application with BCrypt and JWT.
- [x] Develop the React frontend and connect to the API.
- [x] Add comprehensive Unit/Integration tests and Swagger docs.
