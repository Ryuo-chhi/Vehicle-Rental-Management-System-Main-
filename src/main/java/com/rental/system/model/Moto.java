package com.rental.system.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@DiscriminatorValue("Moto")
public class Moto extends Vehicle {
    private static int motoID= 1;

    @Column(name = "helmet_included")
    private boolean helmetIncluded;

    public Moto() {
        super();
    }

    public Moto(Vehicle vehicle, boolean helmetIncluded) {
        super("Moto", vehicle.getPowerSource(), vehicle.getVehicleClass(), vehicle.getVehicleBrand(), vehicle.getVehicleModel(), vehicle.getRentalRatePerDay(), vehicle.getVehicleLicence(), vehicle.getLicencePlate());
        this.setHelmetIncluded(helmetIncluded);
        motoID++;
    }

    public Moto(String vehicleType,
                String powerSource,
                String vehicleClass,
                String vehicleBrand,
                String vehicleModel,
                double rentalRatePerDay,
                String vehicleLicence,
                String licencePlate,
                boolean helmetIncluded) {
        super(vehicleType, powerSource, vehicleClass, vehicleBrand,
              vehicleModel, rentalRatePerDay, vehicleLicence, licencePlate);
        this.setHelmetIncluded(helmetIncluded);
        motoID++;
    }


    public static int getMotoID() {
        return motoID;
    }

    
    public boolean isHelmetIncluded() {
        return helmetIncluded;
    }

    public void setHelmetIncluded(boolean helmetIncluded) {
        this.helmetIncluded = helmetIncluded;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Moto moto = (Moto) o;
        return this.getVehicleCode().equals(moto.getVehicleCode()) && Objects.equals(this.getLicencePlate(), moto.getLicencePlate());

    }
    @Override
    public String toString() {
        return "Moto{" +
                super.toString() + ", helmetIncluded=" + helmetIncluded +
                '}';
    }
}
