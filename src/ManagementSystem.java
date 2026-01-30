import java.util.Scanner;


public class ManagementSystem {
    Vehicle[] garage;
    int garageSize; // capacity
    int count;      // current number of vehicles
    String[][] cars = {
            {"gasoline", "SUV", "Ford", "Escape", "300"},
            {"electric", "Sedan", "Tesla", "Model 3", "500"},
            {"diesel", "Truck", "Toyota", "Hilux", "400"},
            {"hybrid", "Hatchback", "Honda", "Insight", "350"},
            {"gasoline", "Coupe", "BMW", "M4", "600"}
    };

    Customer[] customers;
    int customerSize;
    int customerCount;
    String[][] custs = {
            {"Aruna Smith", "D7654321", "0662345679"},
            {"Bona Johnson", "D2345678", "0122345680"},
            {"Champa Brown", "D3456789", "0172345681"},
            {"Diana Prince", "D4567890", "0882345682"},
            {"Eno Gonzalez", "D5678901", "0972345683"}
    };


    public ManagementSystem(int maxSize){
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
    }

    // Vehicle Management

    public void generateVehicleToGarage(){
        if(count >= garage.length){
            System.out.println("Garage is full! Cannot add new car.");
            return;
        }
        for (String[] car : cars) {
            Vehicle newVehicle = new Vehicle(car[0], car[1], car[2], car[3], Double.parseDouble(car[4]));
            garage[count++] = newVehicle;
        }
    }

    public void vehicleManagement(Scanner scanner){
        System.out.println("Vehicle Management:");
        boolean quit = false;
        int choice;
        do{
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

        } while(!quit);
    }
    
    public void addVehicle(Scanner scanner) {
        if(count >= garage.length){
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
                do{
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
                            System.out.print("Update status(true/false): ");
                            item.isAvailable = scanner.nextBoolean();
                            scanner.nextLine();
                            break;
                        default:
                            System.out.println("Invalid choice!");
                    }
                    System.out.println();

                } while(!quit);
                return;
            }
        }
        System.out.println("Vehicle not found!");
    }

    public Vehicle findVehicleByID(Scanner scanner){
        System.out.print("Enter vehicle ID(int): ");
        int id = scanner.nextInt();
        for(Vehicle vehicle: garage){
            if(vehicle.vehicleId == id) return vehicle;
        }
        return null;
    }

    // Customer Management

    public void generateCustomerToSystem(){
        if(customerCount >= customers.length){
            System.out.println("Customer list is full! Cannot add new customer.");
            return;
        }
        for (String[] cust : custs) {
            Customer newCustomer = new Customer(cust[0], cust[1], cust[2]);
            customers[customerCount++] = newCustomer;
        }
    }

    public void customerManagement(Scanner scanner){
        System.out.println("Customer Management:");
        boolean quit = false;
        int choice;
        do{
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

        } while(!quit);
    }

    public void addCustomer(Scanner scanner) {
        if(customerCount >= customers.length){
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

        Customer newCustomer = new Customer(customerIdCard,customerName,customerPhone);

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
                do{
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

                } while(!quit);
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

    public Customer findCustomerByID(Scanner scanner){
        System.out.print("Enter customer ID(int): ");
        int id = scanner.nextInt();
        for(Customer customer: customers){
            if(customer.customerId == id) return customer;
        }
        return null;
    }

}