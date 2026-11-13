import java.util.*;
import java.time.*;
abstract class Vehicle {
    private final String regNo;
    Vehicle(String regNo) {
        if (regNo == null || regNo.isBlank()) throw new IllegalArgumentException("Invalid registration");
        this.regNo = regNo;
    }
    public String getRegNo() { return regNo; }
    abstract double hourlyRate();
    double fee(long minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Negative duration");
        return ((minutes + 59) / 60) * hourlyRate();
    }
}
interface Chargeable { double chargingFee(int kWh); }
class Bike extends Vehicle {
    Bike(String r) { super(r); }
    double hourlyRate() { return 10; }
}
class Sedan extends Vehicle {
    Sedan(String r) { super(r); }
    double hourlyRate() { return 30; }
}
class EVCar extends Sedan implements Chargeable {
    EVCar(String r) { super(r); }
    public double chargingFee(int kWh) { return kWh * 12.0; }
}
class ParkingTicket {
    final int id; final Vehicle vehicle; final int floor, slot; final long entryMinute;
    ParkingTicket(int id, Vehicle v, int f, int s, long t) {
        this.id=id; vehicle=v; floor=f; slot=s; entryMinute=t;
    }
}
class ParkingFacility {
    private final Vehicle[][] slots;
    private final Map<String, ParkingTicket> active = new LinkedHashMap<>();
    private static int nextTicket = 1001;
    private double collection = 0;
    ParkingFacility(int[] sizes) {
        slots = new Vehicle[sizes.length][];
        for (int i=0;i<sizes.length;i++) slots[i]=new Vehicle[sizes[i]];
    }
    ParkingTicket enter(Vehicle v, long minute, boolean validMonthlyPass) {
        if (active.containsKey(v.getRegNo())) throw new IllegalArgumentException("Duplicate registration");
        for (int f=0;f<slots.length;f++) for (int s=0;s<slots[f].length;s++) {
            if (slots[f][s]==null) {
                slots[f][s]=v;
                ParkingTicket t=new ParkingTicket(nextTicket++,v,f,s,minute);
                active.put(v.getRegNo(),t);
                return t;
            }
        }
        throw new IllegalStateException("Parking full");
    }
    double exit(String regNo, long exitMinute, boolean pass, int kWh) {
        ParkingTicket t=active.remove(regNo);
        if (t==null) throw new IllegalArgumentException("No active ticket");
        slots[t.floor][t.slot]=null;
        double amount=pass ? 0 : t.vehicle.fee(exitMinute-t.entryMinute);
        if (!pass && t.vehicle instanceof Chargeable c) amount += c.chargingFee(kWh);
        collection += amount;
        return amount;
    }
    double getCollection() { return collection; }
}
public class SmartParkingSystem {
    public static void main(String[] args) {
        ParkingFacility p=new ParkingFacility(new int[]{2,1});
        ParkingTicket a=p.enter(new Bike("TN01BI1111"),0,false);
        ParkingTicket b=p.enter(new Sedan("TN02CA2222"),0,false);
        ParkingTicket c=p.enter(new EVCar("TN03EV3333"),0,false);
        System.out.println("Tickets: "+a.id+", "+b.id+", "+c.id);
        System.out.println("Bike fee (60 min): Rs "+p.exit("TN01BI1111",60,false,0));
        System.out.println("Car fee (61 min): Rs "+p.exit("TN02CA2222",61,false,0));
        System.out.println("EV fee (60 min + 5 kWh): Rs "+p.exit("TN03EV3333",60,false,5));
        System.out.println("Daily collection: Rs "+p.getCollection());
        try { p.exit("UNKNOWN",70,false,0); }
        catch (IllegalArgumentException e) { System.out.println("Invalid exit handled: "+e.getMessage()); }
    }
}
