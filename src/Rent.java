public class Rent {
    private int rentId;
    private int rentDays;
    private String startDate;
    private String endDate;
    private String returnDate;
    private boolean status;
    
    private Car car;
    private Customer customer;
    private Payment payment;

    private static int countRentId = 0;

    // create
    public Rent(Car car, Customer customer, int rentDays, String startDate, String endDate) {
        this.rentId = ++countRentId;
        this.setVehicle(car);
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

    public Car getVehicle() {
        return car;
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

    public void setVehicle(Car car) {
        if (car != null) {
            this.car = car;
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
        if (this.rentId != other.rentId) return false;
        if (this.customer == null || other.customer == null) return false;
        return this.customer.getCustomerId() == other.customer.getCustomerId();
    }

    @Override
    public String toString() {
        return "Rent{" +
                "rentId=" + rentId +
                ", rentDays=" + rentDays +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", returnDate='" + returnDate + '\'' + "\n" +
                ", vehicle=" + car + "\n" +
                ", customer=" + customer.toStringSimple() + "\n" +
                ", payment=" + payment + "\n" +
                ", status=" + (status ? "Active" : "Completed") + "\n" +
                '}';
    }


}
