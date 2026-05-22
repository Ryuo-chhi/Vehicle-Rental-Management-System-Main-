# Vehicle Rental Management System — Database Project

> **Course:** Database Systems (CS-DB)  
> **Project Title:** Vehicle Rental Management System  
> **Date:** March 2026

---

## 1. Introduction

### 1.1 Project Description

**Vehicle Rental Management System (VRMS)** keeps your garage running really smooth like never before. 

No more lost papers or broken spreadsheets. One place for bookings, maintenance, schedules, and payments. Your team stays in sync, your customers stay happy, and you actually get to go home on time.

VRMS is a comprehensive relational database solution that models the full lifecycle of vehicle rentals. It centralizes core domains including vehicle inventory (cars and motorcycles), customer registration, staff management, rental transactions, and payment processing.

### 1.2 Objective

The primary objective of VRMS is to provide a single, unified platform to:
- **Centralize Operations:** Replace fragmented paperwork and spreadsheets with a single point of truth for bookings, maintenance, and schedules.
- **Sync Teamwork:** Keep the entire team in sync with real-time updates on vehicle availability and rental statuses.
- **Enhance Customer Satisfaction:** Ensure customers stay happy through faster processing and reliable service.
- **Streamline Payments:** Manage and track all rental payments and deposits efficiently.
- **Implement Robust Data Modeling:** Design a 3NF normalized schema to ensure data integrity and enforce business rules (availability checks, licence requirements, role-based permissions).
- **Drive Operational Efficiency:** Automate rental workflows to reduce manual errors and save time for both staff and management.

---

## 2. Entity-Relationship Model

### 2.1 User Requirements

| # | Requirement |
|---|-------------|
| R1 | The system must store two types of vehicles: **Cars** and **Motorcycles**. |
| R2 | Each vehicle has a unique ID, code, licence plate, and daily rental rate. |
| R3 | Customers must be registered before renting. A customer must provide a national ID number and phone number (9-10 digits). |
| R4 | A customer can only rent a vehicle if the vehicle is **available**. |
| R5 | A Car rental requires the customer to have a **driver's licence**. A Motorcycle rental requires both an **ID card** and a **driver's licence**. |
| R6 | Every rental is managed by a **Staff** member. Staff have two roles: **Manager** (`ManagerStaff` — full access + bonus entitlement) and **Regular** (`RegularStaff` — limited access). |
| R7 | Each rental generates exactly one **Payment** record. |
| R8 | Payment supports multiple methods: CASH, CARD, ABA, ACLEDA, WING. |
| R9 | Upon vehicle return, an immutable **Rent Record** (history snapshot) is created. |
| R10 | A vehicle becomes **unavailable** during an active rental and is set back to **available** upon return. |

---

## 2.2 ER Diagram

```
┌──────────────┐          ┌─────────────────────────────────────────────┐
│   CUSTOMER   │          │                    VEHICLE                  │
│──────────────│          │─────────────────────────────────────────────│
│ PK customer_id│         │ PK vehicle_id                               │
│   full_name   │         │    vehicle_code (unique)                    │
│   id_num (UK) │         │    vehicle_type  ('Car' | 'Moto')           │
│   phone (UK)  │         │    power_source                             │
│   id_card_photo│        │    vehicle_class                            │
│   dl_photo    │         │    brand                                    │
└──────┬───────┘          │    model                                    │
       │                  │    rate_per_day                             │
       │ rents            │    licence (UK)                             │
       │ (1..*)           │    licence_plate (UK)                       │
       │                  │    is_available                             │
   ┌───▼──────────────┐   │    number_of_seats (Car only)               │
   │      RENT        │   │    helmet_included (Moto only)               │
   │──────────────────│   └───────────────────┬─────────────────────────┘
   │ PK rent_id       │                       │
   │ FK customer_id   │                       │
   │ FK vehicle_id    │          ┌────────────┴─────────────────┐
   │ FK staff_id      │          │            STAFF             │
   │ FK payment_id    │          │──────────────────────────────│
   │    rent_days     │          │ PK staff_id                  │
   │    start_date    │          │    full_name                 │
   │    end_date      │          │    role* ('Manager'|'Regular')│
   │    return_date   │          │    salary                    │
   │    status        │          │    bonus (Manager only)      │
   └───────┬──────────┘          │    username (UK)             │
           │                     │    password_hash             │
           │ generates (1:1)     │    status                    │
   ┌───────▼──────────┐          │    is_active                 │
   │     PAYMENT      │◄─────────│    work_station (Regular only)│
   │──────────────────│  managed └──────────────────────────────┘
   │ PK payment_id    │  by
   │    method        │          ┌─────────────────────┐
   │    price         │          │    RENT_RECORD       │
   │    discount      │          │ (History Snapshot)   │
   │    rent_days     │          │─────────────────────│
   │    extra_days    │          │ Denormalized copy of │
   │    damage_fee    │          │ Rent+Vehicle+Customer│
   │    deposit       │          │ +Payment +Staff     │
   │    pay_date      │          └─────────────────────┘
   │    pay_status    │
   │    total_paid    │
   └──────────────────┘
```
*`role` is stored as discriminator; Java implements role via `ManagerStaff` / `RegularStaff` subclasses.*

![ER Diagram — Vehicle Rental Management System](er_diagram.png)

---

## 3. The Relational Model

### 3.1 Relational Schema

```
VEHICLES(vehicle_id PK, vehicle_code UK, vehicle_type, power_source,
        vehicle_class, brand, model, rate_per_day, licence UK,
        licence_plate UK, is_available, number_of_seats, helmet_included)

CUSTOMERS(customer_id PK, full_name, id_num UK, phone UK,
         id_card_photo, dl_photo)

STAFFS(staff_id PK, full_name, role, salary, bonus,
      username UK, password_hash, status, is_active, work_station)

PAYMENTS(payment_id PK, method, price, discount,
        rent_days, extra_days, damage_fee, deposit, pay_date,
        pay_status, total_paid)

RENTS(rent_id PK, customer_id FK→CUSTOMERS, vehicle_id FK→VEHICLES,
     staff_id FK→STAFFS, payment_id FK→PAYMENTS UK,
     rent_days, start_date, end_date, return_date, status)

RENT_RECORDS(record_id PK, rent_id UK,
            vehicle_id, vehicle_code, vehicle_type, power_source,
            vehicle_class, brand, model, licence_plate, rate_per_day,
            customer_id, customer_name, id_num, phone,
            staff_id, staff_name,
            rent_days, start_date, end_date, return_date,
            payment_id, method, price, discount,
            extra_days, damage_fee, deposit, pay_date,
            pay_status, total_paid)
```

---

### 3.2 DDL (Data Definition Language)

