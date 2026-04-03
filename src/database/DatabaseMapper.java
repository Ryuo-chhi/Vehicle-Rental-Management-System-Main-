package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import model.Car;
import model.Customer;
import model.Moto;
import model.Payment;
import model.Rent;
import model.RentRecord;
import model.Vehicle;
import user.ManagerStaff;
import user.RegularStaff;
import user.Staff;

/**
 * Utility class to map MySQL ResultSets into Java Objects (ArrayList /
 * HashSet).
 *
 * NOTE: Since some of your object constructors (like Customer or Vehicle)
 * auto-increment their IDs,
 * restoring objects exactly from the database might require adding setters
 * (e.g. `setCustomerId(int)`)
 * into your classes, or modifying the constructors to accept the Primary Key
 * ID.
 * I have left comments where you should inject the DB IDs.
 */
public class DatabaseMapper {

    // ==========================================
    // 1. CUSTOMERS Result -> HashSet<Customer>
    // ==========================================
    public static HashSet<Customer> mapToCustomers(ResultSet rs) throws SQLException {
        HashSet<Customer> customers = new HashSet<>();
        while (rs != null && rs.next()) {
            Customer customer = new Customer(
                    rs.getString("full_name"),
                    rs.getString("id_num"),
                    rs.getString("phone"),
                    rs.getString("id_card_photo"),
                    rs.getString("dl_photo"));

            // If you want to keep the exact ID from the DB, add setCustomerId() to
            // Customer.java
            customer.setCustomerId(rs.getInt("customer_id"));

            customers.add(customer);
        }
        return customers;
    }

    // ==========================================
    // 2. STAFF Result -> HashSet<Staff>
    // ==========================================
    public static HashSet<Staff> mapToStaff(ResultSet rs) throws SQLException {
        HashSet<Staff> staffList = new HashSet<>();
        while (rs != null && rs.next()) {
            String role = rs.getString("role");
            Staff staff;

            if ("Manager".equalsIgnoreCase(role)) {
                staff = new ManagerStaff(
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getDouble("salary"));
            } else {
                staff = new RegularStaff(
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getDouble("salary"),
                        rs.getString("work_station"));
            }
            staff.setStatus(rs.getBoolean("status"));
            staff.setActive(rs.getBoolean("is_active"));

            staff.setStaffId(rs.getInt("staff_id"));

            staffList.add(staff);
        }
        return staffList;
    }

