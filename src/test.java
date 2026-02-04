import java.util.Scanner;

public class test {
    public static void main(String[] args){
        ManagementSystem M = new ManagementSystem(10);
        Scanner scanner = new Scanner(System.in);
        /* noted: some bug haven't fix
         * 1: when update vehicle price it'll update all the price that related to vehicle class like Vehicle in payment
         * 2: Remove rents is remove immediately it's not check any condition before remove like rent haven't paid yet
         * 3: haven't limited to customer rent vehicle
         * */
        M.addRent(scanner);
//        M.findRentByID(scanner);
        M.showRents();
        M.returnVehicle(scanner);
        M.showRents();
//        M.updateVehicle(scanner);
//        M.showRents();

    }
}
