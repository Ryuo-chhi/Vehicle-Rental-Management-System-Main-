<div align="center">
  <img src="https://img.icons8.com/color/96/000000/car-rental.png" alt="VRMS Logo" />
  
  # 🏎️ Vehicle Rental Management System (VRMS)
  
  **A powerful, modern solution for managing your vehicle rental fleet seamlessly.**
  
  [![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://java.com/)
  [![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)](https://spring.io/projects/spring-boot)
  [![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)](https://reactjs.org/)
  [![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)

</div>

---

A comprehensive **Java backend application** integrated with a **React frontend**, designed to manage vehicle rentals, customer records, payments, and staff operations. This system provides a robust solution for vehicle rental businesses to track their fleet and transactions efficiently.

## 🚀 Features

### 🚗 Vehicle Management

- Manage a diverse fleet of **Cars** and **Motorcycles**.
- Track vehicle specifications: Power source, class, brand, model, and daily rental rates.
- Add, update, remove, and search for vehicles based on multiple filters.
- **Real-time** availability tracking.

### 👥 Customer Management

- Maintain a database of customers with contact information.
- Track required documentation (ID Card and Driver's License photos).
- Full **CRUD operations** for customer records.

### 📝 Rent Management

- Process vehicle rentals with automated availability updates.
- Support for flexible rental periods and specific start/end dates.
- Vehicle return processing with automatically calculated totals.
- Maintain a **snapshot-based rental history** for robust auditing.

### 💳 Payment Management

- Handle deposits, discounts, and damage fees seamlessly.
- Support for multiple payment methods.
- Detailed billing and payment tracking.

### 🔐 Staff & Security

- **Role-Based Access Control (RBAC):** Distinct permissions for `ManagerStaff` and `RegularStaff`.
- **Secure Login:** Password-protected access with masked input.
- **Staff Management:** Admin capabilities to manage staff members, salaries, and employment status.

### 📊 Reports & Analytics

- Generate comprehensive fleet summaries.
- Track total revenue and average rental income.
- Identify **top-rented vehicles** and **most frequent customers**.

## 🛠️ Technical Stack

- **Backend:** Java 17, Spring Boot
- **Frontend:** React (JavaScript/TypeScript), Vite
- **Database:** MySQL (H2 for Testing)
- **Connectivity:** REST APIs, JPA / Hibernate
- **Architecture:** Modular MVC Architecture

## 📋 Prerequisites

Ensure you have the following installed before getting started:

- **Java Development Kit (JDK):** Version 17+
- **Maven:** Build and dependency management
- **MySQL Server:** Installed and actively running

## ⚙️ Setup & Installation

**1. Clone the Repository**

```bash
git clone <repository-url>
cd vehicle-rental-management-system
```

**2. Database Configuration**
Configure your environment variables for MySQL connectivity:

- `DB_URL`: The JDBC connection URL (e.g., `jdbc:mysql://localhost:3306/rental_db`)
- `DB_USERNAME`: Your MySQL username
- `DB_PASSWORD`: Your MySQL password

**3. Build & Run**

```bash
# Package the application
mvn clean package

# Run the Spring Boot application
mvn spring-boot:run
```

## 📂 Project Structure

```text
src/main/java/com/rental/system/
 ├── Main.java           # Application entry point
 ├── controller/         # REST Controllers / Endpoints
 ├── service/            # Business logic layer
 ├── database/           # Database config & migrations
 ├── model/              # Entities (Vehicle, Customer, etc.)
 ├── repository/         # Data Access Layer (JPA Repositories)
 └── user/               # User models and roles
pom.xml                  # Maven configurations
```

## 🔑 Default Credentials

For first-time setup, the system automatically ensures a super-admin exists:

- **Username:** `admin_root`
- **Password:** `root123`

---
<div align="center">
  <i>Built with ❤️ for efficient vehicle fleet management.</i>
</div>