    // ==========================================
    // 3. VEHICLES Result -> ArrayList<Vehicle>
    // ==========================================
    public static ArrayList<Vehicle> mapToVehicles(ResultSet rs) throws SQLException {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        while (rs != null && rs.next()) {
            String type = rs.getString("vehicle_type");
            Vehicle vehicle;

            if ("Car".equalsIgnoreCase(type)) {
                vehicle = new Car(
                        "Car",
                        rs.getString("power_source"),
                        rs.getString("vehicle_class"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getDouble("rate_per_day"),
                        rs.getString("licence"),
                        rs.getString("licence_plate"),
                        rs.getInt("number_of_seats"));
            } else {
                // It is a Moto
                vehicle = new Moto(
                        "Moto",
                        rs.getString("power_source"),
                        rs.getString("vehicle_class"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getDouble("rate_per_day"),
                        rs.getString("licence"),
                        rs.getString("licence_plate"),
                        rs.getBoolean("helmet_included"));
            }

            vehicle.setAvailable(rs.getBoolean("is_available"));

            vehicle.setVehicleId(rs.getInt("vehicle_id"));
            vehicle.setVehicleCode(rs.getString("vehicle_code")); // Overwrite generated code

            vehicles.add(vehicle);
        }
        return vehicles;
    }

    // ==========================================
    // 4. PAYMENTS Result -> ArrayList<Payment>
    // ==========================================
    public static ArrayList<Payment> mapToPayments(ResultSet rs) throws SQLException {
        ArrayList<Payment> payments = new ArrayList<>();
        while (rs != null && rs.next()) {
            Payment payment = new Payment(
                    rs.getInt("rent_days"),
                    rs.getDouble("price"),
                    rs.getDouble("deposit"));

            payment.setPaymentMethod(rs.getString("method"));
            payment.setDiscount(rs.getDouble("discount"));
            payment.setExtraDays(rs.getInt("extra_days"));
            payment.setDamageFee(rs.getDouble("damage_fee"));
            payment.setPayDate(rs.getString("pay_date"));
            payment.setStatus(rs.getString("pay_status"));

            payment.setPaymentId(rs.getInt("payment_id"));

            payments.add(payment);
        }
        return payments;
    }

    // ==========================================
    // 5. RENTS Result -> ArrayList<Rent>
    // ==========================================
    // Note: Since Rent contains full Vehicle, Customer, and Payment objects (not
    // just IDs),
    // you need to pass the pre-loaded collections to assemble the relationships
    // properly.
    public static ArrayList<Rent> mapToRents(
            ResultSet rs,
            ArrayList<Vehicle> allVehicles,
            HashSet<Customer> allCustomers,
            HashSet<Staff> allStaff,
            ArrayList<Payment> allPayments) throws SQLException {

        ArrayList<Rent> rents = new ArrayList<>();
        while (rs != null && rs.next()) {
            int rentID = rs.getInt("rent_id");
            int vehicleID = rs.getInt("vehicle_id");
            int customerID = rs.getInt("customer_id");
            int paymentID = rs.getInt("payment_id");
            int staffID = rs.getInt("staff_id");

            Vehicle rentVehicle = null;
            for (Vehicle v : allVehicles) {
                if (v.getVehicleId() == vehicleID) {
                    rentVehicle = v;
                    break;
                }
            }

            Customer rentCustomer = null;
            for (Customer c : allCustomers) {
                if (c.getCustomerId() == customerID) {
                    rentCustomer = c;
                    break;
                }
            }

            Staff currentStaff = null;
            for (Staff s : allStaff) {
                if (s.getId() == staffID) {
                    currentStaff = s;
                    break;
                }
            }

            if (rentVehicle != null && rentCustomer != null) {
                Rent rent = new Rent(
                        rentVehicle,
                        rentCustomer,
                        currentStaff,
                        rs.getInt("rent_days"),
                        rs.getString("start_date"),
                        rs.getString("end_date"));

                rent.setReturnDate(rs.getString("return_date"));
                rent.setStatus(rs.getBoolean("status"));

                if (paymentID > 0 && allPayments != null) {
                    Payment p = allPayments.stream()
                            .filter(pay -> pay.getPaymentId() == paymentID)
                            .findFirst().orElse(null);
                    rent.setPayment(p);
                }

                rent.setRentId(rentID);
                rentVehicle.setAvailable(false); // list in rents means vehicle is false
                rents.add(rent);
            }
        }
        return rents;
    }

    // =======================================================================
    // HOW TO SOLVE STEPS 1 to 4: Insert generated object and sync memory ID
    // =======================================================================
    /**
     * Call this right after creating a `new Customer(...)` in your application.
     * It handles the INSERT and syncs the DB ID back to the Java object.
     */
    public static Customer saveNewCustomer(Customer customer) {
        // Step 1: The Java Object is already created temporarily in memory (e.g. ID =
        // 1)

        // Prepare the INSERT query
        String query = String.format(
                "INSERT INTO customers (full_name, id_num, phone, id_card_photo, dl_photo) " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s')",
                customer.getCustomerName(),
                customer.getcustomerIdNum(), // Note: Make sure field exactly matches your DB structure
                customer.getCustomerPhone(),
                customer.getIDCardPhoto() == null ? "" : customer.getIDCardPhoto(),
                customer.getDriverLicensePhoto() == null ? "" : customer.getDriverLicensePhoto());

        // Step 2 & 3: Execute Insert and capture the Generated Key from MySQL
        int generatedId = MySQLConnection.executeInsertAndGetId(query);

        if (generatedId != -1) {
            // Step 4: Pass that Database key straight back into your Java object!
            customer.setCustomerId(generatedId);
            System.out.println("Successfully saved to DB! Java ID overridden to match DB ID: " + generatedId);
        } else {
            System.out.println("Failed to insert the Customer into the database.");
        }

        return customer; // The perfectly synced Java Object
    }

    // ==========================================
    // saveNewStaff
    // ==========================================
    public static Staff saveNewStaff(Staff staff) {
        String role = (staff instanceof ManagerStaff) ? "Manager" : "Regular";
        String password = "";

        // As you suggested: pulling the password directly from the existing toString()
        // method natively!
        String staffStr = staff.toString();
        int passStart = staffStr.indexOf("password='") + 10;
        int passEnd = staffStr.indexOf("'", passStart);
        if (passStart >= 10 && passEnd > passStart) {
            password = staffStr.substring(passStart, passEnd);
        }

        String workStation = (staff instanceof RegularStaff) ? ((RegularStaff) staff).getWorkStation() : null;

        String query = String.format(
                "INSERT INTO staffs (full_name, username, password_hash, status, is_active, salary, role, work_station) "
                        +
                        "VALUES ('%s', '%s', '%s', %b, %b, %.2f, '%s', %s)",
                staff.getName(), staff.getUsername(), password,
                staff.getStatus(), staff.isActive(), staff.getSalary(), role,
                workStation == null ? "NULL" : "'" + workStation + "'");

        int generatedId = MySQLConnection.executeInsertAndGetId(query);
        if (generatedId != -1) {
            staff.setStaffId(generatedId);
            System.out.println("Successfully saved Staff to DB! ID: " + generatedId);
        } else {
            System.out.println("Failed to insert Staff into DB.");
        }
        return staff;
    }

    // ==========================================
    // saveNewVehicle
    // ==========================================
    public static Vehicle saveNewVehicle(Vehicle vehicle) {
        String type = (vehicle instanceof Car) ? "Car" : "Moto";
        String seats = "NULL";
        String helmet = "NULL";

        if (vehicle instanceof Car) {
            seats = String.valueOf(((Car) vehicle).getNumberOfSeats());
        } else if (vehicle instanceof Moto) {
            // As you suggested: parsing helmetIncluded directly from toString() !
            String motoStr = vehicle.toString();
            int helmetIdx = motoStr.indexOf("helmetIncluded=");
            if (helmetIdx != -1) {
                int start = helmetIdx + 15;
                int end = motoStr.indexOf('}', start);
                if (end == -1)
                    end = motoStr.length();
                helmet = motoStr.substring(start, end).trim();
            }
        }

        String query = String.format(
                "INSERT INTO vehicles (vehicle_type, vehicle_code, power_source, vehicle_class, brand, model, rate_per_day, licence, licence_plate, is_available, number_of_seats, helmet_included) "
                        +
                        "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %.2f, '%s', '%s', %b, %s, %s)",
                type, vehicle.getVehicleCode(), vehicle.getPowerSource(), vehicle.getVehicleClass(),
                vehicle.getVehicleBrand(), vehicle.getVehicleModel(), vehicle.getRentalRatePerDay(),
                vehicle.getVehicleLicence(), vehicle.getLicencePlate(), vehicle.isAvailable(),
                seats, helmet);

        int generatedId = MySQLConnection.executeInsertAndGetId(query);
        if (generatedId != -1) {
            vehicle.setVehicleId(generatedId);
            System.out.println("Successfully saved Vehicle to DB! ID: " + generatedId);
        } else {
            System.out.println("Failed to insert Vehicle into DB.");
        }
        return vehicle;
    }

    // ==========================================
    // saveNewPayment
    // ==========================================
    public static Payment saveNewPayment(Payment payment) {
        // Derive rentDays safely if needed
        int rentDays = payment.getPrice() > 0
                ? (int) Math.round((payment.expectedTotal() + payment.getDeposit()) / payment.getPrice())
                : 1;

        String query = String.format(
                "INSERT INTO payments (method, rent_days, price, discount, extra_days, damage_fee, pay_date, pay_status, deposit, total_paid) "
                        +
                        "VALUES ('%s', %d, %.2f, %.2f, %d, %.2f, '%s', '%s', %.2f, %.2f)",
                payment.getPaymentMethod(), rentDays, payment.getPrice(), payment.getDiscount(),
                payment.getExtraDays(), payment.getDamageFee(), payment.getPayDate(),
                payment.getStatus(), payment.getDeposit(), payment.calculateTotal());

        int generatedId = MySQLConnection.executeInsertAndGetId(query);
        if (generatedId != -1) {
            payment.setPaymentId(generatedId);
            System.out.println("Successfully saved Payment to DB! ID: " + generatedId);
        } else {
            System.out.println("Failed to insert Payment into DB.");
        }
        return payment;
    }

    // ==========================================
    // saveNewRent
    // ==========================================
    public static Rent saveNewRent(Rent rent) {
        String query = String.format(
                "INSERT INTO rents (vehicle_id, customer_id, staff_id, payment_id, rent_days, start_date, end_date, return_date, status) "
                        +
                        "VALUES (%d, %d, %d, %s, %d, '%s', '%s', '%s', %b)",
                rent.getVehicle().getVehicleId(),
                rent.getCustomer().getCustomerId(),
                rent.getStaff() != null ? rent.getStaff().getId() : 0,
                rent.getPayment() != null ? String.valueOf(rent.getPayment().getPaymentId()) : "NULL",
                rent.getRentDays(),
                rent.getStartDate(),
                rent.getEndDate(),
                rent.getReturnDate(),
                rent.isStatus());

        int generatedId = MySQLConnection.executeInsertAndGetId(query);
        if (generatedId != -1) {
            rent.setRentId(generatedId);
            System.out.println("Successfully saved Rent to DB! ID: " + generatedId);
        } else {
            System.out.println("Failed to insert Rent into DB.");
        }
        return rent;
    }

    // ==========================================
    // saveNewRentRecord
    // ==========================================
    public static void saveNewRentRecord(RentRecord r) {
        String query = String.format(
                "INSERT INTO rent_records (rent_id, vehicle_id, vehicle_type, vehicle_code, power_source, " +
                        "vehicle_class, brand, model, licence_plate, rate_per_day, " +
                        "customer_id, customer_name, id_num, phone, " +
                        "staff_id, staff_name, " +
                        "rent_days, start_date, end_date, return_date, " +
                        "payment_id, method, price, discount, extra_days, damage_fee, deposit, pay_date, pay_status, total_paid) "
                        +
                        "VALUES (%d, %d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', %.2f, " +
                        "%d, '%s', '%s', '%s', " +
                        "%d, '%s', " +
                        "%d, '%s', '%s', '%s', " +
                        "%d, '%s', %.2f, %.2f, %d, %.2f, %.2f, '%s', '%s', %.2f)",
                r.getRentId(), r.getVehicleId(), r.getVehicleType(), r.getVehicleCode(), r.getVehiclePowerSource(),
                r.getVehicleClass(), r.getVehicleBrand(), r.getVehicleModel(), r.getLicencePlate(),
                r.getRentalRatePerDay(),
                r.getCustomerId(), r.getCustomerName(), r.getCustomerIdNum(), r.getCustomerPhone(),
                r.getStaffID(), r.getStaffName(),
                r.getRentDays(), r.getStartDate(), r.getEndDate(), r.getReturnDate(),
                r.getPaymentId(), r.getPaymentMethod(), r.getPrice(), r.getDiscount(),
                r.getExtraDays(), r.getDamageFee(), r.getDeposit(), r.getPayDate(),
                r.getPaymentStatus(), r.getTotalPaid());
        MySQLConnection.executeUpdate(query);
        System.out.println("Saved RentRecord to DB: rent_id=" + r.getRentId());
    }

    // ==========================================
    // mapToRentRecords
    // ==========================================
    public static ArrayList<RentRecord> mapToRentRecords(ResultSet rs) throws SQLException {
        // RentRecord is immutable — we build a lightweight holder here.
        // Since RentRecord's only public constructor takes a Rent object,
        // we reconstruct via a helper that reads every column directly.
        ArrayList<RentRecord> records = new ArrayList<>();
        while (rs != null && rs.next()) {
            records.add(RentRecord.fromResultSet(rs));
        }
        return records;
    }

    // ==========================================
    // DELETE OPERATIONS
    // ==========================================
    public static void deleteVehicle(int vehicleId) {
        String query = "DELETE FROM vehicles WHERE vehicle_id = " + vehicleId;
        MySQLConnection.executeUpdate(query);
        System.out.println("Deleted Vehicle from DB: ID " + vehicleId);
    }

    public static void deleteCustomer(int customerId) {
        String query = "DELETE FROM customers WHERE customer_id = " + customerId;
        MySQLConnection.executeUpdate(query);
        System.out.println("Deleted Customer from DB: ID " + customerId);
    }

    public static void deleteStaff(int staffId) {
        String query = "DELETE FROM staffs WHERE staff_id = " + staffId;
        MySQLConnection.executeUpdate(query);
        System.out.println("Deleted Staff from DB: ID " + staffId);
    }

    public static void deleteRent(int rentId) {
        String query = "DELETE FROM rents WHERE rent_id = " + rentId;
        MySQLConnection.executeUpdate(query);
        System.out.println("Deleted Rent from DB: ID " + rentId);
    }

    public static void deletePayment(int paymentId) {
        String query = "DELETE FROM payments WHERE payment_id = " + paymentId;
        MySQLConnection.executeUpdate(query);
        System.out.println("Deleted Payment from DB: ID " + paymentId);
    }

    // ==========================================
    // UPDATE OPERATIONS
    // ==========================================
    public static void updateVehicle(Vehicle v) {
        String seats = (v instanceof Car) ? String.valueOf(((Car) v).getNumberOfSeats()) : "NULL";
        String helmet = (v instanceof Moto) ? String.valueOf(v.toString().contains("helmetIncluded=true")) : "NULL";

        String query = String.format(
                "UPDATE vehicles SET power_source='%s', vehicle_class='%s', brand='%s', model='%s', " +
                        "rate_per_day=%.2f, is_available=%b, number_of_seats=%s, helmet_included=%s WHERE vehicle_id=%d",
                v.getPowerSource(), v.getVehicleClass(), v.getVehicleBrand(), v.getVehicleModel(),
                v.getRentalRatePerDay(), v.isAvailable(), seats, helmet, v.getVehicleId());
        MySQLConnection.executeUpdate(query);
        System.out.println("Updated Vehicle in DB: ID " + v.getVehicleId());
    }

    public static void updateCustomer(Customer c) {
        String query = String.format(
                "UPDATE customers SET full_name='%s', id_num='%s', phone='%s', " +
                        "id_card_photo='%s', dl_photo='%s' WHERE customer_id=%d",
                c.getCustomerName(), c.getcustomerIdNum(), c.getCustomerPhone(),
                c.getIDCardPhoto(), c.getDriverLicensePhoto(), c.getCustomerId());
        MySQLConnection.executeUpdate(query);
        System.out.println("Updated Customer in DB: ID " + c.getCustomerId());
    }

    public static void updateStaff(Staff s) {
        String workStation = (s instanceof RegularStaff) ? ((RegularStaff) s).getWorkStation() : null;
        String query = String.format(
                "UPDATE staffs SET full_name='%s', status=%b, is_active=%b, salary=%.2f, work_station=%s WHERE staff_id=%d",
                s.getName(), s.getStatus(), s.isActive(), s.getSalary(),
                workStation == null ? "NULL" : "'" + workStation + "'",
                s.getId());
        MySQLConnection.executeUpdate(query);
        System.out.println("Updated Staff in DB: ID " + s.getId());
    }

    public static void updateRent(Rent r) {
        String query = String.format(
                "UPDATE rents SET vehicle_id=%d, customer_id=%d, staff_id=%d, payment_id=%s, rent_days=%d, start_date='%s', "
                        +
                        "end_date='%s', return_date='%s', status=%b WHERE rent_id=%d",
                r.getVehicle().getVehicleId(), r.getCustomer().getCustomerId(),
                r.getStaff() != null ? r.getStaff().getId() : 0,
                (r.getPayment() != null ? String.valueOf(r.getPayment().getPaymentId()) : "NULL"),
                r.getRentDays(), r.getStartDate(), r.getEndDate(), r.getReturnDate(), r.isStatus(), r.getRentId());
        MySQLConnection.executeUpdate(query);
        System.out.println("Updated Rent in DB: ID " + r.getRentId());
    }

    public static void updatePayment(Payment p) {
        String query = String.format(
                "UPDATE payments SET method='%s', rent_days=%d, price=%.2f, discount=%.2f, extra_days=%d, " +
                        "damage_fee=%.2f, pay_date='%s', pay_status='%s', deposit=%.2f, total_paid=%.2f WHERE payment_id=%d",
                p.getPaymentMethod(), p.getRentDays(), p.getPrice(), p.getDiscount(), p.getExtraDays(),
                p.getDamageFee(), p.getPayDate(), p.getStatus(), p.getDeposit(), p.calculateTotal(), p.getPaymentId());
        MySQLConnection.executeUpdate(query);
        System.out.println("Updated Payment in DB: ID " + p.getPaymentId());
    }
}
