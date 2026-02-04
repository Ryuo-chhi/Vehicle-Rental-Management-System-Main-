public class Vehicle {
    int vehicleId;
    String powerSource; // "gasoline", "diesel", "electric", "hybrid"
    String vehicleClass; // "sedan", "SUV", "truck", etc.
    String vehicleBrand;
    String vehicleModel; // "Toyota Camry", "Ford F-150", etc.
    double rentalRatePerDay;
    String vehicleLicence;
    String licencePlate;
    boolean isAvailable;

    static int countVehicleId = 1; // this var keeps track of the next vehicle ID to assign. why static? because it's shared among all instances in order to ensure unique IDs.

    public Vehicle(String powerSource, String vehicleClass, String vehicleBrand, String vehicleModel, double rentalRatePerDay, String vehicleLicence, String licencePlate) {
        this.vehicleId = countVehicleId++;
        this.powerSource = powerSource;
        this.vehicleClass = vehicleClass;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.rentalRatePerDay = rentalRatePerDay;
        this.isAvailable = true;
        this.vehicleLicence = vehicleLicence;
        this.licencePlate = licencePlate;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
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
