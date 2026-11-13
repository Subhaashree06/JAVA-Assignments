class BaseVehicle {
    protected String regNo;
    BaseVehicle(String regNo) { this.regNo = regNo; System.out.println("Vehicle constructor"); }
}
class DemoCar extends BaseVehicle {
    DemoCar(String regNo) { super(regNo); System.out.println("Car constructor"); }
}
class DemoElectricCar extends DemoCar {
    DemoElectricCar(String regNo) { super(regNo); System.out.println("ElectricCar constructor"); }
}
public class InheritanceDemo {
    public static void main(String[] args) { new DemoElectricCar("TN01EV1234"); }
}
