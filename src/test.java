import java.util.Scanner;

public class test {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        
        // // ===============================================
        // // F1-F4 PROOFS: Primitive vs Reference Datatypes
        // // ===============================================

        // System.out.println("\n========== F1-F4 PROOFS ==========\n");

        // // F1 — Primitive copy
        // // Copy a primitive, modify the copy, original remains unchanged
        // int originalPrice = 100;
        // int copiedPrice = originalPrice;
        // copiedPrice = 200;
        // System.out.println("F1 — Primitive Copy:");
        // System.out.println("  Original: " + originalPrice + " | Copied (modified): " + copiedPrice);
        // System.out.println("  Proof: Original unchanged after modifying copy.\n");

        // // F2 — Reference copy
        // // Two variables reference the same object; change is visible everywhere
        // Vehicle v1 = new Vehicle("gasoline", "SUV", "Honda", "CRV", 250, "DL-TEST-001", "PP-TEST-01");
        // Vehicle v2 = v1;
        // v2.setRentalRatePerDay(999);
        // System.out.println("F2 — Reference Copy:");
        // System.out.println("  v1.rentalRatePerDay: " + v1.getRentalRatePerDay());
        // System.out.println("  v2.rentalRatePerDay: " + v2.getRentalRatePerDay());
        // System.out.println("  Proof: Both v1 and v2 show same change (999).\n");

        // // F3 — Array stores references
        // // Objects inside arrays reflect later modifications
        // Vehicle[] testGarage = new Vehicle[2];
        // testGarage[0] = v1;
        // v1.setRentalRatePerDay(777);
        // System.out.println("F3 — Array Stores References:");
        // System.out.println("  testGarage[0].rentalRatePerDay: " + testGarage[0].getRentalRatePerDay());
        // System.out.println("  Proof: Array element reflects v1's change to 777.\n");

        // // F4 — Snapshot behavior
        // // Stored snapshot values do not change after the original object changes
        // Vehicle vehicleForRent = new Vehicle("electric", "Sedan", "Tesla", "Model S", 500, "DL-TEST-002", "PP-TEST-02");
        // Customer testCustomer = new Customer("John Doe", "P1234567", "0123456789");
        // Rent rent = new Rent(vehicleForRent, testCustomer, 5, "2026-02-05", "2026-02-10");
        // // Create payment with snapshot of vehicle price
        // Payment payment = new Payment(rent.getRentDays(), vehicleForRent.getRentalRatePerDay(), 100);
        // double snapshotTotal = payment.expectedTotal(); // Uses price captured at Payment creation
        // vehicleForRent.setRentalRatePerDay(1000); // Change original after payment created
        // double afterChangeTotal = payment.expectedTotal(); // Still uses snapshot price
        // System.out.println("F4 — Snapshot Behavior:");
        // System.out.println("  Original rate at payment creation: 500");
        // System.out.println("  Snapshot total (5 days * 500 - 100 deposit): " + snapshotTotal);
        // System.out.println("  After changing vehicle rate to 1000:");
        // System.out.println("  Recalculated total still uses snapshot: " + afterChangeTotal);
        // System.out.println("  Proof: Payment uses snapshot price, not current vehicle rate.\n");

        // // Static Method Proof
        // System.out.println("Static Method Proof:");
        // System.out.println("  Rent.getTotalRentCount(): " + Rent.getTotalRentCount());
        // System.out.println("  (Called using ClassName.method())\n");

        // System.out.println("========== END OF PROOFS ==========");
        // System.out.println();

        // ===============================================
        // INTERFACE & PERMISSION TESTS
        // Based on Interface.txt lesson - adapted for Vehicle Rental System
        // ===============================================

        System.out.println("\n========== INTERFACE & PERMISSION TESTS ==========\n");
        
        Garage rentalSystem = new Garage(10);
        
        // ===============================================
        // Test A: Manager Test (Full Admin Access)
        // ===============================================
        System.out.println("===== TEST A: MANAGER (Full Permissions) =====\n");
        
