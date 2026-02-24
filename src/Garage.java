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

    // =========================
    //  LOGIN DEPENDENCY
    // =========================
    private IStaff loggedInStaff;   // null = no staff login

    // =========================
    // 4) FEEDBACK MESSAGE
    // =========================
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

        loggedInStaff = null;
        lastMessage = "Garage created. No staff logged in.";

    }

    // =========================
    // GETTERS / SETTERS
    // =========================
    public String getLastMessage() { return lastMessage; }
    public boolean isStaffLoggedIn() { return loggedInStaff != null; }
    public IStaff getLoggedInStaff() { return loggedInStaff; }

    private void setLastMessage(String msg) {
        lastMessage = msg;
    }

    // Staff Management

    public void generateStaffToSystem() {
        IStaff s1 = new ManagerStaff("Admin", "Manager", 0, "admin_root", "root123");
        IStaff s2 = new ManagerStaff("Bob", "Manager", 3000, "bob_manager", "manager123");
        IStaff s3 = new Staff("Chan", "Staff", 1500, "chan_staff", "staff123");
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
            scanner.nextLine(); // consume newline

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
        String name = scanner.nextLine();

        System.out.print("Enter staff role: ");
        String role = scanner.nextLine();

        System.out.print("Enter staff salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine(); 

        System.out.print("Enter staff username: ");
        String username = scanner.nextLine();

        System.out.print("Enter staff password: ");
        String password = scanner.nextLine();

        IStaff newStaff = switch (role.trim().toUpperCase()) {
            case "MANAGER", "ADMIN" -> new ManagerStaff(name, role, salary, username, password);
            case "STAFF" -> new Staff(name, role, salary, username, password);
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
                            3. Salary""");

                    System.out.print("Enter choice: ");
                    choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    switch (choice) {
                        case 0 -> quit = true;
                        case 1 -> {
                            System.out.print("New Name: ");
                            staff.setName(scanner.nextLine());
                        }
                        case 2 -> {
                            System.out.print("New Role: ");
                            staff.setRole(scanner.nextLine());
                        }
                        case 3 -> {
                            System.out.print("New Salary: ");
                            staff.setSalary(scanner.nextDouble());
                            scanner.nextLine();
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
            staffs.remove(staffToRemove);
            staffCount--;
            System.out.println("Staff with ID " + id + " removed successfully.");
            System.out.println("staffCount: " + staffCount);
        } else {
            System.out.println("Staff with ID " + id + " not found.");
        }
    }

    // Car Management

    public void generateVehicleToGarage() {
        String[][] cars = {
            { "gasoline", "SUV", "Ford", "Escape", "300", "VL-01-AB-1234", "PP-1000" },
            { "electric", "Sedan", "Tesla", "Model 3", "500", "VL-02-CD-5678", "PP-1001" },
            { "diesel", "Truck", "Toyota", "Hilux", "400", "VL-03-EF-9012", "PP-1002" },
            { "hybrid", "Hatchback", "Honda", "Insight", "350", "VL-04-GH-3456", "PP-1003" },
            { "gasoline", "Coupe", "BMW", "M4", "600", "VL-05-IJ-7890", "PP-1004" }
        };
        // if (count >= garage.size()) {
        //     System.out.println("Garage is full! Cannot add new car.");
        //     return;
        // }
        for (String[] car : cars) {
            // if (count >= garage.size()) {
            //     System.out.println("Not enough space to add all predefined cars.");
            //     break;
            // }
            Car newCar = new Car(car[0], car[1], car[2], car[3], Double.parseDouble(car[4]), car[5],
                    car[6]);
            garage.add(newCar);
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
        String powerSource = getRequiredInput(scanner, "powerSource");

        System.out.print("Enter car class ( SUV, Sedan, Van): ");
        String vehicleClass = getRequiredInput(scanner, "car class");

        System.out.print("Enter brand: ");
        String brand = getRequiredInput(scanner, "brand");

        System.out.print("Enter model: ");
        String model = getRequiredInput(scanner, "model");

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        System.out.print("Enter car licence (e.g., DL-01-AB-1234): ");
        String vehicleLicence = getRequiredInput(scanner, "car licence");

        System.out.print("Enter licence plate (e.g., PP-1000): ");
        String licencePlate = getRequiredInput(scanner, "licence plate");

        IVehicle newCar = new Car(powerSource, vehicleClass, brand, model, price, vehicleLicence, licencePlate);

        garage.add(newCar);
        System.out.println("Add car successfully. Total cars: " + Car.getCountCarId());
        vehicleCount++;
        System.out.println("vehicleCount: " + vehicleCount);
    }

    public void addMoto(Scanner scanner) {
        System.out.println("Moto is not supported in this version.");
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

        String carID = getRequiredInput(scanner, "car ID");

        for (Rent rent : rents) {
            if (rent != null && rent.getVehicle() != null && rent.getVehicle().getVehicleId().equals(carID)) {
                System.out.println("Vehicle is currently rented and cannot be removed.");
                return;
            }
        }

        boolean removed = garage.removeIf(v -> v.getVehicleId().equals(carID));

        if (removed) {
            System.out.println("Vehicle with ID " + carID + " removed successfully.");
        } else {
            System.out.println("Vehicle with ID " + carID + " not found.");
        }

        vehicleCount--;

        System.out.println("Vehicle with ID " + carID + " removed successfully.");
        System.out.println("vehicleCount: " + vehicleCount );
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
        String carID = getRequiredInput(scanner, "car ID");

        for (IVehicle item:  garage) {
            if (item.getVehicleId().equals(carID)) {
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
                                if (rent != null && rent.getVehicle().getVehicleId().equals(carID)) {
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
                return;
            }
        }
        System.out.println("Vehicle not found!");
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

    public Car findVehicleByID(Scanner scanner) {
        String carID = getRequiredInput(scanner, "car ID");

        for (IVehicle v : garage) {
            if (v.getVehicleId().equals(carID)) {
                return (Car) v;
            }
        }
        return null;
    }

    public Car getVehicleByID(String id) {
        for (IVehicle v : garage) {
            if (v.getVehicleId().equals(id)) {
                return (Car) v;
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

        Car selectedCar = findVehicleByID(scanner);
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
                            Car newCar = findVehicleByID(scanner);
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
                // original car from garage
                Car car = getVehicleByID(rent.getVehicle().getVehicleId());
                if(!car.equals(rent.getVehicle())){
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
                        "Vehicle with ID " + rent.getVehicle().getVehicleId() + " has been returned and is now available.");
                System.out.println();
                rent.setStatus(false);
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

    // Other Management
}