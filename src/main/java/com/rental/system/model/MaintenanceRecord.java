package com.rental.system.model;

public class MaintenanceRecord {
    private int maintenanceId;
    private int vehicleId;
    private String details;
    private double cost;
    private String startDate;
    private String endDate;
    private String status; // ONGOING or COMPLETED

    public MaintenanceRecord(int vehicleId, String details, double cost, String startDate) {
        this.vehicleId = vehicleId;
        this.details = details;
        this.cost = cost;
        this.startDate = startDate;
        this.endDate = "TBD";
        this.status = "ONGOING";
    }

    public MaintenanceRecord(int maintenanceId, int vehicleId, String details, double cost, String startDate, String endDate, String status) {
        this.maintenanceId = maintenanceId;
        this.vehicleId = vehicleId;
        this.details = details;
        this.cost = cost;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters and Setters
    public int getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(int maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Maintenance ID: %d | Vehicle ID: %d | Details: %s | Cost: $%.2f | Started: %s | Ended: %s | Status: %s",
                maintenanceId, vehicleId, details, cost, startDate, endDate, status);
    }
}
