import java.util.Objects;

public class Car implements IVehicle{
    private String vehicleId;
    private String powerSource; // "gasoline", "diesel", "electric", "hybrid"
    private String vehicleClass; // "sedan", "SUV", "truck", etc.
    private String vehicleBrand;
    private String vehicleModel; // "Toyota Camry", "Ford F-150", etc.
    private double rentalRatePerDay;
    private String vehicleLicence;
    private String licencePlate;
    private boolean isAvailable;

    private static int countVehicleId = 1;

    public Car(String powerSource, String vehicleClass, String vehicleBrand, String vehicleModel, double rentalRatePerDay, String vehicleLicence, String licencePlate) {
        this.vehicleId = "Car-" + countVehicleId++;
        this.setPowerSource(powerSource);
        this.setVehicleClass(vehicleClass);
        this.setVehicleBrand(vehicleBrand);
        this.setVehicleModel(vehicleModel);
        this.setRentalRatePerDay(rentalRatePerDay);
        this.isAvailable = true;
        this.setVehicleLicence(vehicleLicence);
        this.setLicencePlate(licencePlate);
    }

    @Override
    public boolean canBeRented(Customer customer) {
        if(!isAvailable){
            System.out.println("Vehicle Not Available");
            return false;
        }
        if(customer.getDriverLicensePhoto() == null){
            System.out.println("Customer does not have Driver Licence");
            return false;
        }
        return true;
    }
    
    //getter
    public String getVehicleId() {
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
        this.rentalRatePerDay = rentalRatePerDay > 0? rentalRatePerDay : 0.0;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setVehicleLicence(String vehicleLicence) {
        this.vehicleLicence = vehicleLicence;
    }



    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(getVehicleBrand(), car.getVehicleBrand()) && Objects.equals(getVehicleModel(), car.getVehicleModel()) && Objects.equals(getLicencePlate(), car.getLicencePlate());
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
