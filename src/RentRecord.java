/**
 * RentRecord — Immutable snapshot of a completed rental.
 *
 * All fields are final and set once at construction time.
 * Even if the original Rent, Vehicle, or Customer objects are modified
 * or removed later, this record remains unchanged (true snapshot).
 */
public final class RentRecord {

    private final int rentId;

    // Vehicle snapshot (primitives / immutable Strings)
    private final String vehicleId;
    private final String vehicleBrand;
    private final String vehicleModel;
    private final String licencePlate;

    // Customer snapshot
    private final int customerId;
    private final String customerName;

    // Rental details snapshot
    private final int rentDays;
    private final String startDate;
    private final String endDate;
    private final String returnDate;

    // Payment snapshot
    private final double totalPaid;
    private final String paymentMethod;

    /**
     * Builds an immutable record from a completed Rent.
     * Call this only after returnVehicle() has been processed.
     */
    public RentRecord(Rent rent) {
        this.rentId = rent.getRentId();

        this.vehicleId    = rent.getVehicle().getVehicleId();
        this.vehicleBrand = rent.getVehicle().getVehicleBrand();
        this.vehicleModel = rent.getVehicle().getVehicleModel();
        this.licencePlate = rent.getVehicle().getLicencePlate();

        this.customerId   = rent.getCustomer().getCustomerId();
        this.customerName = rent.getCustomer().getCustomerName();

        this.rentDays   = rent.getRentDays();
        this.startDate  = rent.getStartDate();
        this.endDate    = rent.getEndDate();
        this.returnDate = rent.getReturnDate();

        this.totalPaid      = rent.getPayment().calculateTotal();
        this.paymentMethod  = rent.getPayment().getPaymentMethod();
    }

    // ===== GETTERS ONLY (no setters — immutable) =====

    public int getRentId()           { return rentId; }
    public String getVehicleId()     { return vehicleId; }
    public String getVehicleBrand()  { return vehicleBrand; }
    public String getVehicleModel()  { return vehicleModel; }
    public String getLicencePlate()  { return licencePlate; }
    public int getCustomerId()       { return customerId; }
    public String getCustomerName()  { return customerName; }
    public int getRentDays()         { return rentDays; }
    public String getStartDate()     { return startDate; }
    public String getEndDate()       { return endDate; }
    public String getReturnDate()    { return returnDate; }
    public double getTotalPaid()     { return totalPaid; }
    public String getPaymentMethod() { return paymentMethod; }

    @Override
    public String toString() {
        return "RentRecord{" +
                "rentId=" + rentId +
                ", vehicle=" + vehicleBrand + " " + vehicleModel + " (" + vehicleId + ")" +
                ", customer=[" + customerId + "] " + customerName +
                ", rentDays=" + rentDays +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", totalPaid=$" + totalPaid +
                '}';
    }
}
