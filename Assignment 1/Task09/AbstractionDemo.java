interface Payable { double paymentAmount(); }
abstract class AbstractVehicle {
    private final String regNo;
    AbstractVehicle(String regNo) { this.regNo = regNo; }
    public String getRegNo() { return regNo; }
    abstract double ratePerHour();
}
class EV extends AbstractVehicle implements Payable {
    EV(String regNo) { super(regNo); }
    @Override double ratePerHour() { return 30; }
    @Override public double paymentAmount() { return ratePerHour() * 2; }
}
public class AbstractionDemo {
    public static void main(String[] args) {
        EV ev = new EV("TN01EV0001");
        System.out.println(ev.getRegNo() + " payment Rs " + ev.paymentAmount());
    }
}
