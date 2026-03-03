package model;

public class Car extends Vehicle {
    private static int carID = 1;

    private int doorOfCar;


    public Car(Vehicle vehicle, int doorOfCar) {
        super(vehicle.getVehicleType(), vehicle.getPowerSource(), vehicle.getVehicleClass(), vehicle.getVehicleBrand(), vehicle.getVehicleModel(), vehicle.getRentalRatePerDay(), vehicle.getVehicleLicence(), vehicle.getLicencePlate());
        this.setDoorOfCar(doorOfCar);
        carID++;

    }

    public int getDoorOfCar() {
        return doorOfCar;
    }

    public void setDoorOfCar(int doorOfCar) {
        if(doorOfCar <= 0){
            System.out.println("Number of doors must be greater than 0. Set to default value 4.");
            this.doorOfCar = 4; // default value
            return;
        }
        this.doorOfCar = doorOfCar;
    }

    @Override
    public boolean canBeRented(Customer customer) {
        if (!this.isAvailable()) {
            System.out.println("Vehicle Not Available");
            return false;
        }
        if(customer.getDriverLicensePhoto() == null || customer.getDriverLicensePhoto().isEmpty()) {
            System.out.println("Customer does not have Driver Licence");
            return false;
        }
        return true;
    }



    public static int getCarID() {return carID;}

}
