package model;

public class Car extends Vehicle {
    private static int carID = 1;

    private int numberOfSeats;


    public Car(Vehicle vehicle, int numberOfSeats) {
        super(vehicle.getVehicleType(), vehicle.getPowerSource(), vehicle.getVehicleClass(), vehicle.getVehicleBrand(), vehicle.getVehicleModel(), vehicle.getRentalRatePerDay(), vehicle.getVehicleLicence(), vehicle.getLicencePlate());
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

    

}
