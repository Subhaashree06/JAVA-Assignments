import java.util.*;
class VehicleRecord {
    final String registration; final String type;
    VehicleRecord(String registration, String type) { this.registration=registration; this.type=type; }
}
public class ProblemModelDemo {
    public static void main(String[] args) {
        VehicleRecord v = new VehicleRecord("TN01AB1234", "Car");
        System.out.println("Candidate object: VehicleRecord");
        System.out.println("Registration: " + v.registration);
        System.out.println("Vehicle type: " + v.type);
        System.out.println("Rule check: registration must be unique and slot must be free.");
    }
}
