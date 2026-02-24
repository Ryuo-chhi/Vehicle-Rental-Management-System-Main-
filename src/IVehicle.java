public interface IVehicle {

    // Getter Methods
    public String getVehicleId() ;

    public String getVehicleBrand();

    public String getVehicleModel() ;

    public double getRentalRatePerDay() ;

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

    //other method
    boolean canBeRented(Customer customer);
}
