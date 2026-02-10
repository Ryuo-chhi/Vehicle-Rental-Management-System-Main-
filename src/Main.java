import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ManagementSystem M = new ManagementSystem(10);

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
                case 1 -> M.vehicleManagement(scanner);
                case 2 -> M.customerManagement(scanner);
                case 3 -> M.rentManagement(scanner);
                case 4 -> M.paymentManagement(scanner);
                case 5, 6 -> System.out.println("Coming soon...");
                default -> System.out.println("Invalid choice!");
            }
            System.out.println();
        }
        scanner.close();

    }
}
