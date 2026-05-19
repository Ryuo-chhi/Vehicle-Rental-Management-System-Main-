package com.rental.system.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import com.rental.system.model.*;
import com.rental.system.service.*;
import com.rental.system.user.*;

@FunctionalInterface
interface VehicleFilter {
    boolean search(Vehicle v);
}

@FunctionalInterface
interface StaffFilter {
    boolean test(Staff staff);
}

public class Garage {

    /* ====== Action Constants ====== */
    public static final String VIEW_VEHICLE = "VIEW_VEHICLE";
    public static final String MANAGE_VEHICLE = "MANAGE_VEHICLE";
    public static final String VIEW_CUSTOMER = "VIEW_CUSTOMER";
    public static final String MANAGE_CUSTOMER = "MANAGE_CUSTOMER";
    public static final String VIEW_RENT = "VIEW_RENT";
    public static final String ADD_RENT = "ADD_RENT";
    public static final String RETURN_VEHICLE = "RETURN_VEHICLE";
    public static final String SHOW_PAYMENT = "SHOW_PAYMENT";
    public static final String MANAGE_STAFF = "MANAGE_STAFF";
    public static final String VIEW_REPORTS = "VIEW_REPORTS";
    public static final String SET_MANAGER_SALARY = "SET_MANAGER_SALARY";
    private ArrayList<String> carClasses = new ArrayList<>() {
        {
            add("SUV");
            add("Sedan");
            add("Van");
            add("Coupe");
            add("Truck");
        }
    };

    private ArrayList<String> powerSources = new ArrayList<>() {
        {
            add("gasoline");
            add("diesel");
            add("electric");
            add("hybrid");
        }
    };

    // Brand
    private ArrayList<String> carBrands = new ArrayList<>() {
        {
            add("Ford");
            add("Tesla");
            add("Toyota");
            add("Honda");
            add("BMW");
        }
    };

    private ArrayList<String> motoClasses = new ArrayList<>() {
        {
            add("Sport");
            add("Cruiser");
            add("Touring");
        }
    };

    // Moto brand
    private ArrayList<String> motoBrands = new ArrayList<>() {
        {
            add("Honda");
            add("Yamaha");
            add("Suzuki");
            add("Kawasaki");
            add("Ducati");
        }
    };

    private com.rental.system.service.VehicleService vehicleService;
    private com.rental.system.service.CustomerService customerService;
    private com.rental.system.service.StaffService staffService;
    private com.rental.system.service.RentalService rentalService;

    // FEEDBACK MESSAGE
    private String lastMessage;

    public Garage(int maxSize) {
        // Initialize services
        this.vehicleService = new com.rental.system.service.VehicleService();
        this.customerService = new com.rental.system.service.CustomerService();
        this.staffService = new com.rental.system.service.StaffService();
        this.rentalService = new com.rental.system.service.RentalService();

        loadVehiclesFromDatabase();
        loadCustomersFromDatabase();
        loadStaffsFromDatabase();

        // Initialize rent list
        loadRentsFromDatabase();
        loadRentsHistoryFromDatabase();

        lastMessage = "Controller.Garage created successfully!";
    }

    private void loadVehiclesFromDatabase() {
        try {
            java.sql.ResultSet rs = com.rental.system.database.MySQLConnection.executeQuery("SELECT * FROM vehicles");
            ArrayList<Vehicle> loaded = com.rental.system.database.DatabaseMapper.mapToVehicles(rs);
            if (loaded != null && !loaded.isEmpty()) {
                this.vehicleService.setVehicles(loaded);
                return;
            }
        } catch (Exception e) {
        }
        this.vehicleService.generateDefaultVehicles();
    }

    private void loadCustomersFromDatabase() {
        try {
            java.sql.ResultSet rs = com.rental.system.database.MySQLConnection.executeQuery("SELECT * FROM customers");
            HashSet<Customer> loaded = com.rental.system.database.DatabaseMapper.mapToCustomers(rs);
            if (loaded != null && !loaded.isEmpty()) {
                customerService.setCustomers(loaded);
                return;
            }
        } catch (Exception e) {
        }
        customerService.generateDefaultCustomers();
    }

    private void loadRentsFromDatabase() {
        try {
            java.sql.ResultSet paymentsRs = com.rental.system.database.MySQLConnection.executeQuery("SELECT * FROM payments");
            java.util.ArrayList<com.rental.system.model.Payment> allPayments = com.rental.system.database.DatabaseMapper.mapToPayments(paymentsRs);

            java.sql.ResultSet rs = com.rental.system.database.MySQLConnection.executeQuery("SELECT * FROM rents");
            java.util.ArrayList<Rent> loaded = com.rental.system.database.DatabaseMapper.mapToRents(rs, this.vehicleService.getAllVehicles(), this.customerService.getAllCustomers(), this.staffService.getAllStaff(), allPayments);
            if (loaded != null && !loaded.isEmpty()) {
                this.rentalService.setRents(loaded);
                return;
            }
        } catch (Exception e) {
        }
    }

    private void loadRentsHistoryFromDatabase() {
        try {
            java.sql.ResultSet rs = com.rental.system.database.MySQLConnection.executeQuery("SELECT * FROM rent_records");
            java.util.ArrayList<RentRecord> loaded = com.rental.system.database.DatabaseMapper.mapToRentRecords(rs);
            if (loaded != null && !loaded.isEmpty()) {
                this.rentalService.setRentalHistory(loaded);
                return;
            }
        } catch (Exception e) {
        }
    }

    private void loadStaffsFromDatabase() {
        try {
            java.sql.ResultSet rs = com.rental.system.database.MySQLConnection.executeQuery("SELECT * FROM staffs");
            HashSet<Staff> loaded = com.rental.system.database.DatabaseMapper.mapToStaff(rs);
            if (loaded != null && !loaded.isEmpty()) {
                this.staffService.setStaffs(loaded);
                return;
            }
        } catch (Exception e) {
        }
        this.staffService.generateDefaultStaff();
    }

    // GETTERS
    public String getLastMessage() {
        return lastMessage;
    }

    public boolean isStaffLoggedIn() {
        return staffService.isStaffLoggedIn();
    }

    private Staff getLoggedInStaff() {
        return staffService.getLoggedInStaff();
    }

    public int getVehicleCount() {
        return vehicleService.getVehicleCount();
    }

    public int getVehicleID() {
        return com.rental.system.service.VehicleService.getNextVehicleID();
    }

    // SETTERS
    private void setLastMessage(String msg) {
        lastMessage = msg;
    }

    // Staff Management

