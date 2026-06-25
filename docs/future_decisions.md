# Future Architectural Decisions: Booking & Reservation System

*Note: This document captures industry-standard approaches for managing vehicle rentals, to be considered for future refactoring.*

## 1. The "Single Source of Truth" (State Machine)
**Current Issue:** Splitting active rentals (`rents`) and completed records (`rent_records`) creates redundancy, requires moving data between tables, and causes foreign-key fragmentation.
**Future Approach:** 
- Merge into a single table: `bookings` (or `reservations`).
- Manage the lifecycle via a `status` Enum (e.g., `PENDING`, `CONFIRMED`, `ACTIVE`, `COMPLETED`, `CANCELLED`).
- **Benefit:** Eliminates the need for a separate historical table. To view history, simply query `SELECT * FROM bookings WHERE status = 'COMPLETED'`.

## 2. Time-Based Availability
**Current Issue:** Relying on a simple `is_available` boolean on the `Vehicle` entity means the system only knows if the car is physically present *right now*. It cannot handle future reservations or overlapping schedules.
**Future Approach:**
- Remove the `is_available` flag from the `vehicles` table.
- Calculate availability dynamically by checking the `bookings` table for overlapping date ranges.
- **Benefit:** Allows users to book a car for next week while it is currently rented out today, preventing double-booking without hard-locking the vehicle's state.

## 3. Immutable Snapshots (Price Locking)
**Current Issue:** Relying solely on real-time vehicle data means if the daily rate of a vehicle changes, past historical receipts might recalculate incorrectly.
**Future Approach:**
- The `bookings` table must save the exact `locked_price_per_day` at the exact moment the reservation is created.
- **Benefit:** Guarantees financial integrity and accurate historical auditing, regardless of future price changes.

## Target Schema Architecture
When refactoring, the core structure should resemble:
1. **`vehicles`**: Vehicle details only (No `is_available` flag).
2. **`bookings`**: Stores `vehicle_id`, `customer_id`, `start_date`, `end_date`, `locked_daily_rate`, `total_price`, and `status` (Enum).
3. **`payments`**: Linked directly to the booking for transaction tracking.
