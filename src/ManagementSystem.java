import java.util.Scanner;

class ManagementSystem {
    Vehicle[] garage;
    int garageSize;
    int vehicleCount;
    String[][] defaultCars = {
            { "gasoline", "SUV", "Ford", "Escape", "300" },
            { "electric", "Sedan", "Tesla", "Model 3", "500" },
            { "diesel", "Truck", "Toyota", "Hilux", "400" },
            { "hybrid", "Hatchback", "Honda", "Insight", "350" },
            { "gasoline", "Coupe", "BMW", "M4", "600" }
    };

    Customer[] customers;
    int customerSize;
    int customerCount;
    String[][] defaultCustomers = {
            { "Aruna Smith", "D7654321", "0662345679" },
            { "Bona Johnson", "D2345678", "0122345680" },
            { "Champa Brown", "D3456789", "0172345681" },
            { "Diana Prince", "D4567890", "0882345682" },
            { "Eno Gonzalez", "D5678901", "0972345683" }
    };

    Rent[] rents;
    int rentSize;
    int rentCount;

    ManagementSystem(int maxSize) {
        this.garage = new Vehicle[maxSize];
        this.garageSize = maxSize;
        this.vehicleCount = 0;
        generateDefaultVehicles();

        this.customers = new Customer[maxSize];
        this.customerSize = maxSize;
        this.customerCount = 0;
        generateDefaultCustomers();

        this.rents = new Rent[maxSize];
        this.rentSize = maxSize;
        this.rentCount = 0;
    }

    // Helper: prompt for ID
    int promptForId(Scanner scanner, String entityName) {
        System.out.print("Enter " + entityName + " ID(int): ");
        int id = scanner.nextInt();
        scanner.nextLine();
        return id;
    }

    // Helper: find vehicle by ID value
    Vehicle findVehicleById(int id) {
        for (int i = 0; i < vehicleCount; i++) {
            if (garage[i].vehicleId == id)
                return garage[i];
        }
        return null;
    }

    // Helper: find customer by ID value
    Customer findCustomerById(int id) {
        for (int i = 0; i < customerCount; i++) {
            if (customers[i].customerId == id)
                return customers[i];
        }
        return null;
    }

    // Helper: find rent by ID value
    Rent findRentById(int id) {
        for (int i = 0; i < rentCount; i++) {
            if (rents[i].rentId == id)
                return rents[i];
        }
        return null;
    }

    // Helper: check if vehicle is rented
    boolean isVehicleRented(int vehicleId) {
        for (int i = 0; i < rentCount; i++) {
            if (rents[i] != null && rents[i].vehicle != null && rents[i].vehicle.vehicleId == vehicleId) {
                return true;
            }
        }
        return false;
    }

    // Helper: check if customer has active rent
    boolean hasCustomerActiveRent(int customerId) {
        for (int i = 0; i < rentCount; i++) {
            if (rents[i] != null && rents[i].customer != null && rents[i].customer.customerId == customerId) {
                return true;
            }
        }
        return false;
    }

    // Generate default vehicles
    void generateDefaultVehicles() {
        for (String[] car : defaultCars) {
            if (vehicleCount >= garageSize)
                break;
            garage[vehicleCount++] = new Vehicle(car[0], car[1], car[2], car[3], Double.parseDouble(car[4]));
        }
    }

    // Generate default customers
    void generateDefaultCustomers() {
        for (String[] cust : defaultCustomers) {
            if (customerCount >= customerSize)
                break;
            customers[customerCount++] = new Customer(cust[0], cust[1], cust[2]);
        }
    }

