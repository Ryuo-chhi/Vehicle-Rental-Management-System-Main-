class Vehicle {
    int vehicleId;
    String powerSource;
    String vehicleClass;
    String vehicleBrand;
    String vehicleModel;
    double rentalRatePerDay;
    boolean isAvailable;

    static int countVehicleId = 1;

    Vehicle(String powerSource, String vehicleClass, String vehicleBrand, String vehicleModel, double rentalRatePerDay) {
        this.vehicleId = countVehicleId++;
        this.powerSource = powerSource;
        this.vehicleClass = vehicleClass;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.rentalRatePerDay = rentalRatePerDay>0 ? rentalRatePerDay : 0;
        this.isAvailable = true;
    }

    @Override
    public String toString() {
        return "Vehicle{vehicleId=" + vehicleId + ", powerSource='" + powerSource + "', vehicleClass='" + vehicleClass + "', vehicleBrand='" + vehicleBrand + "', vehicleModel='" + vehicleModel + "', rentalRatePerDay=" + rentalRatePerDay + "$, isAvailable=" + isAvailable + "}";
    }


}
