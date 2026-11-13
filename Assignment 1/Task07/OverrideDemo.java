abstract class RateVehicle {
    private final String regNo;
    RateVehicle(String regNo) { this.regNo = regNo; }
    abstract double ratePerHour();
    @Override public boolean equals(Object o) {
        return o instanceof RateVehicle && regNo.equals(((RateVehicle)o).regNo);
    }
    @Override public int hashCode() { return regNo.hashCode(); }
}
class RateCar extends RateVehicle {
    RateCar(String regNo) { super(regNo); }
    @Override double ratePerHour() { return 30.0; }
}
public class OverrideDemo {
    public static void main(String[] args) {
        RateCar a = new RateCar("TN01AB1234"), b = new RateCar("TN01AB1234");
        System.out.println(a.ratePerHour());
        System.out.println("Equal registration: " + a.equals(b));
    }
}
