import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    boolean quit = false;
    int choice;
    Scanner scanner = new Scanner(System.in);
    
    System.out.println("Hello Admin!");
    ManagementSystem M = new ManagementSystem(10);

    // Step 1: Create rent (no payment yet)
    // Step 2: Customer pays later
  //  Payment payment = new Payment(250.0, "credit card");
  //  rent.payment = payment; // Link payment to rent

    do {
      System.out.println("""
                    Management System:
                    0. Quit
                    1. Vehicle Management
                    2. Customer Management
                    3. Rent Management
                    4. Payment Management -- soon
                    5. Report Management -- soon
                    6. Other Management -- soon
                    """);
      System.out.print("Enter choice: ");
      choice = scanner.nextInt();
      scanner.nextLine(); // <-- consume the leftover newline

      switch (choice) {
        case 0:
          quit = true;
          System.out.println("Exiting the System...");
          break;
        case 1:
          M.vehicleManagement(scanner);
          break;
        case 2:
          M.customerManagement(scanner);
          break;
        case 3:
          M.rentManagement(scanner);
          break;
        case 4:
          System.out.println("soon...");
          // M.paymentManagement(scanner);
          break;
        case 5:
          System.out.println("soon...");
          break;
        case 6:
          System.out.println("soon...");
          // M.otherManagement(scanner);
          break;
        default:
          System.out.println("Invalid choice!");
      }
      System.out.println();
    } while (!quit);

    scanner.close();

    
  }
}
