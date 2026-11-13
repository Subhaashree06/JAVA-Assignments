import java.nio.file.*;
public class GrowthBenchmark{
 public static void main(String[]x)throws Exception{
  for(int n:new int[]{1000,10000,100000}){
   Path p=Paths.get("growth_"+n+".csv");
   try(var w=Files.newBufferedWriter(p)){for(int i=0;i<n;i++)w.write("P"+i+",192424008,Student"+i+",12,4500.0\n");}
   PassStore s=new PassStore(p);long t=System.nanoTime();boolean found=s.find("P"+(n-1))!=null;long e=System.nanoTime();
   System.out.printf("records=%d found=%s time=%.3f ms%n",n,found,(e-t)/1e6);
  }
 }
}
