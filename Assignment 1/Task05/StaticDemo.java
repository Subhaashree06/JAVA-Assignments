class TicketCounter {
    private static int nextTicket = 1;
    static int issue() { return nextTicket++; }
}
class Gate {
    private final String name;
    Gate(String name) { this.name = name; }
    int issueTicket() { return TicketCounter.issue(); }
    String getName() { return name; }
}
public class StaticDemo {
    public static void main(String[] args) {
        Gate north = new Gate("North"), south = new Gate("South");
        System.out.println(north.getName() + " ticket " + north.issueTicket());
        System.out.println(south.getName() + " ticket " + south.issueTicket());
    }
}
