# Vehicle Rental Management System

A comprehensive console-based Java application designed to manage vehicle rentals, customer records, payments, and staff operations. This system provides a robust solution for vehicle rental businesses to track their fleet and transactions efficiently.

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

- **Language:** Java
- **Database:** MySQL
- **Connectivity:** JDBC (MySQL Connector/J)
- **Architecture:** Modular design with Model-View-Controller (MVC) principles.

## 📋 Prerequisites

- **Java Development Kit (JDK):** Version 17 or higher recommended.
- **MySQL Server:** Installed and running.
- **MySQL JDBC Driver:** Included in the `jdbc/` directory.

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

3.  **Compile the Project:**
    Ensure the JDBC driver is in your classpath.
    ```bash
    javac -cp ".;jdbc/mysql-connector-j-9.6.0.jar" src/*.java src/controller/*.java src/database/*.java src/model/*.java src/user/*.java -d bin
    ```

4.  **Run the Application:**
    ```bash
    java -cp "bin;jdbc/mysql-connector-j-9.6.0.jar" Main
    ```

## 📂 Project Structure

```text
src/
├── Main.java           # Application entry point & menu loop
├── controller/         # Business logic (Garage.java)
├── database/           # Database connectivity & mapping
├── model/              # Data models (Vehicle, Car, Customer, etc.)
└── user/               # Staff and permission management
jdbc/                   # MySQL JDBC Connector
```

## 🔐 Default Credentials

For first-time setup, the system automatically ensures a super-admin exists:
- **Username:** `admin_root`
- **Password:** `root123`

---
*Developed as a robust vehicle management solution.*
