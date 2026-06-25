USE defaultdb;

-- 1. Insert Staff (20 Records)
INSERT INTO staffs (full_name, username, password_hash, status, is_active, salary, role, work_station) VALUES
    ('James Smith', 'jsmith', 'pass123', true, false, 800.0, 'Regular', 'General Station'),
    ('Olivia Johnson', 'ojohnson', 'pass123', true, false, 1200.0, 'Manager', NULL),
    ('William Williams', 'wwilliams', 'pass123', true, false, 800.0, 'Regular', 'General Station'),
    ('Sophia Brown', 'sbrown', 'pass123', true, false, 1200.0, 'Manager', NULL),
    ('Benjamin Jones', 'bjones', 'pass123', true, false, 850.0, 'Regular', 'General Station'),
    ('Isabella Garcia', 'igarcia', 'pass123', true, false, 850.0, 'Regular', 'General Station'),
    ('Lucas Miller', 'lmiller', 'pass123', true, false, 900.0, 'Regular', 'General Station'),
    ('Mia Davis', 'mdavis', 'pass123', true, false, 800.0, 'Regular', 'General Station'),
    ('Henry Rodriguez', 'hrodriguez', 'pass123', true, false, 820.0, 'Regular', 'General Station'),
    ('Amelia Martinez', 'amartinez', 'pass123', true, false, 820.0, 'Regular', 'General Station'),
    ('Alexander Hernandez', 'ahernandez', 'pass123', true, false, 850.0, 'Regular', 'General Station'),
    ('Evelyn Lopez', 'elopez', 'pass123', true, false, 850.0, 'Regular', 'General Station'),
    ('Daniel Gonzalez', 'dgonzalez', 'pass123', true, false, 800.0, 'Regular', 'General Station'),
    ('Abigail Wilson', 'awilson', 'pass123', true, false, 750.0, 'Regular', 'General Station'),
    ('Matthew Anderson', 'manderson', 'pass123', true, false, 900.0, 'Regular', 'General Station'),
    ('Emily Thomas', 'ethomas', 'pass123', false, false, 800.0, 'Regular', 'General Station'),
    ('Joseph Taylor', 'jtaylor', 'pass123', true, false, 850.0, 'Regular', 'General Station'),
    ('Elizabeth Moore', 'emoore', 'pass123', true, false, 850.0, 'Regular', 'General Station'),
    ('David Jackson', 'djackson', 'pass123', true, false, 800.0, 'Regular', 'General Station'),
    ('Sofia Martin', 'smartin', 'pass123', true, false, 1500.0, 'Manager', NULL);


-- 2. Insert Customers (25 Records)
INSERT INTO customers (full_name, id_num, phone, id_card_photo, dl_photo) VALUES
    ('John Doe', 'ID-10001', '012345678', 'john_id.jpg', 'john_dl.jpg'),
    ('Jane Roe', 'ID-10002', '012345679', 'jane_id.jpg', 'jane_dl.jpg'),
    ('Michael Green', 'ID-10003', '012345680', 'michael_id.jpg', 'michael_dl.jpg'),
    ('Sarah White', 'ID-10004', '012345681', 'sarah_id.jpg', 'sarah_dl.jpg'),
    ('Robert Black', 'ID-10005', '012345682', 'robert_id.jpg', 'robert_dl.jpg'),
    ('Jessica Blue', 'ID-10006', '012345683', 'jessica_id.jpg', 'jessica_dl.jpg'),
    ('David Red', 'ID-10007', '012345684', 'david_id.jpg', 'david_dl.jpg'),
    ('Laura Yellow', 'ID-10008', '012345685', 'laura_id.jpg', 'laura_dl.jpg'),
    ('Paul Cyan', 'ID-10009', '012345686', 'paul_id.jpg', 'paul_dl.jpg'),
    ('Emma Magenta', 'ID-10010', '012345687', 'emma_id.jpg', 'emma_dl.jpg'),
    ('William Pink', 'ID-10011', '012345688', 'william_id.jpg', 'william_dl.jpg'),
    ('Olivia Grey', 'ID-10012', '012345689', 'olivia_id.jpg', 'olivia_dl.jpg'),
    ('George Brown', 'ID-10013', '012345690', 'george_id.jpg', 'george_dl.jpg'),
    ('Sophia Violet', 'ID-10014', '012345691', 'sophia_id.jpg', 'sophia_dl.jpg'),
    ('Charles Ash', 'ID-10015', '012345692', 'charles_id.jpg', 'charles_dl.jpg'),
    ('Isabella Mint', 'ID-10016', '012345693', 'isabella_id.jpg', 'isabella_dl.jpg'),
    ('Thomas Teal', 'ID-10017', '012345694', 'thomas_id.jpg', 'thomas_dl.jpg'),
    ('Mia Coral', 'ID-10018', '012345695', 'mia_id.jpg', 'mia_dl.jpg'),
    ('Daniel Plum', 'ID-10019', '012345696', 'daniel_id.jpg', 'daniel_dl.jpg'),
    ('Charlotte Gold', 'ID-10020', '012345697', 'charlotte_id.jpg', 'charlotte_dl.jpg'),
    ('Matthew Silver', 'ID-10021', '012345698', 'matthew_id.jpg', 'matthew_dl.jpg'),
    ('Amelia Bronze', 'ID-10022', '012345699', 'amelia_id.jpg', 'amelia_dl.jpg'),
    ('Anthony Zinc', 'ID-10023', '012345700', 'anthony_id.jpg', 'anthony_dl.jpg'),
    ('Harper Iron', 'ID-10024', '012345701', 'harper_id.jpg', 'harper_dl.jpg'),
    ('Joshua Brass', 'ID-10025', '012345702', 'joshua_id.jpg', 'joshua_dl.jpg');


