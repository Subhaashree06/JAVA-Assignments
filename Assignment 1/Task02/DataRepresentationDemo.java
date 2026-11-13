import java.time.LocalDateTime;
public class DataRepresentationDemo {
    public static void main(String[] args) {
        int[] slotsPerFloor = {20, 25, 30};
        String[][] slots = new String[slotsPerFloor.length][];
        for (int f=0; f<slotsPerFloor.length; f++) slots[f]=new String[slotsPerFloor[f]];
        int minutes=61;
        int billableHours=(minutes+59)/60;
        LocalDateTime entry=LocalDateTime.of(2026,11,13,9,0);
        System.out.println("Jagged floors: " + slots.length);
        System.out.println("Slots per floor: " + slotsPerFloor[0]+", "+slotsPerFloor[1]+", "+slotsPerFloor[2]);
        System.out.println("Entry time: " + entry);
        System.out.println("61 minutes billable hours: " + billableHours);
    }
}