```sql
-- ================================================
-- Vehicle Rental Management System — DDL
-- Database: MySQL 8+
-- ================================================

CREATE DATABASE IF NOT EXISTS vrms;
USE vrms;

-- ── VEHICLE ──────────────────────────────────────
CREATE TABLE vehicles (
    vehicle_id      INT           AUTO_INCREMENT PRIMARY KEY,
    vehicle_code    VARCHAR(20)   NOT NULL UNIQUE,
    vehicle_type    ENUM('Car','Moto') NOT NULL,
    power_source    ENUM('gasoline','diesel','electric','hybrid') NOT NULL,
    vehicle_class   VARCHAR(30)   NOT NULL,
    brand           VARCHAR(50)   NOT NULL,
    model           VARCHAR(100)  NOT NULL,
    rate_per_day    DECIMAL(10,2) NOT NULL CHECK (rate_per_day > 0),
    licence         VARCHAR(50)   NOT NULL UNIQUE,
    licence_plate   VARCHAR(20)   NOT NULL UNIQUE,
    is_available    BOOLEAN       NOT NULL DEFAULT TRUE,
    number_of_seats INT           NULL CHECK (number_of_seats > 0),
    helmet_included BOOLEAN       NULL DEFAULT FALSE
);

-- ── CUSTOMER ─────────────────────────────────────
CREATE TABLE customers (
    customer_id    INT          AUTO_INCREMENT PRIMARY KEY,
    full_name      VARCHAR(100) NOT NULL,
    id_num         VARCHAR(30)  NOT NULL UNIQUE,
    phone          VARCHAR(15)  NOT NULL UNIQUE,      -- validated 9-10 digits
    id_card_photo  VARCHAR(255),
    dl_photo       VARCHAR(255)
);

-- ── STAFF ────────────────────────────────────────
-- role discriminates between ManagerStaff and RegularStaff.
-- salary applies to both roles; bonus is only non-zero for Managers.
CREATE TABLE staffs (
    staff_id      INT           AUTO_INCREMENT PRIMARY KEY,
    full_name     VARCHAR(100)  NOT NULL,
    role          ENUM('Manager','Regular') NOT NULL,
    salary        DECIMAL(10,2) NOT NULL CHECK (salary >= 0),
    bonus         DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (bonus >= 0),  -- Manager only; Regular always 0
    username      VARCHAR(50)   NOT NULL UNIQUE,
    password_hash VARCHAR(255)  NOT NULL,              -- store SHA-2 or BCrypt hash; min raw length 4
    status        BOOLEAN       NOT NULL DEFAULT TRUE,  -- TRUE=employed, FALSE=resigned
    is_active     BOOLEAN       NOT NULL DEFAULT FALSE, -- TRUE=online session, FALSE=offline
    work_station  VARCHAR(100)  NULL                    -- RegularStaff only; NULL for Manager
);

-- ── PAYMENT ──────────────────────────────────────
-- price          = rate at time of rental (Java: Payment.price)
-- discount       = percentage 0-100       (Java: Payment.discount)
CREATE TABLE payments (
    payment_id    INT           AUTO_INCREMENT PRIMARY KEY,
    method        ENUM('CASH','CARD','ABA','ACLEDA','WING','TBD') NOT NULL DEFAULT 'TBD',
    price         DECIMAL(10,2) NOT NULL CHECK (price > 0),
    discount      DECIMAL(5,2)  NOT NULL DEFAULT 0 CHECK (discount BETWEEN 0 AND 100),
    rent_days     INT           NOT NULL CHECK (rent_days > 0),
    extra_days    INT           NOT NULL DEFAULT 0 CHECK (extra_days >= 0),
    damage_fee    DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (damage_fee >= 0),
    deposit       DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (deposit >= 0),
    pay_date      DATE,
    pay_status    ENUM('PENDING','PAID') NOT NULL DEFAULT 'PENDING',
    total_paid    DECIMAL(10,2) NOT NULL DEFAULT 0
);

-- ── RENT ─────────────────────────────────────────
CREATE TABLE rents (
    rent_id     INT  AUTO_INCREMENT PRIMARY KEY,
    customer_id INT  NOT NULL,
    vehicle_id  INT  NOT NULL,
    staff_id    INT  NOT NULL,
    payment_id  INT  NOT NULL UNIQUE,
    rent_days   INT  NOT NULL CHECK (rent_days > 0),
    start_date  DATE NOT NULL,
    end_date    DATE NOT NULL,
    return_date DATE,                                   -- NULL until vehicles is returned
    status      BOOLEAN NOT NULL DEFAULT TRUE,          -- TRUE=Active, FALSE=Completed
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (vehicle_id)  REFERENCES vehicles(vehicle_id),
    FOREIGN KEY (staff_id)    REFERENCES staffs(staff_id),
    FOREIGN KEY (payment_id)  REFERENCES payments(payment_id)
);

-- ── RENT_RECORD (history snapshot) ───────────────
-- Immutable. Populated by s_proc_return_vehicle after rental is closed.
-- Mirrors RentRecord.java final fields (incl. staffID / staffName added in recent iteration).
-- NOTE: Both DatabaseMapper and this DDL now use the plural table name 'rent_records' for consistency.
CREATE TABLE rent_records (
    record_id       INT AUTO_INCREMENT PRIMARY KEY,
    rent_id         INT          NOT NULL UNIQUE,
    -- vehicles snapshot
    vehicle_id      INT          NOT NULL,
    vehicle_code    VARCHAR(20)  NOT NULL,
    vehicle_type    VARCHAR(10)  NOT NULL,
    power_source    VARCHAR(20)  NOT NULL,
    vehicle_class   VARCHAR(30)  NOT NULL,
    brand           VARCHAR(50)  NOT NULL,
    model           VARCHAR(100) NOT NULL,
    licence_plate   VARCHAR(20)  NOT NULL,
    rate_per_day    DECIMAL(10,2) NOT NULL,
    -- customers snapshot
    customer_id     INT          NOT NULL,
    customer_name   VARCHAR(100) NOT NULL,
    id_num          VARCHAR(30)  NOT NULL,
    phone           VARCHAR(15)  NOT NULL,
    -- staffs snapshot (added: RentRecord.staffID / staffName)
    staff_id        INT          NOT NULL,
    staff_name      VARCHAR(100) NOT NULL,
    -- rental details
    rent_days       INT          NOT NULL,
    start_date      DATE         NOT NULL,
    end_date        DATE         NOT NULL,
    return_date     DATE         NOT NULL,
    -- payments snapshot
    payment_id      INT          NOT NULL,
    method          VARCHAR(10)  NOT NULL,
    price           DECIMAL(10,2) NOT NULL,
    discount         DECIMAL(5,2) NOT NULL,
    extra_days      INT          NOT NULL,
    damage_fee      DECIMAL(10,2) NOT NULL,
    deposit         DECIMAL(10,2) NOT NULL,
    pay_date        DATE,
    pay_status      VARCHAR(10)  NOT NULL,
    total_paid      DECIMAL(10,2) NOT NULL
);
```

