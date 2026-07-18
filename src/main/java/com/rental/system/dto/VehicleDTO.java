package com.rental.system.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VehicleDTO {
    private int vehicleId;
    private String vehicleCode;
    private String vehicleBrand;
    private String vehicleModel;
    private String vehicleClass;
    private String powerSource;
    private double rentalRatePerDay;
    private String imageUrl;
    private boolean isAvailable;
}
