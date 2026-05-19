package com.rental.system.service;

import com.rental.system.model.Rent;
import com.rental.system.model.RentRecord;
import com.rental.system.model.Vehicle;
import com.rental.system.model.Payment;
import com.rental.system.database.DatabaseMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RentalService {
    private ArrayList<Rent> rents;
    private ArrayList<RentRecord> rentalHistory;

    public RentalService() {
        this.rents = new ArrayList<>();
        this.rentalHistory = new ArrayList<>();
    }

    // --- The Memory ---
    public ArrayList<Rent> getActiveRents() {
        return rents;
    }

    public void setRents(ArrayList<Rent> rents) {
        this.rents = rents;
    }

    public ArrayList<RentRecord> getRentalHistory() {
        return rentalHistory;
    }

    public void setRentalHistory(ArrayList<RentRecord> history) {
        this.rentalHistory = history;
    }

    public int getActiveRentCount() {
        return (int) rents.stream().filter(Rent::isStatus).count();
    }

    public int getTotalRentCount() {
        return rents.size();
    }

    // --- The Brain (Pure Logic) ---

    public void processNewRent(Rent rent) {
        // Business Rule: Mark vehicle as unavailable
        rent.getVehicle().setAvailable(false);
        DatabaseMapper.updateVehicle(rent.getVehicle());

        // Save payment then rent
        DatabaseMapper.saveNewPayment(rent.getPayment());
        rents.add(rent);
        DatabaseMapper.saveNewRent(rent);
    }

    public void processReturn(Rent rent, String payDate, String paymentMethod) {
        // 1. Process the payment
        rent.getPayment().processPayment(paymentMethod, payDate);
        rent.setReturnDate(payDate);
        rent.setStatus(false);

        // 2. Free the vehicle
        rent.getVehicle().setAvailable(true);

        // 3. Sync to Database
        DatabaseMapper.updatePayment(rent.getPayment());
        DatabaseMapper.updateVehicle(rent.getVehicle());
        DatabaseMapper.updateRent(rent);

        // 4. Add to history
        rentalHistory.add(new RentRecord(rent));
    }

    public void removeRent(Rent rent) {
        if (rent.getVehicle() != null) {
            rent.getVehicle().setAvailable(true);
            DatabaseMapper.updateVehicle(rent.getVehicle());
        }
        if (rent.getPayment() != null) {
            DatabaseMapper.deletePayment(rent.getPayment().getPaymentId());
        }
        DatabaseMapper.deleteRent(rent.getRentId());
        rents.remove(rent);
    }

    public Rent findById(int id) {
        for (Rent r : rents) {
            if (r.getRentId() == id) return r;
        }
        return null;
    }

    public double calculateTotalRevenue() {
        double total = 0;
        for (RentRecord record : rentalHistory) {
            total += record.getTotalPaid();
        }
        return total;
    }

    // --- Statistics Logic for Reports ---

    public Map.Entry<Integer, Integer> getTopVehicleId() {
        if (rentalHistory.isEmpty()) return null;
        HashMap<Integer, Integer> freq = new HashMap<>();
        for (RentRecord r : rentalHistory) {
            int vid = r.getVehicleId();
            freq.put(vid, freq.getOrDefault(vid, 0) + 1);
        }
        return getTopEntry(freq);
    }

    public Map.Entry<Integer, Integer> getTopCustomerId() {
        if (rentalHistory.isEmpty()) return null;
        HashMap<Integer, Integer> freq = new HashMap<>();
        for (RentRecord r : rentalHistory) {
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
