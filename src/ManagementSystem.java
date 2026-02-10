import java.util.Scanner;

public class ManagementSystem {
    Vehicle[] garage;
    int garageSize; // capacity
    int count; // current number of vehicles
    String[][] cars = {
            { "gasoline", "SUV", "Ford", "Escape", "300", "VL-01-AB-1234", "PP-1000" },
            { "electric", "Sedan", "Tesla", "Model 3", "500", "VL-02-CD-5678", "PP-1001" },
            { "diesel", "Truck", "Toyota", "Hilux", "400", "VL-03-EF-9012", "PP-1002" },
            { "hybrid", "Hatchback", "Honda", "Insight", "350", "VL-04-GH-3456", "PP-1003" },
            { "gasoline", "Coupe", "BMW", "M4", "600", "VL-05-IJ-7890", "PP-1004" }
    };

    Customer[] customers;
    int customerSize;
    int customerCount;
    String[][] custs = {
            { "Aruna Smith", "D7654321", "0662345679", "IDCard.jpg", "DriverLicense.jpg" },
            { "Bona Johnson", "D2345678", "0122345680", "IDCard.jpg", "DriverLicense.jpg" },
            { "Champa Brown", "D3456789", "0172345681", "IDCard.jpg", "DriverLicense.jpg" },
            { "Diana Prince", "D4567890", "0882345682", "IDCard.jpg", "DriverLicense.jpg" },
            { "Eno Gonzalez", "D5678901", "0972345683", "IDCard.jpg", "DriverLicense.jpg" }
    };

    Rent[] rents;
    int rentSize;
    int rentCount;

    public ManagementSystem(int maxSize) {
        // Initialize garage
        this.garage = new Vehicle[maxSize];
        this.garageSize = maxSize;
        this.count = 0;
        generateVehicleToGarage();
        // Initialize customer list
        this.customers = new Customer[maxSize];
        this.customerSize = maxSize;
        this.customerCount = 0;
        generateCustomerToSystem();
        // Initialize rent list
        this.rents = new Rent[maxSize];
        this.rentSize = maxSize;
        this.rentCount = 0;
    }

    // Vehicle Management

    public void generateVehicleToGarage() {
        if (count >= garage.length) {
            System.out.println("Garage is full! Cannot add new car.");
            return;
        }
        for (String[] car : cars) {
            if (count >= garage.length) {
                System.out.println("Not enough space to add all predefined cars.");
                break;
            }
            Vehicle newVehicle = new Vehicle(car[0], car[1], car[2], car[3], Double.parseDouble(car[4]), car[5],
                    car[6]);
            garage[count++] = newVehicle;
        }
    }

