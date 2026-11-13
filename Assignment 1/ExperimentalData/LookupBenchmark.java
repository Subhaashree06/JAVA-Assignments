
import java.util.*;
public class LookupBenchmark {
  public static void main(String[] args) {
    int[] sizes={1000,5000,10000,50000,100000};
    Random r=new Random(42);
    System.out.println("records,linear_search_avg_ns,hashmap_lookup_avg_ns,iterations");
    long checksum=0;
    for(int n:sizes){
      String[] arr=new String[n];
      HashMap<String,Integer> map=new HashMap<>();
      for(int i=0;i<n;i++){arr[i]="REG"+i;map.put(arr[i],i);}
      int iterations=20000;
      String target="REG"+(n-1);

      for(int w=0;w<3;w++){for(int i=0;i<iterations;i++){int found=-1;for(int j=0;j<n;j++){if(arr[j].equals(target)){found=j;break;}}checksum+=found;}}
      long t0=System.nanoTime();
      for(int i=0;i<iterations;i++){int found=-1;for(int j=0;j<n;j++){if(arr[j].equals(target)){found=j;break;}}checksum+=found;}
      long linear=System.nanoTime()-t0;
      t0=System.nanoTime();
      for(int i=0;i<iterations;i++){checksum+=map.get(target);}
      long hash=System.nanoTime()-t0;
      System.out.println(n+","+(linear/(double)iterations)+","+(hash/(double)iterations)+","+iterations);
    }
    System.out.println("checksum="+checksum);
  }
}
