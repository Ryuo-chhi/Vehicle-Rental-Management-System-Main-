public class Rent {
    private int rentId;
    private int rentDays;
    private String startDate;
    private String endDate;
    private String returnDate;
    
    private Vehicle vehicle;
    private Customer customer;
    private Payment payment;

    private static int countRentId = 0; // to ensure unique rent IDs

    public Rent(Vehicle vehicle, Customer customer, int rentDays, String startDate, String endDate) {
        this.rentId = ++countRentId;
        this.vehicle = vehicle;
        this.customer = customer;
        this.rentDays = rentDays;
        this.startDate = startDate;
        this.endDate = endDate;
        this.returnDate = "TBD";
        this.payment = null;

    }

    // ===== GETTERS =====
    public int getRentId() {
        return rentId;
    }

    public int getRentDays() {
        return rentDays;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Payment getPayment() {
        return payment;
    }

    public static int getTotalRentCount() {
        return countRentId;
    }

    // ===== SETTER =====
    public void setPayment(Payment payment) {
        if (payment != null) {
            this.payment = payment;
        } else {
            System.out.println("Error: Payment cannot be null");
        }
    }

    public void setRentDays(int rentDays) {
        if (rentDays > 0) {
            this.rentDays = rentDays;
        } else {
            System.out.println("Error: Rent days must be greater than 0");
        }
    }

    public void setVehicle(Vehicle vehicle) {
        if (vehicle != null) {
            this.vehicle = vehicle;
        } else {
            System.out.println("Error: Vehicle cannot be null");
        }
    }

    public void setCustomer(Customer customer) {
        if (customer != null) {
            this.customer = customer;
        } else {
            System.out.println("Error: Customer cannot be null");
        }
    }

    public void setReturnDate(String returnDate) {
        if (returnDate != null && !returnDate.trim().isEmpty()) {
            this.returnDate = returnDate;
        } else {
            System.out.println("Error: Return date cannot be empty");
        }
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
                ", customer=" + customer.toStringSimple() + "\n" +
                ", payment=" + payment + "\n" +
                '}';
    }
}