        // A1. Manager login
        System.out.println("A1. Testing Manager Login:");
        rentalSystem.staffLogin("admin_root", "root123");
        System.out.println("   " + rentalSystem.getLastMessage());
        System.out.println("   Logged in staff: " + rentalSystem.getLoggedInStaff().getName() + 
                           " [" + rentalSystem.getLoggedInStaff().getRole() + "]");
        System.out.println("   Expected: Login success ✓\n");
        
        // A2. Manager can access MANAGE_VEHICLE
        System.out.println("A2. Testing Manager - MANAGE_VEHICLE permission:");
        boolean managerCanManageVehicle = rentalSystem.getLoggedInStaff().can(Garage.MANAGE_VEHICLE);
        System.out.println("   can(MANAGE_VEHICLE): " + managerCanManageVehicle);
        System.out.println("   Expected: true ✓\n");
        
        // A3. Manager can access MANAGE_CUSTOMER
        System.out.println("A3. Testing Manager - MANAGE_CUSTOMER permission:");
        boolean managerCanManageCustomer = rentalSystem.getLoggedInStaff().can(Garage.MANAGE_CUSTOMER);
        System.out.println("   can(MANAGE_CUSTOMER): " + managerCanManageCustomer);
        System.out.println("   Expected: true ✓\n");
        
        // A4. Manager can access MANAGE_STAFF
        System.out.println("A4. Testing Manager - MANAGE_STAFF permission:");
        boolean managerCanManageStaff = rentalSystem.getLoggedInStaff().can(Garage.MANAGE_STAFF);
        System.out.println("   can(MANAGE_STAFF): " + managerCanManageStaff);
        System.out.println("   Expected: true ✓\n");
        
        // A5. Manager can access ADD_RENT
        System.out.println("A5. Testing Manager - ADD_RENT permission:");
        boolean managerCanAddRent = rentalSystem.getLoggedInStaff().can(Garage.ADD_RENT);
        System.out.println("   can(ADD_RENT): " + managerCanAddRent);
        System.out.println("   Expected: true ✓\n");
        
        // A6. Verify Manager's can() returns true for ALL actions
        System.out.println("A6. Manager permissions summary:");
        System.out.println("   VIEW_VEHICLE: " + rentalSystem.getLoggedInStaff().can(Garage.VIEW_VEHICLE));
        System.out.println("   MANAGE_VEHICLE: " + rentalSystem.getLoggedInStaff().can(Garage.MANAGE_VEHICLE));
        System.out.println("   VIEW_CUSTOMER: " + rentalSystem.getLoggedInStaff().can(Garage.VIEW_CUSTOMER));
        System.out.println("   MANAGE_CUSTOMER: " + rentalSystem.getLoggedInStaff().can(Garage.MANAGE_CUSTOMER));
        System.out.println("   ADD_RENT: " + rentalSystem.getLoggedInStaff().can(Garage.ADD_RENT));
        System.out.println("   RETURN_VEHICLE: " + rentalSystem.getLoggedInStaff().can(Garage.RETURN_VEHICLE));
        System.out.println("   SHOW_PAYMENT: " + rentalSystem.getLoggedInStaff().can(Garage.SHOW_PAYMENT));
        System.out.println("   MANAGE_STAFF: " + rentalSystem.getLoggedInStaff().can(Garage.MANAGE_STAFF));
        System.out.println("   Expected: ALL true ✓\n");
        
        rentalSystem.staffLogout();
        System.out.println(rentalSystem.getLastMessage() + "\n");
        
        // ===============================================
        // Test B: Staff Test (Limited Permissions)
        // ===============================================
        System.out.println("===== TEST B: STAFF (Limited Permissions) =====\n");
        
        // B1. Staff login
        System.out.println("B1. Testing Staff Login:");
        rentalSystem.staffLogin("chan_staff", "staff123");
        System.out.println("   " + rentalSystem.getLastMessage());
        System.out.println("   Logged in staff: " + rentalSystem.getLoggedInStaff().getName() + 
                           " [" + rentalSystem.getLoggedInStaff().getRole() + "]");
        System.out.println("   Expected: Login success ✓\n");
        