    public void staffManagement(Scanner scanner) {
        // Check if staff is logged in and has permission
        if (!requireStaffLogin()) {
            System.out.println(getLastMessage());
            return;
        }
        if (!staffService.getLoggedInStaff().can(MANAGE_STAFF)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }

        printSectionHeader("Staff Management");
        boolean quit = false;
        int choice;
        do {
            System.out.println("""
                    0. Back to Main Menu
                    1. Add Staff
                    2. Show Staffs
                    3. Update Staff
                    4. Remove Staff""");

            choice = getRequiredIntInput(scanner, "choice");

            switch (choice) {
                case 0 -> quit = true;
                case 1 -> addStaff(scanner);
                case 2 -> showStaffs(scanner);
                case 3 -> updateStaff(scanner);
                case 4 -> removeStaff(scanner);
                default -> System.out.println("Invalid choice!");
            }
            System.out.println();

        } while (!quit);
    }

    // =========================
    // LOGIN CHECK (dependency)
    // =========================
    private boolean requireStaffLogin() {
        if (staffService.getLoggedInStaff() == null) {
            setLastMessage("Action denied: staff must login first.");
            return false;
        }

        if (!staffService.getLoggedInStaff().isActive()) {
            staffService.logout();
            setLastMessage("Action denied: staff is inactive (auto logout).");
            return false;
        }

        return true;
    }

    // =========================
    // STAFF LOGIN / LOGOUT
    // =========================

    public void staffLogin(String username, String password) {
        String result = staffService.login(username, password);
        setLastMessage(result);
        if (result.contains("success")) {
            System.out.println(result);
            showDashboard();
        }
    }

    public void staffLogout() {
        staffService.logout();
        setLastMessage("Logged out successfully.");
    }

    public void addStaff(Scanner scanner) {
        boolean quit = false;
        int choice;
        do {
            System.out.print(
                    "\n" +
                            "0. Quit\n" +
                            "1. Manager\n" +
                            "2. Regular staff\n");
            choice = getRequiredIntInput(scanner, "position");
            switch (choice) {
                case 0, 1, 2 -> quit = true;
                default -> System.out.println("Invalid choice!");

            }
        } while (!quit);
        if (choice == 0) {
            return;
        }

        String name = getRequiredInput(scanner, "staff name");

        double salary = getRequiredDoubleInput(scanner, "staff salary");
        if (salary <= 0) {
            System.out.println("Salary must be greater than 0.");
            return;
        }

        String username = getRequiredInput(scanner, "staff username");

        // Check username uniqueness
        if (staffService.findByIdAndUsername(-1, username) != null) {
            System.out.println("Username already exists. Please choose another.");
            return;
        }

        String password = getRequiredInput(scanner, "staff password");
        if (password == null || password.trim().length() < 4) {
            System.out.println("Password must be at least 4 characters.");
            return;
        }
        String workStation = getRequiredInput(scanner, "staff work station(Moto or Car)");

        Staff newStaff = null;
        switch (choice) {
            case 1 -> newStaff = new ManagerStaff(name, username, password, salary);
            case 2 -> newStaff = new RegularStaff(name, username, password, salary, workStation);
        }
        if (newStaff != null) {
            staffService.registerNewStaff(newStaff);
        }
    }

    public void showStaffs(Scanner scanner) {
        if (staffService.getStaffCount() == 0) {
            System.out.println("No staffs!");
            return;
        }
        boolean quit = false;
        int choice;
        do {
            System.out.print(
                    "How would you like to show the staff?\n" +
                            "0. Back to Staff Management\n" +
                            "1. All Staffs\n" +
                            "2. Regular Staffs\n" +
                            "3. Manager Staffs\n");
            choice = getRequiredIntInput(scanner, "option");
            switch (choice) {
                case 0 -> quit = true;
                case 1 -> {
                    StaffFilter allStaff = staff -> true;
                    showFilteredStaffs(allStaff);
                }
                case 2 -> {
                    StaffFilter regularOnly = staff -> staff instanceof RegularStaff;
                    showFilteredStaffs(regularOnly);
                }
                case 3 -> {
                    StaffFilter managerOnly = staff -> staff instanceof ManagerStaff;
                    showFilteredStaffs(managerOnly);
                }
                default -> System.out.println("Invalid choice!");
            }
        } while (!quit);
    }