---

### 3.3 Data Dictionary

#### Table: `vehicles`
| Column | Type | Constraints | Java Field | Description |
|--------|------|-------------|------------|-------------|
| vehicle_id | INT | PK, AUTO_INCREMENT | `Vehicle.vehicleId` | Unique vehicle identifier |
| vehicle_code | VARCHAR(20) | NOT NULL, UNIQUE | `Vehicle.vehicleCode` | Type-based code e.g. `Car-1`, `Moto-2` |
| vehicle_type | ENUM | NOT NULL | derived from class | `Car` or `Moto` |
| power_source | ENUM | NOT NULL | `Vehicle.powerSource` | `gasoline`, `diesel`, `electric`, `hybrid` |
| vehicle_class | VARCHAR(30) | NOT NULL | `Vehicle.vehicleClass` | e.g. `sedan`, `SUV`, `sport` |
| brand | VARCHAR(50) | NOT NULL | `Vehicle.vehicleBrand` | Manufacturer name |
| model | VARCHAR(100) | NOT NULL | `Vehicle.vehicleModel` | Full model name e.g. `Toyota Camry` |
| rate_per_day | DECIMAL(10,2) | NOT NULL, >0 | `Vehicle.rentalRatePerDay` | Daily rental price |
| licence | VARCHAR(50) | NOT NULL, UNIQUE | `Vehicle.vehicleLicence` | Vehicle registration licence number |
| licence_plate | VARCHAR(20) | NOT NULL, UNIQUE | `Vehicle.licencePlate` | Physical plate number |
| is_available | BOOLEAN | NOT NULL, DEFAULT TRUE | `Vehicle.isAvailable` | Availability flag |
| number_of_seats | INT           | NULL, >0            | `Car.numberOfSeats` | Passenger capacity (**Car only**) |
| helmet_included | BOOLEAN       | NULL, DEFAULT FALSE | `Moto.helmetIncluded` | Whether helmet is provided (**Moto only**) |
#### Table: `customers`
| Column | Type | Constraints | Java Field | Description |
|--------|------|-------------|------------|-------------|
| customer_id | INT | PK, AUTO_INCREMENT | `Customer.customerId` | Unique customer ID |
| full_name | VARCHAR(100) | NOT NULL | `Customer.customerName` | Customer's full name |
| id_num | VARCHAR(30) | NOT NULL, UNIQUE | `Customer.customerIdNum` | National ID card number |
| phone | VARCHAR(15) | NOT NULL, UNIQUE | `Customer.customerPhone` | Phone (9-10 digits, enforced in setter) |
| id_card_photo | VARCHAR(255) | NULL | `Customer.IDCardPhoto` | File path to ID card image |
| dl_photo | VARCHAR(255) | NULL | `Customer.DriverLicensePhoto` | File path to driver's licence image |

#### Table: `staffs`
> **Note:** In Java, role is determined by class type (`ManagerStaff` or `RegularStaff`). The `role` column is the DB discriminator. `salary` exists in both subclasses; `bonus` only in `ManagerStaff` (always `0` for Regular). `status` = employed/resigned; `is_active` = online session (set TRUE on login, FALSE on logout).
>
> **DB column:** `RegularStaff.workStation` maps to `work_station` (nullable — `NULL` for `ManagerStaff`). `DatabaseMapper.mapToStaff()` should read this column when reconstructing a `RegularStaff`.
>
> **Column alias note:** `DatabaseMapper.mapToStaff()` expects the ResultSet columns to be aliased as `name`, `username`, `password`, `status`, `active`, `staff_id`, `role`, `salary`, `work_station` — ensure your SELECT query aliases `full_name AS name`, `password_hash AS password`, `is_active AS active` accordingly.

| Column | Type | Constraints | Java Field | Description |
|--------|------|-------------|------------|-------------|
| staff_id | INT | PK, AUTO_INCREMENT | `Staff.staffId` | Unique staff ID |
| full_name | VARCHAR(100) | NOT NULL | `Staff.name` | Staff member's name |
| role | ENUM | NOT NULL | Java subclass type | `Manager` → `ManagerStaff`; `Regular` → `RegularStaff` |
| salary | DECIMAL(10,2) | NOT NULL, ≥0 | `ManagerStaff.salary` / `RegularStaff.salary` | Monthly salary |
| bonus | DECIMAL(10,2) | DEFAULT 0, ≥0 | `ManagerStaff.bonus` | Performance bonus — **Manager only**; always 0 for Regular |
| username | VARCHAR(50) | NOT NULL, UNIQUE | `Staff.username` | Login username |
| password_hash | VARCHAR(255) | NOT NULL | `Staff.password` | Hashed password (raw min 4 chars) |
| status | BOOLEAN | DEFAULT TRUE | `Staff.status` | TRUE=employed, FALSE=resigned |
| is_active | BOOLEAN | DEFAULT FALSE | `Staff.active` | TRUE=logged-in session, FALSE=offline |
| work_station | VARCHAR(100) | NULL | `RegularStaff.workStation` | Work station — **Regular only**; NULL for Manager |

#### Table: `payments`
> **Note:** Java `Payment.price` ↔ DB `price`. Java `Payment.discount` ↔ DB `discount`. Java `Payment.status` ↔ DB `pay_status`.
>
> **Column alias note:** `DatabaseMapper.mapToPayments()` expects the ResultSet to expose columns as `price`, `discount`, `status`, `payment_method`, `pay_date` — your SELECT must alias `price AS price`, `discount AS discount`, `pay_status AS status`, `method AS payment_method` accordingly.

| Column | Type | Constraints | Java Field | Description |
|--------|------|-------------|------------|-------------|
| payment_id | INT | PK, AUTO_INCREMENT | `Payment.paymentId` | Unique payment ID |
| method | ENUM | DEFAULT 'TBD' | `Payment.paymentMethod` | CASH, CARD, ABA, ACLEDA, WING, TBD |
| price | DECIMAL | NOT NULL, >0 | `Payment.price` | Rate at time of rental |
| discount | DECIMAL(5,2) | DEFAULT 0, 0–100 | `Payment.discount` | Discount percentage |
| rent_days | INT | NOT NULL, >0 | `Payment.rentDays` | Agreed rental days |
| extra_days | INT | DEFAULT 0, ≥0 | `Payment.extraDays` | Overdue days |
| damage_fee | DECIMAL | DEFAULT 0, ≥0 | `Payment.damageFee` | Damage surcharge |
| deposit | DECIMAL | DEFAULT 0, ≥0 | `Payment.deposit` | Security deposit (deducted from total) |
| pay_date | DATE | NULL | `Payment.payDate` | Date payment was processed |
| pay_status | ENUM | DEFAULT 'PENDING' | `Payment.status` | `PENDING` or `PAID` |
| total_paid | DECIMAL | NOT NULL, DEFAULT 0 | `Payment.calculateTotal()` | Computed final amount (Stored from Java) |

