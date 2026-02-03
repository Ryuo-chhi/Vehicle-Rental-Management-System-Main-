class Rent {
    int rentId;
    int rentDays;
    Vehicle vehicle;
    Customer customer;
    Payment payment;
    double discount;
    int extraDays;
    double damageFee;
    double snapshotRate;

    static int countRentId = 1;

    Rent(Vehicle vehicle, Customer customer, int rentDays) {
        this.rentId = countRentId++;
        this.vehicle = vehicle;
        this.customer = customer;
        this.rentDays = rentDays;
        this.payment = null;
        this.discount = 0.0;
        this.extraDays = 0;
        this.damageFee = 0.0;
        this.snapshotRate = (vehicle != null) ? vehicle.rentalRatePerDay : 0.0;
    }

    double calculateTotal() {
        if (vehicle == null) return 0.0;
        double total = rentDays * snapshotRate - discount;
        return total;
    }

    @Override
    public String toString() {
        return "Rent [rentId=" + rentId + ", rentDays=" + rentDays + ", " + vehicle + ", " + customer + ", payment=" + payment + "]";
    }
}
