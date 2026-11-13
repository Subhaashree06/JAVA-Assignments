import java.io.*;
class CardReaderException extends Exception { CardReaderException(String m){super(m);} }
class ATM {
 double balance=5000;
 void withdraw(String account,String text,boolean cardPresent) throws CardReaderException {
  try {
   if(!cardPresent) throw new FileNotFoundException("card-reader.dat missing");
   int amount=Integer.parseInt(text);
   if(amount<=0) throw new IllegalArgumentException("Amount must be positive");
   if(amount>balance) throw new IllegalStateException("Insufficient balance");
   balance-=amount;
   System.out.println("Withdrawal successful: "+amount+"; Balance: "+balance);
  } catch(FileNotFoundException e) {
   throw new CardReaderException("Card reader unavailable: "+e.getMessage());
  } finally { System.out.println("ATM transaction cleanup completed."); }
 }
}
public class ATMMain {
 public static void main(String[] args) {
  ATM atm=new ATM();
  String[][] tests={{"A100","500","true"},{"A101","abc","true"},{"A102","-50","true"},{"A103","9000","true"},{"A104","100","false"}};
  for(String[] t:tests) {
   System.out.println("--- Test "+t[0]+" ---");
   try {atm.withdraw(t[0],t[1],Boolean.parseBoolean(t[2]));}
   catch(CardReaderException|IllegalArgumentException|IllegalStateException e) {
    System.out.println("Caught: "+e.getClass().getSimpleName()+" - "+e.getMessage());
   }
  }
 }
}
