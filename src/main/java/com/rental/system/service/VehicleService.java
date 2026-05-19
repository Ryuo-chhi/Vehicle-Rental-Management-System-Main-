package com.rental.system.service;

import com.rental.system.model.Vehicle;
import com.rental.system.model.Car;
import com.rental.system.model.Moto;
import com.rental.system.database.DatabaseMapper;
import java.util.ArrayList;

public class VehicleService {
    private ArrayList<Vehicle> garage;
    private int carCount;
    private int motoCount;
    private static int vehicleIDCounter = 0;

    public VehicleService() {
        this.garage = new ArrayList<>();
        this.carCount = 0;
        this.motoCount = 0;
    }

    // --- The Memory ---
    public ArrayList<Vehicle> getAllVehicles() {
        return garage;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.garage = vehicles;
        updateCounts();
        updateIDCounter();
    }

    public int getVehicleCount() {
        return garage.size();
    }

    public int getCarCount() {
        return carCount;
    }

    public int getMotoCount() {
        return motoCount;
    }

    public static int getNextVehicleID() {
        return vehicleIDCounter;
    }

    // --- The Brain (Pure Logic) ---

    public void registerNewVehicle(Vehicle vehicle) {
        garage.add(vehicle);
        if (vehicle instanceof Car) carCount++;
        else if (vehicle instanceof Moto) motoCount++;
        
        DatabaseMapper.saveNewVehicle(vehicle);
        
        if (vehicle.getVehicleId() >= vehicleIDCounter) {
            vehicleIDCounter = vehicle.getVehicleId() + 1;
        }
    }

    public void removeVehicle(Vehicle vehicle) {
        if (garage.remove(vehicle)) {
            if (vehicle instanceof Car) carCount--;
            else if (vehicle instanceof Moto) motoCount--;
            DatabaseMapper.deleteVehicle(vehicle.getVehicleId());
        }
    }

    public void updateVehicleInDB(Vehicle vehicle) {
        DatabaseMapper.updateVehicle(vehicle);
    }

    public Vehicle findById(int id) {
        for (Vehicle v : garage) {
            if (v.getVehicleId() == id) return v;
        }
        return null;
    }

    public Vehicle findByCode(String code) {
        if (code == null) return null;
        for (Vehicle v : garage) {
            if (v.getVehicleCode() != null && v.getVehicleCode().equalsIgnoreCase(code.trim())) {
                return v;
            }
        }
        return null;
    }

    private void updateCounts() {
        carCount = 0;
        motoCount = 0;
        for (Vehicle v : garage) {
            if (v instanceof Car) carCount++;
            else if (v instanceof Moto) motoCount++;
        }
    }

    private void updateIDCounter() {
        for (Vehicle v : garage) {
            if (v.getVehicleId() >= vehicleIDCounter) {
                vehicleIDCounter = v.getVehicleId() + 1;
            }
        }
    }

    public void generateDefaultVehicles() {
        String[][] vehiclesData = {
                { "Car", "gasoline", "SUV", "Ford", "Escape", "300", "VL-01-AB-1234", "PP-1000" },
                { "Car", "electric", "Sedan", "Tesla", "Model 3", "500", "VL-02-CD-5678", "PP-1001" },
                { "Car", "diesel", "Truck", "Toyota", "Hilux", "400", "VL-03-EF-9012", "PP-1002" },
                { "Car", "hybrid", "Hatchback", "Honda", "Insight", "350", "VL-04-GH-3456", "PP-1003" },
                { "Car", "gasoline", "Coupe", "BMW", "M4", "600", "VL-05-IJ-7890", "PP-1004" },
                { "Moto", "gasoline", "Sport", "Honda", "CBR600RR", "75", "MOTO-LIC-2026", "ABC-1234" },
                { "Moto", "gasoline", "Sport", "Honda", "CBR600RR", "75", "MOTO-LIC-2026", "ABC-1234" },
        };

        for (String[] v : vehiclesData) {
            String type = v[0];
            double price = Double.parseDouble(v[5]);
            Vehicle vehicle = switch (type) {
                case "Moto" -> new Moto("Moto", v[1], v[2], v[3], v[4], price, v[6], v[7], true);
                default -> new Car("Car", v[1], v[2], v[3], v[4], price, v[6], v[7], 4);
            };
            garage.add(vehicle);
            if ("Moto".equals(type)) motoCount++;
            else carCount++;
            
            if (vehicle.getVehicleId() >= vehicleIDCounter) {
                vehicleIDCounter = vehicle.getVehicleId() + 1;
            }
        }
    }
}
