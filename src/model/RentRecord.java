package model;

/**
 * RentRecord — Immutable snapshot of a completed rental.
 *
 * Captures every available field from Rent, Vehicle, Customer, and Payment
 * at the moment returnVehicle() is processed.
 * Even if the original objects are later modified or removed, this record stays frozen.
 */
public final class RentRecord {

    private final int rentId;

    // Vehicle snapshot
    private final int vehicleId;
    private final String vehicleType;
    private final String vehicleCode;
    private final String vehiclePowerSource;
    private final String vehicleClass;
    private final String vehicleBrand;
    private final String vehicleModel;
    private final String licencePlate;
    private final double rentalRatePerDay;

    // Customer snapshot
    private final int customerId;
    private final String customerName;
    private final String customerIdNum;
    private final String customerPhone;

    //Staff snapshot
    private final String staffName;
    private final int staffID;

    // Rental details snapshot
    private final int rentDays;
    private final String startDate;
    private final String endDate;
    private final String returnDate;

    // Payment snapshot
    private final int paymentId;
    private final String paymentMethod;
    private final double price;
    private final double discount;
    private final int extraDays;
    private final double damageFee;
    private final double deposit;
    private final String payDate;
    private final String paymentStatus;
    private final double totalPaid;

    /**
     * Builds an immutable record from a completed Rent.
     * Call this only after returnVehicle() has been processed.
     */
    public RentRecord(Rent rent) {
        this.rentId = rent.getRentId();

        this.vehicleId          = rent.getVehicle().getVehicleId();
        this.vehicleType        = rent.getVehicle().getClass().getSimpleName();
        this.vehicleCode        = rent.getVehicle().getVehicleCode();
        this.vehiclePowerSource = rent.getVehicle().getPowerSource();
        this.vehicleClass       = rent.getVehicle().getVehicleClass();
        this.vehicleBrand       = rent.getVehicle().getVehicleBrand();
        this.vehicleModel       = rent.getVehicle().getVehicleModel();
        this.licencePlate       = rent.getVehicle().getLicencePlate();
        this.rentalRatePerDay   = rent.getVehicle().getRentalRatePerDay();

        this.customerId   = rent.getCustomer().getCustomerId();
        this.customerName = rent.getCustomer().getCustomerName();
        this.customerIdNum = rent.getCustomer().getcustomerIdNum();
        this.customerPhone = rent.getCustomer().getCustomerPhone();

        this.staffID = rent.getStaff().getId();
        this.staffName = rent.getStaff().getName();

        this.rentDays   = rent.getRentDays();
        this.startDate  = rent.getStartDate();
        this.endDate    = rent.getEndDate();
        this.returnDate = rent.getReturnDate();

        this.paymentId     = rent.getPayment().getPaymentId();
        this.paymentMethod = rent.getPayment().getPaymentMethod();
        this.price         = rent.getPayment().getPrice();
        this.discount      = rent.getPayment().getDiscount();
        this.extraDays     = rent.getPayment().getExtraDays();
        this.damageFee     = rent.getPayment().getDamageFee();
        this.deposit       = rent.getPayment().getDeposit();
        this.payDate       = rent.getPayment().getPayDate();
        this.paymentStatus = rent.getPayment().getStatus();
        this.totalPaid     = rent.getPayment().calculateTotal();
    }

    // ===== GETTERS ONLY (no setters — immutable) =====

    public int getRentId()            { return rentId; }

    public int getVehicleId()             { return vehicleId; }
    public String getVehicleType()        { return vehicleType; }
    public String getVehicleCode()        { return vehicleCode; }
    public String getVehiclePowerSource() { return vehiclePowerSource; }
    public String getVehicleClass()       { return vehicleClass; }
    public String getVehicleBrand()       { return vehicleBrand; }
    public String getVehicleModel()       { return vehicleModel; }
    public String getLicencePlate()       { return licencePlate; }
    public double getRentalRatePerDay()   { return rentalRatePerDay; }

    public int getCustomerId()        { return customerId; }
    public String getCustomerName()   { return customerName; }
    public String getCustomerIdNum()  { return customerIdNum; }
    public String getCustomerPhone()  { return customerPhone; }

    public int getRentDays()          { return rentDays; }
    public String getStartDate()      { return startDate; }
    public String getEndDate()        { return endDate; }
    public String getReturnDate()     { return returnDate; }

    public int getPaymentId()         { return paymentId; }
    public String getPaymentMethod()  { return paymentMethod; }
    public double getPrice()          { return price; }
    public double getDiscount()       { return discount; }
    public int getExtraDays()         { return extraDays; }
    public double getDamageFee()      { return damageFee; }
    public double getDeposit()        { return deposit; }
    public String getPayDate()        { return payDate; }
    public String getPaymentStatus()  { return paymentStatus; }
    public double getTotalPaid()      { return totalPaid; }

    @Override
    public String toString() {
        return "RentRecord{" +
                "rentId=" + rentId +
                ", staffID=" + staffID +
                ", staffName='" + staffName + '\'' +
                ", vehicle=[" + vehicleId + "] " + vehicleCode + " " + vehicleBrand + " " + vehicleModel + " (" + licencePlate + ")" +
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
