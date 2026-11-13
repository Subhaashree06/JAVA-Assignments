import java.io.*;
import java.nio.file.*;
import java.util.*;
public class PassStore {
  private final Path file;
  PassStore(Path file){this.file=file;}
  public void add(String id,String reg,String name,int route,double amount)throws IOException{
    try(BufferedWriter w=Files.newBufferedWriter(file,StandardOpenOption.CREATE,StandardOpenOption.APPEND)){
      w.write(String.join(",",id,reg,name,""+route,""+amount)); w.newLine();
    }
  }
  public String[] find(String id)throws IOException{
    if(!Files.exists(file))return null;
    try(BufferedReader r=Files.newBufferedReader(file)){
      String line;
      while((line=r.readLine())!=null){
        String[] f=line.split(",",-1);
        if(f.length!=5)continue;
        try{Integer.parseInt(f[3]);Double.parseDouble(f[4]);}
        catch(NumberFormatException e){continue;}
        if(f[0].equals(id))return f;
      }
    }
    return null;
  }
  public static long unbuffered(Path p)throws IOException{
    long n=0;try(InputStream in=Files.newInputStream(p)){int b;while((b=in.read())!=-1)if(b==10)n++;}return n;
  }
  public static long buffered(Path p)throws IOException{
    long n=0;try(InputStream in=new BufferedInputStream(Files.newInputStream(p),8192)){int b;while((b=in.read())!=-1)if(b==10)n++;}return n;
  }
  public static void main(String[] a)throws Exception{
    Path f=Paths.get("passes.csv");Files.deleteIfExists(f);PassStore s=new PassStore(f);
    s.add("P1001","192311001","Arun",12,4500);
    Files.writeString(f,"P1002,DAMAGED LINE\n",StandardOpenOption.APPEND);
    s.add("P1003","192311003","Divya",7,3800);
    System.out.println("Existing: "+Arrays.toString(s.find("P1003")));
    System.out.println("Damaged ID: "+Arrays.toString(s.find("P1002")));
    System.out.println("Missing ID: "+Arrays.toString(s.find("P9999")));
    System.out.println("Damaged row skipped; scan continues.");
  }
}