    private void showFilteredStaffs(StaffFilter filter) {
        boolean found = false;

        for (Staff staff : staffService.getAllStaff()) {
            if (filter.test(staff)) {
                System.out.println(staff);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching staff found.");
        }
    }

    public void updateStaff(Scanner scanner) {
        if (!staffService.getLoggedInStaff().can(MANAGE_STAFF)) {
            System.out.println("Manager section Only!");
            return;
        }
        Staff targetStaff = findStaff(scanner);
        if (targetStaff != null) {
            boolean quit = false;
            int choice;
            do {
                System.out.println("""
                        Update staff:
                        0. Back to Staff Management
                        1. Name
                        2. Salary
                        3. Enable/Disable Status""");

                choice = getRequiredIntInput(scanner, "choice");

                switch (choice) {
                    case 0 -> quit = true;
                    case 1 -> {
                        String newName = getRequiredInput(scanner, "new Name").trim();
                        if (newName.isEmpty()) {
                            System.out.println("Name cannot be empty. No change made.");
                        } else {
                            targetStaff.setName(newName);
                            if (!targetStaff.can("SUPER_ADMIN_CHECK")) {
                                staffService.updateStaffInDB(targetStaff);
                            }
                            System.out.println("Staff name updated to " + newName + ".");
                        }
                    }
                    case 2 -> {
                        if (!staffService.getLoggedInStaff().can(SET_MANAGER_SALARY)) {
                            System.out.println("Only admin can set salary to manager!");
                            return;
                        }
                        double newSalary = getRequiredDoubleInput(scanner, "new Salary");
                        if (newSalary > 0) {
                            if (setSalaryStaff(targetStaff, newSalary)) {
                                if (!targetStaff.can("SUPER_ADMIN_CHECK")) {
                                    staffService.updateStaffInDB(targetStaff);
                                }
                                System.out.println("Staff salary updated to " + newSalary + ".");
                            } else {
                                System.out.println("Cannot change salary of regular staff!");
                                return;
                            }
                        } else {
                            System.out.println("Salary must be greater than 0. No change made.");
                        }
                    }
                    case 3 -> {
                        if (targetStaff instanceof ManagerStaff && !staffService.getLoggedInStaff().can(SET_MANAGER_SALARY)) {
                            System.out.println("You cannot Enable/Disable Manager Status!");
                            return;
                        }
                        String action = targetStaff.getStatus() ? "disable (resign)" : "enable (employ)";
                        System.out.print("Are you sure do you want to " + action + " this staff? ");
                        String confirm = getRequiredInput(scanner, "confirm(yes/no)").trim();
                        if (confirm.equalsIgnoreCase("yes")) {
                            targetStaff.setStatus(!targetStaff.getStatus());
                            if (!targetStaff.can("SUPER_ADMIN_CHECK")) {
                                staffService.updateStaffInDB(targetStaff);
                            }
                            System.out.println(
                                    "Staff status flipped. Now: " + (targetStaff.getStatus() ? "Employed" : "Resigned"));
                        } else {
                            System.out.println("Operation cancelled.");
                        }
                    }
                    default -> System.out.println("Invalid choice!");
                }
                System.out.println();

            } while (!quit);
            return;
        }
        System.out.println("Staff not found!");
    }

    public void removeStaff(Scanner scanner) {
        Staff staffToRemove = findStaff(scanner);
        if (staffToRemove != null) {
            System.out.print("Are you sure do you want to remove this staff? ");
            String confirm = getRequiredInput(scanner, "confirm(yes/no)").trim();
            if (confirm.equalsIgnoreCase("yes")) {
                if (!staffToRemove.can("SUPER_ADMIN_CHECK")) {
                    staffService.removeStaff(staffToRemove);
                    System.out.println("Staff with ID " + staffToRemove.getId() + " removed successfully.");
                } else {
                    System.out.println("Cannot remove Super Admin!");
                }
            } else {
                System.out.println("Operation cancelled.");
            }
        } else {
            System.out.println("Staff not found.");
        }
    }

    public void vehicleManagement(Scanner scanner) {
        // Check if staff is logged in and has permission
        if (!requireStaffLogin()) {
            System.out.println(getLastMessage());
            return;
        }
        if (!getLoggedInStaff().can(VIEW_VEHICLE)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }

        printSectionHeader("Vehicle Management");
        boolean quit = false;
        int choice;
        do {
            System.out.println("""
                    0. Back to Main Menu
                    1. Add Vehicle
                    2. Show Vehicles
                    3. Update Vehicle
                    4. Remove Vehicle
                    5. Search Vehicles""");

            choice = getRequiredIntInput(scanner, "choice");

            switch (choice) {
                case 0:
                    quit = true;
                    break;
                case 1:
                    addVehicle(scanner);
                    break;

                case 2:
                    showVehicle();
                    break;
                case 3:
                    updateVehicle(scanner);
                    break;
                case 4:
                    removeVehicle(scanner);
                    break;
                case 5:
                    printVehiclesByFilter(scanner);
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
            System.out.println();

        } while (!quit);
    }

    // ************* Helper in Vehicle operation **************//

    private void filterVehicle(VehicleFilter filter) {
        boolean found = false;
        for (Vehicle vehicle : vehicleService.getAllVehicles()) {
            if (filter.search(vehicle)) {
                System.out.println(vehicle.toString());
                found = true;
            }

        }
        if (!found) {
            System.out.println("Vehicle Not Found!");
        }
    }

    // ************* Vehicle operation **************//

    public void printVehiclesByFilter(Scanner scanner) {
        boolean quit = false;
        int choice;
        do {
            printSectionHeader("Search Vehicles");
            System.out.println("""
                    0. Back to Vehicle Management
                    1. Vehicle model
                    2. Vehicles price
                    3. Vehicle powerSource
                    4. Vehicle Class
                    5. Vehicle type
                    """);
            choice = getRequiredIntInput(scanner, "choice");

            switch (choice) {
                case 0:
                    quit = true;
                    break;
                case 1:
                    String input = getRequiredInput(scanner, "Vehicle model");
                    filterVehicle(v -> v.getVehicleModel().equalsIgnoreCase(input));
                    break;
                case 2:
                    input = getRequiredInput(scanner, "Vehicle price");
                    filterVehicle(v -> String.format("%.1f", v.getRentalRatePerDay()).equals(input));
                    break;
                case 3:
                    input = getRequiredInput(scanner, "Vehicle powerSource");
                    filterVehicle(v -> v.getPowerSource().equalsIgnoreCase(input));
                    break;
                case 4:
                    input = getRequiredInput(scanner, "Vehicle Class");
                    filterVehicle(v -> v.getVehicleClass().equalsIgnoreCase(input));
                    break;
                case 5:
                    System.out.print("""
                            1. Car
                            2. Moto
                            """);
                    choice = getRequiredIntInput(scanner, "choice");
                    if (choice == 1) {
                        filterVehicle(v -> v instanceof Car);
                        break;
                    } else if (choice == 2) {
                        filterVehicle(v -> v instanceof Moto);
                        break;
                    } else {
                        System.out.println("Invalid choice!");
                        break;
                    }
                default:
                    System.out.println("Invalid choice!");
            }
            System.out.println();

        } while (!quit);
    }

    public void addCar(Scanner scanner) {
        String powerSource = selectInput(scanner, powerSources, "power source");
        String vehicleClass = selectInput(scanner, carClasses, "car class");
        String brand = selectInput(scanner, carBrands, "brand");
        String model = getRequiredInput(scanner, "model");

        double price = getRequiredDoubleInput(scanner, "price");

        String vehicleLicence = getRequiredInput(scanner, "car licence (e.g. VL-01-AB-1234)");
        String licencePlate = getRequiredInput(scanner, "licence plate (e.g. PP-1000)");

        int doorOfCar = getRequiredIntInput(scanner, "number of seats");

        Car newCar = new Car("Car", powerSource, vehicleClass, brand, model,
                price, vehicleLicence, licencePlate, doorOfCar);

        vehicleService.registerNewVehicle(newCar);

        System.out.println("Add car successfully. Total cars: " + this.vehicleService.getCarCount());
        System.out.println("vehicleCount: " + vehicleService.getVehicleCount());
    }

    public void addMoto(Scanner scanner) {
        String powerSource = selectInput(scanner, powerSources, "power source");
        String vehicleClass = selectInput(scanner, motoClasses, "moto class");
        String brand = selectInput(scanner, motoBrands, "brand");
        String model = getRequiredInput(scanner, "model");

        double price = getRequiredDoubleInput(scanner, "price");

        String vehicleLicence = getRequiredInput(scanner, "Moto licence (e.g. VL-01-AB-1234)");
        String licencePlate = getRequiredInput(scanner, "licence plate (e.g. PP-1000)");

        boolean helmetIncluded = Boolean.parseBoolean(getRequiredInput(scanner, "helmet included (true/false)"));

        Moto newMoto = new Moto("Moto", powerSource, vehicleClass, brand, model,
                price, vehicleLicence, licencePlate, helmetIncluded);

        vehicleService.registerNewVehicle(newMoto);
        System.out.println("Add moto successfully. Total motos: " + this.vehicleService.getMotoCount());
        System.out.println("vehicleCount: " + vehicleService.getVehicleCount());
    }

    public void addVehicle(Scanner scanner) {

        if (!getLoggedInStaff().can(MANAGE_VEHICLE)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }
        boolean quit = false;
        do {
            System.out.println(
                    "\n" +
                            "0. Quit\n" +
                            "1. Car\n" +
                            "2. Moto\n");
            int choice = getRequiredIntInput(scanner, "choice");
            switch (choice) {
                case 0:
                    quit = true;
                    break;
                case 1:
                    addCar(scanner);
                    break;
                case 2:
                    addMoto(scanner);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }

        } while (!quit);

    }

    public void showVehicle() {
        if (!getLoggedInStaff().can(VIEW_VEHICLE)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }
        if (vehicleService.getVehicleCount() == 0) {
            System.out.println("Controller.Garage is empty!");
            return;
        }
        System.out.println();
        for (Vehicle car : vehicleService.getAllVehicles()) {
            System.out.println(car.toString());
        }
        System.out.println();
    }

    public void removeVehicle(Scanner scanner) {
        if (!getLoggedInStaff().can(MANAGE_VEHICLE)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }
        if (vehicleService.getVehicleCount() == 0) {
            System.out.println("No vehicle to remove!");
            return;
        }

        Vehicle target = findVehicle(scanner);
        if (target == null) {
            System.out.println("Vehicle not found! ID and code must both match the same vehicle.");
            return;
        }

        for (Rent rent : rentalService.getActiveRents()) {
            if (rent != null && rent.getVehicle() != null &&
                    rent.getVehicle().getVehicleId() == target.getVehicleId()) {
                System.out.println("Vehicle is currently rented and cannot be removed.");
                return;
            }
        }

        vehicleService.removeVehicle(target);
        System.out.println(
                "Vehicle [" + target.getVehicleId() + "] " + target.getVehicleCode() + " removed successfully.");
        System.out.println("vehicleCount: " + vehicleService.getVehicleCount());
    }

    public void updateVehicle(Scanner scanner) {
        if (!getLoggedInStaff().can(MANAGE_VEHICLE)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }
        if (vehicleService.getVehicleCount() == 0) {
            System.out.println("Controller.Garage is Empty!");
            return;
        }
        Vehicle item = findVehicle(scanner);
        if (item == null) {
            System.out.println("Vehicle not found! ID and code must both match the same vehicle.");
            return;
        }
        {
            boolean quit = false;
            int choice;
            do {
                System.out.println("""
                        Update vehicle:
                        0. Back to Vehicle Management
                        1. powerSource
                        2. Vehicle Class
                        3. Brand
                        4. Model
                        5. Price
                        6. Status""");

                choice = getRequiredIntInput(scanner, "choice");

                switch (choice) {
                    case 0:
                        quit = true;
                        break;
                    case 1:
                        String powerSource = getRequiredInput(scanner, "New powerSource");
                        item.setPowerSource(powerSource);
                        vehicleService.updateVehicleInDB(item);
                        break;
                    case 2:
                        String vehicleClass = getRequiredInput(scanner, "New Vehicle Class");
                        item.setVehicleClass(vehicleClass);
                        vehicleService.updateVehicleInDB(item);
                        break;
                    case 3:
                        String vehicleBrand = getRequiredInput(scanner, "New Brand");
                        item.setVehicleBrand(vehicleBrand);
                        vehicleService.updateVehicleInDB(item);
                        break;
                    case 4:
                        String vehicleModel = getRequiredInput(scanner, "New Model");
                        item.setVehicleModel(vehicleModel);
                        vehicleService.updateVehicleInDB(item);
                        break;
                    case 5:
                        double rentalRatePerDay = getRequiredDoubleInput(scanner, "New Price");
                        item.setRentalRatePerDay(rentalRatePerDay);
                        vehicleService.updateVehicleInDB(item);
                        break;
                    case 6:
                        boolean isRented = false;
                        for (Rent rent : rentalService.getActiveRents()) {
                            if (rent != null && rent.getVehicle() != null &&
                                    rent.getVehicle().getVehicleId() == item.getVehicleId()) {
                                isRented = true;
                                break;
                            }
                        }
                        if (isRented) {
                            System.out.println("Cannot change status - car is currently rented!");
                        } else {
                            boolean status = Boolean.parseBoolean(getRequiredInput(scanner, "status (true/false)"));
                            item.setAvailable(status);
                            vehicleService.updateVehicleInDB(item);
                        }
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
                System.out.println();

            } while (!quit);
        }
    }


    // Matches a vehicle only when BOTH numeric id AND code point to the same entry
    // helper function
    private boolean setSalaryStaff(Staff staff, double salary) {
        if (staff == null) {
            System.out.println("Invalid staff!");
            return false;
        }
        staff.setSalary(salary);
        return true;
    }

    private boolean canBeRented(Customer customer, Vehicle vehicle) {
        String vehicleType = vehicle.getClass().getSimpleName();

        switch (vehicleType) {
            case "Car":
                if (!vehicle.isAvailable()) {
                    System.out.println("Vehicle Not Available");
                    return false;
                }
                if (customer.getDriverLicensePhoto() == null || customer.getDriverLicensePhoto().isEmpty()) {
                    System.out.println("Customer does not have Driver Licence");
                    System.out.println("This " + vehicleType
                            + " cannot be rented to this customer! Please check the requirements and try again.");
                    return false;
                }
                System.out.println("Customer is valid");
                return true;
            case "Moto":
                if (!vehicle.isAvailable()) {
                    System.out.println("Vehicle Not Available");
                    return false;
                }
                if (customer.getIDCardPhoto() == null || customer.getIDCardPhoto().isEmpty()) {
                    System.out.println("Customer does not have ID Card");
                    System.out.println("This " + vehicleType
                            + " cannot be rented to this customer! Please check the requirements and try again.");
                    return false;
                }
                System.out.println("Customer is valid");
                return true;
            default:
                System.out.println("Invalid vehicle type!");
        }
        return false;
    }

    public String getRequiredInput(Scanner scanner, String fieldName) {
        String input = "";
        while (input.trim().isEmpty()) {
            System.out.print("Enter " + fieldName + " (required): ");
            input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                System.out.println(fieldName + " cannot be empty!");
                System.out.println();
            }
        }
        return input;
    }

    // Validate Name input
    public String validateNameInput(Scanner scanner, String fieldName) {
        String name;
        while (true) {
            name = getRequiredInput(scanner, fieldName);
            if (name.matches("^[a-zA-Z ]+$")) {
                return name;
            } else {
                System.out.println(
                        "Invalid input. " + fieldName + " should only contain letters, spaces and must not be empty.");
            }
        }
    }

    // Handle user input for a list of options
    public String selectInput(Scanner scanner, ArrayList<String> options, String fieldName) {
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }

        int choice = getRequiredIntInput(scanner, fieldName);
        if (choice < 1 || choice > options.size()) {
            System.out.println("Invalid choice. Please select a valid option.");
            return selectInput(scanner, options, fieldName);
        }
        return options.get(choice - 1);
    }

    public int getRequiredIntInput(Scanner scanner, String fieldName) {
        while (true) {
            String text = getRequiredInput(scanner, fieldName);
            try {
                return Integer.parseInt(text.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid integer for '" + fieldName + "'.");
            }
        }
    }

    public double getRequiredDoubleInput(Scanner scanner, String fieldName) {
        while (true) {
            String text = getRequiredInput(scanner, fieldName);
            try {
                return Double.parseDouble(text.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid decimal number for '" + fieldName + "'.");
            }
        }
    }

    public boolean getRequiredBooleanInput(Scanner scanner, String fieldName) {
        while (true) {
            String text = getRequiredInput(scanner, fieldName).trim().toLowerCase();
            if (text.equals("true") || text.equals("false")) {
                return Boolean.parseBoolean(text);
            }
            System.out.println("Invalid input. Please enter 'true' or 'false' for '" + fieldName + "'.");
        }
    }

    private void printSeparator() {
        System.out.println("------------------------------------------------------------");
    }

    private void printSectionHeader(String title) {
        printSeparator();
        System.out.println("-- " + title + " --");
        printSeparator();
    }

    public Vehicle findVehicle(Scanner scanner) {
        int id = getRequiredIntInput(scanner, "Vehicle ID (number)");
        String code = getRequiredInput(scanner, "Enter Vehicle Code (e.g. Car-1, Moto-2)");
        Vehicle byId = vehicleService.findById(id);
        Vehicle byCode = vehicleService.findByCode(code);
        if (byId != null && byId == byCode)
            return byId;
        return null;
    }

    private Staff findStaff(Scanner scanner) {
        int id = getRequiredIntInput(scanner, "Staff ID (number)");
        String username = getRequiredInput(scanner, "Staff Username ");
        return staffService.findByIdAndUsername(id, username);
    }

    /** Look up a vehicle by its global numeric ID (1, 2, 3, …). */
    public Vehicle getVehicleByID(int id) {
        return vehicleService.findById(id);
    }

    /** Look up a vehicle by its type-based code (e.g. "Car-1", "Moto-2"). */
    public Vehicle getVehicleByCode(String code) {
        return vehicleService.findByCode(code);
    }

    private boolean isValidDateFormat(String date) {
        return date != null && date.matches("\\d{2}-\\d{2}-\\d{4}");
    }

    // Customer Management

    public void addCustomer(Scanner scanner) {
        String customerName = getRequiredInput(scanner, "customer Name");
        String customerIdNum = getRequiredInput(scanner, "customer ID Number");
        String customerPhone = getRequiredInput(scanner, "customer Phone");

        Customer newCustomer = new Customer(customerName, customerIdNum, customerPhone);
        customerService.registerNewCustomer(newCustomer);
        
        System.out.println("Add customer successfully.");
        System.out.println("customerCount: " + customerService.getCount());
    }

    public void showCustomers() {
        if (customerService.getCount() == 0) {
            System.out.println("No customers!");
            return;
        }
        for (Customer customer : customerService.getAllCustomers()) {
            System.out.println(customer.toString());
        }
        System.out.println();
    }

    public void updateCustomer(Scanner scanner) {
        if (customerService.getCount() == 0) {
            System.out.println("No customers!");
            return;
        }
        int id = getRequiredIntInput(scanner, "customer ID(int)");
        Customer item = customerService.findById(id);

        if (item != null) {
            boolean quit = false;
            int choice;
            do {
                System.out.println("""
                            Update customer:
                            0. Back to Customer Management
                            1. ID Card Number
                            2. Name
                            3. Phone
                            4. ID Card Photo
                            5. Driver License Photo
                        """);

                choice = getRequiredIntInput(scanner, "choice");

                switch (choice) {
                    case 0:
                        quit = true;
                        break;
                    case 1:
                        item.setcustomerIdNum(getRequiredInput(scanner, "new ID Card"));
                        customerService.updateCustomerInDB(item);
                        break;
                    case 2:
                        item.setCustomerName(getRequiredInput(scanner, "new Name"));
                        customerService.updateCustomerInDB(item);
                        break;
                    case 3:
                        item.setCustomerPhone(getRequiredInput(scanner, "new Phone"), customerService.getAllCustomers());
                        customerService.updateCustomerInDB(item);
                        break;
                    case 4:
                        item.setIDCardPhoto(getRequiredInput(scanner, "new ID Card Photo"));
                        customerService.updateCustomerInDB(item);
                        break;
                    case 5:
                        item.setDriverLicensePhoto(getRequiredInput(scanner, "new Driver License Photo"));
                        customerService.updateCustomerInDB(item);
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
                System.out.println();

            } while (!quit);
            return;
        }
        System.out.println("Customer not found!");
    }

    public void removeCustomer(Scanner scanner) {
        if (customerService.getCount() == 0) {
            System.out.println("No customer to remove!");
            return;
        }

        int id = getRequiredIntInput(scanner, "customer ID(int)");

        if (rentalService.getTotalRentCount() > 0) {
            for (Rent rent : rentalService.getActiveRents()) {
                if (rent != null && rent.getCustomer() != null && rent.getCustomer().getCustomerId() == id) {
                    System.out.println("Customer is associated with an active rent and cannot be removed.");
                    return;
                }
            }
        }

        Customer customer = customerService.findById(id);
        if (customer != null) {
            customerService.removeCustomer(id);
            System.out.println("Customer with ID " + id + " removed successfully.");
            System.out.println("customerCount: " + customerService.getCount());
        } else {
            System.out.println("Customer not found!");
        }
    }

    public Customer findCustomerByID(Scanner scanner) {
        int id = getRequiredIntInput(scanner, "customer ID(int)");
        return customerService.findById(id);
    }

    public void customerManagement(Scanner scanner) {
        // Check if staff is logged in and has permission
        if (!requireStaffLogin()) {
            System.out.println(getLastMessage());
            return;
        }
        if (!getLoggedInStaff().can(MANAGE_CUSTOMER)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }

        printSectionHeader("Customer Management");
        boolean quit = false;
        int choice;
        do {
            System.out.println("""
                    0. Back to Main Menu
                    1. Add Customer
                    2. Show Customers
                    3. Update Customer
                    4. Remove Customer""");

            choice = getRequiredIntInput(scanner, "choice");

            switch (choice) {
                case 0:
                    quit = true;
                    break;
                case 1:
                    addCustomer(scanner);
                    break;

                case 2:
                    showCustomers();
                    break;
                case 3:
                    updateCustomer(scanner);
                    break;
                case 4:
                    removeCustomer(scanner);
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
            System.out.println();

        } while (!quit);
    }



    // Rent Management

    public void rentManagement(Scanner scanner) {
        // Check if staff is logged in
        if (!requireStaffLogin()) {
            System.out.println(getLastMessage());
            return;
        }

        printSectionHeader("Rent Management");
        boolean quit = false;
        int choice;
        do {
            System.out.println("""
                        0. Back to Main Menu
                        1. Add Rent
                        2. Show Rents
                        3. Update Rent
                        4. Remove Rent
                        5. Return Vehicle
                        6. Lookup Completed Rent
                    """);

            choice = getRequiredIntInput(scanner, "choice");

            switch (choice) {
                case 0 -> quit = true;
                case 1 -> addRent(scanner);
                case 2 -> showRents();
                case 3 -> updateRent(scanner);
                case 4 -> removeRent(scanner);
                case 5 -> returnVehicle(scanner);
                case 6 -> lookupCompletedRent(scanner);
                default -> System.out.println("Invalid choice!");
            }
            System.out.println();

        } while (!quit);
    }
    
    public void reportManagement(Scanner scanner) {
        if (!requireStaffLogin()) {
            System.out.println(getLastMessage());
            return;
        }
        if (!getLoggedInStaff().can(VIEW_REPORTS)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }

        System.out.println("Report Management");
        boolean quit = false;
        do {
            System.out.println("""
                    0. Back to Main Menu
                    1. Show Rent Records
                    2. Generate Full Report
                    """);

            int choice = getRequiredIntInput(scanner, "choice");

            switch (choice) {
                case 0 -> quit = true;
                case 1 -> showRentalHistory();
                case 2 -> generateReport();
                default -> System.out.println("Invalid choice!");
            }
            System.out.println();
        } while (!quit);
    }

    public void addRent(Scanner scanner) {
        int rentDays;

        Vehicle selectedvVehicle = findVehicle(scanner);
        Customer selectedCustomer = findCustomerByID(scanner);

        if (selectedvVehicle == null) {
            System.out.println("Vehicle not found!");
            return;
        }
        if (selectedCustomer == null) {
            System.out.println("Customer not found!");
            return;
        }

        if (!canBeRented(selectedCustomer, selectedvVehicle)) {
            return;
        }

        if (!selectedvVehicle.isAvailable()) {
            System.out.println("Vehicle is not available for rent!");
            return;
        }
        while (true) {
            rentDays = getRequiredIntInput(scanner, "number of days(int)");
            if (rentDays > 0) {
                break;
            } else {
                System.out.println("Rent days must be greater than 0. Please try again.");
            }
        }
        String startDate = getRequiredInput(scanner, "start date");
        while (!isValidDateFormat(startDate)) {
            System.out.println("Invalid date format! Please enter date in dd-MM-yyyy format.");
            startDate = getRequiredInput(scanner, "start date");
        }

        String endDate = getRequiredInput(scanner, "end date");
        while (!isValidDateFormat(endDate)) {
            System.out.println("Invalid date format! Please enter date in dd-MM-yyyy format.");
            endDate = getRequiredInput(scanner, "end date");
        }

        double vehiclePrice = selectedvVehicle.getRentalRatePerDay(); // snapshot vehicle price
        Rent newRent = new Rent(selectedvVehicle, selectedCustomer, getLoggedInStaff(), rentDays, startDate, endDate); 

        double deposit = getRequiredDoubleInput(scanner, "deposit amount($)");
        Payment payment = new Payment(newRent.getRentDays(), vehiclePrice, deposit); 

        newRent.setPayment(payment);

        rentalService.processNewRent(newRent);
        
        System.out.println("Add rent successfully.");
        System.out.println("rentCount: " + rentalService.getTotalRentCount());
    }

    public void showRents() {
        if (rentalService.getTotalRentCount() == 0) {
            System.out.println("No rents!");
            return;
        }
        for (Rent rent : rentalService.getActiveRents()) {
            System.out.println(rent.toString());
        }
        System.out.println();
    }

    public void updateRent(Scanner scanner) {
        if (rentalService.getTotalRentCount() == 0) {
            System.out.println("No rents!");
            return;
        }
        int id = getRequiredIntInput(scanner, "rent ID(int)");
        Rent rent = rentalService.findById(id);

        if (rent != null) {
            boolean quit = false;
            int choice;
            do {
                System.out.println("""
                        Update rent:
                        0. Back to Rent Management
                        1. Rent Days
                        2. Vehicle
                        3. Customer
                        """);
                choice = getRequiredIntInput(scanner, "choice");

                switch (choice) {
                    case 0 -> quit = true;
                    case 1 -> {
                        int newRentDays = getRequiredIntInput(scanner, "New Rent Days");
                        if (newRentDays <= 0) {
                            System.out.println("Rent days must be greater than 0.");
                            break;
                        }
                        if (newRentDays == rent.getRentDays()) {
                            System.out.println("Rent days is the same as before. No update needed.");
                            break;
                        }
                        System.out.println("Warning: Changing rent days may affect the total price.");
                        String confirm = getRequiredInput(scanner, "Confirm (yes/no)").trim();
                        if (!confirm.equalsIgnoreCase("yes")) {
                            System.out.println("Rent days update cancelled.");
                            break;
                        }
                        rent.setRentDays(newRentDays);
                        rent.getPayment().setRentDays(newRentDays);

                        String newStartDate = getRequiredInput(scanner, "new start date");
                        while (!isValidDateFormat(newStartDate)) {
                            System.out.println("Invalid date format! Please enter date in dd-MM-yyyy format.");
                            newStartDate = getRequiredInput(scanner, "new start date");
                        }
                        rent.setStartDate(newStartDate);

                        String newEndDate = getRequiredInput(scanner, "new end date");
                        while (!isValidDateFormat(newEndDate)) {
                            System.out.println("Invalid date format! Please enter date in dd-MM-yyyy format.");
                            newEndDate = getRequiredInput(scanner, "new end date");
                        }
                        rent.setEndDate(newEndDate);
                        com.rental.system.database.DatabaseMapper.updateRent(rent); // Sync → DB
                        com.rental.system.database.DatabaseMapper.updatePayment(rent.getPayment()); // Sync → DB
                        System.out.println("Rent days updated successfully.");
                    }
                    case 2 -> {
                        Vehicle newCar = findVehicle(scanner);
                        if (newCar == null) {
                            System.out.println("Vehicle not found!");
                        } else if (!newCar.isAvailable()) {
                            System.out.println("Selected car is not available!");
                        } else {
                            if (rent.getVehicle() != null) {
                                rent.getVehicle().setAvailable(true);
                                vehicleService.updateVehicleInDB(rent.getVehicle());
                            }
                            newCar.setAvailable(false);
                            vehicleService.updateVehicleInDB(newCar);
                            rent.setVehicle(newCar);
                            rent.getPayment().setPrice(newCar.getRentalRatePerDay());
                            com.rental.system.database.DatabaseMapper.updateRent(rent); // Sync → DB
                            com.rental.system.database.DatabaseMapper.updatePayment(rent.getPayment()); // Sync → DB
                            System.out.println("Vehicle updated successfully.");
                        }
                    }
                    case 3 -> {
                        Customer newCustomer = findCustomerByID(scanner);
                        if (newCustomer == null) {
                            System.out.println("Customer not found!");
                        } else {
                            rent.setCustomer(newCustomer);
                            com.rental.system.database.DatabaseMapper.updateRent(rent); // Sync → DB
                            System.out.println("Customer updated successfully.");
                        }
                    }
                    default -> System.out.println("Invalid choice!");
                }
                System.out.println();

            } while (!quit);
            return;
        }
        System.out.println("Rent ID not found!");
    }

    public void removeRent(Scanner scanner) {
        if (rentalService.getTotalRentCount() == 0) {
            System.out.println("No rent to remove!");
            return;
        }
        int id = getRequiredIntInput(scanner, "rent ID(int)");
        Rent rentToRemove = rentalService.findById(id);

        if (rentToRemove == null) {
            System.out.println("Rent ID not found!");
            return;
        }

        System.out.println("""
                Warning:
                * This action cannot be undone, once it is removed.
                * Removing a rent will also remove its associated payment.

                Are you sure you want to remove this rent?
                """);
        String confirm = getRequiredInput(scanner, "Confirm (yes/no)").trim();
        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Removal cancelled.");
            return;
        }

        rentalService.removeRent(rentToRemove);
        System.out.println("Remove rent successfully.");
    }

    public void returnVehicle(Scanner scanner) {
        if (rentalService.getTotalRentCount() == 0) {
            System.out.println("No rents! Nothing to return.");
            return;
        }
        int id = getRequiredIntInput(scanner, "rent ID(int)");
        Rent rent = rentalService.findById(id);

        if (rent != null) {
            if (!rent.isStatus()) {
                System.out.println("This receipt is already paid!");
                return;
            }

            if (rent.getVehicle() == null) {
                System.out.println("Error: No car associated with this rent!");
                return;
            }
            // prompt staff to verify vehicle by both ID and code
            Vehicle vehicle = findVehicle(scanner);
            if (vehicle == null || vehicle != rent.getVehicle()) {
                System.out.println("Vehicle mismatch! ID and code must match the vehicle on this rent.");
                return;
            }

            System.out.print("Enter payment method: ");
            String paymentMethod = scanner.nextLine();

            // if there's discount or extra day or damage
            boolean update = getRequiredBooleanInput(scanner, "Update special case (true/false)");
            if (update)
                updatePayment(scanner, rent);

            String payDate = getRequiredInput(scanner, "payDate");
            while (!isValidDateFormat(payDate)) {
                System.out.println("Invalid date format! Please enter date in dd-MM-yyyy format.");
                payDate = getRequiredInput(scanner, "payDate");
            }

            rentalService.processReturn(rent, payDate, paymentMethod);

            double total = rent.getPayment().calculateTotal();
            System.out.println("Payment created. Final total amount: $" + total);
            System.out.println(
                    "Vehicle [" + rent.getVehicle().getVehicleId() + "] " + rent.getVehicle().getVehicleCode()
                            + " has been returned and is now available.");
            System.out.println();
            return;
        }
        System.out.println("Rent ID not found!");
    }

    public Rent findRentByID(Scanner scanner) {
        int id = getRequiredIntInput(scanner, "rent ID(int)");
        return rentalService.findById(id);
    }

    // Payment Management
    public void showPayment(Scanner scanner) {
        Rent rent = findRentByID(scanner);
        if (rent == null) {
            System.out.println("Rent not found!");
            return;
        }
        Payment payment = rent.getPayment();
        if (payment == null) {
            System.out.println("No payment associated with this rent!");
            return;
        }
        System.out.println(payment);
    }

    public void updatePayment(Scanner scanner, Rent rent) {
        if (rent == null) {
            System.out.println("Rent not found!");
            return;
        }

        Payment payment = rent.getPayment();

        double discount = getRequiredDoubleInput(scanner, "discount (0 if none)");
        payment.setDiscount(discount);

        int extraDays = getRequiredIntInput(scanner, "extra days (0 if none)");
        payment.setExtraDays(extraDays);

        double damageFee = getRequiredDoubleInput(scanner, "damage fee (0 if none)");
        payment.setDamageFee(damageFee);

        com.rental.system.database.DatabaseMapper.updatePayment(payment); // Sync → DB
        System.out.println("Payment updated successfully!");
    }

    public void paymentManagement(Scanner scanner) {
        // Check if staff is logged in and has permission
        if (!requireStaffLogin()) {
            System.out.println(getLastMessage());
            return;
        }
        if (!getLoggedInStaff().can(SHOW_PAYMENT)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }

        boolean quit = false;
        do {
            System.out.println("""
                    Payment Management:\s
                    0. Back to Main Menu
                    1. Show payment
                    2. Update payment
                    """);
            System.out.print("Enter your choice(int): ");
            int choice = getRequiredIntInput(scanner, "choice");
            switch (choice) {
                case 0 -> quit = true;
                case 1 -> showPayment(scanner);
                case 2 -> updatePayment(scanner, findRentByID(scanner));
                default -> System.out.println("Invalid choice!");
            }
            System.out.println();
        } while (!quit);
    }

    // History Management

    public void lookupCompletedRent(Scanner scanner) {
        if (!requireStaffLogin()) {
            System.out.println(getLastMessage());
            return;
        }
        if (!getLoggedInStaff().can(VIEW_RENT)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }
        if (rentalService.getRentalHistory().isEmpty()) {
            System.out.println("No completed rentals in history yet.");
            return;
        }

        int searchId = getRequiredIntInput(scanner, "rent ID to look up");

        for (RentRecord record : rentalService.getRentalHistory()) {
            if (record.getRentId() == searchId) {
                System.out.println("\n----- Completed Rent Record -----");
                System.out.println("  Rent ID        : " + record.getRentId());
                System.out.println("  --- Vehicle ---");
                System.out.println("  ID / Code      : [" + record.getVehicleId() + "] " + record.getVehicleCode());
                System.out.println("  Type / Vehicle : " + record.getVehicleType() + " — " + record.getVehicleBrand()
                        + " " + record.getVehicleModel() + " (" + record.getLicencePlate() + ")");
                System.out.println(
                        "  Power / Class  : " + record.getVehiclePowerSource() + " / " + record.getVehicleClass());
                System.out.printf("  Rate/day       : $%.2f%n", record.getRentalRatePerDay());
                System.out.println("  --- Customer ---");
                System.out.println("  ID / Name      : [" + record.getCustomerId() + "] " + record.getCustomerName());
                System.out.println("  ID Card No.    : " + record.getCustomerIdNum());
                System.out.println("  Phone          : " + record.getCustomerPhone());
                System.out.println("  --- Rental ---");
                System.out.println("  Rent days      : " + record.getRentDays());
                System.out.println("  Start date     : " + record.getStartDate());
                System.out.println("  End date       : " + record.getEndDate());
                System.out.println("  Returned on    : " + record.getReturnDate());
                System.out.println("  --- Payment ---");
                System.out.println("  Payment ID     : " + record.getPaymentId());
                System.out.println("  Method         : " + record.getPaymentMethod());
                System.out.println("  Pay date       : " + record.getPayDate());
                System.out.printf("  Base price/day : $%.2f%n", record.getPrice());
                System.out.printf("  Discount       : %.1f%%%n", record.getDiscount());
                System.out.println("  Extra days     : " + record.getExtraDays());
                System.out.printf("  Damage fee     : $%.2f%n", record.getDamageFee());
                System.out.printf("  Deposit        : $%.2f%n", record.getDeposit());
                System.out.printf("  Total paid     : $%.2f%n", record.getTotalPaid());
                System.out.println("  Status         : " + record.getPaymentStatus());
                System.out.println("---------------------------------\n");
                return;
            }
        }
        System.out.println("No completed rent found with ID " + searchId + ".");
    }

    public void showRentalHistory() {
        if (!requireStaffLogin()) {
            System.out.println(getLastMessage());
            return;
        }
        if (!getLoggedInStaff().can(VIEW_REPORTS)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }
        if (rentalService.getRentalHistory().isEmpty()) {
            System.out.println("No completed rentals in history yet.");
            return;
        }
        System.out.println("===== Rental History ===== (" + rentalService.getRentalHistory().size() + " records)");
        double totalRevenue = rentalService.calculateTotalRevenue();
        for (RentRecord record : rentalService.getRentalHistory()) {
            System.out.println(record);
        }
        System.out.printf("Total revenue from completed rentals: $%.2f%n", totalRevenue);
    }

    public void generateReport() {
        if (!requireStaffLogin()) {
            System.out.println(getLastMessage());
            return;
        }
        if (!getLoggedInStaff().can(VIEW_REPORTS)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║         VEHICLE RENTAL MANAGEMENT        ║");
        System.out.println("║              FULL REPORT                 ║");
        System.out.println("╚══════════════════════════════════════════╝");

        // ── 1. Fleet summary ──────────────────────────────────────────
        System.out.println("\n── 1. Fleet Summary ─────────────────────");
        int totalCars = 0, totalMotos = 0, availableCars = 0, availableMotos = 0;
        for (Vehicle v : vehicleService.getAllVehicles()) {
            if (v instanceof Car) {
                totalCars++;
                if (v.isAvailable())
                    availableCars++;
            } else if (v instanceof Moto) {
                totalMotos++;
                if (v.isAvailable())
                    availableMotos++;
            }
        }
        int totalVehicles = totalCars + totalMotos;
        int available = availableCars + availableMotos;
        System.out.println("  Total vehicles   : " + totalVehicles);
        System.out.println("  Available        : " + available);
        System.out.println("  Rented out       : " + (totalVehicles - available));
        System.out.printf("  Cars  : %d total, %d available, %d rented%n", totalCars, availableCars,
                totalCars - availableCars);
        System.out.printf("  Motos : %d total, %d available, %d rented%n", totalMotos, availableMotos,
                totalMotos - availableMotos);

        // ── 2. Rental summary ─────────────────────────────────────────
        System.out.println("\n── 2. Rental Summary ────────────────────");
        int activeRents = rentalService.getActiveRentCount();
        int completedRents = rentalService.getRentalHistory().size();
        System.out.println("  Active rents     : " + activeRents);
        System.out.println("  Completed rents  : " + completedRents);
        System.out.println("  Total rents ever : " + (activeRents + completedRents));

        // ── 3. Revenue summary ────────────────────────────────────────
        System.out.println("\n── 3. Revenue Summary ───────────────────");
        if (completedRents == 0) {
            System.out.println("  No completed rentals yet.");
        } else {
            double totalRevenue = rentalService.calculateTotalRevenue();
            double avgRevenue = totalRevenue / completedRents;
            System.out.printf("  Total revenue    : $%.2f%n", totalRevenue);
            System.out.printf("  Average per rent : $%.2f%n", avgRevenue);
        }

        // ── 4. Top rented vehicle ─────────────────────────────────────
        System.out.println("\n── 4. Top Rented Vehicle ────────────────");
        java.util.Map.Entry<Integer, Integer> topV = rentalService.getTopVehicleId();
        if (topV == null) {
            System.out.println("  No data yet.");
        } else {
            Vehicle v = vehicleService.findById(topV.getKey());
            String label = (v != null) ? v.getVehicleCode() + " " + v.getVehicleBrand() + " " + v.getVehicleModel() : "Unknown Vehicle (ID: " + topV.getKey() + ")";
            System.out.println("  " + label + " — rented " + topV.getValue() + " time(s)");
        }

        // ── 5. Top customer ───────────────────────────────────────────
        System.out.println("\n── 5. Top Customer ──────────────────────");
        java.util.Map.Entry<Integer, Integer> topC = rentalService.getTopCustomerId();
        if (topC == null) {
            System.out.println("  No data yet.");
        } else {
            Customer c = customerService.findById(topC.getKey());
            String label = (c != null) ? c.getCustomerName() : "Unknown Customer (ID: " + topC.getKey() + ")";
            System.out.println("  " + label + " (ID: " + topC.getKey() + ") — "
                    + topC.getValue() + " rental(s)");
        }

        System.out.println("\n══════════════════════════════════════════\n");
    }

    // ===== DASHBOARD =====

    public void showDashboard() {
        int availableVehicles = 0;
        for (Vehicle v : vehicleService.getAllVehicles()) {
            if (v.isAvailable())
                availableVehicles++;
        }

        int activeRentsCount = rentalService.getActiveRentCount();

        System.out.println("\n========== GARAGE DASHBOARD ==========");
        System.out.println("Logged in as : " + (getLoggedInStaff() != null
                ? getLoggedInStaff().getName() + " (" + getLoggedInStaff().getClass().getSimpleName() + ")"
                : "N/A"));
        System.out.println("--------------------------------------");
        System.out.println("Total vehicles   : " + vehicleService.getVehicleCount());
        System.out.println("Available now    : " + availableVehicles);
        System.out.println("Rented out       : " + (vehicleService.getVehicleCount() - availableVehicles));
        System.out.println("--------------------------------------");
        System.out.println("Total customers  : " + customerService.getCount());
        System.out.println("Active rents     : " + activeRentsCount);
        System.out.println("Completed rents  : " + rentalService.getRentalHistory().size());
        System.out.println("======================================\n");
    }

    // Other Management
}