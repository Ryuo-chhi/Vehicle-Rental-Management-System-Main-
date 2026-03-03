import java.util.Objects;

public class Car extends Vehicle {
    private static int countCar = 0;    // per-type counter for Car code
    private static int carID = 1;

    public Car(String vehicleType, String powerSource, String vehicleClass, String vehicleBrand, String vehicleModel, double rentalRatePerDay, String vehicleLicence, String licencePlate) {
        super(vehicleType, powerSource, vehicleClass, vehicleBrand, vehicleModel, rentalRatePerDay, vehicleLicence, licencePlate);
        countCar++;
        carID++;
    }

    @Override
    public boolean canBeRented(Customer customer) {
        if (!this.isAvailable()) {
            System.out.println("Vehicle Not Available");
            return false;
        }
        if(customer.getDriverLicensePhoto() == null){
            System.out.println("Customer does not have Driver Licence");
            return false;
        }
        return true;
    }

    public static int getCountCar() {
        return countCar;
    }
    public static int getCarID() {return carID;}

}
