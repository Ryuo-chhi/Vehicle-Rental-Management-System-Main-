package com.rental.system.controller;

import com.rental.system.model.MaintenanceRecord;
import com.rental.system.service.OtherManagementService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance-records")
@PreAuthorize("hasAnyRole('MANAGER', 'REGULAR')")
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

    @GetMapping("/vehicle/{vehicleId}")
    public List<MaintenanceRecord> getMaintenanceByVehicle(@PathVariable int vehicleId) {
        return otherManagementService.getMaintenanceRecordsByVehicle(vehicleId);
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

    @Data
    @NoArgsConstructor
    public static class MaintenanceSendRequest {
        private int vehicleId;
        private String details;
        private double cost;
        private String startDate;
    }

    @Data
    @NoArgsConstructor
    public static class MaintenanceCompleteRequest {
        private int vehicleId;
        private String endDate;
        private double actualCost;
    }
}
