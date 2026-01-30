import java.util.Scanner;

public class ManagementSystem {
    Vehicle[] garage;
    int garageSize; // capacity
    int count;      // current number of vehicles

    public ManagementSystem(int maxSize){
        this.garage = new Vehicle[maxSize];
        this.garageSize = maxSize;
        this.count = 0;
    }

    public void addVehicle(Scanner scanner) {
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

        if(count >= garage.length){
            System.out.println("Garage is full! Cannot add new car.");
            return;
        }
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
}