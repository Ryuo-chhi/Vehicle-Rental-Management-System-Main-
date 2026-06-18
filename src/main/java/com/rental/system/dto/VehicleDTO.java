package com.rental.system.dto;

public class VehicleDTO {
    private int vehicleId;
    private String vehicleCode;
    private String vehicleBrand;
    private String vehicleModel;
    private String vehicleClass;
    private String powerSource;
    private double rentalRatePerDay;
    private String imageUrl;
    private boolean isAvailable;

    public VehicleDTO() {}

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public String getVehicleCode() { return vehicleCode; }
    public void setVehicleCode(String vehicleCode) { this.vehicleCode = vehicleCode; }
    public String getVehicleBrand() { return vehicleBrand; }
    public void setVehicleBrand(String vehicleBrand) { this.vehicleBrand = vehicleBrand; }
    public String getVehicleModel() { return vehicleModel; }
    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }
    public String getVehicleClass() { return vehicleClass; }
    public void setVehicleClass(String vehicleClass) { this.vehicleClass = vehicleClass; }
    public String getPowerSource() { return powerSource; }
    public void setPowerSource(String powerSource) { this.powerSource = powerSource; }
    public double getRentalRatePerDay() { return rentalRatePerDay; }
    public void setRentalRatePerDay(double rentalRatePerDay) { this.rentalRatePerDay = rentalRatePerDay; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}
