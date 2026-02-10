import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Garage myGarage = new Garage(10);

        System.out.println("Hello Admin!");

        boolean quit = false;
        while (!quit) {
            System.out.println("""
                    Management System:
                    0. Quit
                    1. Vehicle Management
                    2. Customer Management
                    3. Rent Management
                    4. Payment Management
                    5. Record Management -- soon
                    6. Other Management -- soon
                    """);
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0 -> {
                    quit = true;
                    System.out.println("Exiting...");
                }
                case 1 -> myGarage.vehicleManagement(scanner);
                case 2 -> myGarage.customerManagement(scanner);
                case 3 -> myGarage.rentManagement(scanner);
                case 4 -> myGarage.paymentManagement(scanner);
                case 5, 6 -> System.out.println("Coming soon...");
                default -> System.out.println("Invalid choice!");
            }
            System.out.println();
        }
        scanner.close();

    }
}
