package com.rental.system.service;

import com.rental.system.model.MaintenanceRecord;
import com.rental.system.model.Promotion;
import com.rental.system.model.SystemSetting;
import com.rental.system.model.Vehicle;
import com.rental.system.config.SystemSettingsHolder;
import com.rental.system.repository.MaintenanceRecordRepository;
import com.rental.system.repository.PromotionRepository;
import com.rental.system.repository.SystemSettingRepository;
import com.rental.system.repository.VehicleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@SuppressWarnings("null")
public class OtherManagementService {
    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final PromotionRepository promotionRepository;
    private final SystemSettingRepository systemSettingRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public OtherManagementService(MaintenanceRecordRepository maintenanceRecordRepository,
                                  PromotionRepository promotionRepository,
                                  SystemSettingRepository systemSettingRepository,
                                  VehicleRepository vehicleRepository) {
        this.maintenanceRecordRepository = maintenanceRecordRepository;
        this.promotionRepository = promotionRepository;
        this.systemSettingRepository = systemSettingRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @PostConstruct
    public void initSettings() {
        try {
            // Load settings from DB into SystemSettingsHolder
            double taxRate = Double.parseDouble(getSettingValue("TAX_RATE", "0.0"));
            double penalty = Double.parseDouble(getSettingValue("LATE_PENALTY_MULTIPLIER", "1.5"));
            int duration = Integer.parseInt(getSettingValue("MAX_RENTAL_DURATION", "30"));

            SystemSettingsHolder.setTaxRate(taxRate);
            SystemSettingsHolder.setPenaltyMultiplier(penalty);
            SystemSettingsHolder.setMaxRentalDuration(duration);
        } catch (Exception e) {
            // Table might not exist yet (e.g., on H2 clean startup before CommandLineRunner builds schema)
            System.err.println("Warning: Could not initialize system settings from DB on startup (tables may not exist yet). Setting defaults.");
            SystemSettingsHolder.setTaxRate(0.0);
            SystemSettingsHolder.setPenaltyMultiplier(1.5);
            SystemSettingsHolder.setMaxRentalDuration(30);
        }
    }

    private String getSettingValue(String key, String defaultValue) {
        return systemSettingRepository.findById(key)
                .map(SystemSetting::getSettingValue)
                .orElse(defaultValue);
    }

    public void updateSetting(String key, String value) {
        systemSettingRepository.save(new SystemSetting(key, value));
        // Update SystemSettingsHolder in real-time
        switch (key) {
            case "TAX_RATE" -> SystemSettingsHolder.setTaxRate(Double.parseDouble(value));
            case "LATE_PENALTY_MULTIPLIER" -> SystemSettingsHolder.setPenaltyMultiplier(Double.parseDouble(value));
            case "MAX_RENTAL_DURATION" -> SystemSettingsHolder.setMaxRentalDuration(Integer.parseInt(value));
        }
    }

    public List<SystemSetting> getAllSettings() {
        return systemSettingRepository.findAll();
    }

    // Maintenance
    public boolean sendVehicleToMaintenance(int vehicleId, String details, double cost, String startDate) {
        Optional<Vehicle> opt = vehicleRepository.findById(vehicleId);
        if (opt.isPresent() && opt.get().isAvailable()) {
            Vehicle vehicle = opt.get();
            vehicle.setAvailable(false);
            vehicleRepository.save(vehicle);

            MaintenanceRecord record = new MaintenanceRecord(vehicleId, details, cost, startDate);
            maintenanceRecordRepository.save(record);
            return true;
        }
        return false;
    }

    public boolean completeVehicleMaintenance(int vehicleId, String endDate, double actualCost) {
        List<MaintenanceRecord> ongoing = maintenanceRecordRepository.findByVehicleIdAndStatus(vehicleId, "ONGOING");
        if (!ongoing.isEmpty()) {
            MaintenanceRecord record = ongoing.get(0);
            record.setEndDate(endDate);
            record.setCost(actualCost);
            record.setStatus("COMPLETED");
            maintenanceRecordRepository.save(record);

            vehicleRepository.findById(vehicleId).ifPresent(vehicle -> {
                vehicle.setAvailable(true);
                vehicleRepository.save(vehicle);
            });
            return true;
        }
        return false;
    }

    public List<MaintenanceRecord> getAllMaintenanceRecords() {
        return maintenanceRecordRepository.findAll();
    }

    public List<MaintenanceRecord> getMaintenanceRecordsByVehicle(int vehicleId) {
        return maintenanceRecordRepository.findByVehicleId(vehicleId);
    }

    public Optional<MaintenanceRecord> updateMaintenanceRecord(int id, MaintenanceRecord updatedRecord) {
        return maintenanceRecordRepository.findById(id).map(existing -> {
            existing.setDetails(updatedRecord.getDetails());
            existing.setCost(updatedRecord.getCost());
            existing.setStartDate(updatedRecord.getStartDate());
            existing.setEndDate(updatedRecord.getEndDate());
            existing.setStatus(updatedRecord.getStatus());
            return maintenanceRecordRepository.save(existing);
        });
    }

    public boolean deleteMaintenanceRecord(int id) {
        if (maintenanceRecordRepository.existsById(id)) {
            maintenanceRecordRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Promotions
    public void savePromotion(Promotion promotion) {
        promotionRepository.save(promotion);
    }

    public Optional<Promotion> findPromotionByCode(String code) {
        return promotionRepository.findByCode(code);
    }

    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    public Optional<Promotion> updatePromotion(int id, Promotion updatedPromotion) {
        return promotionRepository.findById(id).map(existing -> {
            existing.setCode(updatedPromotion.getCode());
            existing.setDiscountPercent(updatedPromotion.getDiscountPercent());
            existing.setActive(updatedPromotion.isActive());
            return promotionRepository.save(existing);
        });
    }

    public boolean deletePromotion(int id) {
        if (promotionRepository.existsById(id)) {
            promotionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
