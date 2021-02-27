//$Id$
package atmprocesswebapplication;
import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import atmprocesswebapplication.ValidateAccountPinNumber;

public class CheckBalanceClass extends HttpServlet
{
	
	public void service(HttpServletRequest req, HttpServletResponse resp)  
	{
		int acc_num = AccountNumber.getAccountNumber();
		PrintWriter out = null;	
		
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{
			out = resp.getWriter();
			
			PreparedStatement pst = con.prepareStatement("select acc_balance from CustomerDetails where acc_no = ?; ");	
			pst.setInt(1, acc_num);
			ResultSet rs = pst.executeQuery();
			rs.next();	
			out.println("<html><body><h1> Account Balance </h1> <p>The account Balance is "+rs.getInt("acc_balance")+"</p></body></html>");
			
			rs.close();
			pst.close();
		}
		catch(Exception e)
		{
			out.println("<html><body><h1>An error Occured in machine</h1> Your account balance not affected</body></html>");
			System.out.println(e);
		}		
	}
}