#### Table: `rents`
> **Note:** Java `Rent` holds full object references; DB stores FK integers. `DatabaseMapper` uses plural table name `rents` in DML — confirm your MySQL schema table name matches.

| Column | Type | Constraints | Java Field | Description |
|--------|------|-------------|------------|-------------|
| rent_id | INT | PK, AUTO_INCREMENT | `Rent.rentId` | Unique rental ID |
| customer_id | INT | FK→customer | `Rent.customer.customerId` | Renting customer |
| vehicle_id | INT | FK→vehicle | `Rent.vehicle.vehicleId` | Rented vehicle |
| staff_id | INT | FK→staff | `Rent.staff` → `Staff.getId()` | Staff who processed the rent |
| payment_id | INT | FK→payment, UNIQUE | `Rent.payment.paymentId` | Associated payment (1:1) |
| rent_days | INT | NOT NULL, >0 | `Rent.rentDays` | Agreed number of days |
| start_date | DATE | NOT NULL | `Rent.startDate` | Rental start date |
| end_date | DATE | NOT NULL | `Rent.endDate` | Expected return date |
| return_date | DATE | NULL | `Rent.returnDate` | Actual return date (NULL until returned) |
| status | BOOLEAN | DEFAULT TRUE | `Rent.status` | TRUE=Active, FALSE=Completed |

#### Table: `rent_records`
> **Note:** Immutable history snapshot. All fields are final in `RentRecord.java`. Staff snapshot (staffID, staffName) was added in a recent iteration and is fully persisted. `DatabaseMapper` uses plural `rent_records` in DML — confirm your MySQL schema table name matches. `fromResultSet()` expects aliased column names (e.g. `vehicle_power_source`, `rental_rate_per_day`, `customer_id_num`, `customer_phone`, `payment_method`, `price`, `discount`, `payment_status`).

| Column | Type | Constraints | Java Field | Description |
|--------|------|-------------|------------|-------------|
| record_id | INT | PK, AUTO_INCREMENT | — | Auto-assigned DB key |
| rent_id | INT | NOT NULL, UNIQUE | `RentRecord.rentId` | Links to original rent |
| vehicle_id | INT | NOT NULL | `RentRecord.vehicleId` | Snapshot vehicle ID |
| vehicle_code | VARCHAR(20) | NOT NULL | `RentRecord.vehicleCode` | |
| vehicle_type | VARCHAR(10) | NOT NULL | `RentRecord.vehicleType` | |
| power_source | VARCHAR(20) | NOT NULL | `RentRecord.vehiclePowerSource` | |
| vehicle_class | VARCHAR(30) | NOT NULL | `RentRecord.vehicleClass` | |
| brand | VARCHAR(50) | NOT NULL | `RentRecord.vehicleBrand` | |
| model | VARCHAR(100) | NOT NULL | `RentRecord.vehicleModel` | |
| licence_plate | VARCHAR(20) | NOT NULL | `RentRecord.licencePlate` | |
| rate_per_day | DECIMAL(10,2) | NOT NULL | `RentRecord.rentalRatePerDay` | |
| customer_id | INT | NOT NULL | `RentRecord.customerId` | |
| customer_name | VARCHAR(100) | NOT NULL | `RentRecord.customerName` | |
| id_num | VARCHAR(30) | NOT NULL | `RentRecord.customerIdNum` | |
| phone | VARCHAR(15) | NOT NULL | `RentRecord.customerPhone` | |
| staff_id | INT | NOT NULL | `RentRecord.staffID` | Snapshot of processing staff |
| staff_name | VARCHAR(100) | NOT NULL | `RentRecord.staffName` | |
| rent_days | INT | NOT NULL | `RentRecord.rentDays` | |
| start_date | DATE | NOT NULL | `RentRecord.startDate` | |
| end_date | DATE | NOT NULL | `RentRecord.endDate` | |
| return_date | DATE | NOT NULL | `RentRecord.returnDate` | |
| payment_id | INT | NOT NULL | `RentRecord.paymentId` | |
| method | VARCHAR(10) | NOT NULL | `RentRecord.paymentMethod` | |
| price | DECIMAL(10,2) | NOT NULL | `RentRecord.price` | |
| discount | DECIMAL(5,2) | NOT NULL | `RentRecord.discount` | |
| extra_days | INT | NOT NULL | `RentRecord.extraDays` | |
| damage_fee | DECIMAL(10,2) | NOT NULL | `RentRecord.damageFee` | |
| deposit | DECIMAL(10,2) | NOT NULL | `RentRecord.deposit` | |
| pay_date | DATE | NULL | `RentRecord.payDate` | |
| pay_status | VARCHAR(10) | NOT NULL | `RentRecord.paymentStatus` | |
| total_paid | DECIMAL(10,2) | NOT NULL | `RentRecord.totalPaid` | |

---

## 4. SQL — Query, Programming

### 4.1 DML — Sample Data

