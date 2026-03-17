import controller.Garage;
import java.util.Scanner;
import user.ManagerStaff;
import user.Staff;
@FunctionalInterface
interface StaffFilter{
    public int test(int a, int b);
}

public class test {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        
        // ===============================================
        // INTERFACE & PERMISSION TESTS
        // Based on Interface.txt lesson - adapted for Model.Vehicle Rental System
        // ===============================================

        System.out.println("\n========== INTERFACE & PERMISSION TESTS ==========\n");
        
        Garage rentalSystem = new Garage(10);
        


        // Model.Moto m1 = new Model.Moto("Model.Moto", "gas", "dirt bike", "BMW", "g1", 120, "1243433","pp-9999");
        // Model.Moto m2 = new Model.Moto("Model.Moto", "gas", "dirt bike", "BMW", "g1", 120, "1243433","pp-9999");
        // System.out.println(m1.toString());
        // System.out.println(m2.toString());
//        Moto m1 = new Moto(new Vehicle("Moto", "gas", "dirt bike", "BMW", "g1", 120, "1243433","pp-9999"), true);
//        System.out.println(m1.getClass().getSimpleName());

        Staff staff = new ManagerStaff("u","u_u", "root123", 3000);
        ManagerStaff m = new ManagerStaff("y", "y_y", "root123", 3000);
        System.out.println(m.getSalary());
//        System.out.println(staff instanceof ManagerStaff);
//        System.out.println(m instanceof ManagerStaff);
        rentalSystem.addAdmin();

        scanner.close();
    }



}
