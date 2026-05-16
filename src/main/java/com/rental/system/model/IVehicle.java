package com.rental.system.model;

public interface IVehicle {

    // Getter Methods
    /** Unique sequential integer ID across all vehicles: 1, 2, 3, … */
    public int getVehicleId();

    /** Type-based code, e.g. "Model.Car-1", "Model.Moto-2" */
    public String getVehicleCode();

    /** Type of vehicle: "Model.Car" or "Model.Moto" */
//    public String getVehicleType();

    public String getPowerSource();

    public String getVehicleClass();

    public String getVehicleBrand();

    public String getVehicleModel();

    public double getRentalRatePerDay();

    public String getVehicleLicence();

    public String getLicencePlate();

    public boolean isAvailable();

    // Setter Methods

    public void setPowerSource(String powerSource);

    public void setVehicleClass(String vehicleClass);

    public void setVehicleBrand(String vehicleBrand);

    public void setVehicleModel(String vehicleModel);

    public void setRentalRatePerDay(double rentalRatePerDay);

    public void setAvailable(boolean available);

    public void setVehicleLicence(String vehicleLicence);

    public void setLicencePlate(String licencePlate);

}
