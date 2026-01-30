import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    boolean quit = false;
    int choice;
    Scanner scanner = new Scanner(System.in);
    
    System.out.println("Hello Admin!");
    ManagementSystem M = new ManagementSystem(10);
    Vehicle v1 = new Vehicle( "gasoline", "SUV", "Ford", "Escape", 300);
    Vehicle v2 = new Vehicle( "gasoline", "SUV", "Ford", "Territory", 3000);
//    Customer customer = new Customer("John Doe", "D1234567", "john.d");

    // Step 1: Create rent (no payment yet)
//    Rent rent = new Rent(vehicle, customer, 5);

    // Step 2: Customer pays later
//    Payment payment = new Payment(250.0, "credit card");
//    rent.payment = payment; // Link payment to rent

//    M.addVehicle(v1);
//    M.addVehicle(v2);

//    M.showVehicle();
//    M.removeVehicle(1);
//    M.showVehicle();

    do {
      System.out.println("""
                    Management System:
                    0. Quit
                    1. Add vehicle
                    2. Remove vehicle
                    3. Update vehicle
                    4. Show all vehicles
                    """);
      System.out.print("Enter choice: ");
      choice = scanner.nextInt();
      scanner.nextLine(); // <-- consume the leftover newline

      switch (choice) {
        case 0:
          quit = true;
          System.out.println("Exiting...");
          break;
        case 1:
          M.addVehicle(scanner);
          break;
        case 2:
          M.removeVehicle(scanner);
          break;
        case 3:
          M.updateVehicle(scanner);
          break;
        case 4:
          System.out.println();
          M.showVehicle();
          break;
        default:
          System.out.println("Invalid choice!");
      }
      System.out.println();
    } while (!quit);

    scanner.close();


  }
}
