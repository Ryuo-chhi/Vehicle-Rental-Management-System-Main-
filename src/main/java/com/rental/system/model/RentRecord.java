package com.rental.system.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rent_records")
public class RentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private int recordId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rent_id", nullable = false)
    private Rent rent;

    // Snapshot of only what can change (payment details and final return date)
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "price")
    private double price;
    @Column(name = "discount")
    private double discount;
    @Column(name = "extra_days")
    private int extraDays;
    @Column(name = "damage_fee")
    private double damageFee;
    @Column(name = "deposit")
    private double deposit;
    @Column(name = "pay_date")
    private String payDate;
    @Column(name = "payment_status")
    private String paymentStatus;
    @Column(name = "total_paid")
    private double totalPaid;
    @Column(name = "return_date")
    private String returnDate;

    public RentRecord() {}

    /**
     * Builds a record from a completed Rent.
     */
    public RentRecord(Rent rent) {
        this.rent = rent;
        if (rent.getPayment() != null) {
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
        this.returnDate = rent.getReturnDate();
    }

    // ===== GETTERS =====
    public int getRecordId()          { return recordId; }
    public Rent getRent()             { return rent; }
    public String getPaymentMethod()  { return paymentMethod; }
    public double getPrice()          { return price; }
    public double getDiscount()       { return discount; }
    public int getExtraDays()         { return extraDays; }
    public double getDamageFee()      { return damageFee; }
    public double getDeposit()        { return deposit; }
    public String getPayDate()        { return payDate; }
    public String getPaymentStatus()  { return paymentStatus; }
    public double getTotalPaid()      { return totalPaid; }
    public String getReturnDate()     { return returnDate; }

    // ===== DELEGATE HELPER GETTERS FOR BACKWARD COMPATIBILITY =====
    public int getRentId() {
        return rent != null ? rent.getRentId() : 0;
    }

    public int getVehicleId() {
        return (rent != null && rent.getVehicle() != null) ? rent.getVehicle().getVehicleId() : 0;
    }

    public int getCustomerId() {
        return (rent != null && rent.getCustomer() != null) ? rent.getCustomer().getCustomerId() : 0;
    }

    @Override
    public String toString() {
        return "RentRecord{" +
                "recordId=" + recordId +
                ", rentId=" + getRentId() +
                ", vehicleId=" + getVehicleId() +
                ", customerId=" + getCustomerId() +
                ", returnDate='" + returnDate + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", totalPaid=$" + totalPaid +
                '}';
    }
}
