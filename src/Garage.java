import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;


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

    private ArrayList<IVehicle> garage;
    private int vehicleCount; // current number of vehicles

    private HashSet<Customer> customers;
    private int customerCount;

    private ArrayList<Rent> rents;
    private int rentCount;

    private HashSet<IStaff> staffs;
    private int staffCount;

    private ArrayList<RentRecord> rentalHistory;

    //  LOGIN DEPENDENCY & FEEDBACK MESSAGE
    private IStaff loggedInStaff;   // null = no staff login
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
        lastMessage = "Garage created successfully!";

    }

    // GETTERS 
    public String getLastMessage() { return lastMessage; }
    public boolean isStaffLoggedIn() { return loggedInStaff != null; }
    public IStaff getLoggedInStaff() { return loggedInStaff; }
    
    // SETTERS
    private void setLastMessage(String msg) { lastMessage = msg; }

    // Staff Management

    public void generateStaffToSystem() {
        IStaff s1 = new ManagerStaff("Admin", "Manager", 0, "admin_root", "root123");
        IStaff s2 = new ManagerStaff("Bob", "Manager", 3000, "bob_manager", "manager123");
        IStaff s3 = new RegularStaff("Chan", "Staff", 1500, "chan_staff", "staff123");
        staffs.add(s1);
        staffs.add(s2);
        staffs.add(s3);
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

        System.out.println("Staff Management:");
        boolean quit = false;
        int choice;
        do {
            System.out.println("""
                    0. Back to Main Menu
                    1. Add Staff
                    2. Show Staffs
                    3. Update Staff
                    4. Remove Staff""");

            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0 -> quit = true;
                case 1 -> addStaff(scanner);
                case 2 -> showStaffs();
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

        for (IStaff s:  staffs) {

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
        System.out.print("Enter staff name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Staff name cannot be empty.");
            return;
        }

        System.out.print("Enter staff role: ");
        String role = scanner.nextLine().trim();
        if (role.isEmpty()) {
            System.out.println("Staff role cannot be empty.");
            return;
        }

        System.out.print("Enter staff salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine(); 
        if (salary <= 0) {
            System.out.println("Salary must be greater than 0.");
            return;
        }

        System.out.print("Enter staff username: ");
        String username = scanner.nextLine().trim();
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }
        // Check username uniqueness
        for (IStaff s : staffs) {
            if (s.getUsername().equalsIgnoreCase(username)) {
                System.out.println("Username already exists. Please choose another.");
                return;
            }
        }

        System.out.print("Enter staff password: ");
        String password = scanner.nextLine();
        if (password == null || password.trim().length() < 4) {
            System.out.println("Password must be at least 4 characters.");
            return;
        }

        IStaff newStaff = switch (role.trim().toUpperCase()) {
            case "MANAGER", "ADMIN" -> new ManagerStaff(name, role, salary, username, password);
            case "STAFF" -> new RegularStaff(name, role, salary, username, password);
            default -> {
                yield null;
            }
        };

        if (newStaff == null) {
            System.out.println("Invalid role! Staff will not be created.");
            return;
        } else if (staffs.add(newStaff)) {
            staffCount++;
            System.out.println("Add staff successfully.");
            System.out.println("staffCount: " + staffCount);
        } else {
            System.out.println("Failed to add staff. A staff with the same ID already exists.");
        }
    }

    public void showStaffs() {
        if (staffCount == 0) {
            System.out.println("No staffs!");
            return;
        }
        for (IStaff staff : staffs) {
            System.out.println(staff.toString());
        }
        System.out.println();
    }

    public void updateStaff(Scanner scanner) {
        System.out.print("Enter staff ID(int): ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        for (IStaff staff : staffs) {
            if (staff.getId() == id) {
                boolean quit = false;
                int choice;
                do {
                    System.out.println("""
                            Update staff:
                            0. Back to Staff Management
                            1. Name
                            2. Role
                            3. Salary
                            4. Enable/Disable Status""");

                    System.out.print("Enter choice: ");
                    choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    switch (choice) {
                        case 0 -> quit = true;
                        case 1 -> {
                            System.out.print("New Name: ");
                            String newName = scanner.nextLine().trim();
                            if (newName.isEmpty()) {
                                System.out.println("Name cannot be empty. No change made.");
                            } else {
                                staff.setName(newName);
                                System.out.println("Staff name updated to " + newName + ".");
                            }
                        }
                        case 2 -> {
                            System.out.print("New Role: ");
                            String newRole = scanner.nextLine().trim();
                            String currentRole = staff.getRole();
                            if (newRole.equalsIgnoreCase(currentRole)) {
                                System.out.println("Role is already set to " + currentRole + ". No change made.");
                            } else if (currentRole.equalsIgnoreCase("Manager") && newRole.equalsIgnoreCase("Staff")) {
                                System.out.print("Are you sure you want to demote this staff? (yes/no): ");
                                String confirm = scanner.nextLine().trim();
                                if (confirm.equalsIgnoreCase("yes")) {
                                    staff.setRole(newRole);
                                    System.out.println("Staff demoted to " + newRole + ".");
                                } else {
                                    System.out.println("Operation cancelled.");
                                }
                            } else if (currentRole.equalsIgnoreCase("Staff") && newRole.equalsIgnoreCase("Manager")) {
                                System.out.print("Are you sure you want to promote this staff? (yes/no): ");
                                String confirm = scanner.nextLine().trim();
                                if (confirm.equalsIgnoreCase("yes")) {
                                    staff.setRole(newRole);
                                    System.out.println("Staff promoted to " + newRole + ".");
                                } else {
                                    System.out.println("Operation cancelled.");
                                }
                            } else {
                                staff.setRole(newRole);
                                System.out.println("Staff role changed to " + newRole + ".");
                            }
                        }
                        case 3 -> {
                            System.out.print("New Salary: ");
                            double newSalary = scanner.nextDouble();
                            scanner.nextLine();
                            if (newSalary > 0) {
                                staff.setSalary(newSalary);
                                System.out.println("Staff salary updated to " + newSalary + ".");
                            } else {
                                System.out.println("Salary must be greater than 0. No change made.");
                            }
                        }
                        case 4 -> {
                            String action = staff.getStatus() ? "disable (resign)" : "enable (employ)";
                            System.out.print("Are you sure do you want to " + action + " this staff? (yes/no): ");
                            String confirm = scanner.nextLine().trim();
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
        System.out.print("Enter staff ID(int): ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        IStaff staffToRemove = null;
        for (IStaff staff : staffs) {
            if (staff.getId() == id) {
                staffToRemove = staff;
                break;
            }
        }

        if (staffToRemove != null) {
            System.out.print("Are you sure do you want to remove this staff? (yes/no): ");
            String confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("yes")) {
                staffs.remove(staffToRemove);
                staffCount--;
                System.out.println("Staff with ID " + id + " removed successfully.");
                System.out.println("staffCount: " + staffCount);
            } else {
                System.out.println("Operation cancelled.");
            }
        } else {
            System.out.println("Staff with ID " + id + " not found.");
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
            IVehicle vehicle = switch (type) {
                case "Moto" -> new Moto(v[0],v[1], v[2], v[3], v[4], price, v[6], v[7]);
                default     -> new Car( v[0],v[1], v[2], v[3], v[4], price, v[6], v[7]);
            };
            garage.add(vehicle);
            vehicleCount++;
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

        System.out.println("Vehicle Management:");
        boolean quit = false;
        int choice;
        do {
            System.out.println("""
                    0. Back to Main Menu
                    1. Add Vehicle
                    2. Show Vehicles
                    3. Update Vehicle
                    4. Remove Vehicle""");

            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

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
                default:
                    System.out.println("Invalid choice!");
            }
            System.out.println();

        } while (!quit);
    }

    public void addCar(Scanner scanner) {
        String powerSource = getRequiredInput(scanner, "power source (gasoline/diesel/electric/hybrid)");
        String vehicleClass = getRequiredInput(scanner, "car class (SUV/Sedan/Van/Coupe/Truck)");
        String brand = getRequiredInput(scanner, "brand");
        String model = getRequiredInput(scanner, "model");

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        String vehicleLicence = getRequiredInput(scanner, "car licence (e.g. VL-01-AB-1234)");
        String licencePlate = getRequiredInput(scanner, "licence plate (e.g. PP-1000)");

        IVehicle newCar = new Car("Car",powerSource, vehicleClass, brand, model, price, vehicleLicence, licencePlate);

        garage.add(newCar);
        System.out.println("Add car successfully. Total cars: " + Car.getCountCar());
        vehicleCount++;
        System.out.println("vehicleCount: " + vehicleCount);
    }

    public void addMoto(Scanner scanner) {
        String powerSource = getRequiredInput(scanner, "power source (gasoline/diesel/electric/hybrid)");
        String vehicleClass = getRequiredInput(scanner, "Moto class (Sport/Cruiser/Touring)");
        String brand = getRequiredInput(scanner, "brand");
        String model = getRequiredInput(scanner, "model");

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        String vehicleLicence = getRequiredInput(scanner, "Moto licence (e.g. VL-01-AB-1234)");
        String licencePlate = getRequiredInput(scanner, "licence plate (e.g. PP-1000)");

        IVehicle newMoto = new Moto("Moto",powerSource, vehicleClass, brand, model, price, vehicleLicence, licencePlate);

        garage.add(newMoto);
        System.out.println("Add moto successfully. Total motos: " + Moto.getCountMoto());
        vehicleCount++;
        System.out.println("vehicleCount: " + vehicleCount);
    }

    public void addVehicle(Scanner scanner) {

        if (!loggedInStaff.can(MANAGE_VEHICLE)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }

        while (true) {
            System.out.print("Enter vehicle type (Car or Moto): ");
            String vehicleType = scanner.nextLine().trim();
            if (vehicleType.equalsIgnoreCase("Car")) {
                addCar(scanner);
                break;
            } else if (vehicleType.equalsIgnoreCase("Moto")) {
                addMoto(scanner);
                break;
            } else {
                System.out.println("Invalid vehicle type. Please enter 'Car' or 'Moto'.");
            }
        }
        
        
    }

    public void showVehicle() {
        if (!loggedInStaff.can(VIEW_VEHICLE)) {
            System.out.println("Access denied: insufficient permissions.");
            return;
        }
        if (vehicleCount == 0) {
            System.out.println("Garage is empty!");
            return;
        }
        for(IVehicle car : garage) {
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

        System.out.print("Enter vehicle ID (number): ");
        int idInput = scanner.nextInt();
        scanner.nextLine();
        String codeInput = getRequiredInput(scanner, "vehicle code (e.g. Car-1, Moto-2)");

        IVehicle target = findVehicleByIdAndCode(idInput, codeInput);
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
            System.out.println("Garage is Empty!");
            return;
        }
        System.out.print("Enter vehicle ID (number): ");
        int idInput = scanner.nextInt();
        scanner.nextLine();
        String codeInput = getRequiredInput(scanner, "vehicle code (e.g. Car-1, Moto-2)");

        IVehicle item = findVehicleByIdAndCode(idInput, codeInput);
        if (item == null) {
            System.out.println("Vehicle not found! ID and code must both match the same vehicle.");
            return;
        }
        {
            boolean quit = false;
                int choice;
                do {
                    System.out.println("""
                            Update car:
                            0. Back to Vehicle Management
                            1. powerSource
                            2. Vehicle Class
                            3. Brand
                            4. Model
                            5. Price
                            6. Status""");

                    System.out.print("Enter choice: ");
                    choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

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
                            System.out.print("New Price: ");
                            double rentalRatePerDay = scanner.nextDouble();
                            item.setRentalRatePerDay(rentalRatePerDay);
                            scanner.nextLine();
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
                                System.out.print("Update status(true/false): ");
                                boolean status = scanner.nextBoolean();
                                item.setAvailable(status);
                                scanner.nextLine();
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
    private IVehicle findVehicleByIdAndCode(int id, String code) {
        for (IVehicle v : garage) {
            if (v != null && v.getVehicleId() == id
                    && v.getVehicleCode().equals(code.trim())) {
                return v;
            }
        }
        return null;
    }

    //helper function
    public String getRequiredInput(Scanner scanner, String fieldName) {
        String input = "";
        while (input.trim().isEmpty()) {
            System.out.print("Enter " + fieldName + " (required): ");
            input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                System.out.println(fieldName + " cannot be empty!");
            }
        }
        return input;
    }

    public IVehicle findVehicleByID(Scanner scanner) {
        String code = getRequiredInput(scanner, "vehicle code (e.g. Car-1, Moto-2)");
        return getVehicleByCode(code);
    }

    /** Look up a vehicle by its global numeric ID (1, 2, 3, …). */
    public IVehicle getVehicleByID(int id) {
        for (IVehicle v : garage) {
            if (v.getVehicleId() == id) {
                return v;
            }
        }
        return null;
    }

    /** Look up a vehicle by its type-based code (e.g. "Car-1", "Moto-2"). */
    public IVehicle getVehicleByCode(String code) {
        for (IVehicle v : garage) {
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
            { "Eno Gonzalez", "D5678901", "0972345683", "IDCard.jpg", "DriverLicense.jpg" }
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

        System.out.println("Customer Management:");
        boolean quit = false;
        int choice;
        do {
            System.out.println("""
                    0. Back to Main Menu
                    1. Add Customer
                    2. Show Customers
                    3. Update Customer
                    4. Remove Customer""");

            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

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
        System.out.print("Enter customer Name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter customer ID Number: ");
        String customerIdNum = scanner.nextLine();

        System.out.print("Enter customer Phone: ");
        String customerPhone = scanner.nextLine();

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
        System.out.print("Enter customer ID(int): ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline
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

                    System.out.print("Enter choice: ");
                    choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    switch (choice) {
                        case 0:
                            quit = true;
                            break;
                        case 1:
                            System.out.print("New ID Card: ");
                            item.setcustomerIdNum(scanner.nextLine());
                            break;

                        case 2:
                            System.out.print("New Name: ");
                            item.setCustomerName(scanner.nextLine());
                            break;
                        case 3:
                            System.out.print("New Phone: ");
                            item.setCustomerPhone(scanner.nextLine(), customers);
                            break;
                        case 4:
                            System.out.print("New ID Card Photo: ");
                            item.setIDCardPhoto(scanner.nextLine());
                            break;
                        case 5:
                            System.out.print("New Driver License Photo: ");
                            item.setDriverLicensePhoto(scanner.nextLine());
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

        System.out.print("Enter customer ID(int): ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

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
        System.out.print("Enter customer ID(int): ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

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

        System.out.println("Rent Management:");
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

            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 

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
        while (true) {
            System.out.print("Enter number of days(int): ");
            rentDays = scanner.nextInt();
            scanner.nextLine(); 
            if (rentDays > 0) {
                break;
            } else {
                System.out.println("Rent days must be greater than 0. Please try again.");
            }
        }

        IVehicle selectedCar = findVehicleByID(scanner);
        Customer selectedCustomer = findCustomerByID(scanner);

        if (selectedCar == null) {
            System.out.println("Vehicle not found!");
            return;
        }
        if (selectedCustomer == null) {
            System.out.println("Customer not found!");
            return;
        }
        if (!selectedCar.isAvailable()) {
            System.out.println("Vehicle is not available for rent!");
            return;
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

        double vehiclePrice = selectedCar.getRentalRatePerDay(); // snapshot car price
        Rent newRent = new Rent(selectedCar, selectedCustomer, rentDays, startDate, endDate); // create rent without payment first

        System.out.print("Enter deposit amount: ");
        double deposit = scanner.nextDouble();
        scanner.nextLine();
        Payment payment = new Payment(newRent.getRentDays(), vehiclePrice, deposit); // create payment with deposit and base price

        newRent.setPayment(payment); // add payment to rent

        selectedCar.setAvailable(false); // mark car as unavailable

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
        System.out.print("Enter rent ID(int): ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

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
                    System.out.print("Enter choice: ");
                    choice = scanner.nextInt();
                    scanner.nextLine(); 

                    switch (choice) {
                        case 0 -> quit = true;
                        case 1 -> {
                            System.out.print("New Rent Days: ");
                            int newRentDays = scanner.nextInt();
                            scanner.nextLine();

                            if (newRentDays <= 0) {
                                System.out.println("Rent days must be greater than 0.");
                                break;
                            }

                            if (newRentDays == rent.getRentDays()) {
                                System.out.println("Rent days is the same as before. No update needed.");
                                break;
                            }

                            System.out.print("Warning: Changing rent days may affect the total price. Confirm (yes/no): ");
                            String confirm = scanner.nextLine();
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
                            IVehicle newCar = findVehicleByID(scanner);
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
        System.out.print("Enter rent ID(int): ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

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
                             
                            Are you sure you want to remove this rent? (yes/no): 
                            """);
        String confirm = scanner.nextLine();
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
        System.out.print("Enter rent ID(int): ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

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
                // original vehicle from garage
                IVehicle car = getVehicleByID(rent.getVehicle().getVehicleId());
                if (!car.equals(rent.getVehicle())) {
                    System.out.println("Vehicle mismatch");
                    return;
                }

                System.out.print("Enter payment method: ");
                String paymentMethod = scanner.nextLine();

                // if there's discount or extra day or damage
                System.out.print("Update special case(true/false): ");
                boolean update = scanner.nextBoolean();
                scanner.nextLine(); // consume newline after boolean
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
        System.out.print("Enter rent ID(int): ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

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

        System.out.print("Enter discount (0 if none): ");
        double discount = scanner.nextDouble();
        scanner.nextLine();
        payment.setDiscount(discount);

        System.out.print("Enter extra days (0 if none): ");
        int extraDays = scanner.nextInt();
        scanner.nextLine();
        payment.setExtraDays(extraDays);

        System.out.print("Enter damage fee (0 if none): ");
        double damageFee = scanner.nextDouble();
        scanner.nextLine();
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
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
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

        System.out.print("Enter rent ID to look up: ");
        int searchId = scanner.nextInt();
        scanner.nextLine();

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
        for (IVehicle v : garage) {
            if (v.getVehicleType().equals("Car")) {
                totalCars++;
                if (v.isAvailable()) availableCars++;
            } else if (v.getVehicleType().equals("Moto")) {
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
            int topCustomerId = -1, topCustomerCount = 0;
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
        for (IVehicle v : garage) {
            if (v.isAvailable()) availableVehicles++;
        }

        int activeRents = 0;
        for (Rent r : rents) {
            if (r.isStatus()) activeRents++;
        }

        System.out.println("\n========== GARAGE DASHBOARD ==========");
        System.out.println("Logged in as : " + (loggedInStaff != null ? loggedInStaff.getName() + " (" + loggedInStaff.getRole() + ")" : "N/A"));
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