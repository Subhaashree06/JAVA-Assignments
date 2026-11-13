import java.sql.*;
public class JdbcBenchmarks{
 static String url="jdbc:mysql://localhost:3306/transport_db",u="root",pw="Colab@12345";
 static String ins="INSERT INTO bus_pass VALUES(?,?,?,?,?)";
 static void reset(Connection c)throws Exception{try(Statement s=c.createStatement()){s.executeUpdate("DROP TABLE IF EXISTS bus_pass");s.executeUpdate("CREATE TABLE bus_pass(pass_id VARCHAR(30) PRIMARY KEY,reg_no VARCHAR(30),name VARCHAR(80),route_no INT,amount DOUBLE)");}}
 static long count(Connection c)throws Exception{try(Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT COUNT(*) FROM bus_pass")){r.next();return r.getLong(1);}}
 static void statement(Connection c,int n)throws Exception{try(Statement s=c.createStatement()){for(int i=0;i<n;i++)s.executeUpdate("INSERT INTO bus_pass VALUES('S"+i+"','192424008','Student"+i+"',12,4500.0)");}}
 static void prepared(Connection c,int n)throws Exception{try(PreparedStatement p=c.prepareStatement(ins)){for(int i=0;i<n;i++){p.setString(1,"P"+i);p.setString(2,"192424008");p.setString(3,"Student"+i);p.setInt(4,12);p.setDouble(5,4500);p.executeUpdate();}}}
 static void batch(Connection c,int n)throws Exception{boolean old=c.getAutoCommit();c.setAutoCommit(false);try(PreparedStatement p=c.prepareStatement(ins)){for(int i=0;i<n;i++){p.setString(1,"B"+i);p.setString(2,"192424008");p.setString(3,"Student"+i);p.setInt(4,12);p.setDouble(5,4500);p.addBatch();if((i+1)%1000==0)p.executeBatch();}p.executeBatch();c.commit();}catch(Exception e){c.rollback();throw e;}finally{c.setAutoCommit(old);}}
 public static void main(String[]z)throws Exception{
  try(Connection c=DriverManager.getConnection(url,u,pw)){
   for(int n:new int[]{10,1000,10000,100000}){
    reset(c);long t=System.nanoTime();statement(c,n);System.out.printf("Statement %d rows: %.3f ms%n",n,(System.nanoTime()-t)/1e6);
    if(n==10)try(Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT * FROM bus_pass");){ResultSetMetaData m=r.getMetaData();System.out.println("Metadata columns="+m.getColumnCount()+" first="+m.getColumnName(1)+" type="+m.getColumnTypeName(1));}
    reset(c);t=System.nanoTime();prepared(c,n);System.out.printf("PreparedStatement %d rows: %.3f ms%n",n,(System.nanoTime()-t)/1e6);
    reset(c);t=System.nanoTime();batch(c,n);System.out.printf("Batch transaction %d rows: %.3f ms%n",n,(System.nanoTime()-t)/1e6);
   }
   reset(c);c.setAutoCommit(false);
   try(PreparedStatement p=c.prepareStatement(ins)){
    for(int i=0;i<10;i++){if(i==5)throw new SQLException("Forced failure halfway");p.setString(1,"R"+i);p.setString(2,"192424008");p.setString(3,"Rollback"+i);p.setInt(4,12);p.setDouble(5,4500);p.addBatch();}
    p.executeBatch();c.commit();
   }catch(Exception e){c.rollback();System.out.println("Forced failure: "+e.getMessage());}
   c.setAutoCommit(true);System.out.println("Rows remaining after rollback="+count(c));
  }
 }
}
