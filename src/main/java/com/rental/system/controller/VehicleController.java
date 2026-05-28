package com.rental.system.controller;

import com.rental.system.model.Vehicle;
import com.rental.system.model.Car;
import com.rental.system.model.Moto;
import com.rental.system.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @PostMapping("/cars")
    public ResponseEntity<Vehicle> registerCar(@RequestBody Car car) {
        vehicleService.registerNewVehicle(car);
        return ResponseEntity.ok(car);
    }

    @PostMapping("/motos")
    public ResponseEntity<Vehicle> registerMoto(@RequestBody Moto moto) {
        vehicleService.registerNewVehicle(moto);
        return ResponseEntity.ok(moto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable int id) {
        Vehicle vehicle = vehicleService.findById(id);
        if (vehicle != null) {
            return ResponseEntity.ok(vehicle);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Vehicle> getVehicleByCode(@PathVariable String code) {
        Vehicle vehicle = vehicleService.findByCode(code);
        if (vehicle != null) {
            return ResponseEntity.ok(vehicle);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable int id) {
        Vehicle vehicle = vehicleService.findById(id);
        if (vehicle != null) {
            vehicleService.removeVehicle(vehicle);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
