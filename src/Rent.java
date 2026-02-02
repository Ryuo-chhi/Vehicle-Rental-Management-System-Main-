public class Rent {
    int rentId;
    int rentDays;
    Vehicle vehicle;
    Customer customer;
    Payment payment;
    double discount;
    int extraDays;
    double damageFee;

    static int countRentId = 1; // to ensure unique rent IDs

    public Rent(Vehicle vehicle, Customer customer, int rentDays) {
        this.rentId = countRentId++;
        this.vehicle = vehicle;
        this.customer = customer;
        this.rentDays = rentDays;
        this.payment = null;
        this.discount = 0.0;
        this.extraDays = 0;
        this.damageFee = 0.0;
    }

    public double calculateTotal() {
        if (vehicle == null) {
            return 0.0;
        }
        //Snapshot current rental rate
        double price = vehicle.rentalRatePerDay;

        double total = rentDays * price - discount;
        return total;
    }

    @Override
    public String toString() {
        return "Rent [rentId=" + rentId + ", rentDays=" + rentDays + ", " + vehicle + ", " + customer + ", payment="
                + payment + "]";
    }

}
