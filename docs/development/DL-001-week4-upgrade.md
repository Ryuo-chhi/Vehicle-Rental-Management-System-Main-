# DL-001: Week 4 Upgrade — Static, Access Control, Getter & Setter

**Status:** 🟢 APPROVED — READY FOR IMPLEMENTATION  
**Date:** 2026-02-05  
**Author:** AI Assistant

---

## Problem Statement

Upgrade the Vehicle Rental Management System to meet Week 4 requirements:
- All fields `private`, methods `public` when needed
- Getters for all private fields
- At least 1 setter with validation
- Static methods to access static variables (called via `ClassName.method()`)
- Fix Main.java to use new constructor signatures
- Update F1-F4 proofs

---

## User Answers

| Question | Answer |
|----------|--------|
| Main.java approach | Fix constructors AND implement Week 4 requirements |
| F4 snapshot | Depends on implementation |
| Add `public class` to Customer/Main | Yes — "No default access" applies to classes too |

---

## Week 4 Requirements Checklist

| Requirement | Status | Notes |
|-------------|--------|-------|
| Min 4 classes + Main | ✅ Already done | Customer, Vehicle, Payment, Rent, ManagementSystem |
| Static variable | ✅ Already done | `countXxxId` in each class |
| Static method | 🔜 TODO | Add `getTotalXxxCount()` |
| All fields private | 🔜 TODO | Currently default access |
| Methods public when needed | 🔜 TODO | Add `public` to methods |
| Getters for private fields | 🔜 TODO | None exist |
| At least 1 setter with validation | 🔜 TODO | Only `setPayment()` exists (no validation) |
| Array + counter in class | ✅ Already done | ManagementSystem has arrays + counts |
| Object references (no duplication) | ✅ Already done | Rent references Vehicle, Customer |
| String comparison with .equals() | Need to verify | |
| Null safety | ✅ Already done | Search methods return null |
| Snapshot design | ✅ Already done | Payment captures price at creation |
| F1-F4 proofs in Main | 🔜 TODO | Fix constructors, use getter/setter |

---

## Implementation Plan

### Phase 1: Fix Main.java Constructors
- Update Vehicle constructor (7 args)
- Update Rent constructor (5 args)
- Add Payment creation for F4 proof

### Phase 2: Add Private + Getters to All Classes
For each class: `private` fields + getter methods

### Phase 3: Add Setters with Validation
- `Vehicle.setRentalRatePerDay()` — validate > 0
- `Customer.setCustomerPhone()` — validate length
- `Payment.setDiscount()` — validate 0-100

### Phase 4: Add Static Methods
- `Customer.getTotalCustomerCount()`
- `Vehicle.getTotalVehicleCount()`

### Phase 5: Update ManagementSystem
Change direct field access to use getters

### Phase 6: Update Main.java Proofs
- F2: use setter/getter
- Add static method call demo

---

## Files to Modify

1. `Customer.java` — public class, private fields, getters, setter, static method
2. `Vehicle.java` — private fields, getters, setter, static method  
3. `Payment.java` — private fields, getters, setter
4. `Rent.java` — private fields, getters
5. `ManagementSystem.java` — private fields, use getters
6. `Main.java` — public class, fix constructors, update proofs
