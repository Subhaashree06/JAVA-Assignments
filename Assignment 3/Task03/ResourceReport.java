import java.io.*;
import java.nio.file.*;
public class ResourceReport {
 static void manual(Path d,Path out) {
  BufferedWriter w=null;
  try {
   w=Files.newBufferedWriter(out);
   for(int i=1;i<=3;i++) {
    BufferedReader r=Files.newBufferedReader(d.resolve("store"+i+".txt"));
    String line;while((line=r.readLine())!=null)w.write(line+"\n");
    r.close();
    if(i==2)throw new IOException("Injected failure after store2");
   }
  } catch(IOException e){System.out.println("Manual error: "+e.getMessage());}
  finally {if(w!=null)try{w.close();}catch(IOException e){System.out.println(e.getMessage());}}
 }
 static void safe(Path d,Path out) {
  try(BufferedWriter w=Files.newBufferedWriter(out)) {
   for(int i=1;i<=3;i++) {
    try(BufferedReader r=Files.newBufferedReader(d.resolve("store"+i+".txt"))) {
     String line;while((line=r.readLine())!=null)w.write(line+"\n");
    }
    if(i==2)throw new IOException("Injected failure after store2");
   }
  } catch(IOException e){System.out.println("Try-with-resources error: "+e.getMessage());}
 }
 public static void main(String[] args)throws Exception {
  Path d=Paths.get("input");Files.createDirectories(d);
  for(int i=1;i<=3;i++)Files.writeString(d.resolve("store"+i+".txt"),"Store "+i+",100\n");
  manual(d,Paths.get("manual_report.txt"));safe(d,Paths.get("safe_report.txt"));
  System.out.println("Manual output bytes: "+Files.size(Paths.get("manual_report.txt")));
  System.out.println("Safe output bytes: "+Files.size(Paths.get("safe_report.txt")));
 }
}
