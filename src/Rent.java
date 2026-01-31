public class Rent {
    int rentId;
    int rentDays;
    Vehicle vehicle;
    Customer customer;
    Payment payment;
    int extraDays;
    double damageFee;
    double discount;

    static int nextRentId = 1; // to ensure unique rent IDs

    public Rent(Vehicle vehicle, Customer customer, int rentDays) {
        this.rentId = nextRentId++;
        this.vehicle = vehicle;
        this.customer = customer;
        this.rentDays = rentDays;
        this.payment = null;
        this.extraDays = 0;
        this.damageFee = 0.0;
        this.discount = 0.0;
    }

    public double calculateTotal() {
        if (vehicle == null) {
            return 0.0;
        }
        double base = (rentDays + extraDays) * vehicle.rentalRatePerDay;
        return base + damageFee - discount;
    }

    @Override
    public String toString() {
        return "Rent [rentId=" + rentId + ", rentDays=" + rentDays + ", " + vehicle + ", " + customer + ", payment="
                + payment + "]";
    }

}
