import java.time.*;
interface PricingRule { double fee(long minutes, LocalTime entry, LocalTime exit); }
interface OneDayPass { boolean isValidFor(LocalDate date); }
class FlatEBikeRule implements PricingRule {
    public double fee(long minutes, LocalTime in, LocalTime out) { return 25.0; }
}
class DeliveryVanRule implements PricingRule {
    private final double carRate;
    DeliveryVanRule(double carRate) { this.carRate=carRate; }
    public double fee(long minutes, LocalTime in, LocalTime out) {
        long total=0;
        LocalTime t=in;
        for (long i=0;i<minutes;i++) {
            LocalTime current=t.plusMinutes(i);
            if (!current.isBefore(LocalTime.of(9,0)) && current.isBefore(LocalTime.of(17,0))) total++;
        }
        long regular=minutes-total;
        return ((total+59)/60.0)*(2*carRate) + ((regular+59)/60.0)*carRate;
    }
}
class VisitorDayPass implements OneDayPass {
    private final LocalDate validDate;
    VisitorDayPass(LocalDate date) { validDate=date; }
    public boolean isValidFor(LocalDate date) { return validDate.equals(date); }
}
public class DesignChallenge {
    public static void main(String[] args) {
        int[] slotsPerFloor={20,25,30,35,40};
        System.out.println("Floors: "+slotsPerFloor.length);
        System.out.println("E-bike flat fee: Rs "+new FlatEBikeRule().fee(180,LocalTime.NOON,LocalTime.of(15,0)));
        System.out.println("Delivery van fee (2h entirely in 9-5): Rs "+
            new DeliveryVanRule(30).fee(120,LocalTime.of(10,0),LocalTime.NOON));
        VisitorDayPass pass=new VisitorDayPass(LocalDate.of(2026,11,13));
        System.out.println("Visitor day pass valid: "+pass.isValidFor(LocalDate.of(2026,11,13)));
    }
}