    public void vehicleManagement(Scanner scanner) {
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

    public void addVehicle(Scanner scanner) {
        if (count >= garage.length) {
            System.out.println("Garage is full! Cannot add new car.");
            return;
        }
        // Take inputs
        System.out.print("Enter powerSource ( Electric, Gasoline): ");
        String powerSource = scanner.nextLine();

        System.out.print("Enter vehicle class ( SUV, Sedan, Van): ");
        String vehicleClass = scanner.nextLine();

        System.out.print("Enter brand: ");
        String brand = scanner.nextLine();

        System.out.print("Enter model: ");
        String model = scanner.nextLine();

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        System.out.print("Enter vehicle licence (e.g., DL-01-AB-1234): ");
        String vehicleLicence = scanner.nextLine();

        System.out.print("Enter licence plate (e.g., PP-1000): ");
        String licencePlate = scanner.nextLine();

        Vehicle newVehicle = new Vehicle(powerSource, vehicleClass, brand, model, price, vehicleLicence, licencePlate);

        garage[count++] = newVehicle;
        System.out.println("Add vehicle successfully.");
        System.out.println("count: " + count);
    }

    public void showVehicle() {
        if (count == 0) {
            System.out.println("Garage is empty!");
            return;
        }
        for (int i = 0; i < count; i++) {
            System.out.println(garage[i].toString());
        }
        System.out.println();
    }

    public void removeVehicle(Scanner scanner) {
        if (count == 0) {
            System.out.println("No vehicle to remove!");
            return;
        }

        System.out.print("Enter vehicle ID(int): ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        for (int i = 0; i < rentCount; i++) {
            if (rents[i] != null && rents[i].getVehicle() != null && rents[i].getVehicle().getVehicleId() == id) {
                System.out.println("Vehicle is currently rented and cannot be removed.");
                return;
            }
        }

        int index = -1;
        for (int i = 0; i < count; i++) {
            if (garage[i].getVehicleId() == id) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("Vehicle with ID " + id + " not found.");
            return;
        }

        for (int i = index; i < count - 1; i++) {
            garage[i] = garage[i + 1];
        }

        garage[count - 1] = null;
        count--;

        System.out.println("Vehicle with ID " + id + " removed successfully.");
        System.out.println("count: " + count);
    }

    public void updateVehicle(Scanner scanner) {
        if (count == 0) {
            System.out.println("Garage is Empty!");
            return;
        }
        System.out.print("Enter vehicle ID(int): ");
        int id = scanner.nextInt();
        for (int i = 0; i < count; i++) {
            Vehicle item = garage[i];
            if (item.getVehicleId() == id) {
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
                            // Check if vehicle is currently rented
                            boolean isRented = false;
                            for (int j = 0; j < rentCount; j++) {
                                if (rents[j] != null && rents[j].getVehicle().getVehicleId() == id) {
                                    isRented = true;
                                    break;
                                }
                            }
                            if (isRented) {
                                System.out.println("Cannot change status - vehicle is currently rented!");
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
    public Vehicle findVehicleByID(Scanner scanner) {
        System.out.print("Enter vehicle ID(int): ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        for (int i = 0; i < count; i++) {
            if (garage[i].getVehicleId() == id)
                return garage[i];
        }
        return null;
    }
    public Vehicle getVehicleByID(int id) {
        for (int i = 0; i < count; i++) {
            if (garage[i].getVehicleId() == id)
                return garage[i];
        }
        return null;
    }

    // Customer Management

    public void generateCustomerToSystem() {
        if (customerCount >= customers.length) {
            System.out.println("Customer list is full! Cannot add new customer.");
            return;
        }
        for (String[] cust : custs) {
            Customer newCustomer = new Customer(cust[0], cust[1], cust[2], cust[3], cust[4]);
            customers[customerCount++] = newCustomer;
        }
    }

    public void customerManagement(Scanner scanner) {
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
        if (customerCount >= customers.length) {
            System.out.println("Customer list is full! Cannot add new customer.");
            return;
        }
        // Take inputs
        System.out.print("Enter customer Name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter customer ID Card: ");
        String customerIdCard = scanner.nextLine();

        System.out.print("Enter customer Phone: ");
        String customerPhone = scanner.nextLine();

        Customer newCustomer = new Customer(customerName, customerIdCard, customerPhone);

        customers[customerCount++] = newCustomer;
        System.out.println("Add customer successfully.");
        System.out.println("customerCount: " + customerCount);
    }

    public void showCustomers() {
        if (customerCount == 0) {
            System.out.println("No customers!");
            return;
        }
        for (int i = 0; i < customerCount; i++) {
            System.out.println(customers[i].toString());
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
        for (int i = 0; i < customerCount; i++) {
            Customer item = customers[i];
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
                            item.setCustomerIdCard(scanner.nextLine());
                            break;

                        case 2:
                            System.out.print("New Name: ");
                            item.setCustomerName(scanner.nextLine());
                            break;
                        case 3:
                            System.out.print("New Phone: ");
                            item.setCustomerPhone(scanner.nextLine());
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
            for (int i = 0; i < rentCount; i++) {
                if (rents[i] != null && rents[i].getCustomer() != null && rents[i].getCustomer().getCustomerId() == id) {
                    System.out.println("Customer is associated with an active rent and cannot be removed.");
                    return;
                }
            }
        }

        int index = -1;
        for (int i = 0; i < customerCount; i++) {
            if (customers[i].getCustomerId() == id) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("Customer with ID " + id + " not found.");
            return;
        }

        for (int i = index; i < customerCount - 1; i++) {
            customers[i] = customers[i + 1];
        }

        customers[customerCount - 1] = null;
        customerCount--;

        System.out.println("Customer with ID " + id + " removed successfully.");
        System.out.println("customerCount: " + customerCount);
    }

    public Customer findCustomerByID(Scanner scanner) {
        System.out.print("Enter customer ID(int): ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        for (int i = 0; i < customerCount; i++) {
            if (customers[i].getCustomerId() == id)
                return customers[i];
        }
        return null;
    }

    // Rent Management

    public void rentManagement(Scanner scanner) {
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
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 0:
                    quit = true;
                    break;
                case 1:
                    addRent(scanner);
                    break;
                case 2:
                    showRents();
                    break;
                case 3:
                    updateRent(scanner);
                    break;
                case 4:
                    removeRent(scanner);
                    break;
                case 5:
                    returnVehicle(scanner);
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
            System.out.println();

        } while (!quit);
    }

    public void addRent(Scanner scanner) {

        if (rentCount >= rents.length) {
            System.out.println("Rent list is full! Cannot add new rent.");
            return;
        }
        // Take inputs
        int rentDays;
        while (true) {
            System.out.print("Enter number of days(int): ");
            rentDays = scanner.nextInt();
            scanner.nextLine(); // consume newline
            if (rentDays > 0) {
                break;
            } else {
                System.out.println("Rent days must be greater than 0. Please try again.");
            }
        }

        Vehicle selectedVehicle = findVehicleByID(scanner);
        Customer selectedCustomer = findCustomerByID(scanner);

        if (selectedVehicle == null) {
            System.out.println("Vehicle not found!");
            return;
        }
        if (selectedCustomer == null) {
            System.out.println("Customer not found!");
            return;
        }
        if (selectedVehicle.isAvailable()) {
            System.out.println("Vehicle is not available for rent!");
            return;
        }
        System.out.print("Enter start date: ");
        String startDate = scanner.nextLine();
        System.out.print("Enter end date: ");
        String endDate = scanner.nextLine();

        // snapshot vehicle price
        double vehiclePrice = selectedVehicle.getRentalRatePerDay();
        Rent newRent = new Rent(selectedVehicle, selectedCustomer, rentDays, startDate, endDate);

        System.out.print("Enter deposit amount: ");
        double deposit = scanner.nextDouble();
        scanner.nextLine(); // consume newline
        Payment payment = new Payment(newRent.getRentDays(), vehiclePrice, deposit);

        // add payment to rent
        newRent.setPayment(payment);

        // Mark vehicle as unavailable
        selectedVehicle.setAvailable(false);

        rents[rentCount++] = newRent;
        System.out.println("Add rent successfully.");
        System.out.println("rentCount: " + rentCount);
    }

    public void showRents() {
        if (rentCount == 0) {
            System.out.println("No rents!");
            return;
        }
        for (int i = 0; i < rentCount; i++) {
            System.out.println(rents[i].toString());
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
        scanner.nextLine(); // consume newline

        for (int i = 0; i < rentCount; i++) {
            Rent item = rents[i];
            if (item.getRentId() == id) {
                boolean quit = false;
                int choice;
                do {
                    System.out.println("""
                            Update rent:
                            0. Back to Rent Management
                            1. Rent Days
                            2. Vehicle
                            3. Customer
                            4. Payment""");

                    System.out.print("Enter choice: ");
                    choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    switch (choice) {
                        case 0:
                            quit = true;
                            break;
                        case 1:
                            System.out.print("New Rent Days: ");
                            item.setRentDays(scanner.nextInt());
                            scanner.nextLine();
                            break;

                        case 2:
                            Vehicle newVehicle = findVehicleByID(scanner);
                            if (newVehicle == null) {
                                System.out.println("Vehicle not found!");
                            } else if (newVehicle.isAvailable()) {
                                System.out.println("Selected vehicle is not available!");
                            } else {
                                // Mark old vehicle as available
                                if (item.getVehicle() != null) {
                                    item.getVehicle().setAvailable(true);
                                }
                                // Mark new vehicle as unavailable
                                newVehicle.setAvailable(false);
                                // Update the vehicle
                                item.setVehicle(newVehicle);
                                System.out.println("Vehicle updated successfully.");
                            }
                            break;
                        case 3:
                            Customer newCustomer = findCustomerByID(scanner);
                            if (newCustomer == null) {
                                System.out.println("Customer not found!");
                            } else {
                                item.setCustomer(newCustomer);
                                System.out.println("Customer updated successfully.");
                            }
                            break;
                        case 4:
                            // update payment
                            break;
                        default:
                            System.out.println("Invalid choice!");
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
        for (int i = 0; i < rentCount; i++) {
            if (rents[i].getRentId() == id) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("Rent ID not found!");
            return;
        }
        // Mark vehicle as available when rent is removed
        if (rents[index].getVehicle() != null) {
            rents[index].getVehicle().setAvailable(true);
        } else {
            System.out.println("Warning: Rent has no associated vehicle!");
        }

        for (int i = index; i < rentCount - 1; i++) {
            rents[i] = rents[i + 1];
        }
        rents[--rentCount] = null; // clear last element & decrement count
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

        for (int i = 0; i < rentCount; i++) {
            Rent item = rents[i];
            if (item.getRentId() == id) {
                if (item.isStatus()) {
                    System.out.println("This receipt is already paid!");
                    return;
                }

                if (item.getVehicle() == null) {
                    System.out.println("Error: No vehicle associated with this rent!");
                    return;
                }
                // original vehicle from garage
                Vehicle vehicle = getVehicleByID(item.getVehicle().getVehicleId());
                if(!vehicle.equals(item.getVehicle())){
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
                    updatePayment(scanner, item);

                // Require payDate - keep asking until valid input
                String payDate = getRequiredInput(scanner, "payDate");
                item.getPayment().processPayment(paymentMethod, payDate);

                // Update rent return date
                item.setReturnDate(payDate);

                // Mark vehicle as available
                item.getVehicle().setAvailable(true);

                double total = item.getPayment().calculateTotal();
                System.out.println("Payment created. Final total amount: $" + total);
                System.out.println(
                        "Vehicle with ID " + item.getVehicle().getVehicleId() + " has been returned and is now available.");
                System.out.println();
                item.setStatus(false);
                return;
            }
        }
        System.out.println("Rent ID not found!");
    }

    public Rent findRentByID(Scanner scanner) {
        System.out.print("Enter rent ID(int): ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        for (int i = 0; i < rentCount; i++) {
            if (rents[i].getRentId() == id)
                return rents[i];
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
        System.out.println(payment.toString());
    }
    public void updatePayment(Scanner scanner, Rent item) {
        if (item == null) {
            System.out.println("Rent not found!");
            return;
        }

        Payment payment = item.getPayment();

        System.out.print("Enter discount (0 if none): ");
        double discount = scanner.nextDouble();
        payment.setDiscount(discount);

        System.out.print("Enter extra days (0 if none): ");
        int extraDays = scanner.nextInt();
        payment.setExtraDays(extraDays);

        System.out.print("Enter damage fee (0 if none): ");
        double damageFee = scanner.nextDouble();
        payment.setDamageFee(damageFee);

        scanner.nextLine(); // consume newline

        System.out.println("Payment updated successfully!");
    }


    public void paymentManagement(Scanner scanner) {
        boolean quit = false;
        do {
            System.out.println("Payment Management: \n" +
                    "0. Back to Main Menu\n" +
                    "1. Show payment\n" +
                    "2. Update payment\n");
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