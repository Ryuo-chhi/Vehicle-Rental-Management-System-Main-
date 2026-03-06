package model;

import java.util.Objects;

public class Moto extends Vehicle {
    private static int motoID= 1;

    private boolean helmetIncluded;

    public Moto(Vehicle vehicle, boolean helmetIncluded) {
        super(vehicle.getVehicleType(), vehicle.getPowerSource(), vehicle.getVehicleClass(), vehicle.getVehicleBrand(), vehicle.getVehicleModel(), vehicle.getRentalRatePerDay(), vehicle.getVehicleLicence(), vehicle.getLicencePlate());
        this.helmetIncluded = helmetIncluded;
        motoID++;
    }


    public static int getMotoID() {
        return motoID;
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