```sql
-- Insert Staff
INSERT INTO staffs (full_name, role, salary, username, password_hash, status, is_active)
VALUES
  ('Sophea Keo',   'Manager', 1200.00, 'sophea_mgr',  SHA2('admin1234', 256), TRUE, FALSE),
  ('Dara Chan',    'Regular',  700.00, 'dara_staff',  SHA2('staff5678', 256), TRUE, FALSE),
  ('Sreyleak Nim', 'Regular',  700.00, 'sreyleak_s',  SHA2('pass1234', 256),  TRUE, FALSE);

-- Insert Vehicles
INSERT INTO vehicles (vehicle_code, vehicle_type, power_source, vehicle_class, brand, model, rate_per_day, licence, licence_plate, is_available)
VALUES
  ('Car-1',  'Car',  'gasoline', 'SUV',    'Ford',  'Escape 2022',    300.00, 'VL-01-AB-1234', 'PP-1000', TRUE),
  ('Car-2',  'Car',  'electric', 'Sedan',  'Tesla', 'Model 3 2023',   500.00, 'VL-02-CD-5678', 'PP-1001', TRUE),
  ('Car-3',  'Car',  'diesel',   'Truck',  'Toyota','Hilux 2022',     400.00, 'VL-03-EF-9012', 'PP-1002', TRUE),
  ('Moto-1', 'Moto', 'gasoline', 'Sport',  'Honda', 'CBR600RR 2022',   75.00, 'MOTO-LIC-2026', 'ABC-1234', TRUE);

INSERT INTO cars (vehicle_id, number_of_seats) VALUES (1, 4), (2, 4), (3, 4);
INSERT INTO motos (vehicle_id, helmet_included) VALUES (4, TRUE);

-- Insert Customers
INSERT INTO customers (full_name, id_num, phone, id_card_photo, dl_photo)
VALUES
  ('Virak Heng',  'KH-ID-001', '0123456789', '/photos/id/virak.jpg',  '/photos/dl/virak_dl.jpg'),
  ('Bopha Lim',   'KH-ID-002', '0987654321', '/photos/id/bopha.jpg',  '/photos/dl/bopha_dl.jpg'),
  ('Kosal Prak',  'KH-ID-003', '0112233445', '/photos/id/kosal.jpg',  NULL);

-- Insert Payment (for Rent 1) — method='TBD' until vehicles returned
INSERT INTO payments (method, price, discount, rent_days, extra_days, damage_fee, deposit, pay_status)
VALUES ('TBD', 300.00, 0, 5, 0, 0, 50.00, 'PENDING');

-- Insert Rent
INSERT INTO rents (customer_id, vehicle_id, staff_id, payment_id, rent_days, start_date, end_date, status)
VALUES (1, 1, 2, 1, 5, '2026-03-04', '2026-03-09', TRUE);

-- Mark vehicles as unavailable
UPDATE vehicles SET is_available = FALSE WHERE vehicle_id = 1;

-- Process Payment on return (finalize)
UPDATE payments SET method = 'CASH', pay_date = '2026-03-09', pay_status = 'PAID'
WHERE payment_id = 1;

-- Complete rental
UPDATE rents SET return_date = '2026-03-09', status = FALSE WHERE rent_id = 1;

-- Restore vehicles availability
UPDATE vehicles SET is_available = TRUE WHERE vehicle_id = 1;

-- Delete a customers (only if no active rents)
DELETE FROM customers WHERE customer_id = 3
  AND customer_id NOT IN (SELECT customer_id FROM rents WHERE status = TRUE);
```

---

### 4.2 DQL — Analytical Queries

```sql
-- Q1: All available vehicles with daily rate
SELECT vehicle_id, vehicle_code, vehicle_type, brand, model,
       rate_per_day, licence_plate
FROM vehicles
WHERE is_available = TRUE
ORDER BY rate_per_day;

-- Q2: Active rentals with customers and vehicles info
SELECT r.rent_id, c.full_name AS customers, v.vehicle_code,
       v.brand, v.model, r.start_date, r.end_date, r.rent_days
FROM rents r
JOIN customers c ON r.customer_id = c.customer_id
JOIN vehicles  v ON r.vehicle_id  = v.vehicle_id
WHERE r.status = TRUE;

-- Q3: Revenue summary per vehicles type
SELECT v.vehicle_type,
       COUNT(*)          AS total_rentals,
       SUM(p.total_paid + p.deposit) AS total_revenue
FROM rents r
         JOIN vehicles v ON r.vehicle_id = v.vehicle_id
         JOIN payments p ON r.payment_id = p.payment_id
WHERE r.status = FALSE AND p.pay_status = 'PAID'
GROUP BY v.vehicle_type;

-- Q4: Top 3 customers by total spend
SELECT c.customer_id, c.full_name,
       COUNT(r.rent_id)  AS total_rents,
       SUM(p.total_paid) AS total_spent
FROM customers c
JOIN rents    r ON c.customer_id = r.customer_id
JOIN payments p ON r.payment_id  = p.payment_id
WHERE p.pay_status = 'PAID'
GROUP BY c.customer_id, c.full_name
ORDER BY total_spent DESC
LIMIT 3;

-- Q5: Rentals with extra days (overdue)
SELECT r.rent_id, c.full_name, v.vehicle_code,
       p.extra_days, p.damage_fee, p.total_paid
FROM rents r
JOIN customers c ON r.customer_id = c.customer_id
JOIN vehicles  v ON r.vehicle_id  = v.vehicle_id
JOIN payments  p ON r.payment_id  = p.payment_id
WHERE p.extra_days > 0;

-- Q6: Monthly revenue report
SELECT DATE_FORMAT(p.pay_date, '%Y-%m') AS month,
       COUNT(*) AS rentals,
       SUM(p.total_paid) AS revenue
FROM payments p
WHERE p.pay_status = 'PAID'
GROUP BY month
ORDER BY month DESC;
```

---

### 4.3 Views

```sql
-- VIEW 1: Available vehicles summary
CREATE VIEW v_available_vehicles AS
SELECT v.vehicle_id, v.vehicle_code, v.vehicle_type,
       v.brand, v.model, v.vehicle_class,
       v.rate_per_day, v.licence_plate
FROM vehicles v
WHERE v.is_available = TRUE;

-- VIEW 2: Active rentals dashboard
CREATE VIEW v_active_rentals AS
SELECT r.rent_id,
       c.full_name AS customer_name, c.phone,
       v.vehicle_code, v.brand, v.model, v.licence_plate,
       r.start_date, r.end_date,
       p.price, p.deposit, p.pay_status,
       s.full_name AS handled_by
FROM rents r
JOIN customers c ON r.customer_id = c.customer_id
JOIN vehicles  v ON r.vehicle_id  = v.vehicle_id
JOIN payments  p ON r.payment_id  = p.payment_id
JOIN staffs    s ON r.staff_id    = s.staff_id
WHERE r.status = TRUE;

-- VIEW 3: Completed rentals history
CREATE VIEW v_rental_history AS
SELECT rr.record_id, rr.rent_id,
       rr.customer_name, rr.vehicle_code, rr.brand, rr.model,
       rr.start_date, rr.return_date, rr.rent_days, rr.extra_days,
       rr.method, rr.total_paid
FROM rent_records rr
ORDER BY rr.return_date DESC;

-- VIEW 4: Staff performance (rentals handled)
CREATE VIEW v_staff_performance AS
SELECT s.staff_id, s.full_name, s.role,
       COUNT(r.rent_id)   AS rentals_handled,
       SUM(p.total_paid)  AS revenue_generated
FROM staffs s
LEFT JOIN rents    r ON s.staff_id   = r.staff_id
LEFT JOIN payments p ON r.payment_id = p.payment_id AND p.pay_status = 'PAID'
GROUP BY s.staff_id, s.full_name, s.role;
```

---

### 4.4 Stored Procedures