-- 3. Insert Vehicles (20 Records)
-- First 10 vehicles are NOT available (they are in the 10 active rents)
-- Last 10 vehicles are available
INSERT INTO vehicles (vehicle_type, vehicle_code, power_source, vehicle_class, brand, model, rate_per_day, licence, licence_plate, is_available, number_of_seats, helmet_included) VALUES
    ('Car', 'Car-1', 'Gasoline', 'Sedan', 'Toyota', 'Camry', 50.0, 'VL-001', 'PP-2A 1234', false, 5, NULL),
    ('Car', 'Car-2', 'Hybrid', 'SUV', 'Lexus', 'RX300', 80.0, 'VL-002', 'PP-2A 2345', false, 5, NULL),
    ('Car', 'Car-3', 'Gasoline', 'Truck', 'Ford', 'Ranger', 60.0, 'VL-003', 'PP-2A 3456', false, 4, NULL),
    ('Car', 'Car-4', 'Electric', 'Sedan', 'Tesla', 'Model 3', 100.0, 'VL-004', 'PP-2A 4567', false, 5, NULL),
    ('Car', 'Car-5', 'Gasoline', 'Van', 'Hyundai', 'H1', 75.0, 'VL-005', 'PP-2A 5678', false, 12, NULL),
    ('Car', 'Car-6', 'Gasoline', 'SUV', 'Toyota', 'Fortuner', 70.0, 'VL-006', 'PP-2B 1111', false, 7, NULL),
    ('Car', 'Car-7', 'Gasoline', 'Sedan', 'Honda', 'Civic', 45.0, 'VL-007', 'PP-2B 2222', false, 5, NULL),
    ('Car', 'Car-8', 'Hybrid', 'Sedan', 'Toyota', 'Prius', 40.0, 'VL-008', 'PP-2B 3333', false, 5, NULL),
    ('Car', 'Car-9', 'Diesel', 'Truck', 'Toyota', 'Hilux', 65.0, 'VL-009', 'PP-2B 4444', false, 5, NULL),
    ('Car', 'Car-10', 'Gasoline', 'SUV', 'Mazda', 'CX-5', 60.0, 'VL-010', 'PP-2B 5555', false, 5, NULL),
    ('Moto', 'Moto-1', 'Gasoline', 'Standard', 'Honda', 'Dream', 15.0, 'VL-011', 'PP-1A 1234', true, NULL, true),
    ('Moto', 'Moto-2', 'Gasoline', 'Scooter', 'Honda', 'Scoopy', 12.0, 'VL-012', 'PP-1A 2345', true, NULL, true),
    ('Moto', 'Moto-3', 'Gasoline', 'Sport', 'Yamaha', 'R15', 25.0, 'VL-013', 'PP-1A 3456', true, NULL, true),
    ('Moto', 'Moto-4', 'Gasoline', 'Standard', 'Suzuki', 'Viva', 10.0, 'VL-014', 'PP-1A 4567', true, NULL, false),
    ('Moto', 'Moto-5', 'Electric', 'Scooter', 'VinFast', 'Klara', 15.0, 'VL-015', 'PP-1A 5678', true, NULL, true),
    ('Moto', 'Moto-6', 'Gasoline', 'Standard', 'Honda', 'Wave', 8.0, 'VL-016', 'PP-1B 1111', true, NULL, true),
    ('Moto', 'Moto-7', 'Gasoline', 'Standard', 'Honda', 'PCX', 20.0, 'VL-017', 'PP-1B 2222', true, NULL, true),
    ('Moto', 'Moto-8', 'Gasoline', 'Sport', 'Kawasaki', 'Ninja 300', 35.0, 'VL-018', 'PP-1B 3333', true, NULL, true),
    ('Moto', 'Moto-9', 'Gasoline', 'Offroad', 'Honda', 'CRF250', 30.0, 'VL-019', 'PP-1B 4444', true, NULL, false),
    ('Moto', 'Moto-10', 'Gasoline', 'Scooter', 'Yamaha', 'Fino', 10.0, 'VL-020', 'PP-1B 5555', true, NULL, true);


