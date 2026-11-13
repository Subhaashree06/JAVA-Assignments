public class ConverterLogic {
 public static String convert(String text,String currency){try{double v=Double.parseDouble(text.trim());if(v<0||!Double.isFinite(v))return "Invalid amount";double rate=currency.equals("USD")?0.012:0.011;return String.format(java.util.Locale.US,"%.2f %s",v*rate,currency);}catch(Exception e){return "Invalid amount";}}
 public static void main(String[] a){System.out.println("1000 INR to USD: "+convert("1000","USD"));System.out.println("1000 INR to EUR: "+convert("1000","EUR"));System.out.println("Invalid input: "+convert("abc","USD"));}
}
