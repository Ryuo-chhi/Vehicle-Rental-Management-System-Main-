import java.util.Objects;

public class Vehicle {
    private int vehicleId;
    private String powerSource; // "gasoline", "diesel", "electric", "hybrid"
    private String vehicleClass; // "sedan", "SUV", "truck", etc.
    private String vehicleBrand;
    private String vehicleModel; // "Toyota Camry", "Ford F-150", etc.
    private double rentalRatePerDay;
    private String vehicleLicence;
    private String licencePlate;
    private boolean isAvailable;

    static int countVehicleId = 1; // this var keeps track of the next vehicle ID to assign. why static? because it's shared among all instances in order to ensure unique IDs.

    public Vehicle(String powerSource, String vehicleClass, String vehicleBrand, String vehicleModel, double rentalRatePerDay, String vehicleLicence, String licencePlate) {
        this.vehicleId = countVehicleId++;
        this.powerSource = powerSource;
        this.vehicleClass = vehicleClass;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.rentalRatePerDay = rentalRatePerDay > 0? rentalRatePerDay : 0;
        this.isAvailable = true;
        this.vehicleLicence = vehicleLicence;
        this.licencePlate = licencePlate;
    }
    //getter
    public int getVehicleId() {
        return vehicleId;
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

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public static int getCountVehicleId() {
        return countVehicleId;
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
        this.rentalRatePerDay = rentalRatePerDay > 0? rentalRatePerDay : 0;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(powerSource, vehicle.powerSource) && Objects.equals(vehicleClass, vehicle.vehicleClass) && Objects.equals(vehicleBrand, vehicle.vehicleBrand) && Objects.equals(vehicleModel, vehicle.vehicleModel) && Objects.equals(vehicleLicence, vehicle.vehicleLicence) && Objects.equals(licencePlate, vehicle.licencePlate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(powerSource, vehicleClass, vehicleBrand, vehicleModel, vehicleLicence, licencePlate);
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