-- 4. Insert Payments (10 Active Rent Payments - PENDING)
-- For active rents, pay_date is NULL and total_paid (final balance) is 0.
-- We use IDs 21-30 to indicate these are the most recent payments.
INSERT INTO payments (payment_id, method, rent_days, price, discount, extra_days, damage_fee, pay_date, pay_status, deposit, total_paid) VALUES
    (21, 'TBD', 3, 50.0, 0, 0, 0, NULL, 'PENDING', 150.0, 0.00),
    (22, 'TBD', 2, 80.0, 5, 0, 0, NULL, 'PENDING', 200.0, 0.00),
    (23, 'TBD', 4, 60.0, 0, 0, 0, NULL, 'PENDING', 100.0, 0.00),
    (24, 'TBD', 1, 100.0, 0, 0, 0, NULL, 'PENDING', 300.0, 0.00),
    (25, 'TBD', 5, 75.0, 10, 0, 0, NULL, 'PENDING', 200.0, 0.00),
    (26, 'TBD', 3, 70.0, 0, 0, 0, NULL, 'PENDING', 200.0, 0.00),
    (27, 'TBD', 7, 45.0, 15, 0, 0, NULL, 'PENDING', 150.0, 0.00),
    (28, 'TBD', 2, 40.0, 0, 0, 0, NULL, 'PENDING', 100.0, 0.00),
    (29, 'TBD', 3, 65.0, 0, 0, 0, NULL, 'PENDING', 200.0, 0.00),
    (30, 'TBD', 1, 60.0, 0, 0, 0, NULL, 'PENDING', 150.0, 0.00);


-- 5. Insert Rents (10 Active Records)
-- status is true, return_date is NULL.
-- Using IDs 21-30 to indicate these are current rents.
INSERT INTO rents (rent_id, customer_id, vehicle_id, staff_id, payment_id, rent_days, start_date, end_date, return_date, status) VALUES
    (21, 1, 1, 1, 21, 3, '2026-03-28', '2026-03-31', NULL, true),
    (22, 2, 2, 2, 22, 2, '2026-03-29', '2026-03-31', NULL, true),
    (23, 3, 3, 3, 23, 4, '2026-03-27', '2026-03-31', NULL, true),
    (24, 4, 4, 4, 24, 1, '2026-03-30', '2026-03-31', NULL, true),
    (25, 5, 5, 5, 25, 5, '2026-03-26', '2026-03-31', NULL, true),
    (26, 6, 6, 6, 26, 3, '2026-03-28', '2026-03-31', NULL, true),
    (27, 7, 7, 7, 27, 7, '2026-03-24', '2026-03-31', NULL, true),
    (28, 8, 8, 8, 28, 2, '2026-03-29', '2026-03-31', NULL, true),
    (29, 9, 9, 9, 29, 3, '2026-03-28', '2026-03-31', NULL, true),
    (30, 10, 10, 10, 30, 1, '2026-03-30', '2026-03-31', NULL, true);


