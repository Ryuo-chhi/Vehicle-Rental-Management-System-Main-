package com.rental.system.controller;

import com.rental.system.model.Vehicle;
import com.rental.system.model.Car;
import com.rental.system.model.Moto;
import com.rental.system.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('MANAGER', 'REGULAR')")
    public ResponseEntity<Vehicle> registerCar(@RequestBody Car car) {
        vehicleService.registerNewVehicle(car);
        return ResponseEntity.ok(car);
    }

    @PutMapping("/cars/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'REGULAR')")
    public ResponseEntity<Vehicle> updateCar(@PathVariable int id, @RequestBody Car carDetails) {
        Vehicle existing = vehicleService.findById(id);
        if (existing instanceof Car) {
            Car car = (Car) existing;
            car.setVehicleBrand(carDetails.getVehicleBrand());
            car.setVehicleModel(carDetails.getVehicleModel());
            car.setPowerSource(carDetails.getPowerSource());
            car.setVehicleClass(carDetails.getVehicleClass());
            car.setRentalRatePerDay(carDetails.getRentalRatePerDay());
            car.setVehicleLicence(carDetails.getVehicleLicence());
            car.setLicencePlate(carDetails.getLicencePlate());
            car.setNumberOfSeats(carDetails.getNumberOfSeats());
            car.setImageUrl(carDetails.getImageUrl());
            car.setAvailable(carDetails.isAvailable());
            vehicleService.updateVehicleInDB(car);
            return ResponseEntity.ok(car);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/motos")
    @PreAuthorize("hasAnyRole('MANAGER', 'REGULAR')")
    public ResponseEntity<Vehicle> registerMoto(@RequestBody Moto moto) {
        vehicleService.registerNewVehicle(moto);
        return ResponseEntity.ok(moto);
    }

    @PutMapping("/motos/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'REGULAR')")
    public ResponseEntity<Vehicle> updateMoto(@PathVariable int id, @RequestBody Moto motoDetails) {
        Vehicle existing = vehicleService.findById(id);
        if (existing instanceof Moto) {
            Moto moto = (Moto) existing;
            moto.setVehicleBrand(motoDetails.getVehicleBrand());
            moto.setVehicleModel(motoDetails.getVehicleModel());
            moto.setPowerSource(motoDetails.getPowerSource());
            moto.setVehicleClass(motoDetails.getVehicleClass());
            moto.setRentalRatePerDay(motoDetails.getRentalRatePerDay());
            moto.setVehicleLicence(motoDetails.getVehicleLicence());
            moto.setLicencePlate(motoDetails.getLicencePlate());
            moto.setHelmetIncluded(motoDetails.isHelmetIncluded());
            moto.setImageUrl(motoDetails.getImageUrl());
            moto.setAvailable(motoDetails.isAvailable());
            vehicleService.updateVehicleInDB(moto);
            return ResponseEntity.ok(moto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('MANAGER', 'REGULAR')")
    public ResponseEntity<java.util.Map<String, String>> uploadVehicleImage(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", "File is empty"));
        }
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = System.currentTimeMillis() + "_" + java.util.UUID.randomUUID().toString().substring(0, 8) + extension;
            
            // Paths
            java.nio.file.Path srcPath = java.nio.file.Paths.get("src", "main", "resources", "static", "images", "vehicles", fileName);
            java.nio.file.Path targetPath = java.nio.file.Paths.get("target", "classes", "static", "images", "vehicles", fileName);
            
            // Create directories if they don't exist
            java.nio.file.Files.createDirectories(srcPath.getParent());
            java.nio.file.Files.createDirectories(targetPath.getParent());
            
            // Save files
            java.nio.file.Files.write(srcPath, file.getBytes());
            java.nio.file.Files.write(targetPath, file.getBytes());
            
            String imageUrl = "/images/vehicles/" + fileName;
            return ResponseEntity.ok(java.util.Map.of("imageUrl", imageUrl));
        } catch (java.io.IOException e) {
            return ResponseEntity.status(500).body(java.util.Map.of("error", "Failed to store file: " + e.getMessage()));
        }
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
    @PreAuthorize("hasAnyRole('MANAGER', 'REGULAR')")
    public ResponseEntity<Void> deleteVehicle(@PathVariable int id) {
        Vehicle vehicle = vehicleService.findById(id);
        if (vehicle != null) {
            vehicleService.removeVehicle(vehicle);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
