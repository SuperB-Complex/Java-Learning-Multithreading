package tta.basics.dayfive.two;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {
	public static void main(String[] args) throws SQLException {
		//Driver d=new oracle.jdbc.driver.OracleDriver();
		//DriverManager.registerDriver(d);
		//Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "oracle");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/antra_training&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC","root","Vincy.mava");

//		try {
//			String dbServer = "jdbc:mysql://localhost:3306/antra_training&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			System.out.println("数据库驱动加载成功");
//			
//			Connection c =DriverManager.getConnection(dbServer,"root","Vincy.mava");
//			System.out.println("数据库读取成功");
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}

		System.out.println(con);
//		Statement stmt=con.createStatement();
//		//stmt.executeUpdate("insert into emp values(7,'JinJin',672)");
//		ResultSet rs=stmt.executeQuery("select * from student;");
//		while(rs.next()) {
//			System.out.println(rs.getInt(1));
//			System.out.println(rs.getString(2));
//			System.out.println(rs.getInt(3));
//		}
//		
//		con.close();
	}
}
