package model;

import controller.Garage;

import java.util.Objects;

public class Vehicle implements IVehicle {
    private int vehicleId;        // global sequential ID: 1, 2, 3, …
    private String vehicleCode;   // type-based code: Car-1, Car-2, …
    private String vehicleType; // fixed type label for DB / display
    private String powerSource; // "gasoline", "diesel", "electric", "hybrid"
    private String vehicleClass; // "sedan", "SUV", "truck", etc.
    private String vehicleBrand;
    private String vehicleModel; // "Toyota Camry", "Ford F-150", etc.
    private double rentalRatePerDay;
    private String vehicleLicence;
    private String licencePlate;
    private boolean isAvailable;


    public Vehicle(String vehicleType,String powerSource, String vehicleClass, String vehicleBrand, String vehicleModel, double rentalRatePerDay, String vehicleLicence, String licencePlate) {
        this.vehicleId = Garage.getVehicleCount() + 1; // assign current count + 1 as ID
        this.vehicleCode = vehicleType.equals("Moto") ?  vehicleType + "-" + Moto.getMotoID() : vehicleType + "-" + Car.getCarID();
        this.vehicleType = vehicleType;
        this.setPowerSource(powerSource);
        this.setVehicleClass(vehicleClass);
        this.setVehicleBrand(vehicleBrand);
        this.setVehicleModel(vehicleModel);
        this.setRentalRatePerDay(rentalRatePerDay);
        this.isAvailable = true;
        this.setVehicleLicence(vehicleLicence);
        this.setLicencePlate(licencePlate);
    }

    @Override
    public boolean canBeRented(Customer customer) {
        return false;
    }
    
    //getter
    public int getVehicleId() {
        return vehicleId;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getPowerSource() {
        return powerSource;
    }

    public String getVehicleClass() {
        return vehicleClass;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public double getRentalRatePerDay() {
        return rentalRatePerDay;
    }

    public String getVehicleLicence() {
        return vehicleLicence;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    //setter

    public void setPowerSource(String powerSource) {
        this.powerSource = powerSource;
    }

    public void setVehicleClass(String vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public void setRentalRatePerDay(double rentalRatePerDay) {
        this.rentalRatePerDay = rentalRatePerDay > 0? rentalRatePerDay : 0.0;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setVehicleLicence(String vehicleLicence) {
        this.vehicleLicence = vehicleLicence;
    }



    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle car = (Vehicle) o;
        return vehicleId == car.vehicleId && Objects.equals(getLicencePlate(), car.getLicencePlate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId, licencePlate);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", vehicleType='" + vehicleType + '\'' +
                ", vehicleCode='" + vehicleCode + '\'' +
                ", powerSource='" + powerSource + '\'' +
                ", vehicleClass='" + vehicleClass + '\'' +
                ", vehicleBrand='" + vehicleBrand + '\'' +
                ", vehicleModel='" + vehicleModel + '\'' +
                ", rentalRatePerDay=" + rentalRatePerDay + "$" +
                ", vehicleLicence='" + vehicleLicence + '\'' +
                ", licencePlate='" + licencePlate + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