    // Vehicle Management
    void vehicleManagement(Scanner scanner) {
        System.out.println("Vehicle Management:");
        boolean quit = false;
        while (!quit) {
            System.out.println("""
                    0.Quit
                    1. Add Vehicle
                    2. Show Vehicles
                    3. Update Vehicle
                    4. Remove Vehicle""");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0 -> quit = true;
                case 1 -> addVehicle(scanner);
                case 2 -> showVehicles();
                case 3 -> updateVehicle(scanner);
                case 4 -> removeVehicle(scanner);
                default -> System.out.println("Invalid choice!");
            }
            System.out.println();
        }
    }

    void addVehicle(Scanner scanner) {
        if (vehicleCount >= garageSize) {
            System.out.println("Garage is full!");
            return;
        }
        System.out.print("Enter powerSource (Electric, Gasoline): ");
        String powerSource = scanner.nextLine();
        System.out.print("Enter vehicle class (SUV, Sedan, Van): ");
        String vehicleClass = scanner.nextLine();
        System.out.print("Enter brand: ");
        String brand = scanner.nextLine();
        System.out.print("Enter model: ");
        String model = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        garage[vehicleCount++] = new Vehicle(powerSource, vehicleClass, brand, model, price);
        System.out.println("Vehicle added successfully.");
    }

    void showVehicles() {
        if (vehicleCount == 0) {
            System.out.println("Garage is empty!");
            return;
        }
        for (int i = 0; i < vehicleCount; i++) {
            System.out.println(garage[i].toString());
        }
    }

    void removeVehicle(Scanner scanner) {
        if (vehicleCount == 0) {
            System.out.println("No vehicle to remove!");
            return;
        }
        int id = promptForId(scanner, "vehicle");
        if (isVehicleRented(id)) {
            System.out.println("Vehicle is currently rented and cannot be removed.");
            return;
        }

        int index = -1;
        for (int i = 0; i < vehicleCount; i++) {
            if (garage[i].vehicleId == id) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("Vehicle not found.");
            return;
        }

        for (int i = index; i < vehicleCount - 1; i++) {
            garage[i] = garage[i + 1];
        }
        garage[--vehicleCount] = null;
        System.out.println("Vehicle removed successfully.");
    }

    void updateVehicle(Scanner scanner) {
        if (vehicleCount == 0) {
            System.out.println("Garage is empty!");
            return;
        }
        int id = promptForId(scanner, "vehicle");
        Vehicle item = findVehicleById(id);
        if (item == null) {
            System.out.println("Vehicle not found!");
            return;
        }

        boolean quit = false;
        while (!quit) {
            System.out.println("""
                    Update vehicle:
                    0.Quit  1.PowerSource  2.Class  3.Brand  4.Model  5.Price  6.Status""");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0 -> quit = true;
                case 1 -> {
                    System.out.print("New powerSource: ");
                    item.powerSource = scanner.nextLine();
                }
                case 2 -> {
                    System.out.print("New Class: ");
                    item.vehicleClass = scanner.nextLine();
                }
                case 3 -> {
                    System.out.print("New Brand: ");
                    item.vehicleBrand = scanner.nextLine();
                }
                case 4 -> {
                    System.out.print("New Model: ");
                    item.vehicleModel = scanner.nextLine();
                }
                case 5 -> {
                    System.out.print("New Price: ");
                    item.rentalRatePerDay = scanner.nextDouble();
                    item.rentalRatePerDay = item.rentalRatePerDay>0 ? item.rentalRatePerDay : 0;
                    scanner.nextLine();
                }
                case 6 -> {
                    if (isVehicleRented(id)) {
                        System.out.println("Cannot change status - vehicle is currently rented!");
                    } else {
                        System.out.print("Update status(true/false): ");
                        item.isAvailable = scanner.nextBoolean();
                        scanner.nextLine();
                    }
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    Vehicle findVehicleByID(Scanner scanner) {
        return findVehicleById(promptForId(scanner, "vehicle"));
    }

    // Customer Management
    void customerManagement(Scanner scanner) {
        System.out.println("Customer Management:");
        boolean quit = false;
        while (!quit) {
            System.out.println("""
                    0.Quit
                    1. Add Customer
                    2. Show Customers
                    3. Update Customer
                    4. Remove Customer""");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0 -> quit = true;
                case 1 -> addCustomer(scanner);
                case 2 -> showCustomers();
                case 3 -> updateCustomer(scanner);
                case 4 -> removeCustomer(scanner);
                default -> System.out.println("Invalid choice!");
            }
            System.out.println();
        }
    }

    void addCustomer(Scanner scanner) {
        if (customerCount >= customerSize) {
            System.out.println("Customer list is full!");
            return;
        }
        System.out.print("Enter customer ID Card: ");
        String idCard = scanner.nextLine();
        System.out.print("Enter customer Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter customer Phone: ");
        String phone = scanner.nextLine();

        customers[customerCount++] = new Customer(name, idCard, phone);
        System.out.println("Customer added successfully.");
    }

    void showCustomers() {
        if (customerCount == 0) {
            System.out.println("No customers!");
            return;
        }
        for (int i = 0; i < customerCount; i++) {
            System.out.println(customers[i].toString());
        }
    }

    void updateCustomer(Scanner scanner) {
        if (customerCount == 0) {
            System.out.println("No customers!");
            return;
        }
        int id = promptForId(scanner, "customer");
        Customer item = findCustomerById(id);
        if (item == null) {
            System.out.println("Customer not found!");
            return;
        }

        boolean quit = false;
        while (!quit) {
            System.out.println("""
                    Update customer:
                    0.Quit  1.ID Card  2.Name  3.Phone""");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0 -> quit = true;
                case 1 -> {
                    System.out.print("New ID Card: ");
                    item.customerIdCard = scanner.nextLine();
                }
                case 2 -> {
                    System.out.print("New Name: ");
                    item.customerName = scanner.nextLine();
                }
                case 3 -> {
                    System.out.print("New Phone: ");
                    item.customerPhone = scanner.nextLine();
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    void removeCustomer(Scanner scanner) {
        if (customerCount == 0) {
            System.out.println("No customer to remove!");
            return;
        }
        int id = promptForId(scanner, "customer");
        if (hasCustomerActiveRent(id)) {
            System.out.println("Customer has active rent and cannot be removed.");
            return;
        }

        int index = -1;
        for (int i = 0; i < customerCount; i++) {
            if (customers[i].customerId == id) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("Customer not found.");
            return;
        }

        for (int i = index; i < customerCount - 1; i++) {
            customers[i] = customers[i + 1];
        }
        customers[--customerCount] = null;
        System.out.println("Customer removed successfully.");
    }

    Customer findCustomerByID(Scanner scanner) {
        return findCustomerById(promptForId(scanner, "customer"));
    }

    // Rent Management
    void rentManagement(Scanner scanner) {
        System.out.println("Rent Management:");
        boolean quit = false;
        while (!quit) {
            System.out.println("""
                    0.Quit
                    1. Add Rent
                    2. Show Rents
                    3. Update Rent
                    4. Remove Rent
                    5. Return Vehicle""");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
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
        }
    }

    void addRent(Scanner scanner) {
        if (rentCount >= rentSize) {
            System.out.println("Rent list is full!");
            return;
        }
        System.out.print("Enter number of days: ");
        int rentDays = scanner.nextInt();
        scanner.nextLine();

        Vehicle vehicle = findVehicleByID(scanner);
        Customer customer = findCustomerByID(scanner);

        if (vehicle == null) {
            System.out.println("Vehicle not found!");
            return;
        }
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }
        if (!vehicle.isAvailable) {
            System.out.println("Vehicle is not available!");
            return;
        }
        if (hasCustomerActiveRent(customer.customerId)) {
            System.out.println("Customer already has an active rent!");
            return;
        }

        vehicle.isAvailable = false;
        rents[rentCount++] = new Rent(vehicle, customer, rentDays);
        System.out.println("Rent added successfully.");
    }

    void showRents() {
        if (rentCount == 0) {
            System.out.println("No rents!");
            return;
        }
        for (int i = 0; i < rentCount; i++) {
            System.out.println(rents[i].toString());
        }
    }

    void updateRent(Scanner scanner) {
        if (rentCount == 0) {
            System.out.println("No rents!");
            return;
        }
        int id = promptForId(scanner, "rent");
        Rent item = findRentById(id);
        if (item == null) {
            System.out.println("Rent not found!");
            return;
        }

        boolean quit = false;
        while (!quit) {
            System.out.println("""
                    Update rent:
                    0.Quit  1.Rent Days  2.Vehicle  3.Customer""");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0 -> quit = true;
                case 1 -> {
                    System.out.print("New Rent Days: ");
                    item.rentDays = scanner.nextInt();
                    scanner.nextLine();
                }
                case 2 -> {
                    Vehicle newVehicle = findVehicleByID(scanner);
                    if (newVehicle == null)
                        System.out.println("Vehicle not found!");
                    else if (!newVehicle.isAvailable)
                        System.out.println("Vehicle not available!");
                    else {
                        if (item.vehicle != null)
                            item.vehicle.isAvailable = true;
                        newVehicle.isAvailable = false;
                        item.vehicle = newVehicle;
                        System.out.println("Vehicle updated.");
                    }
                }
                case 3 -> {
                    Customer newCustomer = findCustomerByID(scanner);
                    if (newCustomer == null)
                        System.out.println("Customer not found!");
                    else {
                        item.customer = newCustomer;
                        System.out.println("Customer updated.");
                    }
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    void removeRent(Scanner scanner) {
        if (rentCount == 0) {
            System.out.println("No rent to remove!");
            return;
        }
        int id = promptForId(scanner, "rent");

        int index = -1;
        for (int i = 0; i < rentCount; i++) {
            if (rents[i].rentId == id) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("Rent not found!");
            return;
        }

        if (rents[index].vehicle != null) {
            rents[index].vehicle.isAvailable = true;
        }

        for (int i = index; i < rentCount - 1; i++) {
            rents[i] = rents[i + 1];
        }
        rents[--rentCount] = null;
        System.out.println("Rent removed successfully.");
    }

    void returnVehicle(Scanner scanner) {
        if (rentCount == 0) {
            System.out.println("No rents!");
            return;
        }
        int id = promptForId(scanner, "rent");
        Rent item = findRentById(id);
        if (item == null) {
            System.out.println("Rent not found!");
            return;
        }
        if (item.vehicle == null) {
            System.out.println("Rent has no vehicle!");
            return;
        }
        if (item.payment != null) {
            System.out.println("Already paid.");
            return;
        }

        System.out.print("Enter discount (0 if none): ");
        item.discount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter extra days (0 if none): ");
        item.extraDays = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter damage fee (0 if none): ");
        item.damageFee = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter payment method: ");
        String paymentMethod = scanner.nextLine();

        item.payment = new Payment(item, paymentMethod);
        item.vehicle.isAvailable = true;
        System.out.println("Payment: $" + item.payment.finalTotalPayment());
        System.out.println("Vehicle returned and now available.");
    }

    Rent findRentByID(Scanner scanner) {
        return findRentById(promptForId(scanner, "rent"));
    }
    // Rental Agreements Management will be added later - as part of Rent Management
    // Invoice Management will be added later - as part of Payment Management
    // Record Management will be added later - as part of Rent Management or TBD
    // Report Management & Other Management will be added later - TBD
}