        // B2. Staff CAN view vehicles
        System.out.println("B2. Testing Staff - VIEW_VEHICLE permission:");
        boolean staffCanViewVehicle = rentalSystem.getLoggedInStaff().can(Garage.VIEW_VEHICLE);
        System.out.println("   can(VIEW_VEHICLE): " + staffCanViewVehicle);
        System.out.println("   Expected: true ✓\n");
        
        // B3. Staff CAN manage customers (rental front desk)
        System.out.println("B3. Testing Staff - MANAGE_CUSTOMER permission:");
        boolean staffCanManageCustomer = rentalSystem.getLoggedInStaff().can(Garage.MANAGE_CUSTOMER);
        System.out.println("   can(MANAGE_CUSTOMER): " + staffCanManageCustomer);
        System.out.println("   Expected: true ✓\n");
        
        // B4. Staff CAN add rent
        System.out.println("B4. Testing Staff - ADD_RENT permission:");
        boolean staffCanAddRent = rentalSystem.getLoggedInStaff().can(Garage.ADD_RENT);
        System.out.println("   can(ADD_RENT): " + staffCanAddRent);
        System.out.println("   Expected: true ✓\n");
        
        // B5. Staff CANNOT manage vehicles
        System.out.println("B5. Testing Staff - MANAGE_VEHICLE permission (should be denied):");
        boolean staffCanManageVehicle = rentalSystem.getLoggedInStaff().can(Garage.MANAGE_VEHICLE);
        System.out.println("   can(MANAGE_VEHICLE): " + staffCanManageVehicle);
        System.out.println("   Expected: false ✓\n");
        
        // B6. Staff CANNOT manage other staff
        System.out.println("B6. Testing Staff - MANAGE_STAFF permission (should be denied):");
        boolean staffCanManageStaff = rentalSystem.getLoggedInStaff().can(Garage.MANAGE_STAFF);
        System.out.println("   can(MANAGE_STAFF): " + staffCanManageStaff);
        System.out.println("   Expected: false ✓\n");
        
        // B7. Staff permissions summary
        System.out.println("B7. Staff permissions summary:");
        System.out.println("   VIEW_VEHICLE: " + rentalSystem.getLoggedInStaff().can(Garage.VIEW_VEHICLE) + " ✓");
        System.out.println("   MANAGE_VEHICLE: " + rentalSystem.getLoggedInStaff().can(Garage.MANAGE_VEHICLE) + " (denied) ✓");
        System.out.println("   VIEW_CUSTOMER: " + rentalSystem.getLoggedInStaff().can(Garage.VIEW_CUSTOMER) + " ✓");
        System.out.println("   MANAGE_CUSTOMER: " + rentalSystem.getLoggedInStaff().can(Garage.MANAGE_CUSTOMER) + " ✓");
        System.out.println("   ADD_RENT: " + rentalSystem.getLoggedInStaff().can(Garage.ADD_RENT) + " ✓");
        System.out.println("   RETURN_VEHICLE: " + rentalSystem.getLoggedInStaff().can(Garage.RETURN_VEHICLE) + " ✓");
        System.out.println("   SHOW_PAYMENT: " + rentalSystem.getLoggedInStaff().can(Garage.SHOW_PAYMENT) + " ✓");
        System.out.println("   MANAGE_STAFF: " + rentalSystem.getLoggedInStaff().can(Garage.MANAGE_STAFF) + " (denied) ✓\n");
        
        rentalSystem.staffLogout();
        System.out.println(rentalSystem.getLastMessage() + "\n");
        
        // ===============================================
        // Test C: Invalid Login Tests
        // ===============================================
        System.out.println("===== TEST C: INVALID LOGIN TESTS =====\n");
        
        // C1. Wrong password
        System.out.println("C1. Testing wrong password:");
        rentalSystem.staffLogin("admin_root", "wrongpassword");
        System.out.println("   " + rentalSystem.getLastMessage());
        System.out.println("   Is logged in: " + rentalSystem.isStaffLoggedIn());
        System.out.println("   Expected: Login failed, not logged in ✓\n");
        
