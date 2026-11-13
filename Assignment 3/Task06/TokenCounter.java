public class TokenCounter {
 private final int[] buffer=new int[5];private int count=0,in=0,out=0;
 public synchronized void put(int token)throws InterruptedException {
  while(count==buffer.length)wait();
  buffer[in]=token;in=(in+1)%buffer.length;count++;notifyAll();
 }
 public synchronized int take()throws InterruptedException {
  while(count==0)wait();
  int token=buffer[out];out=(out+1)%buffer.length;count--;notifyAll();return token;
 }
 static class Wallet {final int id;int balance;Wallet(int i,int b){id=i;balance=b;}}
 static void transfer(Wallet a,Wallet b,int amount) {
  Wallet first=a.id<b.id?a:b,second=a.id<b.id?b:a;
  synchronized(first){synchronized(second){if(a.balance>=amount){a.balance-=amount;b.balance+=amount;}}}
 }
 public static void main(String[] args)throws InterruptedException {
  TokenCounter c=new TokenCounter();int[] sums=new int[2];
  Thread cook=new Thread(()->{try{for(int t=1;t<=20;t++)c.put(t);}catch(InterruptedException e){Thread.currentThread().interrupt();}});
  Thread[] students=new Thread[2];
  for(int s=0;s<2;s++){final int id=s;students[s]=new Thread(()->{try{for(int k=0;k<10;k++)sums[id]+=c.take();}catch(InterruptedException e){Thread.currentThread().interrupt();}});}
  cook.start();for(Thread t:students)t.start();cook.join();for(Thread t:students)t.join();
  System.out.println("Sum of tokens collected: "+(sums[0]+sums[1])+" (expected 210)");
  Wallet a=new Wallet(1,1000),b=new Wallet(2,1000);
  Thread x=new Thread(()->transfer(a,b,100)),y=new Thread(()->transfer(b,a,200));
  x.start();y.start();x.join();y.join();
  System.out.println("Wallet total: "+(a.balance+b.balance));
 }
}
