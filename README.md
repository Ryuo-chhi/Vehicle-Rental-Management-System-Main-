# Vehicle Rental Management System

A comprehensive Java backend application integrated with a React frontend, designed to manage vehicle rentals, customer records, payments, and staff operations. This system provides a robust solution for vehicle rental businesses to track their fleet and transactions efficiently.

## 🚀 Features

### 1. Vehicle Management
- Manage a diverse fleet of **Cars** and **Motorcycles**.
- Track vehicle specifications: Power source, class, brand, model, and daily rental rates.
- Add, update, remove, and search for vehicles based on multiple filters.
- Real-time availability tracking.

### 2. Customer Management
- Maintain a database of customers with contact information.
- Track required documentation (ID Card and Driver's License photos).
- Full CRUD operations for customer records.

### 3. Rent Management
- Process vehicle rentals with automated availability updates.
- Support for flexible rental periods and specific start/end dates.
- Vehicle return processing with calculated totals.
- Maintain a snapshot-based rental history for auditing.

### 4. Payment Management
- Handle deposits, discounts, and damage fees.
- Support for multiple payment methods.
- Detailed billing and payment tracking.

### 5. Staff & Security
- **Role-Based Access Control (RBAC):** Distinct permissions for `ManagerStaff` and `RegularStaff`.
- **Secure Login:** Password-protected access with masked input (via `java.io.Console`).
- **Staff Management:** Admin capabilities to manage staff members, salaries, and employment status.

### 6. Reports & Analytics
- Generate comprehensive fleet summaries.
- Track total revenue and average rental income.
- Identify top-rented vehicles and most frequent customers.

## 🛠️ Technical Stack

- **Backend:** Java
- **Frontend:** React (JavaScript/TypeScript)
- **Database:** MySQL
- **Connectivity:** JDBC (MySQL Connector/J) / REST APIs
- **Architecture:** Client-Server architecture with modular MVC principles.

## 📋 Prerequisites

- **Java Development Kit (JDK):** Version 17 or higher recommended.
- **Maven:** For build and dependency management.
- **MySQL Server:** Installed and running.

## ⚙️ Setup & Installation

1.  **Clone the Repository:**
    ```bash
    git clone <repository-url>
    cd vehicle-rental-management-system
    ```

2.  **Database Configuration:**
    Configure the following environment variables on your system to allow the application to connect to your MySQL instance:
    - `DB_URL`: The JDBC connection URL (e.g., `jdbc:mysql://localhost:3306/rental_db`)
    - `DB_USERNAME`: Your MySQL username.
    - `DB_PASSWORD`: Your MySQL password.

3.  **Build the Project:**
    Use Maven to package the application:
    ```bash
    mvn clean package
    ```

4.  **Run the Application:**
    Run the Spring Boot application:
    ```bash
    mvn spring-boot:run
    ```

## 📂 Project Structure

```text
src/main/java/com/rental/system/
├── Main.java           # Spring Boot application entry point
├── controller/         # REST Controllers / endpoints
├── service/            # Service layer logic
├── database/           # Database configuration & mappings
├── model/              # Data models/Entities (Vehicle, Car, Customer, etc.)
└── user/               # User models and roles (Staff, ManagerStaff, etc.)
pom.xml                 # Maven configuration and dependencies
```

## 🔐 Default Credentials

For first-time setup, the system automatically ensures a super-admin exists:
- **Username:** `admin_root`
- **Password:** `root123`

