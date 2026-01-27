public class RentalService {
    int rentId;
    Vehicle vehicle;
    Customer customer;
    int days;
    Payment payment;

    public RentalService(int rentId, Vehicle vehicle, Customer customer, int days) {
        this.rentId = rentId;
        this.vehicle = vehicle;
        this.customer = customer;
        this.days = days;
        this.payment = null;
    }

//    public Rental rentVehicle(Customer customer, Vehicle vehicle, int days) {}



}
