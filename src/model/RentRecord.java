package model;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public int getStaffID()           { return staffID; }
    public String getStaffName()      { return staffName; }

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

    /**
     * Static factory: reconstruct a RentRecord directly from a DB ResultSet row.
     * Used by DatabaseMapper.mapToRentRecords().
     */
    public static RentRecord fromResultSet(ResultSet rs) throws SQLException {
        // Build a lightweight mutable container, then copy into final fields via this constructor trick:
        // We use the package-private all-args constructor below.
        return new RentRecord(
            rs.getInt("rent_id"),
            rs.getInt("vehicle_id"),        rs.getString("vehicle_type"),
            rs.getString("vehicle_code"),   rs.getString("power_source"),
            rs.getString("vehicle_class"),  rs.getString("brand"),
            rs.getString("model"),          rs.getString("licence_plate"),
            rs.getDouble("rate_per_day"),
            rs.getInt("customer_id"),       rs.getString("customer_name"),
            rs.getString("id_num"),         rs.getString("phone"),
            rs.getInt("staff_id"),          rs.getString("staff_name"),
            rs.getInt("rent_days"),         rs.getString("start_date"),
            rs.getString("end_date"),       rs.getString("return_date"),
            rs.getInt("payment_id"),        rs.getString("method"),
            rs.getDouble("price"),          rs.getDouble("discount"),
            rs.getInt("extra_days"),        rs.getDouble("damage_fee"),
            rs.getDouble("deposit"),        rs.getString("pay_date"),
            rs.getString("pay_status"),     rs.getDouble("total_paid")
        );
    }

    /** All-args constructor used exclusively by fromResultSet(). */
    private RentRecord(
            int rentId,
            int vehicleId, String vehicleType, String vehicleCode,
            String vehiclePowerSource, String vehicleClass, String vehicleBrand,
            String vehicleModel, String licencePlate, double rentalRatePerDay,
            int customerId, String customerName, String customerIdNum, String customerPhone,
            int staffID, String staffName,
            int rentDays, String startDate, String endDate, String returnDate,
            int paymentId, String paymentMethod, double price, double discount,
            int extraDays, double damageFee, double deposit, String payDate,
            String paymentStatus, double totalPaid) {
        this.rentId           = rentId;
        this.vehicleId        = vehicleId;
        this.vehicleType      = vehicleType;
        this.vehicleCode      = vehicleCode;
        this.vehiclePowerSource = vehiclePowerSource;
        this.vehicleClass     = vehicleClass;
        this.vehicleBrand     = vehicleBrand;
        this.vehicleModel     = vehicleModel;
        this.licencePlate     = licencePlate;
        this.rentalRatePerDay = rentalRatePerDay;
        this.customerId       = customerId;
        this.customerName     = customerName;
        this.customerIdNum    = customerIdNum;
        this.customerPhone    = customerPhone;
        this.staffID          = staffID;
        this.staffName        = staffName;
        this.rentDays         = rentDays;
        this.startDate        = startDate;
        this.endDate          = endDate;
        this.returnDate       = returnDate;
        this.paymentId        = paymentId;
        this.paymentMethod    = paymentMethod;
        this.price            = price;
        this.discount         = discount;
        this.extraDays        = extraDays;
        this.damageFee        = damageFee;
        this.deposit          = deposit;
        this.payDate          = payDate;
        this.paymentStatus    = paymentStatus;
        this.totalPaid        = totalPaid;
    }

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
