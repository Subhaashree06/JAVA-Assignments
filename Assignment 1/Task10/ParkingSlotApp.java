import parking.model.ParkingSlot;
public class ParkingSlotApp {
    public static void main(String[] args) {
        ParkingSlot slot = new ParkingSlot(0, 5);
        System.out.println(slot.occupy("TN01AB1234"));
        System.out.println(slot);
        System.out.println(slot.release());
    }
}
