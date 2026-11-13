class ParkingTicket {
    private final int ticketNo;
    private final String regNo;
    private final long entryMinute;
    ParkingTicket() { this(0, "UNKNOWN", 0L); }
    ParkingTicket(int ticketNo, String regNo) { this(ticketNo, regNo, System.currentTimeMillis()); }
    ParkingTicket(int ticketNo, String regNo, long entryMinute) {
        if (ticketNo < 0 || regNo == null || regNo.isBlank()) throw new IllegalArgumentException("Invalid ticket");
        this.ticketNo = ticketNo; this.regNo = regNo; this.entryMinute = entryMinute;
    }
    ParkingTicket(ParkingTicket other) { this(other.ticketNo, other.regNo, other.entryMinute); }
    public String toString() { return ticketNo + " | " + regNo + " | " + entryMinute; }
}
public class ConstructorDemo {
    public static void main(String[] args) {
        ParkingTicket a = new ParkingTicket(101, "TN01AB1234", 1000L);
        ParkingTicket b = new ParkingTicket(a);
        System.out.println(a); System.out.println(b);
    }
}
