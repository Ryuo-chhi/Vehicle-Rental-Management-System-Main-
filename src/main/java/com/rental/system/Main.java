package com.rental.system;

import com.rental.system.service.CustomerService;
import com.rental.system.service.OtherManagementService;
import com.rental.system.service.StaffService;
import com.rental.system.service.VehicleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner initData(VehicleService vehicleService,
                                      CustomerService customerService,
                                      StaffService staffService,
                                      OtherManagementService otherManagementService) {
        return args -> {
            System.out.println("Initializing system settings...");
            if (otherManagementService.getAllSettings().isEmpty()) {
                otherManagementService.updateSetting("TAX_RATE", "10.0");
                otherManagementService.updateSetting("LATE_PENALTY_MULTIPLIER", "1.5");
                otherManagementService.updateSetting("MAX_RENTAL_DURATION", "30");
            }
            // Trigger refresh of holder configurations
            otherManagementService.initSettings();

            System.out.println("Initializing default staff...");
            staffService.generateDefaultStaff();

            System.out.println("Initializing default customers...");
            customerService.generateDefaultCustomers();

            System.out.println("Initializing default vehicles...");
            vehicleService.generateDefaultVehicles();

            System.out.println("Initialization completed successfully!");
        };
    }
}