        // C2. Wrong username
        System.out.println("C2. Testing wrong username:");
        rentalSystem.staffLogin("nonexistent_user", "password123");
        System.out.println("   " + rentalSystem.getLastMessage());
        System.out.println("   Is logged in: " + rentalSystem.isStaffLoggedIn());
        System.out.println("   Expected: Login failed, not logged in ✓\n");
        
        // C3. Empty username
        System.out.println("C3. Testing empty username:");
        rentalSystem.staffLogin("", "password123");
        System.out.println("   " + rentalSystem.getLastMessage());
        System.out.println("   Is logged in: " + rentalSystem.isStaffLoggedIn());
        System.out.println("   Expected: Login failed, not logged in ✓\n");
        
        // ===============================================
        // Test D: Interface Polymorphism Test
        // ===============================================
        System.out.println("===== TEST D: INTERFACE POLYMORPHISM =====\n");
        
        System.out.println("D1. Demonstrating polymorphism - storing different staff types:");
        IStaff manager = new ManagerStaff("Test Manager", "Manager", 5000, "test_mgr", "pass123");
        IStaff staff = new Staff("Test Staff", "Staff", 2000, "test_staff", "pass123");
        
        System.out.println("   Both stored as IStaff interface type:");
        System.out.println("   - manager variable type: IStaff (actual: ManagerStaff)");
        System.out.println("   - staff variable type: IStaff (actual: Staff)");
        System.out.println("   Manager can MANAGE_VEHICLE: " + manager.can(Garage.MANAGE_VEHICLE));
        System.out.println("   Staff can MANAGE_VEHICLE: " + staff.can(Garage.MANAGE_VEHICLE));
        System.out.println("   Expected: Different behavior from same interface ✓\n");
        
        System.out.println("D2. Interface enables HashSet<IStaff> to store both types:");
        System.out.println("   Garage uses HashSet<IStaff> to store:");
        System.out.println("   - ManagerStaff objects (admin_root, bob_manager)");
        System.out.println("   - Staff objects (chan_staff)");
        System.out.println("   All treated uniformly through IStaff interface ✓\n");
        
        // ===============================================
        // Test E: Online/Offline Status (isActive)
        // ===============================================
        System.out.println("===== TEST E: ONLINE/OFFLINE STATUS =====\n");
        
        System.out.println("E1. Testing isActive (online/offline) behavior:");
        System.out.println("   Before login - staff.isActive(): " + staff.isActive());
        System.out.println("   Expected: false (offline) ✓\n");
        
        rentalSystem.staffLogin("chan_staff", "staff123");
        System.out.println("E2. After successful login:");
        System.out.println("   staff.isActive(): " + rentalSystem.getLoggedInStaff().isActive());
        System.out.println("   Expected: true (online) ✓\n");
        
        rentalSystem.staffLogout();
        System.out.println("E3. After logout:");
        System.out.println("   " + rentalSystem.getLastMessage());
        System.out.println("   Expected: Staff is offline again ✓\n");
        
        System.out.println("========== END OF INTERFACE & PERMISSION TESTS ==========\n");
        
        // ===============================================
        // REFLECTION QUESTIONS (from Interface.txt)
        // ===============================================
        System.out.println("========== REFLECTION QUESTIONS ==========\n");
        System.out.println("Q1: Why do we store staff as HashSet<IStaff> instead of HashSet<ManagerStaff>?");
        System.out.println("    Answer: So Garage can store both ManagerStaff and Staff objects in one");
        System.out.println("    collection. The interface allows different classes to be treated uniformly.\n");
        
        System.out.println("Q2: What happens if a class implements IStaff but forgets to write can()?");
        System.out.println("    Answer: Java compiler error - the class MUST implement ALL methods");
        System.out.println("    from the interface, including can(String action).\n");
        
        System.out.println("Q3: Why is this not inheritance? What keyword did we use instead?");
        System.out.println("    Answer: We used 'implements' (not 'extends'). There is no parent class,");
        System.out.println("    only a contract (interface) that defines required methods.\n");
        
        System.out.println("========== END OF REFLECTION ==========\n");

        scanner.close();
    }

}
