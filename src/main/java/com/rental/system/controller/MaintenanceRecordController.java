package com.rental.system.controller;

import com.rental.system.model.MaintenanceRecord;
import com.rental.system.service.OtherManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance-records")
public class MaintenanceRecordController {
    private final OtherManagementService otherManagementService;

    @Autowired
    public MaintenanceRecordController(OtherManagementService otherManagementService) {
        this.otherManagementService = otherManagementService;
    }

    @GetMapping
    public List<MaintenanceRecord> getMaintenanceHistory() {
        return otherManagementService.getAllMaintenanceRecords();
    }

    @PostMapping
    public ResponseEntity<?> sendVehicleToMaintenance(@RequestBody MaintenanceSendRequest request) {
        boolean success = otherManagementService.sendVehicleToMaintenance(
                request.getVehicleId(),
                request.getDetails(),
                request.getCost(),
                request.getStartDate()
        );
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Vehicle cannot be sent to maintenance (not found or already busy).");
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completeVehicleMaintenance(@RequestBody MaintenanceCompleteRequest request) {
        boolean success = otherManagementService.completeVehicleMaintenance(
                request.getVehicleId(),
                request.getEndDate(),
                request.getActualCost()
        );
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Vehicle is not currently in maintenance.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceRecord> updateMaintenanceRecord(@PathVariable int id, @RequestBody MaintenanceRecord record) {
        return otherManagementService.updateMaintenanceRecord(id, record)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMaintenanceRecord(@PathVariable int id) {
        if (otherManagementService.deleteMaintenanceRecord(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    public static class MaintenanceSendRequest {
        private int vehicleId;
        private String details;
        private double cost;
        private String startDate;

        public int getVehicleId() { return vehicleId; }
        public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
        public String getDetails() { return details; }
        public void setDetails(String details) { this.details = details; }
        public double getCost() { return cost; }
        public void setCost(double cost) { this.cost = cost; }
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
    }

    public static class MaintenanceCompleteRequest {
        private int vehicleId;
        private String endDate;
        private double actualCost;

        public int getVehicleId() { return vehicleId; }
        public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public double getActualCost() { return actualCost; }
        public void setActualCost(double actualCost) { this.actualCost = actualCost; }
    }
}
