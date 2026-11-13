package parking.model;
public class ParkingSlot {
    private final int floor;
    private final int slotNumber;
    private String regNo;
    public ParkingSlot(int floor, int slotNumber) {
        if (floor < 0 || slotNumber < 0) throw new IllegalArgumentException("Invalid slot");
        this.floor = floor; this.slotNumber = slotNumber;
    }
    public boolean occupy(String regNo) {
        if (regNo == null || regNo.isBlank() || this.regNo != null) return false;
        this.regNo = regNo; return true;
    }
    public boolean release() {
        if (regNo == null) return false;
        regNo = null; return true;
    }
    public String getRegNo() { return regNo; }
    public String toString() { return "Floor " + floor + ", Slot " + slotNumber + ", Vehicle " + regNo; }
}
