public class Main {
  public static void main(String[] args) {
    System.out.println("Hello Admin!");

    Vehicle vehicle = new Vehicle("EV", "sedan", "Tesla Model 3");
    Customer customer = new Customer("John Doe", "D1234567", "john.d");

    // Step 1: Create rent (no payment yet)
    Rent rent = new Rent(vehicle, customer, 5);

    // Step 2: Customer pays later
    Payment payment = new Payment(250.0, "credit card");
    rent.payment = payment; // Link payment to rent
  }
}
