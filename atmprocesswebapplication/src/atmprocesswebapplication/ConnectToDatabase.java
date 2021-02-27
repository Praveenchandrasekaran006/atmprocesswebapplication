//$Id$
package atmprocesswebapplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectToDatabase {
	
	public static Connection getConnectionObj() 
	{
		String url = "jdbc:mysql://localhost:3306/atm";
		String uname = "root";
		String pass = "1603";
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(url,uname,pass);
			return con;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return null;
	}

}
