import java.io.*;import java.nio.file.*;import java.sql.*;
public class MigrationAndMetadata{
 public static void main(String[]x)throws Exception{
  String url="jdbc:mysql://localhost:3306/transport_db";
  Path f=Paths.get("migration_passes.csv");
  Files.writeString(f,"M1001,192424008,Asha,4,3200.0\nM1002,192424009,Ravi,7,3800.0\nBROKEN,LINE\nM1004,192424010,Meena,not_a_route,4000.0\n");
  try(Connection c=DriverManager.getConnection(url,"root","Colab@12345")){
   try(Statement s=c.createStatement()){s.executeUpdate("DROP TABLE IF EXISTS bus_pass");s.executeUpdate("CREATE TABLE bus_pass(pass_id VARCHAR(30) PRIMARY KEY,reg_no VARCHAR(30),name VARCHAR(80),route_no INT,amount DOUBLE)");}
   boolean old=c.getAutoCommit();c.setAutoCommit(false);int loaded=0,lineNo=0;
   try(BufferedReader r=Files.newBufferedReader(f);PreparedStatement p=c.prepareStatement("INSERT INTO bus_pass VALUES(?,?,?,?,?)")){
    String line;while((line=r.readLine())!=null){lineNo++;String[] a=line.split(",",-1);
     try{if(a.length!=5)throw new IllegalArgumentException("expected 5 fields");int route=Integer.parseInt(a[3]);double amount=Double.parseDouble(a[4]);if(amount<0)throw new IllegalArgumentException("negative amount");p.setString(1,a[0]);p.setString(2,a[1]);p.setString(3,a[2]);p.setInt(4,route);p.setDouble(5,amount);p.addBatch();loaded++;if(loaded%1000==0)p.executeBatch();}
     catch(Exception e){System.out.println("Rejected line "+lineNo+": "+line+" | "+e.getMessage());}
    }
    p.executeBatch();c.commit();
   }catch(Exception e){c.rollback();throw e;}finally{c.setAutoCommit(old);}
   System.out.println("Valid records loaded="+loaded);
   DatabaseMetaData m=c.getMetaData();System.out.println("Database="+m.getDatabaseProductName()+" "+m.getDatabaseProductVersion());System.out.println("Driver="+m.getDriverName()+" "+m.getDriverVersion());
   try(ResultSet t=m.getTables(c.getCatalog(),null,"%",new String[]{"TABLE"})){System.out.println("Tables:");while(t.next())System.out.println(t.getString("TABLE_NAME"));}
  }
 }
}
