package controller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import model.*;
import user.*;

@FunctionalInterface
interface VehicleFilter {
    boolean search(Vehicle v);
}
@FunctionalInterface
interface StaffFilter {
    boolean test(Staff staff);
}

public class Garage {

    /*====== Action Constants ====== */
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
    public static final String SET_MANAGER_SALARY= "SET_MANAGER_SALARY";
    private ArrayList<String> carClasses = new ArrayList<>(){{
        add("SUV");
        add("Sedan");
        add("Van");
        add("Coupe");
        add("Truck");
    }};

    private ArrayList<String> powerSources = new ArrayList<>(){{
        add("gasoline");
        add("diesel");
        add("electric");
        add("hybrid");
    }};

    // Brand 
    private ArrayList<String> carBrands = new ArrayList<>(){{
        add("Ford");
        add("Tesla");
        add("Toyota");
        add("Honda");
        add("BMW");
    }};

    private ArrayList<String> motoClasses = new ArrayList<>(){{
        add("Sport");
        add("Cruiser");
        add("Touring");
    }};

    // Moto brand
        private ArrayList<String> motoBrands = new ArrayList<>(){{
            add("Honda");
            add("Yamaha");
            add("Suzuki");
            add("Kawasaki");
            add("Ducati");
        }};



    private ArrayList<Vehicle> garage;
    private static int vehicleID = 0;
    private static int vehicleCount; // current number of vehicles
    private int carCount;
    private int motoCount;

    private HashSet<Customer> customers;
    private int customerCount;

    private ArrayList<Rent> rents;
    private int rentCount;

    private HashSet<Staff> staffs;
    private int staffCount;

    private ArrayList<RentRecord> rentalHistory;

    //  LOGIN DEPENDENCY & FEEDBACK MESSAGE
    private Staff loggedInStaff;   // null = no staff login
    private String lastMessage;

    public Garage(int maxSize) {
        // Initialize garage
        this.garage = new ArrayList<>(maxSize);
        this.vehicleCount = 0;
        generateVehicleToGarage();
        // Initialize customer list
        this.customers = new HashSet<>();
        this.customerCount = 0;
        generateCustomerToSystem();
        // Initialize rent list
        this.rents = new ArrayList<>(maxSize);
        this.rentCount = 0;
        // Initialize staff list
        this.staffs = new HashSet<>();
        this.staffCount = 0;
        generateStaffToSystem();
        // Initialize rental history
        this.rentalHistory = new ArrayList<>();

        loggedInStaff = null;
        lastMessage = "Controller.Garage created successfully!";

    }

    public void addAdmin(String name, String username, String password){
        Staff admin = new ManagerStaff(name, username, password, 0){
            @Override
            public boolean can(String action) {
                // TODO Auto-generated method stub
                return true;
            }
        };
        staffs.add(admin);
        // System.out.println(admin);



    }


    // GETTERS
    public String getLastMessage() { return lastMessage; }
    public boolean isStaffLoggedIn() { return loggedInStaff != null; }
    public Staff getLoggedInStaff() { return loggedInStaff; }
    public static int getVehicleCount() { return vehicleCount; }
    public static int getVehicleID() { return vehicleID; }

    // SETTERS
    private void setLastMessage(String msg) { lastMessage = msg; }

    // Staff Management

    public void generateStaffToSystem() {
        addAdmin("Admin",   "admin_root", "root123");
        Staff s2 = new ManagerStaff("Bob",  "bob_manager", "manager123", 0);
        Staff s3 = new RegularStaff("Chan", "chan_staff", "staff123",1500,"Station-Moto");
        staffs.add(s3);
        staffs.add(s2);
        staffCount += 3;
    }