```sql
-- PROCEDURE 1: Add a new rental
DELIMITER $$
CREATE PROCEDURE s_proc_create_rent(
    p_customer_id  INT,
    p_vehicle_id   INT,
    p_staff_id     INT,
    p_rent_days    INT,
    p_start_date   DATE,
    p_end_date     DATE,
    p_deposit      DECIMAL(10,2)
)
BEGIN
    DECLARE v_rate   DECIMAL(10,2);
    DECLARE v_pay_id INT;
    DECLARE v_avail  BOOLEAN;

    -- Check availability
    SELECT is_available, rate_per_day INTO v_avail, v_rate
    FROM vehicles WHERE vehicle_id = p_vehicle_id;

    IF NOT v_avail THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Vehicle is not available.';
    END IF;

    -- Create payments record (method TBD until return)
    INSERT INTO payments (method, price, discount, rent_days, deposit, pay_status)
    VALUES ('TBD', v_rate, 0, p_rent_days, p_deposit, 'PENDING');

    SET v_pay_id = LAST_INSERT_ID();

    -- Create rents record
    INSERT INTO rents (customer_id, vehicle_id, staff_id, payment_id,
                      rent_days, start_date, end_date, status)
    VALUES (p_customer_id, p_vehicle_id, p_staff_id, v_pay_id,
            p_rent_days, p_start_date, p_end_date, TRUE);

    -- Mark vehicles as unavailable
    UPDATE vehicles SET is_available = FALSE WHERE vehicle_id = p_vehicle_id;

    SELECT LAST_INSERT_ID() AS new_rent_id;
END$$
DELIMITER ;

-- PROCEDURE 2: Return vehicles and finalize payments
DELIMITER $$
CREATE PROCEDURE s_proc_return_vehicle(
    p_rent_id      INT,
    p_return_date  DATE,
    p_extra_days   INT,
    p_damage_fee   DECIMAL(10,2),
    p_method       VARCHAR(10),
    p_discount DECIMAL(5,2)
)
BEGIN
    DECLARE v_vehicle_id INT;
    DECLARE v_payment_id INT;
    DECLARE v_rent_days  INT;

    SELECT vehicle_id, payment_id, rent_days
    INTO v_vehicle_id, v_payment_id, v_rent_days
    FROM rents WHERE rent_id = p_rent_id;

    -- Update payments (mirrors Payment.processPayment() + setExtraDays/setDamageFee)
    UPDATE payments
    SET extra_days   = p_extra_days,
        damage_fee   = p_damage_fee,
        discount     = p_discount,
        rent_days    = v_rent_days + p_extra_days,
        method       = p_method,
        pay_date     = p_return_date,
        pay_status   = 'PAID',
        total_paid   = (price * (v_rent_days + p_extra_days))
                           + p_damage_fee
            - ((p_discount/100) * price * (v_rent_days + p_extra_days))
            - deposit
    WHERE payment_id = v_payment_id;

    -- Close rental
    UPDATE rents
    SET return_date = p_return_date, status = FALSE
    WHERE rent_id = p_rent_id;

    -- Restore vehicles availability
    UPDATE vehicles SET is_available = TRUE WHERE vehicle_id = v_vehicle_id;

    -- Archive to rent_records (mirrors RentRecord constructor)
    INSERT INTO rent_records
        SELECT NULL, r.rent_id,
               v.vehicle_id, v.vehicle_code, v.vehicle_type, v.power_source,
               v.vehicle_class, v.brand, v.model, v.licence_plate, v.rate_per_day,
               c.customer_id, c.full_name, c.id_num, c.phone,
               s.staff_id, s.full_name,
               r.rent_days, r.start_date, r.end_date, r.return_date,
               p.payment_id, p.method, p.price, p.discount,
               p.extra_days, p.damage_fee, p.deposit, p.pay_date,
               p.pay_status, p.total_paid
        FROM rents r
        JOIN vehicles  v ON r.vehicle_id  = v.vehicle_id
        JOIN customers c ON r.customer_id = c.customer_id
        JOIN payments  p ON r.payment_id  = p.payment_id
        JOIN staffs    s ON r.staff_id    = s.staff_id
        WHERE r.rent_id = p_rent_id;
END$$
DELIMITER ;

-- PROCEDURE 3: Get customers rental history
DELIMITER $$
CREATE PROCEDURE s_proc_customer_history(p_customer_id INT)
BEGIN
    SELECT rr.rent_id, rr.vehicle_code, rr.brand, rr.model,
           rr.start_date, rr.return_date, rr.rent_days,
           rr.method, rr.total_paid
    FROM rent_records rr
    WHERE rr.customer_id = p_customer_id
    ORDER BY rr.return_date DESC;
END$$
DELIMITER ;

-- PROCEDURE 4: Add a new vehicle
DROP PROCEDURE IF EXISTS s_proc_add_vehicle;
DELIMITER $$
CREATE PROCEDURE s_proc_add_vehicle(
    p_vehicle_code    VARCHAR(20),
    p_vehicle_type    VARCHAR(20),
    p_power_source    VARCHAR(20),
    p_vehicle_class   VARCHAR(30),
    p_brand           VARCHAR(50),
    p_model           VARCHAR(100),
    p_rate_per_day    DECIMAL(10,2),
    p_licence         VARCHAR(50),
    p_licence_plate   VARCHAR(20),
    p_is_available    BOOLEAN,
    p_number_of_seats INT,
    p_helmet_included BOOLEAN
)
BEGIN
INSERT INTO vehicles (
    vehicle_code, vehicle_type, power_source, vehicle_class,
    brand, model, rate_per_day, licence, licence_plate,
    is_available, number_of_seats, helmet_included
) VALUES (
             p_vehicle_code, p_vehicle_type, p_power_source, p_vehicle_class,
             p_brand, p_model, p_rate_per_day, p_licence, p_licence_plate,
             p_is_available, p_number_of_seats, p_helmet_included
         );

SELECT LAST_INSERT_ID() AS new_vehicle_id;
END$$
DELIMITER ;

-- PROCEDURE 5: Update an existing vehicle by id and vehicle code
DROP PROCEDURE IF EXISTS s_proc_update_vehicle;
DELIMITER $$
CREATE PROCEDURE s_proc_update_vehicle(
    p_vehicle_id      INT,
    p_vehicle_code    VARCHAR(20),
    p_vehicle_type    VARCHAR(20),
    p_power_source    VARCHAR(20),
    p_vehicle_class   VARCHAR(30),
    p_brand           VARCHAR(50),
    p_model           VARCHAR(100),
    p_rate_per_day    DECIMAL(10,2),
    p_licence         VARCHAR(50),
    p_licence_plate   VARCHAR(20),
    p_is_available    BOOLEAN,
    p_number_of_seats INT,
    p_helmet_included BOOLEAN
)
BEGIN
    IF p_vehicle_id IS NULL OR p_vehicle_code IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Both vehicle_id and vehicle_code must be provided to update.';
END IF;

UPDATE vehicles
SET
    vehicle_type    = IF(p_vehicle_type IS NULL, vehicle_type, p_vehicle_type),
    power_source    = IF(p_power_source IS NULL, power_source, p_power_source),
    vehicle_class   = IF(p_vehicle_class IS NULL, vehicle_class, p_vehicle_class),
    brand           = IF(p_brand IS NULL, brand, p_brand),
    model           = IF(p_model IS NULL, model, p_model),
    rate_per_day    = IF(p_rate_per_day IS NULL, rate_per_day, p_rate_per_day),
    licence         = IF(p_licence IS NULL, licence, p_licence),
    licence_plate   = IF(p_licence_plate IS NULL, licence_plate, p_licence_plate),
    is_available    = IF(p_is_available IS NULL, is_available, p_is_available),
    number_of_seats = IF(p_number_of_seats IS NULL, number_of_seats, p_number_of_seats),
    helmet_included = IF(p_helmet_included IS NULL, helmet_included, p_helmet_included)
WHERE vehicle_id = p_vehicle_id
  AND vehicle_code = p_vehicle_code;

IF ROW_COUNT() = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Vehicle not found for update.';
END IF;
END$$
DELIMITER ;

-- PROCEDURE 6: Remove a vehicle by id or vehicle code
DROP PROCEDURE IF EXISTS s_proc_remove_vehicle;
DELIMITER $$
CREATE PROCEDURE s_proc_remove_vehicle(
    p_vehicle_id   INT,
    p_vehicle_code VARCHAR(20)
)
BEGIN
    IF p_vehicle_id IS NULL AND p_vehicle_code IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Either vehicle_id or vehicle_code must be provided.';
END IF;

DELETE FROM vehicles
WHERE
    (p_vehicle_id IS NOT NULL AND vehicle_id = p_vehicle_id)
   OR (p_vehicle_id IS NULL AND vehicle_code = p_vehicle_code);

IF ROW_COUNT() = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Vehicle not found for deletion.';
END IF;
END$$
DELIMITER ;

```

