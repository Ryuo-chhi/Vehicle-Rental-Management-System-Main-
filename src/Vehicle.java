public class Vehicle {
    int vehicleId;
    String vehiclePowerSource; // "gasoline", "electric", "hybrid"
    String vehicleClass; // "sedan", "SUV", "truck", etc.
    String vehicleModel; // "Toyota Camry", "Ford F-150", etc.
    boolean isAvailable; 

    static int nextVehicleId = 1; // this var keeps track of the next vehicle ID to assign. why static? because it's shared among all instances in order to ensure unique IDs.

    public Vehicle(String vehiclePowerSource, String vehicleClass, String vehicleModel) {
        this.vehicleId = nextVehicleId++;
        this.vehiclePowerSource = vehiclePowerSource;
        this.vehicleClass = vehicleClass;
        this.vehicleModel = vehicleModel;
        this.isAvailable = true;
    }

    @Override
    public String toString() {
        return "Vehicle [id=" + vehicleId + ", power=" + vehiclePowerSource + ", vehicleClass=" + vehicleClass + ", model="
                + vehicleModel
                + ", isAvailable=" + isAvailable + "]";
    }

}
