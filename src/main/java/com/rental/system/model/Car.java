package com.rental.system.model;

import java.util.Objects;

public class Car extends Vehicle {
    private static int carID = 1;

    private int numberOfSeats;


    public Car(Vehicle vehicle, int numberOfSeats) {
        super("Car", vehicle.getPowerSource(), vehicle.getVehicleClass(), vehicle.getVehicleBrand(), vehicle.getVehicleModel(), vehicle.getRentalRatePerDay(), vehicle.getVehicleLicence(), vehicle.getLicencePlate());
        this.setNumberOfSeats(numberOfSeats);
        carID++;

    }

    public Car(String vehicleType,
               String powerSource,
               String vehicleClass,
               String vehicleBrand,
               String vehicleModel,
               double rentalRatePerDay,
               String vehicleLicence,
               String licencePlate,
               int numberOfSeats) {
        super(vehicleType, powerSource, vehicleClass, vehicleBrand,
              vehicleModel, rentalRatePerDay, vehicleLicence, licencePlate);
        this.setNumberOfSeats(numberOfSeats);
        carID++;
    }

    public static int getCarID() {return carID;}

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        if(numberOfSeats <= 0){
            System.out.println("Number of seats must be greater than 0. Set to default value 4.");
            this.numberOfSeats = 4; // default value
            return;
        }
        this.numberOfSeats = numberOfSeats;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Car car = (Car) o;
        return this.getVehicleCode().equals(car.getVehicleCode()) && Objects.equals(this.getLicencePlate(), car.getLicencePlate());

    }

    @Override
    public String toString() {
        return "Car{" +
                super.toString() + ", numberOfSeats=" + numberOfSeats +
                '}';
    }
}
