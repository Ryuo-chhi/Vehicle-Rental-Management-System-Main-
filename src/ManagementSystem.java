import java.util.Scanner;

public class ManagementSystem {
    Vehicle[] garage;
    int garageSize; // capacity
    int count; // current number of vehicles
    String[][] cars = {
            { "gasoline", "SUV", "Ford", "Escape", "300" },
            { "electric", "Sedan", "Tesla", "Model 3", "500" },
            { "diesel", "Truck", "Toyota", "Hilux", "400" },
            { "hybrid", "Hatchback", "Honda", "Insight", "350" },
            { "gasoline", "Coupe", "BMW", "M4", "600" }
    };

    Customer[] customers;
    int customerSize;
    int customerCount;
    String[][] custs = {
            { "Aruna Smith", "D7654321", "0662345679" },
            { "Bona Johnson", "D2345678", "0122345680" },
            { "Champa Brown", "D3456789", "0172345681" },
            { "Diana Prince", "D4567890", "0882345682" },
            { "Eno Gonzalez", "D5678901", "0972345683" }
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
            Vehicle newVehicle = new Vehicle(car[0], car[1], car[2], car[3], Double.parseDouble(car[4]));
            garage[count++] = newVehicle;
        }
    }

    public void vehicleManagement(Scanner scanner) {
        System.out.println("Vehicle Management:");
        boolean quit = false;
        int choice;
        do {
            System.out.println("""
                    0.Quit
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

        Vehicle newVehicle = new Vehicle(powerSource, vehicleClass, brand, model, price);

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
            if (rents[i] != null && rents[i].vehicle != null && rents[i].vehicle.vehicleId == id) {
                System.out.println("Vehicle is currently rented and cannot be removed.");
                return;
            }
        }

        int index = -1;
        for (int i = 0; i < count; i++) {
            if (garage[i].vehicleId == id) {
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
            if (item.vehicleId == id) {
                boolean quit = false;
                int choice;
                do {
                    System.out.println("""
                            Update vehicle:
                            0.Quit
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
                            System.out.print("New powerSource: ");
                            item.powerSource = scanner.nextLine();
                            break;

                        case 2:
                            System.out.print("New Vehicle Class: ");
                            item.vehicleClass = scanner.nextLine();
                            break;
                        case 3:
                            System.out.print("New Brand: ");
                            item.vehicleBrand = scanner.nextLine();
                            break;
                        case 4:
                            System.out.print("New Model: ");
                            item.vehicleModel = scanner.nextLine();
                            break;
                        case 5:
                            System.out.print("New Price: ");
                            item.rentalRatePerDay = scanner.nextDouble();
                            scanner.nextLine();
                            break;
                        case 6:
                            // Check if vehicle is currently rented
                            boolean isRented = false;
                            for (int j = 0; j < rentCount; j++) {
                                if (rents[j] != null && rents[j].vehicle.vehicleId == id) {
                                    isRented = true;
                                    break;
                                }
                            }
                            if (isRented) {
                                System.out.println("Cannot change status - vehicle is currently rented!");
                            } else {
                                System.out.print("Update status(true/false): ");
                                item.isAvailable = scanner.nextBoolean();
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

    public Vehicle findVehicleByID(Scanner scanner) {
        System.out.print("Enter vehicle ID(int): ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        for (int i = 0; i < count; i++) {
            if (garage[i].vehicleId == id)
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
            Customer newCustomer = new Customer(cust[0], cust[1], cust[2]);
            customers[customerCount++] = newCustomer;
        }
    }

    public void customerManagement(Scanner scanner) {
        System.out.println("Customer Management:");
        boolean quit = false;
        int choice;
        do {
            System.out.println("""
                    0.Quit
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
        System.out.print("Enter customer ID Card: ");
        String customerIdCard = scanner.nextLine();

        System.out.print("Enter customer Name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter customer Phone: ");
        String customerPhone = scanner.nextLine();

        Customer newCustomer = new Customer(customerIdCard, customerName, customerPhone);

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
            if (item.customerId == id) {
                boolean quit = false;
                int choice;
                do {
                    System.out.println("""
                            Update customer:
                            0.Quit
                            1. ID Card
                            2. Name
                            3. Phone""");

                    System.out.print("Enter choice: ");
                    choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    switch (choice) {
                        case 0:
                            quit = true;
                            break;
                        case 1:
                            System.out.print("New ID Card: ");
                            item.customerIdCard = scanner.nextLine();
                            break;

                        case 2:
                            System.out.print("New Name: ");
                            item.customerName = scanner.nextLine();
                            break;
                        case 3:
                            System.out.print("New Phone: ");
                            item.customerPhone = scanner.nextLine();
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
                if (rents[i] != null && rents[i].customer != null && rents[i].customer.customerId == id) {
                    System.out.println("Customer is associated with an active rent and cannot be removed.");
                    return;
                }
            }
        }

        int index = -1;
        for (int i = 0; i < customerCount; i++) {
            if (customers[i].customerId == id) {
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
            if (customers[i].customerId == id)
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
                        0.Quit
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
        System.out.print("Enter number of days: ");
        int rentDays = scanner.nextInt();
        scanner.nextLine(); // consume newline

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
        if (!selectedVehicle.isAvailable) {
            System.out.println("Vehicle is not available for rent!");
            return;
        }

        Rent newRent = new Rent(selectedVehicle, selectedCustomer, rentDays);
        // Mark vehicle as unavailable
        selectedVehicle.isAvailable = false;

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
            if (item.rentId == id) {
                boolean quit = false;
                int choice;
                do {
                    System.out.println("""
                            Update rent:
                            0.Quit
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
                            item.rentDays = scanner.nextInt();
                            scanner.nextLine();
                            break;

                        case 2:
                            Vehicle newVehicle = findVehicleByID(scanner);
                            if (newVehicle == null) {
                                System.out.println("Vehicle not found!");
                            } else if (!newVehicle.isAvailable) {
                                System.out.println("Selected vehicle is not available!");
                            } else {
                                // Mark old vehicle as available
                                if (item.vehicle != null) {
                                    item.vehicle.isAvailable = true;
                                }
                                // Mark new vehicle as unavailable
                                newVehicle.isAvailable = false;
                                // Update the vehicle
                                item.vehicle = newVehicle;
                                System.out.println("Vehicle updated successfully.");
                            }
                            break;
                        case 3:
                            Customer newCustomer = findCustomerByID(scanner);
                            if (newCustomer == null) {
                                System.out.println("Customer not found!");
                            } else {
                                item.customer = newCustomer;
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
            if (rents[i].rentId == id) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("Rent ID not found!");
            return;
        }
        // Mark vehicle as available when rent is removed
        if (rents[index].vehicle != null) {
            rents[index].vehicle.isAvailable = true;
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
            if (item.rentId == id) {
                if (item.vehicle == null) {
                    System.out.println("Warning: Rent has no associated vehicle!"); // most likely not happen, an error
                                                                                    // (corrupted data or manual
                                                                                    // editing).
                    return;
                }
                if (item.payment != null) {
                    System.out.println("Payment already recorded for this rent.");
                    return;
                }

                System.out.print("Enter extra days (0 if none): ");
                item.extraDays = scanner.nextInt();
                scanner.nextLine(); // consume newline

                System.out.print("Enter damage fee (0 if none): ");
                item.damageFee = scanner.nextDouble();
                scanner.nextLine(); // consume newline

                System.out.print("Enter discount (0 if none): ");
                item.discount = scanner.nextDouble();
                scanner.nextLine(); // consume newline

                System.out.print("Enter payment method: ");
                String paymentMethod = scanner.nextLine();

                double total = item.calculateTotal();
                item.payment = new Payment(total, paymentMethod);

                item.vehicle.isAvailable = true;
                System.out.println("Payment created. Total amount: $" + total);
                System.out.println(
                        "Vehicle with ID " + item.vehicle.vehicleId + " has been returned and is now available.");
                return;
            }
        }
        System.out.println("Rent ID not found!");
    }

}