    public void staffManagement(Scanner scanner) {
        // Check if staff is logged in and has permission
        if (!requireStaffLogin()) {
            System.out.println(getLastMessage());
            return;
        }
        if (!loggedInStaff.can(MANAGE_STAFF)) {
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
        if (loggedInStaff == null) {
            setLastMessage("Action denied: staff must login first.");
            return false;
        }

        if (!loggedInStaff.isActive()) {
            loggedInStaff = null;
            setLastMessage("Action denied: staff is inactive (auto logout).");
            return false;
        }

        return true;
    }

    // =========================
    // STAFF LOGIN / LOGOUT
    // =========================

    public void staffLogin(String username, String password) {

        if (username.isBlank() || password == null) {
            setLastMessage("Login failed: missing username/password.");
            return;
        }

        for (Staff s:  staffs) {

            if (s.getUsername().equalsIgnoreCase(username.trim())) {

                if (!s.getStatus()) {
                    setLastMessage("Login failed: staff is no longer employed.");
                    return;
                }

                if (!s.checkPassword(password)) {
                    setLastMessage("Login failed: wrong password.");
                    return;
                }

                // Set staff as online (active)
                s.setActive(true);
                loggedInStaff = s;
                setLastMessage("\nLogin success. Welcome " + s.getUsername() + "!\n");
                showDashboard();
                return;
            }
        }

        setLastMessage("Login failed: username not found.");
    }

    public void staffLogout() {
        if (loggedInStaff != null) {
            // Set staff as offline (inactive)
            loggedInStaff.setActive(false);
        }
        loggedInStaff = null;
        setLastMessage("Logged out successfully.");
    }

    public void addStaff(Scanner scanner) {
        boolean quit = false;
        int choice;
        do{
            System.out.print(
                    "\n" +
                    "0. Quit\n" +
                    "1. Manager\n" +
                    "2. Regular staff\n"
            );
            choice = getRequiredIntInput(scanner, "position");
            switch (choice) {
                case 0,1,2 -> quit = true;
                default -> System.out.println("Invalid choice!");

            }
        }while (!quit);
        if(choice == 0){return;}

        String name = getRequiredInput(scanner,"staff name");

        double salary = getRequiredDoubleInput(scanner, "staff salary");
        if (salary <= 0) {
            System.out.println("Salary must be greater than 0.");
            return;
        }

        String username = getRequiredInput(scanner, "staff username");

        // Check username uniqueness
        for (Staff s : staffs) {
            if (s.getUsername().equalsIgnoreCase(username)) {
                System.out.println("Username already exists. Please choose another.");
                return;
            }
        }

        String password = getRequiredInput(scanner, "staff password");
        if (password == null || password.trim().length() < 4) {
            System.out.println("Password must be at least 4 characters.");
            return;
        }
        String workStation = getRequiredInput(scanner, "staff work station(Moto or Car)");

        switch (choice) {
            case 1 -> staffs.add(new ManagerStaff(name, username, password, salary));
            case 2 -> staffs.add(new RegularStaff(name, username, password, salary,workStation));
            default -> {
                System.out.println("Can't create new staff.");
            }
        }

    }

    public void showStaffs(Scanner scanner) {
        if (staffCount == 0) {
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
                            "3. Manager Staffs\n"
            );
            choice = getRequiredIntInput(scanner, "option");
            switch (choice) {
                case 0 -> quit = true;
                case 1 -> {
                    StaffFilter allStaff = new StaffFilter() {
                        public boolean test(Staff staff){
                            return true;
                        }
                    };
                    showFilteredStaffs(allStaff);
                }
                case 2 -> {
                    StaffFilter regularOnly = new StaffFilter() {
                        @Override
                        public boolean test(Staff staff) {
                            return staff instanceof RegularStaff;
                        }
                    };
                    showFilteredStaffs(regularOnly);
                }
                case 3 -> {
                    StaffFilter managerOnly = new StaffFilter() {
                        @Override
                        public boolean test(Staff staff) {
                            return staff instanceof ManagerStaff;
                        }
                    };
                    showFilteredStaffs(managerOnly);
                }
                default -> System.out.println("Invalid choice!");
            }
        } while(!quit);
    }

