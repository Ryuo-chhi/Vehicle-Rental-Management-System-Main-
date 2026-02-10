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

    private static int countVehicleId = 1;

    public Vehicle(String powerSource, String vehicleClass, String vehicleBrand, String vehicleModel, double rentalRatePerDay, String vehicleLicence, String licencePlate) {
        this.vehicleId = countVehicleId++;
        this.powerSource = powerSource;
        this.vehicleClass = vehicleClass;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.setRentalRatePerDay(rentalRatePerDay);
        this.isAvailable = true;
        this.vehicleLicence = vehicleLicence;
        this.licencePlate = licencePlate;
    }
    //getter
    public int getVehicleId() {
        return vehicleId;
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

    public String getLicencePlate() {
        return licencePlate;
    }

    public boolean isAvailable() {
        return !isAvailable;
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
        this.rentalRatePerDay = rentalRatePerDay > 0? rentalRatePerDay : 0.0;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(getVehicleBrand(), vehicle.getVehicleBrand()) && Objects.equals(getVehicleModel(), vehicle.getVehicleModel()) && Objects.equals(getLicencePlate(), vehicle.getLicencePlate());
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
