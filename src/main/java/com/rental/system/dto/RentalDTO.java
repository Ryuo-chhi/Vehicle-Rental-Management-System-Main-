package com.rental.system.dto;

import com.rental.system.model.Rent;

public class RentalDTO {
    private int rentId;
    private int rentDays;
    private String startDate;
    private String endDate;
    private String returnDate;
    private boolean status;
    private String vehicleSummary;
    private String customerSummary;
    private double totalExpected;

    public RentalDTO(Rent rent) {
        this.rentId = rent.getRentId();
        this.rentDays = rent.getRentDays();
        this.startDate = rent.getStartDate();
        this.endDate = rent.getEndDate();
        this.returnDate = rent.getReturnDate();
        this.status = rent.isStatus();
        this.vehicleSummary = rent.getVehicle() != null ? rent.getVehicle().getVehicleBrand() + " " + rent.getVehicle().getVehicleModel() : "Unknown Vehicle";
        this.customerSummary = rent.getCustomer() != null ? rent.getCustomer().getCustomerName() : "Unknown Customer";
        this.totalExpected = rent.getPayment() != null ? rent.getPayment().expectedTotal() : 0.0;
    }

    public int getRentId() { return rentId; }
    public int getRentDays() { return rentDays; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getReturnDate() { return returnDate; }
    public boolean isStatus() { return status; }
    public String getVehicleSummary() { return vehicleSummary; }
    public String getCustomerSummary() { return customerSummary; }
    public double getTotalExpected() { return totalExpected; }
}