-- 6. Insert Rent Records (20 Historical Records)
-- Historical records represent past transactions that are finished (PAID).
-- record_id is PK, rent_id is from the rents table.
-- rent_id 1-20 are used for historical data (older rents).
INSERT INTO rent_records (rent_id, payment_method, price, discount, extra_days, damage_fee, deposit, pay_date, payment_status, total_paid, return_date) VALUES
    (1, 11, 'Moto-1', 'Moto', 'Gasoline', 'Standard', 'Honda', 'Dream', 'PP-1A 1234', 15.0, 11, 'William Pink', 'ID-10011', '012345688', 11, 'Alexander Hernandez', 3, '2026-03-01', '2026-03-04', '2026-03-04', 1, 'CASH', 15.0, 0, 0, 0, 50.0, '2026-03-04', 'PAID', 45.0),
    (3, 13, 'Moto-3', 'Moto', 'Gasoline', 'Sport', 'Yamaha', 'R15', 'PP-1A 3456', 25.0, 13, 'George Brown', 'ID-10013', '012345690', 13, 'Daniel Gonzalez', 2, '2026-03-05', '2026-03-07', '2026-03-08', 3, 'CASH', 25.0, 5, 1, 0, 50.0, '2026-03-08', 'PAID', 71.25),
    (2, 12, 'Moto-2', 'Moto', 'Gasoline', 'Scooter', 'Honda', 'Scoopy', 'PP-1A 2345', 12.0, 12, 'Olivia Grey', 'ID-10012', '012345689', 12, 'Evelyn Lopez', 5, '2026-03-02', '2026-03-07', '2026-03-07', 2, 'ABA', 12.0, 0, 0, 0, 60.0, '2026-03-07', 'PAID', 60.0),
    (4, 1, 'Car-1', 'Car', 'Gasoline', 'Sedan', 'Toyota', 'Camry', 'PP-2A 1234', 50.0, 14, 'Sophia Violet', 'ID-10014', '012345691', 14, 'Abigail Wilson', 1, '2026-03-10', '2026-03-11', '2026-03-11', 4, 'CARD', 50.0, 0, 0, 20, 100.0, '2026-03-11', 'PAID', 70.0),
    (5, 2, 'Car-2', 'Car', 'Hybrid', 'SUV', 'Lexus', 'RX300', 'PP-2A 2345', 80.0, 15, 'Charles Ash', 'ID-10015', '012345692', 15, 'Matthew Anderson', 4, '2026-03-12', '2026-03-16', '2026-03-16', 5, 'ABA', 80.0, 10, 0, 0, 200.0, '2026-03-16', 'PAID', 288.0),
    (6, 14, 'Moto-4', 'Moto', 'Gasoline', 'Standard', 'Suzuki', 'Viva', 'PP-1A 4567', 10.0, 16, 'Isabella Mint', 'ID-10016', '012345693', 1, 'James Smith', 7, '2026-03-10', '2026-03-17', '2026-03-17', 6, 'WING', 10.0, 0, 0, 0, 70.0, '2026-03-17', 'PAID', 70.0),
    (7, 3, 'Car-3', 'Car', 'Gasoline', 'Truck', 'Ford', 'Ranger', 'PP-2A 3456', 60.0, 17, 'Thomas Teal', 'ID-10017', '012345694', 2, 'Olivia Johnson', 3, '2026-03-15', '2026-03-18', '2026-03-18', 7, 'ABA', 60.0, 0, 0, 0, 150.0, '2026-03-18', 'PAID', 180.0),
    (8, 15, 'Moto-5', 'Moto', 'Electric', 'Scooter', 'VinFast', 'Klara', 'PP-1A 5678', 15.0, 18, 'Mia Coral', 'ID-10018', '012345695', 3, 'William Williams', 2, '2026-03-20', '2026-03-22', '2026-03-22', 8, 'ACLEDA', 15.0, 0, 0, 0, 30.0, '2026-03-22', 'PAID', 30.0),
    (10, 16, 'Moto-6', 'Moto', 'Gasoline', 'Standard', 'Honda', 'Wave', 'PP-1B 1111', 8.0, 20, 'Charlotte Gold', 'ID-10020', '012345697', 5, 'Benjamin Jones', 10, '2026-03-10', '2026-03-20', '2026-03-20', 10, 'CASH', 8.0, 15, 0, 0, 50.0, '2026-03-20', 'PAID', 68.0),
    (9, 4, 'Car-4', 'Car', 'Electric', 'Sedan', 'Tesla', 'Model 3', 'PP-2A 4567', 100.0, 19, 'Daniel Plum', 'ID-10019', '012345696', 4, 'Sophia Brown', 1, '2026-03-22', '2026-03-23', '2026-03-23', 9, 'CASH', 100.0, 0, 0, 0, 100.0, '2026-03-23', 'PAID', 100.0),
    (11, 5, 'Car-5', 'Car', 'Gasoline', 'Van', 'Hyundai', 'H1', 'PP-2A 5678', 75.0, 21, 'Matthew Silver', 'ID-10021', '012345698', 6, 'Isabella Garcia', 2, '2026-03-24', '2026-03-26', '2026-03-26', 11, 'ABA', 75.0, 0, 0, 0, 150.0, '2026-03-26', 'PAID', 150.0),
    (12, 17, 'Moto-7', 'Moto', 'Gasoline', 'Standard', 'Honda', 'PCX', 'PP-1B 2222', 20.0, 22, 'Amelia Bronze', 'ID-10022', '012345699', 7, 'Lucas Miller', 3, '2026-03-25', '2026-03-28', '2026-03-28', 12, 'CASH', 20.0, 0, 0, 0, 60.0, '2026-03-28', 'PAID', 60.0),
    (13, 6, 'Car-6', 'Car', 'Gasoline', 'SUV', 'Toyota', 'Fortuner', 'PP-2B 1111', 70.0, 23, 'Anthony Zinc', 'ID-10023', '012345700', 8, 'Mia Davis', 4, '2026-03-20', '2026-03-24', '2026-03-24', 13, 'ABA', 70.0, 5, 0, 50, 200.0, '2026-03-24', 'PAID', 316.0),
    (14, 18, 'Moto-8', 'Moto', 'Gasoline', 'Sport', 'Kawasaki', 'Ninja 300', 'PP-1B 3333', 35.0, 24, 'Harper Iron', 'ID-10024', '012345701', 9, 'Henry Rodriguez', 1, '2026-03-27', '2026-03-28', '2026-03-28', 14, 'CASH', 35.0, 0, 0, 0, 35.0, '2026-03-28', 'PAID', 35.0),
    (15, 7, 'Car-7', 'Car', 'Gasoline', 'Sedan', 'Honda', 'Civic', 'PP-2B 2222', 45.0, 25, 'Joshua Brass', 'ID-10025', '012345702', 10, 'Amelia Martinez', 5, '2026-03-10', '2026-03-15', '2026-03-15', 15, 'ABA', 45.0, 0, 0, 0, 100.0, '2026-03-15', 'PAID', 225.0),
    (16, 8, 'Car-8', 'Car', 'Hybrid', 'Sedan', 'Toyota', 'Prius', 'PP-2B 3333', 40.0, 1, 'John Doe', 'ID-10001', '012345678', 1, 'James Smith', 2, '2026-03-01', '2026-03-03', '2026-03-03', 16, 'CASH', 40.0, 0, 0, 0, 80.0, '2026-03-03', 'PAID', 80.0),
    (18, 9, 'Car-9', 'Car', 'Diesel', 'Truck', 'Toyota', 'Hilux', 'PP-2B 4444', 65.0, 3, 'Michael Green', 'ID-10003', '012345680', 3, 'William Williams', 4, '2026-03-15', '2026-03-19', '2026-03-19', 18, 'ABA', 65.0, 0, 0, 0, 200.0, '2026-03-19', 'PAID', 260.0),
    (17, 19, 'Moto-9', 'Moto', 'Gasoline', 'Offroad', 'Honda', 'CRF250', 'PP-1B 4444', 30.0, 2, 'Jane Roe', 'ID-10002', '012345679', 2, 'Olivia Johnson', 3, '2026-03-05', '2026-03-08', '2026-03-08', 17, 'ABA', 30.0, 10, 0, 0, 90.0, '2026-03-08', 'PAID', 81.0),
    (19, 20, 'Moto-10', 'Moto', 'Gasoline', 'Scooter', 'Yamaha', 'Fino', 'PP-1B 5555', 10.0, 4, 'Sarah White', 'ID-10004', '012345681', 4, 'Sophia Brown', 1, '2026-03-25', '2026-03-26', '2026-03-26', 19, 'CASH', 10.0, 0, 0, 0, 10.0, '2026-03-26', 'PAID', 10.0),
    (20, 10, 'Car-10', 'Car', 'Gasoline', 'SUV', 'Mazda', 'CX-5', 'PP-2B 5555', 60.0, 5, 'Robert Black', 'ID-10005', '012345682', 5, 'Benjamin Jones', 3, '2026-03-20', '2026-03-23', '2026-03-23', 20, 'ABA', 60.0, 0, 0, 100, 150.0, '2026-03-23', 'PAID', 280.0);
