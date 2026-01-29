public class Rent {
    int rentId;
    int rentDays;
    Vehicle vehicle;
    Customer customer;
    Payment payment;

    static int nextRentId = 1; // to ensure unique rent IDs

    public Rent(Vehicle vehicle, Customer customer, int rentDays) {
        this.rentId = nextRentId++;
        this.vehicle = vehicle;
        this.customer = customer;
        this.rentDays = rentDays;
        this.payment = null;
    }

    @Override
    public String toString() {
        return "Rent [rentId=" + rentId + ", rentDays=" + rentDays + ", vehicle=" + vehicle + ", customer=" + customer
                + ", payment=" + payment + "]";
    }

}
