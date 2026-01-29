public class Vehicle {
    int vehicleId;
    String vehiclePower;
    String vehicleClass;
    String vehicleModel;
    boolean isAvailable;

    static int nextVehicleId = 1; // this var keeps track of the next vehicle ID to assign. why static? because it's shared among all instances in order to ensure unique IDs.

    public Vehicle(String vehiclePower, String vehicleClass, String vehicleModel) {
        this.vehicleId = nextVehicleId++;
        this.vehiclePower = vehiclePower;
        this.vehicleClass = vehicleClass;
        this.vehicleModel = vehicleModel;
        this.isAvailable = true;
    }

    @Override
    public String toString() {
        return "Vehicle [id=" + vehicleId + ", power=" + vehiclePower + ", vehicleClass=" + vehicleClass + ", model="
                + vehicleModel
                + ", isAvailable=" + isAvailable + "]";
    }

}