---

## 5. Demo (Role-Based Access & Reporting)

### 5.1 Role-Based Access Control

In Java, permissions are enforced via the `can(String action)` method on each `Staff` subclass.  
`Garage` constants define each action string. The matrix below reflects the actual `RegularStaff.can()` and `ManagerStaff.can()` implementations:

| Action Constant | Operation | Manager | Regular |
|-----------------|-----------|:-------:|:-------:|
| `MANAGE_VEHICLE` | Add / Update / Remove Vehicle | ✅ | ❌ |
| `VIEW_VEHICLE` | View Vehicles | ✅ | ✅ |
| `MANAGE_CUSTOMER` | Add / Update Customer | ✅ | ✅ |
| `VIEW_CUSTOMER` | View Customers | ✅ | ✅ |
| `ADD_RENT` | Add Rental | ✅ | ✅ |
| `VIEW_RENT` | View Rentals | ✅ | ✅ |
| `RETURN_VEHICLE` | Return Vehicle | ✅ | ✅ |
| `SHOW_PAYMENT` | View Payments | ✅ | ✅ |
| `MANAGE_STAFF` | Manage Staff | ✅ | ❌ |
| `VIEW_REPORTS` | View Reports | ✅ | ❌ |
| `SET_MANAGER_SALARY` | Set Manager Salary | ❌ | ❌ |

> `SET_MANAGER_SALARY` is blocked for **all** roles including Manager (prevented in `ManagerStaff.can()`).

### 5.2 Java ↔ Database Field Mapping (Complete Reference)

| Java Class | Java Field | DB Table | DB Column | Notes |
|------------|------------|----------|-----------|-------|
| `Vehicle` | `vehicleId` | `vehicles` | `vehicle_id` | Auto-assigned via `Garage.vehicleID + 1` |
| `Vehicle` | `vehicleCode` | `vehicles` | `vehicle_code` | e.g. `Car-1`, `Moto-2` |
| `Vehicle` | `powerSource` | `vehicles` | `power_source` | |
| `Vehicle` | `vehicleClass` | `vehicles` | `vehicle_class` | |
| `Vehicle` | `vehicleBrand` | `vehicles` | `brand` | |
| `Vehicle` | `vehicleModel` | `vehicles` | `model` | |
| `Vehicle` | `rentalRatePerDay` | `vehicles` | `rate_per_day` | |
| `Vehicle` | `vehicleLicence` | `vehicles` | `licence` | |
| `Vehicle` | `licencePlate` | `vehicles` | `licence_plate` | |
| `Vehicle` | `isAvailable` | `vehicles` | `is_available` | |
| `Car` | `numberOfSeats` | `vehicles` | `number_of_seats` | |
| `Moto` | `helmetIncluded` | `vehicles` | `helmet_included` | |
| `Customer` | `customerId` | `customers` | `customer_id` | |
| `Customer` | `customerName` | `customers` | `full_name` | |
| `Customer` | `customerIdNum` | `customers` | `id_num` | |
| `Customer` | `customerPhone` | `customers` | `phone` | Validated 9-10 digits |
| `Customer` | `IDCardPhoto` | `customers` | `id_card_photo` | |
| `Customer` | `DriverLicensePhoto` | `customers` | `dl_photo` | |
| `Staff` | `staffId` | `staffs` | `staff_id` | |
| `Staff` | `name` | `staffs` | `full_name` | |
| subclass type | (`instanceof`) | `staffs` | `role` | `ManagerStaff`→`Manager`, `RegularStaff`→`Regular` |
| `ManagerStaff` / `RegularStaff` | `salary` | `staffs` | `salary` | Both subclasses carry salary |
| `ManagerStaff` | `bonus` | `staffs` | `bonus` | 0 for Regular |
| `Staff` | `username` | `staffs` | `username` | |
| `Staff` | `password` | `staffs` | `password_hash` | Raw ≥4 chars; store as SHA-2/BCrypt |
| `Staff` | `status` | `staffs` | `status` | TRUE=employed |
| `Staff` | `active` | `staffs` | `is_active` | TRUE=logged-in session |
| `RegularStaff` | `workStation` | `staffs` | `work_station` | NULL for Manager |
| `Payment` | `paymentId` | `payments` | `payment_id` | |
| `Payment` | `paymentMethod` | `payments` | `method` | |
| `Payment` | **`price`** | `payments` | **`price`** | Matches |
| `Payment` | **`discount`** | `payments` | **`discount`** | Matches |
| `Payment` | `rentDays` | `payments` | `rent_days` | |
| `Payment` | `extraDays` | `payments` | `extra_days` | |
| `Payment` | `damageFee` | `payments` | `damage_fee` | |
| `Payment` | `deposit` | `payments` | `deposit` | |
| `Payment` | `payDate` | `payments` | `pay_date` | |
| `Payment` | `status` | `payments` | `pay_status` | |
| `Payment` | `calculateTotal()` | `payments` | `total_paid` | Stored from Java |
| `Rent` | `rentId` | `rents` | `rent_id` | |
| `Rent` | `vehicle.vehicleId` | `rents` | `vehicle_id` | Object ref → FK |
| `Rent` | `customer.customerId` | `rents` | `customer_id` | Object ref → FK |
| `Rent` | `staff` → `getId()` | `rents` | `staff_id` | Object ref → FK |
| `Rent` | `payment.paymentId` | `rents` | `payment_id` | Object ref → FK |
| `Rent` | `rentDays` | `rents` | `rent_days` | |
| `Rent` | `startDate` | `rents` | `start_date` | |
| `Rent` | `endDate` | `rents` | `end_date` | |
| `Rent` | `returnDate` | `rents` | `return_date` | NULL until returned |
| `Rent` | `status` | `rents` | `status` | TRUE=Active |
| `RentRecord` | `rentId` | `rent_records` | `rent_id` | Immutable snapshot |
| `RentRecord` | `vehicleId` | `rent_records` | `vehicle_id` | |
| `RentRecord` | `vehicleCode` | `rent_records` | `vehicle_code` | |
| `RentRecord` | `vehicleType` | `rent_records` | `vehicle_type` | |
| `RentRecord` | `vehiclePowerSource` | `rent_records` | `power_source` | |
| `RentRecord` | `vehicleClass` | `rent_records` | `vehicle_class` | |
| `RentRecord` | `vehicleBrand` | `rent_records` | `brand` | |
| `RentRecord` | `vehicleModel` | `rent_records` | `model` | |
| `RentRecord` | `licencePlate` | `rent_records` | `licence_plate` | |
| `RentRecord` | `rentalRatePerDay` | `rent_records` | `rate_per_day` | |
| `RentRecord` | `customerId` | `rent_records` | `customer_id` | |
| `RentRecord` | `customerName` | `rent_records` | `customer_name` | |
| `RentRecord` | `customerIdNum` | `rent_records` | `id_num` | |
| `RentRecord` | `customerPhone` | `rent_records` | `phone` | |
| `RentRecord` | **`staffID`** | `rent_records` | **`staff_id`** | ⚠ Added in recent iteration |
| `RentRecord` | **`staffName`** | `rent_records` | **`staff_name`** | ⚠ Added in recent iteration |
| `RentRecord` | `rentDays` | `rent_records` | `rent_days` | |
| `RentRecord` | `startDate` | `rent_records` | `start_date` | |
| `RentRecord` | `endDate` | `rent_records` | `end_date` | |
| `RentRecord` | `returnDate` | `rent_records` | `return_date` | |
| `RentRecord` | `paymentId` | `rent_records` | `payment_id` | |
| `RentRecord` | `paymentMethod` | `rent_records` | `method` | |
| `RentRecord` | `price` | `rent_records` | `price` | Matches |
| `RentRecord` | `discount` | `rent_records` | `discount` | Matches |
| `RentRecord` | `extraDays` | `rent_records` | `extra_days` | |
| `RentRecord` | `damageFee` | `rent_records` | `damage_fee` | |
| `RentRecord` | `deposit` | `rent_records` | `deposit` | |
| `RentRecord` | `payDate` | `rent_records` | `pay_date` | |
| `RentRecord` | `paymentStatus` | `rent_records` | `pay_status` | |
| `RentRecord` | `totalPaid` | `rent_records` | `total_paid` | Stored value (Java recalculates) |

