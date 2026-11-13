import java.sql.*;
public class LoginSecurity{
 public static void main(String[]x)throws Exception{
  String url="jdbc:mysql://localhost:3306/transport_db";
  try(Connection c=DriverManager.getConnection(url,"root","Colab@12345");Statement s=c.createStatement()){
   s.executeUpdate("DROP TABLE IF EXISTS users_test");
   s.executeUpdate("CREATE TABLE users_test(id VARCHAR(50) PRIMARY KEY,pwd VARCHAR(100))");
   s.executeUpdate("INSERT INTO users_test VALUES('student1','correct123')");
   String id="' OR '1'='1' -- ",pwd="wrong";
   String unsafe="SELECT * FROM users_test WHERE id='"+id+"' AND pwd='"+pwd+"'";
   try(ResultSet r=s.executeQuery(unsafe)){System.out.println("Unsafe accepted? "+r.next());}
   try(PreparedStatement p=c.prepareStatement("SELECT * FROM users_test WHERE id=? AND pwd=?")){
    p.setString(1,id);p.setString(2,pwd);
    try(ResultSet r=p.executeQuery()){System.out.println("PreparedStatement accepted? "+r.next());}
   }
  }
 }
}
