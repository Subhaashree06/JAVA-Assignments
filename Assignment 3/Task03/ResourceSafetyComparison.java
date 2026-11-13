import java.io.*;
import java.nio.file.*;

public class ResourceSafetyComparison {
    static int manualOpenReaders = 0;
    static int safeOpenReaders = 0;

    static class TrackedReader extends BufferedReader {
        private final boolean safe;
        TrackedReader(Reader in, boolean safe) {
            super(in); this.safe = safe;
            if (safe) safeOpenReaders++; else manualOpenReaders++;
        }
        @Override public void close() throws IOException {
            super.close();
            if (safe) safeOpenReaders--; else manualOpenReaders--;
        }
    }

    static void manual(Path input, Path output) {
        BufferedWriter writer = null;
        TrackedReader reader = null;
        try {
            writer = Files.newBufferedWriter(output);
            reader = new TrackedReader(Files.newBufferedReader(input), false);
            String line = reader.readLine();
            if (line != null) writer.write(line);
            throw new IOException("Injected read/processing failure before reader.close()");
        } catch (IOException e) {
            System.out.println("Manual version: " + e.getMessage());
        } finally {
            if (writer != null) try { writer.close(); } catch (IOException ignored) {}
            // Deliberately mirrors a fragile manual-close pattern: reader is not closed here.
        }
        System.out.println("Manual readers still open: " + manualOpenReaders);
    }

    static void safe(Path input, Path output) {
        try (BufferedWriter writer = Files.newBufferedWriter(output);
             TrackedReader reader = new TrackedReader(Files.newBufferedReader(input), true)) {
            String line = reader.readLine();
            if (line != null) writer.write(line);
            throw new IOException("Injected read/processing failure before normal completion");
        } catch (IOException e) {
            System.out.println("Try-with-resources: " + e.getMessage());
        }
        System.out.println("Try-with-resources readers still open: " + safeOpenReaders);
    }

    public static void main(String[] args) throws Exception {
        Path in = Paths.get("store.txt");
        Files.writeString(in, "Store 1,100\n");
        manual(in, Paths.get("manual.txt"));
        safe(in, Paths.get("safe.txt"));
    }
}
