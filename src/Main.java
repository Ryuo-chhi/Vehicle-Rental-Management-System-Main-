public class Main {
  public static void main(String[] args) {
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
    M.addVehicle(v2);

    M.showVehicle();
    M.removeVehicle(1);
    M.showVehicle();


  }
}