### 5.3 Database Summary
- **DBMS:** MySQL 8+
- **Database name:** `vrms`
- **Core tables:** `vehicles`, `customers`, `staffs`, `payments`, `rents`, `rent_records`
- **Key DB objects:** 4 Views, 3 Stored Procedures, CHECK constraints on all numeric columns

> **Table naming:** All tables use **plural** names, matching `DatabaseMapper.java` DML conventions (`customers`, `vehicles`, `rents`, `payments`, `rent_records`, etc.).

### 5.4 Sample Reports

**Report 1 — Daily Revenue:**
```sql
SELECT pay_date, COUNT(*) AS transactions, SUM(total_paid) AS daily_revenue
FROM payments
WHERE pay_status = 'PAID'
GROUP BY pay_date
ORDER BY pay_date DESC;
```

**Report 2 — Vehicle Utilization Rate:**
```sql
SELECT v.vehicle_id, v.vehicle_code, v.brand, v.model,
       COUNT(r.rent_id) AS times_rented,
       SUM(r.rent_days) AS total_days_rented
FROM vehicles v
LEFT JOIN rents r ON v.vehicle_id = r.vehicle_id
GROUP BY v.vehicle_id, v.vehicle_code, v.brand, v.model
ORDER BY total_days_rented DESC;
```

**Report 3 — Pending Payments (overdue active rentals):**
```sql
SELECT r.rent_id, c.full_name, c.phone,
       v.vehicle_code, r.start_date, r.end_date,
       DATEDIFF(CURDATE(), r.end_date) AS days_overdue
FROM rents r
JOIN customers c ON r.customer_id = c.customer_id
JOIN vehicles  v ON r.vehicle_id  = v.vehicle_id
WHERE r.status = TRUE AND r.end_date < CURDATE();
```

---

## 6. Conclusion and Future Work

### 6.1 Outcome / Completed

| # | Item | Status |
|---|------|--------|
| 1 | ER Diagram with all entities and relationships | ✅ Done |
| 2 | Relational schema (normalized to 3NF) | ✅ Done |
| 3 | Full DDL — all tables with constraints | ✅ Done |
| 4 | Data Dictionaries for all tables (synced to Java) | ✅ Done |
| 5 | DML — sample inserts, updates, deletes | ✅ Done |
| 6 | Analytical DQL queries (6 queries) | ✅ Done |
| 7 | 4 Views for dashboard and reporting | ✅ Done |
| 8 | 3 Stored procedures (create rent, return, history) | ✅ Done |
| 9 | Complete Java↔DB field mapping (all classes) | ✅ Done |
| 10 | Role-based access mapping (Manager vs Regular, with action constants) | ✅ Done |

### 6.2 Pending Tasks / Future Work

| # | Future Feature | Priority |
|---|---------------|----------|
| 1 | **Password hashing** — Use BCrypt instead of SHA-2 for `password_hash` | High |
| 2 | **Triggers** — Auto-update `is_available` on `rent` INSERT/UPDATE instead of manual DML | High |
| 3 | **Discount table** — Separate configurable discount tiers per customer type | Medium |
| 4 | **Audit log table** — Track who modified which record and when | Medium |
| 5 | **Multi-branch support** — Add `garage`/`branch` table for multi-location expansion | Low |
| 6 | **Notification system** — Email/SMS alerts for overdue returns | Low |
| 7 | **Vehicle maintenance log** — Track service history per vehicle | Low |
