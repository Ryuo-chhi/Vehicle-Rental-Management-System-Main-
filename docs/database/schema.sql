-- ================================================
-- Vehicle Rental Management System — DDL
-- Database: MySQL 8+
-- ================================================

USE defaultdb;

DROP TABLE IF EXISTS rents;
DROP TABLE IF EXISTS staffs;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS vehicles;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS rent_records;

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
    -- Subtype specific columns
    number_of_seats  INT           NULL CHECK (number_of_seats > 0),
    helmet_included BOOLEAN       NULL DEFAULT FALSE
);

-- ── CUSTOMER ─────────────────────────────────────
CREATE TABLE customers (
    customer_id    INT          AUTO_INCREMENT PRIMARY KEY,
    full_name      VARCHAR(100) NOT NULL,
    id_num         VARCHAR(30)  NOT NULL UNIQUE,
    phone          VARCHAR(15)  NOT NULL UNIQUE,
    id_card_photo  VARCHAR(255),
    dl_photo       VARCHAR(255)
);

-- ── STAFF ────────────────────────────────────────
CREATE TABLE staffs (
    staff_id      INT           AUTO_INCREMENT PRIMARY KEY,
    full_name     VARCHAR(100)  NOT NULL,
    role          ENUM('Manager','Regular') NOT NULL,
    salary        DECIMAL(10,2) NOT NULL CHECK (salary >= 0),
    bonus         DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (bonus >= 0),
    username      VARCHAR(50)   NOT NULL UNIQUE,
    password_hash VARCHAR(255)  NOT NULL,
    status        BOOLEAN       NOT NULL DEFAULT TRUE,
    is_active     BOOLEAN       NOT NULL DEFAULT FALSE,
    work_station  VARCHAR(100)  NULL
);

-- ── PAYMENT ──────────────────────────────────────
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
    pay_status      ENUM('PENDING','PAID') NOT NULL DEFAULT 'PENDING',
    total_paid      DECIMAL(10,2) NOT NULL DEFAULT 0
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
    return_date DATE,
    status      BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (vehicle_id)  REFERENCES vehicles(vehicle_id),
    FOREIGN KEY (staff_id)    REFERENCES staffs(staff_id),
    FOREIGN KEY (payment_id)  REFERENCES payments(payment_id)
);

-- ── RENT_RECORD (history snapshot) ───────────────
CREATE TABLE rent_records (
    record_id       INT AUTO_INCREMENT PRIMARY KEY,
    rent_id         INT          NOT NULL UNIQUE,
    vehicle_id      INT          NOT NULL,
    vehicle_code    VARCHAR(20)  NOT NULL,
    vehicle_type    VARCHAR(10)  NOT NULL,
    power_source    VARCHAR(20)  NOT NULL,
    vehicle_class   VARCHAR(30)  NOT NULL,
    brand           VARCHAR(50)  NOT NULL,
    model           VARCHAR(100) NOT NULL,
    licence_plate   VARCHAR(20)  NOT NULL,
    rate_per_day    DECIMAL(10,2) NOT NULL,
    customer_id     INT          NOT NULL,
    customer_name   VARCHAR(100) NOT NULL,
    id_num          VARCHAR(30)  NOT NULL,
    phone           VARCHAR(15)  NOT NULL,
    staff_id        INT          NOT NULL,
    staff_name      VARCHAR(100) NOT NULL,
    rent_days       INT          NOT NULL,
    start_date      DATE         NOT NULL,
    end_date        DATE         NOT NULL,
    return_date     DATE         NOT NULL,
    payment_id      INT          NOT NULL,
    method          VARCHAR(10)  NOT NULL,
    price           DECIMAL(10,2) NOT NULL,
    discount        DECIMAL(5,2) NOT NULL,
    extra_days      INT          NOT NULL,
    damage_fee      DECIMAL(10,2) NOT NULL,
    deposit         DECIMAL(10,2) NOT NULL,
    pay_date        DATE,
    pay_status      VARCHAR(10)  NOT NULL,
    total_paid      DECIMAL(10,2) NOT NULL
);
