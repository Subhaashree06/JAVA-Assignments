import java.io.*;import java.nio.file.*;import java.util.*;import java.util.concurrent.*;
public class ResultProcessor {
 static class R {String file;boolean ok;int score;String msg;R(String f,boolean o,int s,String m){file=f;ok=o;score=s;msg=m;}}
 static class Work implements Callable<R> {
  Path p;String n;Work(Path p,String n){this.p=p;this.n=n;}
  public R call(){
   try{
    if(!Files.exists(p))throw new FileNotFoundException("Missing score file");
    int score=Integer.parseInt(Files.readString(p).trim());
    if(score<0||score>100)throw new IllegalArgumentException("Score outside 0..100");
    long x=0;for(int i=0;i<20000;i++)x+=(i*31L)%97;
    return new R(n,true,score,"OK");
   }catch(Exception e){return new R(n,false,0,e.getClass().getSimpleName()+": "+e.getMessage());}
  }
 }
 public static void main(String[] args)throws Exception {
  Path d=Paths.get("scores");Files.createDirectories(d);
  for(int i=0;i<10000;i++){
   Path p=d.resolve(String.format("student_%05d.txt",i));
   if(i%100==0)Files.writeString(p,"corrupt");
   else if(i%100==1)Files.deleteIfExists(p);
   else Files.writeString(p,Integer.toString(i%101));
  }
  for(int pool:new int[]{1,2,4,8}){
   ExecutorService ex=Executors.newFixedThreadPool(pool);
   ConcurrentLinkedQueue<R> results=new ConcurrentLinkedQueue<>();
   List<Future<R>> futures=new ArrayList<>();long start=System.nanoTime();
   for(int i=0;i<10000;i++){String n=String.format("student_%05d.txt",i);futures.add(ex.submit(new Work(d.resolve(n),n)));}
   for(Future<R> f:futures)try{results.add(f.get());}catch(Exception e){results.add(new R("unknown",false,0,e.toString()));}
   ex.shutdown();long ms=(System.nanoTime()-start)/1000000;
   long ok=results.stream().filter(r->r.ok).count(),bad=results.size()-ok;
   System.out.println("Pool="+pool+", success="+ok+", failure="+bad+", time_ms="+ms);
   try(PrintWriter w=new PrintWriter("pool_"+pool+"_summary.csv")){
    w.println("file,success,score,message");for(R r:results)w.println(r.file+","+r.ok+","+r.score+",\""+r.msg.replace('"','\'')+"\"");
   }
   try(PrintWriter w=new PrintWriter("pool_"+pool+"_measurement.csv")){
    w.println("pool_size,total_files,successes,failures,time_ms");w.println(pool+","+results.size()+","+ok+","+bad+","+ms);
   }
  }
 }
}
