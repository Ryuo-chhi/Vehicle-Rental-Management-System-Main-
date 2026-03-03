import java.util.Objects;

public class Moto extends Vehicle {
    private static int countMoto= 0;    // per-type counter for Car code
    private static int motoID= 1;    // per-type counter for Car code


    public Moto(String vehicleType, String powerSource, String vehicleClass, String vehicleBrand, String vehicleModel, double rentalRatePerDay, String vehicleLicence, String licencePlate) {
        super(vehicleType, powerSource, vehicleClass, vehicleBrand, vehicleModel, rentalRatePerDay, vehicleLicence, licencePlate);
        countMoto++;
        motoID++;
    }

    @Override
    public boolean canBeRented(Customer customer) {
        if (!this.isAvailable()) {
            System.out.println("Vehicle Not Available");
            return false;
        }
        if (customer.getIDCardPhoto() == null) {
            System.out.println("Customer does not have ID Card");
            return false;
        }
        if (customer.getDriverLicensePhoto() == null) {
            System.out.println("Customer does not have Motorcycle Driver License");
            return false;
        }
        return true;
    }

    public static int getCountMoto() {
        return countMoto;
    }
    public static int getMotoID() {
        return motoID;
    }


}
