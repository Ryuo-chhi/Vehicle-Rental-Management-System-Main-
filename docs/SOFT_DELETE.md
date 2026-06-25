# Soft Delete Architecture Note

## What is Soft Delete?
In this project, vehicles are **never physically deleted** from the database using SQL `DELETE` commands. Instead, they are "Soft Deleted." 

Soft deletion means we simply flip a boolean flag (`isDeleted = true`) on the `Vehicle` entity.

## Why do we use it?
When a vehicle is rented, the `rents` table creates a **Foreign Key constraint** pointing to the `vehicles` table. If we physically delete a vehicle:
1. The database will crash (`DataIntegrityViolationException`) because it refuses to break the rental history.
2. If we force the deletion (e.g., using `ON DELETE SET NULL`), we would permanently lose the historical data about which car was rented (its brand, model, license plate, etc.).

## How it works in our Codebase
1. **The Database:** `Vehicle.java` has a `private boolean isDeleted = false;` field.
2. **The Repository:** `VehicleRepository.java` uses `findByIsDeletedFalse()` instead of the standard `findAll()`.
3. **The Logic:** When a staff member deletes an available vehicle in the frontend, `VehicleService.removeVehicle()` simply sets `isDeleted(true)` and saves it. 

## Result
To the frontend user, the vehicle instantly vanishes from the "Vehicles" tab (because the repository filters it out). However, the row still physically exists in the database. This guarantees that all past `RentRecord` history remains 100% intact and accurate forever.

*Note: We also explicitly block the deletion of any vehicle that is currently marked as `!isAvailable()` (Active Rental) to prevent deleting a vehicle that is literally out on the road.*
