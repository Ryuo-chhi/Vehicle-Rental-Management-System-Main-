package com.rental.system.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "vehicles", indexes = {
    @Index(name = "idx_vehicle_available", columnList = "is_available")
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Vehicle implements IVehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private int vehicleId;        // global sequential ID: 1, 2, 3, …

    @Column(name = "vehicle_code", unique = true, nullable = false)
    private String vehicleCode;   // type-based code: Car-1, Car-2, …

    @Column(name = "power_source")
    private String powerSource; // "gasoline", "diesel", "electric", "hybrid"

    @Column(name = "vehicle_class")
    private String vehicleClass; // "sedan", "SUV", "truck", etc.

    @Column(name = "brand")
    private String vehicleBrand;

    @Column(name = "model")
    private String vehicleModel; // "Toyota Camry", "Ford F-150", etc.

    @Column(name = "rate_per_day")
    private double rentalRatePerDay;

    @Column(name = "licence")
    private String vehicleLicence;

    @Column(name = "licence_plate")
    private String licencePlate;

    @Column(name = "is_available")
    private boolean isAvailable;

    @Column(name = "image_url")
    private String imageUrl; // Can be a URL or local file path like /images/car1.jpg

    /**
     * SOFT DELETE FLAG: 
     * We never physically delete vehicles to preserve foreign key constraints for historical rent records.
     * When a vehicle is deleted from the UI, this flag is set to true. 
     * Repositories should use findByIsDeletedFalse() to ignore deleted vehicles.
     */
    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted = false;

    public Vehicle() {
        this.isAvailable = true;
    }

    public Vehicle(String vehicleType, String powerSource, String vehicleClass, String vehicleBrand, String vehicleModel, double rentalRatePerDay, String vehicleLicence, String licencePlate) {
        this.vehicleCode = vehicleType.equals("Moto") ? vehicleType + "-" + Moto.getMotoID() : vehicleType + "-" + Car.getCarID();
        this.setPowerSource(powerSource);
        this.setVehicleClass(vehicleClass);
        this.setVehicleBrand(vehicleBrand);
        this.setVehicleModel(vehicleModel);
        this.setRentalRatePerDay(rentalRatePerDay);
        this.isAvailable = true;
        this.setVehicleLicence(vehicleLicence);
        this.setLicencePlate(licencePlate);
    }

    
    //getter
    public int getVehicleId() {
        return vehicleId;
    }

    public String getVehicleCode() {
        return vehicleCode;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    //setter
    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setVehicleCode(String vehicleCode) {
        this.vehicleCode = vehicleCode;
    }

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

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setVehicleLicence(String vehicleLicence) {
        this.vehicleLicence = vehicleLicence;
    }



    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return vehicleId == vehicle.vehicleId && Objects.equals(licencePlate, vehicle.licencePlate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId, licencePlate);
    }

    @Override
    public String toString() {
        return " " +
                "vehicleId=" + vehicleId +
                ", vehicleCode='" + vehicleCode + '\'' +
                ", powerSource='" + powerSource + '\'' +
                ", vehicleClass='" + vehicleClass + '\'' +
                ", vehicleBrand='" + vehicleBrand + '\'' +
                ", vehicleModel='" + vehicleModel + '\'' +
                ", rentalRatePerDay=" + rentalRatePerDay + "$" +
                ", vehicleLicence='" + vehicleLicence + '\'' +
                ", licencePlate='" + licencePlate + '\'' +
                ", isAvailable=" + isAvailable +
                ' ';
    }
}
