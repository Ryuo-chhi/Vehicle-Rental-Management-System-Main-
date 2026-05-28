package com.rental.system.service;

import com.rental.system.model.Vehicle;
import com.rental.system.model.Car;
import com.rental.system.model.Moto;
import com.rental.system.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@SuppressWarnings("null")
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // --- The Memory ---
    public ArrayList<Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicleRepository.findAll());
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        vehicleRepository.saveAll(vehicles);
    }

    public int getVehicleCount() {
        return (int) vehicleRepository.count();
    }

    public int getCarCount() {
        int count = 0;
        for (Vehicle v : vehicleRepository.findAll()) {
            if (v instanceof Car) count++;
        }
        return count;
    }

    public int getMotoCount() {
        int count = 0;
        for (Vehicle v : vehicleRepository.findAll()) {
            if (v instanceof Moto) count++;
        }
        return count;
    }

    // --- The Brain (Pure Logic) ---

    public void registerNewVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        vehicleRepository.delete(vehicle);
    }

    public void updateVehicleInDB(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    public Vehicle findById(int id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    public Vehicle findByCode(String code) {
        if (code == null) return null;
        return vehicleRepository.findByVehicleCode(code.trim()).orElse(null);
    }

    public void generateDefaultVehicles() {
        if (vehicleRepository.count() > 0) return;
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
            vehicleRepository.save(vehicle);
        }
    }
}
