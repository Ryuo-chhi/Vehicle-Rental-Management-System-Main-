package com.rental.system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "maintenance_records")
public class MaintenanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maintenance_id")
    private int maintenanceId;

    @Column(name = "vehicle_id")
    private int vehicleId;

    @Column(name = "details")
    private String details;

    @Column(name = "cost")
    private double cost;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "status")
    private String status; // ONGOING or COMPLETED

    public MaintenanceRecord(int vehicleId, String details, double cost, String startDate) {
        this.vehicleId = vehicleId;
        this.details = details;
        this.cost = cost;
        this.startDate = startDate;
        this.endDate = "TBD";
        this.status = "ONGOING";
    }

    public MaintenanceRecord(int maintenanceId, int vehicleId, String details, double cost, String startDate, String endDate, String status) {
        this.maintenanceId = maintenanceId;
        this.vehicleId = vehicleId;
        this.details = details;
        this.cost = cost;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Maintenance ID: %d | Vehicle ID: %d | Details: %s | Cost: $%.2f | Started: %s | Ended: %s | Status: %s",
                maintenanceId, vehicleId, details, cost, startDate, endDate, status);
    }
}

