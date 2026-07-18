package com.rental.system.controller;

import com.rental.system.model.Rent;
import com.rental.system.model.RentRecord;
import com.rental.system.model.Vehicle;
import com.rental.system.model.Customer;
import com.rental.system.model.Payment;
import com.rental.system.user.Staff;
import com.rental.system.service.RentalService;
import com.rental.system.service.VehicleService;
import com.rental.system.service.CustomerService;
import com.rental.system.service.StaffService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.rental.system.security.StaffPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalService rentalService;
    private final VehicleService vehicleService;
    private final CustomerService customerService;
    private final StaffService staffService;

    @Autowired
    public RentalController(RentalService rentalService,
                            VehicleService vehicleService,
                            CustomerService customerService,
                            StaffService staffService) {
        this.rentalService = rentalService;
        this.vehicleService = vehicleService;
        this.customerService = customerService;
        this.staffService = staffService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'REGULAR', 'CUSTOMER')")
    public ResponseEntity<?> createRental(@RequestBody RentRequest request) {
        Vehicle vehicle = vehicleService.findById(request.getVehicleId());
        Customer customer = customerService.findById(request.getCustomerId());
        
        Staff staff = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof StaffPrincipal principal) {
            staff = principal.getStaff();
        } else {
            // Fallback for missing auth context (shouldn't happen if secured properly)
            staff = staffService.findByIdAndUsername(request.getStaffId(), request.getStaffUsername());
        }

        if (vehicle == null) {
            return ResponseEntity.badRequest().body("Vehicle not found.");
        }
        if (!vehicle.isAvailable()) {
            return ResponseEntity.badRequest().body("Vehicle is not available.");
        }
        if (customer == null) {
            return ResponseEntity.badRequest().body("Customer not found.");
        }
        if (staff == null) {
            return ResponseEntity.badRequest().body("Staff validation failed.");
        }

        Payment payment = new Payment(request.getRentDays(), vehicle.getRentalRatePerDay(), request.getDeposit());
        Rent rent = new Rent(vehicle, customer, staff, request.getRentDays(), request.getStartDate(), request.getEndDate());
        rent.setPayment(payment);

        rentalService.processNewRent(rent);
        return ResponseEntity.ok(rent);
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('MANAGER', 'REGULAR')")
    public ResponseEntity<?> returnVehicle(@PathVariable int id, @RequestBody ReturnRequest request) {
        Rent rent = rentalService.findById(id);
        if (rent == null) {
            return ResponseEntity.notFound().build();
        }
        if (!rent.isStatus()) {
            return ResponseEntity.badRequest().body("Rental is already completed.");
        }

        if (request.getDiscount() > 0) {
            rent.getPayment().setDiscount(request.getDiscount());
        }
        if (request.getDamageFee() > 0) {
            rent.getPayment().setDamageFee(request.getDamageFee());
        }
        if (request.getExtraDays() > 0) {
            rent.getPayment().setExtraDays(request.getExtraDays());
            rent.setRentDays(rent.getRentDays() + request.getExtraDays());
        }

        rentalService.processReturn(rent, request.getPayDate(), request.getPaymentMethod());
        return ResponseEntity.ok(rent);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'REGULAR', 'CUSTOMER')")
    public ResponseEntity<?> getRentalById(@PathVariable int id) {
        Rent rent = rentalService.findById(id);
        if (rent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rent);
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'REGULAR', 'CUSTOMER')")
    public List<Rent> getRentalsByCustomer(@PathVariable int customerId) {
        return rentalService.findByCustomerId(customerId);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('MANAGER', 'REGULAR')")
    public List<Rent> getActiveRentals() {
        return rentalService.getActiveRents();
    }

    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('MANAGER', 'REGULAR')")
    public List<RentRecord> getRentalHistory() {
        return rentalService.getRentalHistory();
    }

    @GetMapping("/revenue")
    @PreAuthorize("hasAnyRole('MANAGER', 'REGULAR')")
    public ResponseEntity<Double> getTotalRevenue() {
        return ResponseEntity.ok(rentalService.calculateTotalRevenue());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'REGULAR')")
    public ResponseEntity<?> deleteRental(@PathVariable int id) {
        Rent rent = rentalService.findById(id);
        if (rent != null) {
            rentalService.removeRent(rent);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // DTOs
    @Data
    @NoArgsConstructor
    public static class RentRequest {
        private int vehicleId;
        private int customerId;
        private int staffId;
        private String staffUsername;
        private int rentDays;
        private String startDate;
        private String endDate;
        private double deposit;
    }

    @Data
    @NoArgsConstructor
    public static class ReturnRequest {
        private String payDate;
        private String paymentMethod;
        private double discount;
        private double damageFee;
        private int extraDays;
    }
}
