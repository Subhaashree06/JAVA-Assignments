import java.util.*;
public class ThreadStations {
 static class ByThread extends Thread {
  int id;List<Integer> done;ByThread(int i,List<Integer>d){id=i;done=d;}
  public void run(){try{Thread.sleep(10+new Random().nextInt(60));}catch(InterruptedException e){interrupt();}synchronized(done){done.add(id);}}
 }
 static class ByRunnable implements Runnable {
  int id;List<Integer> done;ByRunnable(int i,List<Integer>d){id=i;done=d;}
  public void run(){try{Thread.sleep(10+new Random().nextInt(60));}catch(InterruptedException e){Thread.currentThread().interrupt();}synchronized(done){done.add(id);}}
 }
 public static void main(String[] args)throws Exception {
  for(int run=1;run<=10;run++){
   List<Integer> done=Collections.synchronizedList(new ArrayList<>());
   Thread[] ts=new Thread[4];ts[0]=new ByThread(1,done);
   for(int i=1;i<4;i++)ts[i]=new Thread(new ByRunnable(i+1,done));
   for(Thread t:ts)t.start();
   System.out.println("Run "+run+" station1 alive immediately? "+ts[0].isAlive());
   for(Thread t:ts)t.join();
   System.out.println("Run "+run+" finish order: "+done);
  }
  System.out.println("All stations completed after join().");
 }
}
