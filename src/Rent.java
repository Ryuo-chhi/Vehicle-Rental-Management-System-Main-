public class Rent {
    private int rentId;
    private int rentDays;
    private String startDate;
    private String endDate;
    private String returnDate;
    private boolean status;
    
    private Vehicle vehicle;
    private Customer customer;
    private Payment payment;

    private static int countRentId = 0;

    // create
    public Rent(Vehicle vehicle, Customer customer, int rentDays, String startDate, String endDate) {
        this.rentId = ++countRentId;
        this.setVehicle(vehicle);
        this.setCustomer(customer);
        this.setRentDays(rentDays);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setReturnDate("TBD");
        this.payment = null; // Initialized to null, will be set later(when payment is processed)
        this.setStatus(true);
    }

    // rent lookup
    public Rent(int rentId, Customer customer) {
        if (rentId > 0) {
            this.rentId = rentId;
        } else {
            System.out.println("Error: Rent ID must be greater than 0");
        }
        this.setCustomer(customer);
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

    public boolean isStatus() {
        return status;
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

    public void setRentDays(int rentDays) {
        if (rentDays > 0) {
            this.rentDays = rentDays;
        } else {
            System.out.println("Error: Rent days must be greater than 0");
        }
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Rent other = (Rent) obj;
        return this.rentId == other.rentId &&
               this.vehicle != null && other.vehicle != null &&
               this.vehicle.getVehicleId() == other.vehicle.getVehicleId();
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
                ", status=" + (status ? "Active" : "Completed") + "\n" +
                '}';
    }


}
