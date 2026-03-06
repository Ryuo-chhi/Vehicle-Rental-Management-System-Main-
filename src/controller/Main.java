package controller;

import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Garage myGarage = new Garage(10);

        System.out.println("\nWelcome to the Model.Vehicle Rental Management System!\n");

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
                    ========================================
                         VEHICLE RENTAL MANAGEMENT SYSTEM
                    ========================================
                    -- MAIN MENU --
                      0.  Quit & Logout
                    ----------------------------------------
                    -- OPERATIONS --
                      1.  Vehicle Management
                      2.  Customer Management
                      3.  Rent Management
                      4.  Payment Management
                    ----------------------------------------
                    -- MANAGER --
                      5.  Staff Management
                      6.  Report Management
                      7.  Other Management       [ soon ]
                    ========================================
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
                case 6 -> myGarage.generateReport();
                case 7 -> System.out.println("Coming soon...");
                default -> System.out.println("Invalid choice!");
            }
            System.out.println();
        }
        scanner.close();

    }
}
