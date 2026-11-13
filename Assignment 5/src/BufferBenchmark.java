import java.nio.file.*;import java.util.*;
public class BufferBenchmark{
 public static void main(String[]x)throws Exception{
  for(int mb:new int[]{1,10,50}){
   Path p=Paths.get("log_"+mb+"MB.txt");byte[] b=new byte[1024*1024];Arrays.fill(b,(byte)'x');b[b.length-1]=10;
   try(var o=Files.newOutputStream(p)){for(int i=0;i<mb;i++)o.write(b);}
   long t=System.nanoTime();long a=PassStore.unbuffered(p);long u=System.nanoTime();
   long z=PassStore.buffered(p);long v=System.nanoTime();
   System.out.printf("%d MB lines=%d unbuffered=%.3f ms buffered=%.3f ms%n",mb,a,(u-t)/1e6,(v-u)/1e6);
  }
 }
}
