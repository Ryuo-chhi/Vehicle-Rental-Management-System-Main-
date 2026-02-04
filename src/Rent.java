public class Rent {
    int rentId;
    int rentDays;
    String startDate;
    String endDate;
    String returnDate;
    Vehicle vehicle;
    Customer customer;
    Payment payment;

    static int countRentId = 1; // to ensure unique rent IDs

    public Rent(Vehicle vehicle, Customer customer, int rentDays, String startDate, String endDate) {
        this.rentId = countRentId++;
        this.vehicle = vehicle;
        this.customer = customer;
        this.rentDays = rentDays;
        this.startDate = startDate;
        this.endDate = endDate;
        this.returnDate = "TBD";
        this.payment = null;

    }
    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "Rent{" +
                "rentId=" + rentId +
                ", rentDays=" + rentDays +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", returnDate='" + returnDate + '\'' + "\n" +
                ", vehicle=" + vehicle + "\n" +
                ", customer=" + customer +"\n" +
                ", payment=" + payment + "\n" +
                '}';
    }
}
