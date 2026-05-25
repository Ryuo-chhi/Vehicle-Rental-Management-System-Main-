package com.rental.system;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testMainClassExists() {
        // Basic test to ensure the test structure is working
        assertNotNull(Main.class);
    }

    @Test
    public void testMaintenanceRecordModel() {
        com.rental.system.model.MaintenanceRecord record = new com.rental.system.model.MaintenanceRecord(
            1, "Oil change", 45.50, "05-06-2026"
        );
        assertEquals(1, record.getVehicleId());
        assertEquals("Oil change", record.getDetails());
        assertEquals(45.50, record.getCost());
        assertEquals("05-06-2026", record.getStartDate());
        assertEquals("TBD", record.getEndDate());
        assertEquals("ONGOING", record.getStatus());

        record.setStatus("COMPLETED");
        record.setEndDate("06-06-2026");
        assertEquals("COMPLETED", record.getStatus());
        assertEquals("06-06-2026", record.getEndDate());
    }

    @Test
    public void testPromotionModel() {
        com.rental.system.model.Promotion promo = new com.rental.system.model.Promotion("SUMMER10", 10.0);
        assertEquals("SUMMER10", promo.getCode());
        assertEquals(10.0, promo.getDiscountPercent());
        assertTrue(promo.isActive());

        promo.setActive(false);
        assertFalse(promo.isActive());
    }

    @Test
    public void testPaymentCalculation() {
        com.rental.system.model.Payment payment = new com.rental.system.model.Payment(5, 50.0, 100.0);
        payment.setDiscount(10.0); // 10% discount
        payment.setExtraDays(2);
        payment.setDamageFee(25.0);

        // Without DB settings connection, it falls back to 1.5x penalty and 0% tax.
        // Base cost: 50 * 5 = 250
        // Extra days: 50 * 2 * 1.5 = 150
        // Subtotal: 250 + 150 = 400
        // Discount: 400 * 0.10 = 40
        // Before Tax: 400 - 40 + 25 = 385
        // Tax: 0% of 385 = 0
        // Final: 385 - deposit(100) = 285
        assertEquals(285.0, payment.calculateTotal(), 0.001);
    }
}
