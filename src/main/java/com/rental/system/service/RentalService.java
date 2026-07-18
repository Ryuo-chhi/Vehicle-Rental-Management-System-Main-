package com.rental.system.service;

import com.rental.system.model.Rent;
import com.rental.system.model.RentRecord;
import com.rental.system.repository.RentRepository;
import com.rental.system.repository.VehicleRepository;
import com.rental.system.repository.RentRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("null")
public class RentalService {
    private final RentRepository rentRepository;
    private final VehicleRepository vehicleRepository;
    private final RentRecordRepository rentRecordRepository;

    @Autowired
    public RentalService(RentRepository rentRepository,
                         VehicleRepository vehicleRepository,
                         RentRecordRepository rentRecordRepository) {
        this.rentRepository = rentRepository;
        this.vehicleRepository = vehicleRepository;
        this.rentRecordRepository = rentRecordRepository;
    }

    // --- The Memory ---
    public ArrayList<Rent> getActiveRents() {
        return new ArrayList<>(rentRepository.findByStatus(true));
    }

    public void setRents(ArrayList<Rent> rents) {
        rentRepository.saveAll(rents);
    }

    public ArrayList<RentRecord> getRentalHistory() {
        return new ArrayList<>(rentRecordRepository.findAll());
    }

    public void setRentalHistory(ArrayList<RentRecord> history) {
        rentRecordRepository.saveAll(history);
    }

    public int getActiveRentCount() {
        return (int) rentRepository.findByStatus(true).size();
    }

    public int getTotalRentCount() {
        return (int) rentRepository.count();
    }

    // --- The Brain (Pure Logic) ---

    public void processNewRent(Rent rent) {
        // Business Rule: Mark vehicle as unavailable
        rent.getVehicle().setAvailable(false);
        vehicleRepository.save(rent.getVehicle());

        // Save rent (cascade will save payment)
        rentRepository.save(rent);
    }

    public void processReturn(Rent rent, String payDate, String paymentMethod) {
        // 1. Process the payment
        rent.getPayment().processPayment(paymentMethod, payDate);
        rent.setReturnDate(payDate);
        rent.setStatus(false);

        // 2. Free the vehicle
        rent.getVehicle().setAvailable(true);

        // 3. Sync to Database
        vehicleRepository.save(rent.getVehicle());
        rentRepository.save(rent);

        // 4. Add to history
        rentRecordRepository.save(new RentRecord(rent));
    }

    public void removeRent(Rent rent) {
        if (rent.getVehicle() != null) {
            rent.getVehicle().setAvailable(true);
            vehicleRepository.save(rent.getVehicle());
        }
        rentRepository.delete(rent);
    }

    public Rent findById(int id) {
        return rentRepository.findById(id).orElse(null);
    }

    public List<Rent> findByCustomerId(int customerId) {
        return rentRepository.findByCustomer_CustomerId(customerId);
    }

    public double calculateTotalRevenue() {
        double total = 0;
        for (RentRecord record : rentRecordRepository.findAll()) {
            total += record.getTotalPaid();
        }
        return total;
    }

    // --- Statistics Logic for Reports ---

    public Map.Entry<Integer, Integer> getTopVehicleId() {
        List<RentRecord> history = rentRecordRepository.findAll();
        if (history.isEmpty()) return null;
        HashMap<Integer, Integer> freq = new HashMap<>();
        for (RentRecord r : history) {
            int vid = r.getVehicleId();
            freq.put(vid, freq.getOrDefault(vid, 0) + 1);
        }
        return getTopEntry(freq);
    }

    public Map.Entry<Integer, Integer> getTopCustomerId() {
        List<RentRecord> history = rentRecordRepository.findAll();
        if (history.isEmpty()) return null;
        HashMap<Integer, Integer> freq = new HashMap<>();
        for (RentRecord r : history) {
            int cid = r.getCustomerId();
            freq.put(cid, freq.getOrDefault(cid, 0) + 1);
        }
        return getTopEntry(freq);
    }

    private Map.Entry<Integer, Integer> getTopEntry(HashMap<Integer, Integer> freq) {
        Map.Entry<Integer, Integer> top = null;
        for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
            if (top == null || entry.getValue() > top.getValue()) {
                top = entry;
            }
        }
        return top;
    }
}
