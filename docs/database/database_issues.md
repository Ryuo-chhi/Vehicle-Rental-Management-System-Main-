### Database Issues Log, Clarifications & Solutions

---

1. **Procedure when returning vehicle: Total price is not updating**
    - **Clarification:** The `UPDATE` statement in the `s_proc_return_vehicle` procedure updates several payment fields but forgets to recalculate and update the `total_paid` column itself. The value remains at 0 or its initial deposit-only state.
    - **Solution:** Add the `total_paid` calculation directly into the `UPDATE` statement within the procedure. This ensures the database accurately reflects the final balance when a vehicle is returned.
    ```sql
    -- Updated logic for s_proc_return_vehicle:
    UPDATE payments
    SET extra_days   = p_extra_days,
        damage_fee   = p_damage_fee,
        discount     = p_discount,
        rent_days    = v_rent_days + p_extra_days,
        method       = p_method,
        pay_date     = p_return_date,
        pay_status   = 'PAID',
        -- Recalculate total_paid: (Rate * Total Days) - Discount + Damage - Deposit
        total_paid   = (price * (v_rent_days + p_extra_days)) 
                       - ((p_discount / 100) * price * (v_rent_days + p_extra_days))
                       + p_damage_fee 
                       - deposit
    WHERE payment_id = v_payment_id;
    ```

2. **Rent status update failure**
    - **Clarification:** While the `s_proc_return_vehicle` procedure does include an `UPDATE rents SET status = FALSE`, this only happens when the procedure is called. If a payment is updated to 'PAID' via a simple `UPDATE` on the `payments` table (outside the procedure), the `rents` status remains `TRUE` (Active).
    - **Solution:** Implement a database **TRIGGER** that automatically updates the corresponding rent status to `FALSE` whenever a payment's `pay_status` is changed to 'PAID'.
    ```sql
    CREATE TRIGGER trg_after_payment_paid
    AFTER UPDATE ON payments
    FOR EACH ROW
    BEGIN
        IF NEW.pay_status = 'PAID' AND OLD.pay_status <> 'PAID' THEN
            UPDATE rents SET status = FALSE WHERE payment_id = NEW.payment_id;
        END IF;
    END;
    ```

3. **Total revenue calculation error (DQL Q3)**
    - **Clarification:** The `total_paid` column represents the "Final Balance" paid at return (Total Cost - Deposit). Therefore, `total_revenue` must be the sum of `total_paid` AND the initial `deposit`.
    - **Solution:** Update the `SUM` function in the DQL query to include both components.
    ```sql
    SELECT v.vehicle_type,
           COUNT(*) AS total_rentals,
           SUM(p.total_paid + p.deposit) AS total_revenue
    FROM rents r
    JOIN vehicles v ON r.vehicle_id = v.vehicle_id
    JOIN payments p ON r.payment_id = p.payment_id
    WHERE r.status = FALSE AND p.pay_status = 'PAID'
    GROUP BY v.vehicle_type;
    ```

4. **Vehicle ID assignment problem**
    - **Clarification:** The Java code is manually calculating `vehicleId` (e.g., `Garage.getVehicleID() + 1`), while the database is also using `AUTO_INCREMENT`. This leads to conflicts when the Java count gets out of sync with the actual DB primary keys.
    - **Solution:** Stop manual ID assignment in Java. Let MySQL handle the ID via `AUTO_INCREMENT`, and retrieve the generated ID back into Java after insertion using `LAST_INSERT_ID()`.
    - **Implementation:** 
        1. Remove `this.vehicleId = ...` from the `Vehicle` constructor.
        2. Ensure `DatabaseMapper.saveNewVehicle` captures the `generatedId` (which it currently does).
        3. Use the DB-provided ID as the single source of truth.
