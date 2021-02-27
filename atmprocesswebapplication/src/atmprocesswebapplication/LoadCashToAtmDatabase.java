//$Id$
package atmprocesswebapplication;

import java.sql.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import atmprocesswebapplication.ConnectToDatabase;

public class LoadCashToAtmDatabase extends HttpServlet
{
	PrintWriter out = null;
	public void service(HttpServletRequest req, HttpServletResponse resp) 
	{		
		String query = "select * from AtmDetails;";
		String query_1 = "truncate table AtmDetails"; 
		int hundreds_count = 0;
		int five_hundreds_count = 0;
		int thousands_count = 0;
		
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{
			out = resp.getWriter();
			int hundreds = Integer.parseInt(req.getParameter("hundreds"));
			int five_hundreds = Integer.parseInt(req.getParameter("five_hundreds"));
			int thousands = Integer.parseInt(req.getParameter("thousands"));
			
			Statement st = con.createStatement();	
			ResultSet rs = st.executeQuery(query);			
			while(rs.next())
			{
				hundreds_count = rs.getInt("hundreds_count");
				five_hundreds_count = rs.getInt("five_hundreds_count");
				thousands_count = rs.getInt("thousands_count");	
			}
			st.executeUpdate(query_1);
			st.close();
			
			PreparedStatement pst = con.prepareStatement("insert into AtmDetails (hundreds_count, five_hundreds_count, thousands_count, total) values (?,?,?,?);");
			
			hundreds += hundreds_count;
			five_hundreds += five_hundreds_count;
			thousands += thousands_count;
			int total = (hundreds*100)+(five_hundreds*500)+(thousands*1000);
			
			pst.setInt(1, hundreds);
			pst.setInt(2, five_hundreds);
			pst.setInt(3, thousands);
			pst.setInt(4, total);
			pst.executeUpdate();
			pst.close();
			con.close();
			
			
			out.println("<html><body> <h1>Total Amount in ATM</h1> The total amount in ATM is "+total+" </body></html>");
		
		}
		catch(NumberFormatException e)
		{
			out.println("<html><body><h1> Invalid!! </h1> Please Enter Numbers </body></html>");
		}
		catch(Exception e)
		{
			out.println("<html><body><h1>An error Occured in machine</h1> Your account balance not affected</body></html>");
			System.out.println(e);
		}	
	}
}