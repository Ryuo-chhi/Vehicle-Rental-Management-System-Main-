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
                    4. Payment Management -- soon
                    5. Record Management -- soon
                    6. Other Management -- soon
                    """);
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0 -> { quit = true; System.out.println("Exiting..."); }
                case 1 -> M.vehicleManagement(scanner);
                case 2 -> M.customerManagement(scanner);
                case 3 -> M.rentManagement(scanner);
                case 4, 5, 6 -> System.out.println("Coming soon...");
                default -> System.out.println("Invalid choice!");
            }
            System.out.println();
        }
        scanner.close();

        // ===============================================
        // F1-F4 PROOFS: Primitive vs Reference Datatypes
        // ===============================================

        System.out.println("\n========== F1-F4 PROOFS ==========\n");

        // F1 — Primitive copy
        // Copy a primitive, modify the copy, original remains unchanged
        int originalPrice = 100;
        int copiedPrice = originalPrice;
        copiedPrice = 200;
        System.out.println("F1 — Primitive Copy:");
        System.out.println("  Original: " + originalPrice + " | Copied (modified): " + copiedPrice);
        System.out.println("  Proof: Original unchanged after modifying copy.\n");

        // F2 — Reference copy
        // Two variables reference the same object; change is visible everywhere
        Vehicle v1 = new Vehicle("gasoline", "SUV", "Honda", "CRV", 250);
        Vehicle v2 = v1;
        v2.rentalRatePerDay = 999;
        System.out.println("F2 — Reference Copy:");
        System.out.println("  v1.rentalRatePerDay: " + v1.rentalRatePerDay);
        System.out.println("  v2.rentalRatePerDay: " + v2.rentalRatePerDay);
        System.out.println("  Proof: Both v1 and v2 show same change (999).\n");

        // F3 — Array stores references
        // Objects inside arrays reflect later modifications
        Vehicle[] testGarage = new Vehicle[2];
        testGarage[0] = v1;
        v1.rentalRatePerDay = 777;
        System.out.println("F3 — Array Stores References:");
        System.out.println("  testGarage[0].rentalRatePerDay: " + testGarage[0].rentalRatePerDay);
        System.out.println("  Proof: Array element reflects v1's change to 777.\n");

        // F4 — Snapshot behavior
        // Stored snapshot values do not change after the original object changes
        Vehicle vehicleForRent = new Vehicle("electric", "Sedan", "Tesla", "Model S", 500);
        Customer testCustomer = new Customer("John Doe", "P1234567", "0123456789");
        Rent rent = new Rent(vehicleForRent, testCustomer, 5);
        double snapshotTotal = rent.calculateTotal(); // Uses snapshotRate captured at creation
        vehicleForRent.rentalRatePerDay = 1000; // Change original after rent created
        double afterChangeTotal = rent.calculateTotal();
        System.out.println("F4 — Snapshot Behavior:");
        System.out.println("  Original rate at rent creation: 500");
        System.out.println("  Snapshot total (5 days * 500): " + snapshotTotal);
        System.out.println("  After changing vehicle rate to 1000:");
        System.out.println("  Recalculated total still uses snapshot: " + afterChangeTotal);
        System.out.println("  Proof: Rent uses snapshotRate, not current vehicle rate.\n");

        System.out.println("========== END OF PROOFS ==========");
    }
}
