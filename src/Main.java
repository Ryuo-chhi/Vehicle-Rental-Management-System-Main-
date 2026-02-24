import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Garage myGarage = new Garage(10);

        System.out.println("Welcome to the Vehicle Rental Management System!");

        System.out.println("Please login to continue...");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        myGarage.staffLogin(username, password);
        System.out.println(myGarage.getLastMessage());
        
        if (!myGarage.isStaffLoggedIn()) {
            System.out.println("Login failed. Exiting...");
            scanner.close();
            return;
        }

        boolean quit = false;
        while (!quit) {
            System.out.println("""
                    Management System:
                    0. Quit and Logout
                    1. Vehicle Management
                    2. Customer Management
                    3. Rent Management
                    4. Payment Management
                    5. Staff Management 
                    6. Other Management -- soon
                    """);
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0 -> {
                    quit = true;
                    myGarage.staffLogout();
                    System.out.println("Exiting and logging out...");
                }
                case 1 -> myGarage.vehicleManagement(scanner);
                case 2 -> myGarage.customerManagement(scanner);
                case 3 -> myGarage.rentManagement(scanner);
                case 4 -> myGarage.paymentManagement(scanner);
                case 5 -> myGarage.staffManagement(scanner);
                case 6 -> System.out.println("Coming soon...");
                default -> System.out.println("Invalid choice!");
            }
            System.out.println();
        }
        scanner.close();

    }
}
