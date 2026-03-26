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
import model.Vehicle;
import user.ManagerStaff;
import user.RegularStaff;
import user.Staff;

/**
 * Utility class to map MySQL ResultSets into Java Objects (ArrayList / HashSet).
 *
 * NOTE: Since some of your object constructors (like Customer or Vehicle) auto-increment their IDs,
 * restoring objects exactly from the database might require adding setters (e.g. `setCustomerId(int)`) 
 * into your classes, or modifying the constructors to accept the Primary Key ID. 
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
                    rs.getString("customer_name"),
                    rs.getString("customer_id_num"),
                    rs.getString("customer_phone"),
                    rs.getString("id_card_photo"),
                    rs.getString("driver_license_photo")
            );
            
            // If you want to keep the exact ID from the DB, add setCustomerId() to Customer.java
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
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getDouble("salary")
                );
            } else {
                staff = new RegularStaff(
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getDouble("salary"),
                        "General Station"  // Since your DB has no workstation column, pass string
                );
            }
            staff.setStatus(rs.getBoolean("status"));
            staff.setActive(rs.getBoolean("active"));
            
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
                        rs.getString("vehicle_brand"),
                        rs.getString("vehicle_model"),
                        rs.getDouble("rental_rate_per_day"),
                        rs.getString("vehicle_licence"),
                        rs.getString("licence_plate"),
                        rs.getInt("number_of_seats")
                );
            } else {
                // It is a Moto
                vehicle = new Moto(
                        "Moto",
                        rs.getString("power_source"),
                        rs.getString("vehicle_class"),
                        rs.getString("vehicle_brand"),
                        rs.getString("vehicle_model"),
                        rs.getDouble("rental_rate_per_day"),
                        rs.getString("vehicle_licence"),
                        rs.getString("licence_plate"),
                        rs.getBoolean("helmet_included")
                );
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
                    rs.getDouble("deposit")
            );
            
            payment.setPaymentMethod(rs.getString("payment_method"));
            payment.setDiscount(rs.getDouble("discount"));
            payment.setExtraDays(rs.getInt("extra_days"));
            payment.setDamageFee(rs.getDouble("damage_fee"));
            payment.setPayDate(rs.getString("pay_date"));
            payment.setStatus(rs.getString("status"));
            
            payment.setPaymentId(rs.getInt("payment_id"));
            
            payments.add(payment);
        }
        return payments;
    }

    // ==========================================
    // 5. RENTS Result -> ArrayList<Rent>
    // ==========================================
    // Note: Since Rent contains full Vehicle, Customer, and Payment objects (not just IDs),
    // you need to pass the pre-loaded collections to assemble the relationships properly.
    public static ArrayList<Rent> mapToRents(
            ResultSet rs, 
            ArrayList<Vehicle> allVehicles, 
            HashSet<Customer> allCustomers,
            HashSet<Staff> allStaff,
            ArrayList<Payment> allPayments) throws SQLException {
                
        ArrayList<Rent> rents = new ArrayList<>();
        while (rs != null && rs.next()) {
            int vehicleId = rs.getInt("vehicle_id");
            int customerId = rs.getInt("customer_id");
            int paymentId = rs.getInt("payment_id");
            int staffID = rs.getInt("staff_id");

            // Look up existing references
            Vehicle rentVehicle = allVehicles.stream().filter(v -> v.getVehicleId() == vehicleId).findFirst().orElse(null);
            Customer rentCustomer = allCustomers.stream().filter(c -> c.getCustomerId() == customerId).findFirst().orElse(null);
            Staff currentStaff = allStaff.stream().filter(s -> s.getId() == staffID ).findFirst().orElse(null);

            if (rentVehicle != null && rentCustomer != null) {
                Rent rent = new Rent(
                        rentVehicle,
                        rentCustomer,
                        currentStaff,
                        rs.getInt("rent_days"),
                        rs.getString("start_date"),
                        rs.getString("end_date")
                );
                
                rent.setReturnDate(rs.getString("return_date"));
                rent.setStatus(rs.getBoolean("status"));
                
                // Attach the payment if it exists
                if (paymentId > 0 && allPayments != null) {
                    Payment p = allPayments.stream().filter(pay -> pay.getPaymentId() == paymentId).findFirst().orElse(null);
                    rent.setPayment(p);
                }
                
                rent.setRentId(rs.getInt("rent_id"));
                
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
        // Step 1: The Java Object is already created temporarily in memory (e.g. ID = 1)
        
        // Prepare the INSERT query
        String query = String.format(
            "INSERT INTO customers (customer_name, customer_id_num, customer_phone, id_card_photo, driver_license_photo) " +
            "VALUES ('%s', '%s', '%s', '%s', '%s')",
            customer.getCustomerName(),
            customer.getcustomerIdNum(), // Note: Make sure field exactly matches your DB structure
            customer.getCustomerPhone(),
            customer.getIDCardPhoto() == null ? "" : customer.getIDCardPhoto(),
            customer.getDriverLicensePhoto() == null ? "" : customer.getDriverLicensePhoto()
        );

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
        
        // As you suggested: pulling the password directly from the existing toString() method natively!
        String staffStr = staff.toString();
        int passStart = staffStr.indexOf("password='") + 10;
        int passEnd = staffStr.indexOf("'", passStart);
        if (passStart >= 10 && passEnd > passStart) {
            password = staffStr.substring(passStart, passEnd);
        }

        String query = String.format(
            "INSERT INTO staff (name, username, password, status, active, salary, role) " +
            "VALUES ('%s', '%s', '%s', %b, %b, %.2f, '%s')",
            staff.getName(), staff.getUsername(), password,
            staff.getStatus(), staff.isActive(), staff.getSalary(), role
        );

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
                if (end == -1) end = motoStr.length();
                helmet = motoStr.substring(start, end).trim();
            }
        }
        
        String query = String.format(
            "INSERT INTO vehicles (vehicle_type, vehicle_code, power_source, vehicle_class, vehicle_brand, vehicle_model, rental_rate_per_day, vehicle_licence, licence_plate, is_available, number_of_seats, helmet_included) " +
            "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %.2f, '%s', '%s', %b, %s, %s)",
            type, vehicle.getVehicleCode(), vehicle.getPowerSource(), vehicle.getVehicleClass(),
            vehicle.getVehicleBrand(), vehicle.getVehicleModel(), vehicle.getRentalRatePerDay(),
            vehicle.getVehicleLicence(), vehicle.getLicencePlate(), vehicle.isAvailable(),
            seats, helmet
        );

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
            "INSERT INTO payments (payment_method, rent_days, price, discount, extra_days, damage_fee, pay_date, status, deposit) " +
            "VALUES ('%s', %d, %.2f, %.2f, %d, %.2f, '%s', '%s', %.2f)",
            payment.getPaymentMethod(), rentDays, payment.getPrice(), payment.getDiscount(),
            payment.getExtraDays(), payment.getDamageFee(), payment.getPayDate(),
            payment.getStatus(), payment.getDeposit()
        );

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
            "INSERT INTO rents (vehicle_id, customer_id, payment_id, rent_days, start_date, end_date, return_date, status) " +
            "VALUES (%d, %d, %s, %d, '%s', '%s', '%s', %b)",
            rent.getVehicle().getVehicleId(),
            rent.getCustomer().getCustomerId(),
            rent.getPayment() != null ? String.valueOf(rent.getPayment().getPaymentId()) : "NULL",
            rent.getRentDays(),
            rent.getStartDate(),
            rent.getEndDate(),
            rent.getReturnDate(),
            rent.isStatus()
        );

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
        String query = "DELETE FROM staff WHERE staff_id = " + staffId;
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
            "UPDATE vehicles SET power_source='%s', vehicle_class='%s', vehicle_brand='%s', vehicle_model='%s', " +
            "rental_rate_per_day=%.2f, is_available=%b, number_of_seats=%s, helmet_included=%s WHERE vehicle_id=%d",
            v.getPowerSource(), v.getVehicleClass(), v.getVehicleBrand(), v.getVehicleModel(), 
            v.getRentalRatePerDay(), v.isAvailable(), seats, helmet, v.getVehicleId()
        );
        MySQLConnection.executeUpdate(query);
        System.out.println("Updated Vehicle in DB: ID " + v.getVehicleId());
    }

    public static void updateCustomer(Customer c) {
        String query = String.format(
            "UPDATE customers SET customer_name='%s', customer_id_num='%s', customer_phone='%s', " +
            "id_card_photo='%s', driver_license_photo='%s' WHERE customer_id=%d",
            c.getCustomerName(), c.getcustomerIdNum(), c.getCustomerPhone(), 
            c.getIDCardPhoto(), c.getDriverLicensePhoto(), c.getCustomerId()
        );
        MySQLConnection.executeUpdate(query);
        System.out.println("Updated Customer in DB: ID " + c.getCustomerId());
    }

    public static void updateStaff(Staff s) {
        String query = String.format(
            "UPDATE staff SET name='%s', status=%b, active=%b, salary=%.2f WHERE staff_id=%d",
            s.getName(), s.getStatus(), s.isActive(), s.getSalary(), s.getId()
        );
        MySQLConnection.executeUpdate(query);
        System.out.println("Updated Staff in DB: ID " + s.getId());
    }

    public static void updateRent(Rent r) {
        String query = String.format(
            "UPDATE rents SET vehicle_id=%d, customer_id=%d, payment_id=%s, rent_days=%d, start_date='%s', " +
            "end_date='%s', return_date='%s', status=%b WHERE rent_id=%d",
            r.getVehicle().getVehicleId(), r.getCustomer().getCustomerId(), 
            (r.getPayment() != null ? String.valueOf(r.getPayment().getPaymentId()) : "NULL"),
            r.getRentDays(), r.getStartDate(), r.getEndDate(), r.getReturnDate(), r.isStatus(), r.getRentId()
        );
        MySQLConnection.executeUpdate(query);
        System.out.println("Updated Rent in DB: ID " + r.getRentId());
    }

    public static void updatePayment(Payment p) {
        String query = String.format(
            "UPDATE payments SET payment_method='%s', rent_days=%d, price=%.2f, discount=%.2f, extra_days=%d, " +
            "damage_fee=%.2f, pay_date='%s', status='%s', deposit=%.2f WHERE payment_id=%d",
            p.getPaymentMethod(), p.getRentDays(), p.getPrice(), p.getDiscount(), p.getExtraDays(),
            p.getDamageFee(), p.getPayDate(), p.getStatus(), p.getDeposit(), p.getPaymentId()
        );
        MySQLConnection.executeUpdate(query);
        System.out.println("Updated Payment in DB: ID " + p.getPaymentId());
    }
}