    private void showFilteredStaffs(StaffFilter filter) {
        boolean found = false;

        for (Staff staff : staffs) {
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
        if(!getLoggedInStaff().can(MANAGE_STAFF)) {
            System.out.println("Manager section Only!");
            return;}
        Staff targetStaff = findStaff(scanner);
        for (Staff staff : staffs) {
            if (staff.equals(targetStaff)) {
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
                                staff.setName(newName);
                                System.out.println("Staff name updated to " + newName + ".");
                            }
                        }
                        case 2 -> {
                           if( !getLoggedInStaff().can(SET_MANAGER_SALARY)) {
                               System.out.println("Only admin can set salary to manager!");
                               return;
                           }

                            double newSalary = getRequiredDoubleInput(scanner, "new Salary");
                            if (newSalary > 0) {
                                if(setSalaryStaff(staff, newSalary)){
                                    System.out.println("Change salary of staff successfully!");
                                }else {
                                    System.out.println("Cannot change salary regular staff!");
                                    return;
                                }
                                System.out.println("Staff salary updated to " + newSalary + ".");
                            } else {
                                System.out.println("Salary must be greater than 0. No change made.");
                            }
                        }
                        case 3 -> {
                            if( targetStaff instanceof ManagerStaff && !getLoggedInStaff().can(SET_MANAGER_SALARY) ){
                                System.out.println("You cannot Enable/Disable Manager Status!");
                                return;
                            }
                            String action = staff.getStatus() ? "disable (resign)" : "enable (employ)";
                            System.out.print("Are you sure do you want to " + action + " this staff? ");
                            String confirm = getRequiredInput(scanner, "confirm(yes/no)").trim();
                            if (confirm.equalsIgnoreCase("yes")) {
                                staff.setStatus(!staff.getStatus());
                                System.out.println("Staff status flipped. Now: " + (staff.getStatus() ? "Employed" : "Resigned"));
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
        }
        System.out.println("Staff not found!");
    }

    public void removeStaff(Scanner scanner) {

        Staff staffToRemove = findStaff(scanner);
//        for (Staff staff : staffs) {
//            if (staff.equals(staffToRemove)) {
//                staffToRemove = staff;
//                break;
//            }
//        }

        if (staffToRemove != null) {
            System.out.print("Are you sure do you want to remove this staff? ");
            String confirm = getRequiredInput(scanner, "confirm(yes/no)").trim();
            if (confirm.equalsIgnoreCase("yes")) {
                staffs.remove(staffToRemove);
                staffCount--;
                System.out.println("Staff with ID " + staffToRemove.getId() + " removed successfully.");
                System.out.println("staffCount: " + staffCount);
            } else {
                System.out.println("Operation cancelled.");
            }
        } else {
            System.out.println("Staff with ID " + staffToRemove.getId() + " not found.");
        }
    }

    // Vehicle Management


    public void generateVehicleToGarage() {
        // type, powerSource, vehicleClass, brand, model, price, licence, licencePlate
        String[][] vehicles = {
            { "Car",  "gasoline", "SUV",      "Ford",  "Escape",   "300", "VL-01-AB-1234", "PP-1000" },
            { "Car",  "electric", "Sedan",    "Tesla", "Model 3",  "500", "VL-02-CD-5678", "PP-1001" },
            { "Car",  "diesel",   "Truck",    "Toyota","Hilux",    "400", "VL-03-EF-9012", "PP-1002" },
            { "Car",  "hybrid",   "Hatchback","Honda", "Insight",  "350", "VL-04-GH-3456", "PP-1003" },
            { "Car",  "gasoline", "Coupe",    "BMW",   "M4",       "600", "VL-05-IJ-7890", "PP-1004" },
            { "Moto", "gasoline", "Sport",    "Honda", "CBR600RR", "75",  "MOTO-LIC-2026", "ABC-1234" },
            { "Moto", "gasoline", "Sport",    "Honda", "CBR600RR", "75",  "MOTO-LIC-2026", "ABC-1234" },
        };

        for (String[] v : vehicles) {
            String type   = v[0];
            double price  = Double.parseDouble(v[5]);
            Vehicle vehicle = switch (type) {
                case "Moto" -> new Moto("Moto", v[1], v[2], v[3], v[4], price, v[6], v[7], true);
                default     -> new Car("Car",  v[1], v[2], v[3], v[4], price, v[6], v[7], 4);
            };
            if("Moto".equals(type)) motoCount++;
            else carCount++;
            garage.add(vehicle);
            vehicleCount++;
            vehicleID ++;
        }
    }

    public void vehicleManagement(Scanner scanner) {
        // Check if staff is logged in and has permission
        if (!requireStaffLogin()) {
            System.out.println(getLastMessage());
            return;
        }
        if (!loggedInStaff.can(VIEW_VEHICLE)) {
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

    //************* Helper in Vehicle operation **************//

    private void filterVehicle(VehicleFilter filter) {
        boolean found = false;
        for (Vehicle vehicle : garage) {
            if(filter.search(vehicle)) {
                System.out.println(vehicle.toString());
                found = true;
            }

        }
        if (!found) {
            System.out.println("Vehicle Not Found!");
        }
    }

    //************* Vehicle operation **************//

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
                    filterVehicle(v -> String.format("%.1f", v.getRentalRatePerDay()).equals(input) );
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
                    if (choice == 1){
                        filterVehicle(v -> v instanceof Car);
                        break;
                    }else if (choice == 2){
                        filterVehicle(v -> v instanceof Moto);
                        break;
                    }else{
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

        garage.add(newCar);
        carCount++;
        System.out.println("Add car successfully. Total cars: " + this.carCount);

        vehicleCount++;
        vehicleID ++;
        System.out.println("vehicleCount: " + vehicleCount);
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

        garage.add(newMoto);
        motoCount++;
        System.out.println("Add moto successfully. Total motos: " + this.motoCount);

        vehicleCount++;
        vehicleID ++;
        System.out.println("vehicleCount: " + vehicleCount);
    }

    public void addVehicle(Scanner scanner) {

        if (!loggedInStaff.can(MANAGE_VEHICLE)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }
        boolean quit = false;
        do {
            System.out.println(
                    "\n" +
                    "0. Quit\n" +
                    "1. Car\n" +
                    "2. Moto\n"
            );
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

        }while (!quit);


    }

    public void showVehicle() {
        if (!loggedInStaff.can(VIEW_VEHICLE)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }
        if (vehicleCount == 0) {
            System.out.println("Controller.Garage is empty!");
            return;
        }
        System.out.println();
        for(Vehicle car : garage) {
            System.out.println(car.toString());
        }
        System.out.println();
    }

    public void removeVehicle(Scanner scanner) {
        if (!loggedInStaff.can(MANAGE_VEHICLE)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }
        if (vehicleCount == 0) {
            System.out.println("No vehicle to remove!");
            return;
        }

        Vehicle target = findVehicle(scanner);
        if (target == null) {
            System.out.println("Vehicle not found! ID and code must both match the same vehicle.");
            return;
        }

        for (Rent rent : rents) {
            if (rent != null && rent.getVehicle() != null &&
                    rent.getVehicle().getVehicleId() == target.getVehicleId()) {
                System.out.println("Vehicle is currently rented and cannot be removed.");
                return;
            }
        }

        garage.remove(target);
        vehicleCount--;
        System.out.println("Vehicle [" + target.getVehicleId() + "] " + target.getVehicleCode() + " removed successfully.");
        System.out.println("vehicleCount: " + vehicleCount);
    }

    public void updateVehicle(Scanner scanner) {
        if (!loggedInStaff.can(MANAGE_VEHICLE)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }
        if (vehicleCount == 0) {
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
                            break;

                        case 2:
                            String vehicleClass = getRequiredInput(scanner, "New Vehicle Class");
                            item.setVehicleClass(vehicleClass);
                            break;
                        case 3:
                            String vehicleBrand = getRequiredInput(scanner, "New Brand");
                            item.setVehicleBrand(vehicleBrand);
                            break;
                        case 4:
                            String vehicleModel = getRequiredInput(scanner, "New Model");
                            item.setVehicleModel(vehicleModel);
                            break;
                        case 5:
                            double rentalRatePerDay = getRequiredDoubleInput(scanner, "New Price");
                            item.setRentalRatePerDay(rentalRatePerDay);
                            break;
                        case 6:
                            // Check if car is currently rented
                            boolean isRented = false;
                            for (Rent rent : rents) {
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
    //helper function
    private boolean setSalaryStaff(Staff staff, double salary) {
        if(staff == null) {
            System.out.println("Invalid staff!");
            return false;
        }
        staff.setSalary(salary);
        return true;
    }
    private boolean canBeRented(Customer customer, Vehicle vehicle) {
        String vehicleType =  vehicle.getClass().getSimpleName();

        switch (vehicleType) {
            case "Car":
                if (!vehicle.isAvailable()) {
                    System.out.println("Vehicle Not Available");
                    return false;
                }
                if(customer.getDriverLicensePhoto() == null || customer.getDriverLicensePhoto().isEmpty()) {
                    System.out.println("Customer does not have Driver Licence");
                    System.out.println("This " + vehicleType + " cannot be rented to this customer! Please check the requirements and try again.");
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
                    System.out.println("This " + vehicleType + " cannot be rented to this customer! Please check the requirements and try again.");
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
                System.out.println("Invalid input. " + fieldName + " should only contain letters, spaces and must not be empty.");
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
        int id = getRequiredIntInput(scanner,"Vehicle ID (number)");
        String code = getRequiredInput(scanner, "Enter Vehicle Code (e.g. Car-1, Moto-2)");
        Vehicle byId = getVehicleByID(id);
        Vehicle byCode = getVehicleByCode(code);
        if (byId != null && byId == byCode) return byId;
        return null;
    }

    private Staff findStaff(Scanner scanner) {
        int id = getRequiredIntInput(scanner, "Staff ID (number)");
        String username = getRequiredInput(scanner, "Staff Username ");
        for(Staff staff : staffs){
            if(staff.getId() == id && staff.getUsername().equals(username)){
                return staff;
            }
        }
        return null;
    }

    /** Look up a vehicle by its global numeric ID (1, 2, 3, …). */
    public Vehicle getVehicleByID(int id) {
        for (Vehicle v : garage) {
            if (v.getVehicleId() == id) {
                return v;
            }
        }
        return null;
    }

    /** Look up a vehicle by its type-based code (e.g. "Car-1", "Moto-2"). */
    public Vehicle getVehicleByCode(String code) {
        for (Vehicle v : garage) {
            if (v.getVehicleCode() != null && v.getVehicleCode().equals(code.trim())) {
                return v;
            }
        }
        return null;
    }

    private boolean isValidDateFormat(String date) {
        return date != null && date.matches("\\d{2}-\\d{2}-\\d{4}");
    }

    // Customer Management

    public void generateCustomerToSystem() {
        String[][] custs = {
            { "Aruna Smith", "D7654321", "0662345679", "IDCard.jpg", "DriverLicense.jpg" },
            { "Bona Johnson", "D2345678", "0122345680", "IDCard.jpg", "DriverLicense.jpg" },
            { "Champa Brown", "D3456789", "0172345681", "IDCard.jpg", "DriverLicense.jpg" },
            { "Diana Prince", "D4567890", "0882345682", "IDCard.jpg", "DriverLicense.jpg" },
            { "Eno Gonzalez", "D5678901", "0972345683", "IDCard.jpg", "" }
        };

        for (String[] cust : custs) {
            Customer newCustomer = new Customer(cust[0], cust[1], cust[2], cust[3], cust[4]);
            customers.add(newCustomer);
            customerCount++;
        }
    }

    public void customerManagement(Scanner scanner) {
        // Check if staff is logged in and has permission
        if (!requireStaffLogin()) {
            System.out.println(getLastMessage());
            return;
        }
        if (!loggedInStaff.can(MANAGE_CUSTOMER)) {
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

    public void addCustomer(Scanner scanner) {
        // Take inputs
        String customerName = getRequiredInput(scanner, "customer Name");

        String customerIdNum = getRequiredInput(scanner, "customer ID Number");

        String customerPhone = getRequiredInput(scanner, "customer Phone");

        Customer newCustomer = new Customer(customerName, customerIdNum, customerPhone);

        customers.add(newCustomer);
        customerCount++;
        System.out.println("Add customer successfully.");
        System.out.println("customerCount: " + customerCount);
    }

    public void showCustomers() {
        if (customerCount == 0) {
            System.out.println("No customers!");
            return;
        }
        for (Customer customer : customers) {
            System.out.println(customer.toString());
        }
        System.out.println();
    }

    public void updateCustomer(Scanner scanner) {
        if (customerCount == 0) {
            System.out.println("No customers!");
            return;
        }
        int id = getRequiredIntInput(scanner,"customer ID(int)");
        for (Customer item : customers) {

            if (item.getCustomerId() == id) {
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
                            break;

                        case 2:
                            item.setCustomerName(getRequiredInput(scanner, "new Name"));
                            break;
                        case 3:
                            item.setCustomerPhone(getRequiredInput(scanner, "new Phone"), customers);
                            break;
                        case 4:
                            item.setIDCardPhoto(getRequiredInput(scanner, "new ID Card Photo"));
                            break;
                        case 5:
                            item.setDriverLicensePhoto(getRequiredInput(scanner, "new Driver License Photo"));
                            break;
                        default:
                            System.out.println("Invalid choice!");
                    }
                    System.out.println();

                } while (!quit);
                return;
            }
        }
        System.out.println("Customer not found!");
    }

    public void removeCustomer(Scanner scanner) {
        if (customerCount == 0) {
            System.out.println("No customer to remove!");
            return;
        }

        int id = getRequiredIntInput(scanner, "customer ID(int)");

        if (rentCount > 0) {
             for (Rent rent : rents) {
                if (rent != null && rent.getCustomer() != null && rent.getCustomer().getCustomerId() == id) {
                    System.out.println("Customer is associated with an active rent and cannot be removed.");
                    return;
                }
            }
        }

        for (Customer customer : customers) {
            if (customer.getCustomerId() == id) {
                customers.remove(customer);
                customerCount--;
                break;
            }
        }

        System.out.println("Customer with ID " + id + " removed successfully.");
        System.out.println("customerCount: " + customerCount);
    }

    public Customer findCustomerByID(Scanner scanner) {
        int id = getRequiredIntInput(scanner, "customer ID(int)");

        for (Customer customer : customers) {
            if (customer.getCustomerId() == id)
                return customer;
        }
        return null;
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

        if(!canBeRented(selectedCustomer, selectedvVehicle)) {
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
        Rent newRent = new Rent(selectedvVehicle, selectedCustomer, rentDays, startDate, endDate); // create rent without payment first

        double deposit = getRequiredDoubleInput(scanner, "deposit amount($)");
        Payment payment = new Payment(newRent.getRentDays(), vehiclePrice, deposit); // create payment with deposit and base price

        newRent.setPayment(payment); // add payment to rent

        selectedvVehicle.setAvailable(false); // mark vehicle as unavailable

        // add rent to list
        rents.add(newRent);
        rentCount++;
        System.out.println("Add rent successfully.");
        System.out.println("rentCount: " + rentCount);
    }

    public void showRents() {
        if (rentCount == 0) {
            System.out.println("No rents!");
            return;
        }
        for (Rent rent : rents) {
            System.out.println(rent.toString());
        }
        System.out.println();
    }

    public void updateRent(Scanner scanner) {
        if (rentCount == 0) {
            System.out.println("No rents!");
            return;
        }
        int id = getRequiredIntInput(scanner, "rent ID(int)");

        for (Rent rent : rents) {
            if (rent.getRentId() == id) {
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
                            rent.getPayment().setRentDays(newRentDays); // update rent days in payment as well

                            System.out.println("Rent days updated successfully.");
                            System.out.println("Please re-enter start date and end date again.");

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
                        }
                        case 2 -> {
                            Vehicle newCar = findVehicle(scanner);
                            if (newCar == null) {
                                System.out.println("Vehicle not found!");
                            } else if (!newCar.isAvailable()) {
                                System.out.println("Selected car is not available!");
                            } else {
                                // Mark old car as available
                                if (rent.getVehicle() != null) {
                                    rent.getVehicle().setAvailable(true);
                                }
                                // Mark new car as unavailable
                                newCar.setAvailable(false);
                                // Update the car
                                rent.setVehicle(newCar);
                                rent.getPayment().setPrice(newCar.getRentalRatePerDay()); // update price in payment as well
                                System.out.println("Vehicle updated successfully.");
                            }
                        }
                        case 3 -> {
                            Customer newCustomer = findCustomerByID(scanner);
                            if (newCustomer == null) {
                                System.out.println("Customer not found!");
                            } else {
                                rent.setCustomer(newCustomer);
                                System.out.println("Customer updated successfully.");
                            }
                        }
                        default -> System.out.println("Invalid choice!");
                    }
                    System.out.println();

                } while (!quit);
                return;
            }
        }
    }

    public void removeRent(Scanner scanner) {
        if (rentCount == 0) {
            System.out.println("No rent to remove!");
            return;
        }
        int id = getRequiredIntInput(scanner, "rent ID(int)");

        int index = -1;
        for (int i = 0; i < rents.size(); i++) {
            if (rents.get(i).getRentId() == id) {
                index = i;
                break;
            }
        }
        if (index == -1) {
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

        Rent rentToRemove = rents.get(index);
        // Mark car as available when rent is removed
        if (rentToRemove.getVehicle() != null) {
            rentToRemove.getVehicle().setAvailable(true);
        } else {
            System.out.println("Warning: Rent has no associated car!");
        }

        rents.remove(index);
        rentCount--;

        System.out.println("Remove rent successfully.");
    }

    public void returnVehicle(Scanner scanner) {
        if (rentCount == 0) {
            System.out.println("No rents! Nothing to return.");
            return;
        }
        int id = getRequiredIntInput(scanner, "rent ID(int)");

        for (Rent rent : rents) {
            if (rent.getRentId() == id) {
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

                // Process payment
                rent.getPayment().processPayment(paymentMethod, payDate);

                // Set rent return date
                rent.setReturnDate(payDate);

                // Mark car as available
                rent.getVehicle().setAvailable(true);

                double total = rent.getPayment().calculateTotal();
                System.out.println("Payment created. Final total amount: $" + total);
                System.out.println(
                        "Vehicle [" + rent.getVehicle().getVehicleId() + "] " + rent.getVehicle().getVehicleCode() + " has been returned and is now available.");
                System.out.println();
                rent.setStatus(false);
                // --- Snapshot: add immutable record to rental history ---
                rentalHistory.add(new RentRecord(rent));
                return;
            }
        }
        System.out.println("Rent ID not found!");
    }

    public Rent findRentByID(Scanner scanner) {
        int id = getRequiredIntInput(scanner,"rent ID(int)");

        for (Rent rent : rents) {
            if (rent.getRentId() == id)
                return rent;
        }
        return null;
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

        System.out.println("Payment updated successfully!");
    }


    public void paymentManagement(Scanner scanner) {
        // Check if staff is logged in and has permission
        if (!requireStaffLogin()) {
            System.out.println(getLastMessage());
            return;
        }
        if (!loggedInStaff.can(SHOW_PAYMENT)) {
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
        if (!loggedInStaff.can(VIEW_RENT)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }
        if (rentalHistory.isEmpty()) {
            System.out.println("No completed rentals in history yet.");
            return;
        }

        int searchId = getRequiredIntInput(scanner,"rent ID to look up");

        for (RentRecord record : rentalHistory) {
            if (record.getRentId() == searchId) {
                System.out.println("\n----- Completed Rent Record -----");
                System.out.println("  Rent ID        : " + record.getRentId());
                System.out.println("  --- Vehicle ---");
                System.out.println("  ID / Code      : [" + record.getVehicleId() + "] " + record.getVehicleCode());
                System.out.println("  Type / Vehicle : " + record.getVehicleType() + " — " + record.getVehicleBrand() + " " + record.getVehicleModel() + " (" + record.getLicencePlate() + ")");
                System.out.println("  Power / Class  : " + record.getVehiclePowerSource() + " / " + record.getVehicleClass());
                System.out.printf( "  Rate/day       : $%.2f%n", record.getRentalRatePerDay());
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
                System.out.printf( "  Base price/day : $%.2f%n", record.getPrice());
                System.out.printf( "  Discount       : %.1f%%%n", record.getDiscount());
                System.out.println("  Extra days     : " + record.getExtraDays());
                System.out.printf( "  Damage fee     : $%.2f%n", record.getDamageFee());
                System.out.printf( "  Deposit        : $%.2f%n", record.getDeposit());
                System.out.printf( "  Total paid     : $%.2f%n", record.getTotalPaid());
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
        if (!loggedInStaff.can(VIEW_REPORTS)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }
        if (rentalHistory.isEmpty()) {
            System.out.println("No completed rentals in history yet.");
            return;
        }
        System.out.println("===== Rental History ===== (" + rentalHistory.size() + " records)");
        double totalRevenue = 0;
        for (RentRecord record : rentalHistory) {
            System.out.println(record);
            totalRevenue += record.getTotalPaid();
        }
        System.out.printf("Total revenue from completed rentals: $%.2f%n", totalRevenue);
    }

    public void generateReport() {
        if (!requireStaffLogin()) {
            System.out.println(getLastMessage());
            return;
        }
        if (!loggedInStaff.can(VIEW_REPORTS)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println(  "║         VEHICLE RENTAL MANAGEMENT        ║");
        System.out.println(  "║              FULL REPORT                 ║");
        System.out.println(  "╚══════════════════════════════════════════╝");

        // ── 1. Fleet summary ──────────────────────────────────────────
        System.out.println("\n── 1. Fleet Summary ─────────────────────");
        int totalCars = 0, totalMotos = 0, availableCars = 0, availableMotos = 0;
        for (Vehicle v : garage) {
            if (v instanceof Car) {
                totalCars++;
                if (v.isAvailable()) availableCars++;
            } else if (v instanceof Moto) {
                totalMotos++;
                if (v.isAvailable()) availableMotos++;
            }
        }
        int totalVehicles = totalCars + totalMotos;
        int available     = availableCars + availableMotos;
        System.out.println("  Total vehicles   : " + totalVehicles);
        System.out.println("  Available        : " + available);
        System.out.println("  Rented out       : " + (totalVehicles - available));
        System.out.printf( "  Cars  : %d total, %d available, %d rented%n", totalCars,  availableCars,  totalCars  - availableCars);
        System.out.printf( "  Motos : %d total, %d available, %d rented%n", totalMotos, availableMotos, totalMotos - availableMotos);

        // ── 2. Rental summary ─────────────────────────────────────────
        System.out.println("\n── 2. Rental Summary ────────────────────");
        int activeRents = 0;
        for (Rent r : rents) {
            if (r.isStatus()) activeRents++;
        }
        System.out.println("  Active rents     : " + activeRents);
        System.out.println("  Completed rents  : " + rentalHistory.size());
        System.out.println("  Total rents ever : " + (activeRents + rentalHistory.size()));

        // ── 3. Revenue summary ────────────────────────────────────────
        System.out.println("\n── 3. Revenue Summary ───────────────────");
        if (rentalHistory.isEmpty()) {
            System.out.println("  No completed rentals yet.");
        } else {
            double totalRevenue = 0;
            for (RentRecord r : rentalHistory) totalRevenue += r.getTotalPaid();
            double avgRevenue = totalRevenue / rentalHistory.size();
            System.out.printf("  Total revenue    : $%.2f%n", totalRevenue);
            System.out.printf("  Average per rent : $%.2f%n", avgRevenue);
        }

        // ── 4. Top rented vehicle ─────────────────────────────────────
        System.out.println("\n── 4. Top Rented Vehicle ────────────────");
        if (rentalHistory.isEmpty()) {
            System.out.println("  No data yet.");
        } else {
            java.util.HashMap<Integer, Integer> vehicleFreq = new java.util.HashMap<>();
            java.util.HashMap<Integer, String>  vehicleLabel = new java.util.HashMap<>();
            for (RentRecord r : rentalHistory) {
                int vid = r.getVehicleId();
                vehicleFreq.put(vid, vehicleFreq.getOrDefault(vid, 0) + 1);
                vehicleLabel.put(vid, r.getVehicleCode() + " " + r.getVehicleBrand() + " " + r.getVehicleModel());
            }
            int topVehicleId = -1, topVehicleCount = 0;
            for (java.util.Map.Entry<Integer, Integer> entry : vehicleFreq.entrySet()) {
                if (entry.getValue() > topVehicleCount) {
                    topVehicleCount = entry.getValue();
                    topVehicleId    = entry.getKey();
                }
            }
            System.out.println("  " + vehicleLabel.get(topVehicleId) + " — rented " + topVehicleCount + " time(s)");
        }

        // ── 5. Top customer ───────────────────────────────────────────
        System.out.println("\n── 5. Top Customer ──────────────────────");
        if (rentalHistory.isEmpty()) {
            System.out.println("  No data yet.");
        } else {
            java.util.HashMap<Integer, Integer> customerFreq  = new java.util.HashMap<>();
            java.util.HashMap<Integer, String>  customerLabel = new java.util.HashMap<>();
            for (RentRecord r : rentalHistory) {
                int cid = r.getCustomerId();
                customerFreq.put(cid, customerFreq.getOrDefault(cid, 0) + 1);
                customerLabel.put(cid, r.getCustomerName());
            }
            int topCustomerId = -1;
            int topCustomerCount = 0;
            for (java.util.Map.Entry<Integer, Integer> entry : customerFreq.entrySet()) {
                if (entry.getValue() > topCustomerCount) {
                    topCustomerCount = entry.getValue();
                    topCustomerId    = entry.getKey();
                }
            }
            System.out.println("  " + customerLabel.get(topCustomerId) + " (ID: " + topCustomerId + ") — " + topCustomerCount + " rental(s)");
        }

        System.out.println("\n══════════════════════════════════════════\n");
    }

    // ===== DASHBOARD =====

    public void showDashboard() {
        int availableVehicles = 0;
        for (Vehicle v : garage) {
            if (v.isAvailable()) availableVehicles++;
        }

        int activeRents = 0;
        for (Rent r : rents) {
            if (r.isStatus()) activeRents++;
        }

        System.out.println("\n========== GARAGE DASHBOARD ==========");
        System.out.println("Logged in as : " + (loggedInStaff != null ? loggedInStaff.getName() + " (" + loggedInStaff.getClass().getSimpleName() + ")" : "N/A"));
        System.out.println("--------------------------------------");
        System.out.println("Total vehicles   : " + vehicleCount);
        System.out.println("Available now    : " + availableVehicles);
        System.out.println("Rented out       : " + (vehicleCount - availableVehicles));
        System.out.println("--------------------------------------");
        System.out.println("Total customers  : " + customerCount);
        System.out.println("Active rents     : " + activeRents);
        System.out.println("Completed rents  : " + rentalHistory.size());
        System.out.println("======================================\n");
    }

    // Other Management
}